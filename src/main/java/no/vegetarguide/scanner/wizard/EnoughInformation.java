package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import no.vegetarguide.scanner.AlertDialogFragment;
import no.vegetarguide.scanner.BaseActivity;
import no.vegetarguide.scanner.MainActivity;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.SingleClickListener;
import no.vegetarguide.scanner.integration.ModifyProductRequest;
import no.vegetarguide.scanner.integration.ModifyProductRequestHandler;
import no.vegetarguide.scanner.integration.ModifyProductResponse;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.MODIFY_PRODUCT_SUCCESS;

public class EnoughInformation extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enough_information);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ModifyProductRequest.class.getSimpleName());
        ModifyProductRequest modifyRequest = (ModifyProductRequest) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, EnoughInformationFragment.newInstance(modifyRequest))
                    .commit();
        }
    }


    public static class EnoughInformationFragment extends Fragment {
        private View progressBar;
        private ModifyProductRequest modifyRequest;

        public EnoughInformationFragment() {
        }

        public static EnoughInformationFragment newInstance(ModifyProductRequest modifyRequest) {
            EnoughInformationFragment frag = new EnoughInformationFragment();
            Bundle args = new Bundle();
            args.putParcelable(ModifyProductRequest.class.getSimpleName(), modifyRequest);
            frag.setArguments(args);
            return frag;
        }


        private void initProgressBar(View rootView) {
            progressBar = rootView.findViewById(R.id.progressbar);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_enough_information, container, false);
            Bundle arguments = getArguments();
            if (arguments == null) {
                throw new IllegalStateException("Missing required state arguments bundle");
            }
            modifyRequest = arguments.getParcelable(ModifyProductRequest.class.getSimpleName());

            initProgressBar(rootView);

            initStatusMessage(rootView, modifyRequest.getProduct());

            initSubmitButton(rootView);

            return initCancelButton(rootView);
        }

        private View initCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {

                    Intent gotoStart = new Intent(getActivity(), MainActivity.class);
                    gotoStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoStart);
                }
            });
            return rootView;
        }

        private void initSubmitButton(View rootView) {
            View submitButton = rootView.findViewById(R.id.submit_button);
            final Context ctx = getActivity();
            submitButton.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (isNetworkAvailable(ctx)) {
                        performRequest();
                    } else {
                        DialogFragment dialog = AlertDialogFragment.newInstance(
                                R.string.network_error_title, R.string.no_network_connection);
                        if (dialog != null)
                            dialog.show(getFragmentManager(), "networkError");
                    }
                }

            });
        }

        private void initStatusMessage(View rootView, Product product) {
            final String format = getString(R.string.product_status_format);
            TextView status = (TextView) rootView.findViewById(R.id.status);

            if (product.isAnimalDerivedForCertain()) {
                status.setText(String.format(format, getString(R.string.status_not_vegetarian)));
            } else if (product.isMaybeVegan()) {
                if (product.isVegan()) {
                    status.setText(String.format(format, getString(R.string.status_vegan)));
                } else if (product.isVegetarian()) {
                    status.setText(String.format(format, getString(R.string.status_vegetarian_and_maybe_vegan)));
                } else if (product.isMaybeVegetarian()) {
                    status.setText(String.format(format, getString(R.string.status_maybe_vegetarian_or_maybe_vegan)));
                }
            } else if (product.isVegetarian()) {
                status.setText(String.format(format, getString(R.string.status_vegetarian)));
            } else {
                status.setText(String.format(format, getString(R.string.status_maybe_vegetarian)));
            }
        }

        private void showProgressBar() {
            progressBar.setVisibility(View.VISIBLE);
        }

        private void hideProgressBar() {
            progressBar.setVisibility(View.GONE);
        }

        private void performRequest() {
            showProgressBar();

            ModifyProductRequestHandler requestHandler = new ModifyProductRequestHandler(modifyRequest);

            requestHandler.execute(VolleySingleton.getInstance(getActivity()).getRequestQueue(),
                    createModifyResponseListener(getActivity()),
                    createModifyErrorListener());
        }

        private Response.Listener<ModifyProductResponse> createModifyResponseListener(final Activity activity) {
            return new Response.Listener<ModifyProductResponse>() {
                @Override
                public void onResponse(ModifyProductResponse response) {
                    if (response.isSuccess()) {
                        Intent result = new Intent(activity, MainActivity.class);
                        result.putExtra(MODIFY_PRODUCT_SUCCESS, true);
                        activity.setResult(Activity.RESULT_OK, result);
                        hideProgressBar();

                        Toast.makeText(activity, R.string.success_product_submitted, Toast.LENGTH_LONG).show();

                        activity.finish();
                        result.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(result);
                    } else {
                        hideProgressBar();

                        String errorMessage = String.format(getString(R.string.error_product_not_submitted_format), response.getMessage());
                        AlertDialogFragment dialog =
                                AlertDialogFragment.newInstance(R.string.error_product_not_submitted_format, errorMessage);
                        if (dialog != null)
                            dialog.show(getFragmentManager(), "errorResponse");
                    }
                }

            };
        }

        private Response.ErrorListener createModifyErrorListener() {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Throwable cause = error.getCause();

                    int errorTitleResource = R.string.network_error_title;
                    int errorMessageResource = R.string.error_generic_maybe_network;
                    if (error instanceof NoConnectionError
                            || error instanceof TimeoutError
                            || error instanceof NetworkError) {
                        errorMessageResource = R.string.network_route_error;
                    } else if (error instanceof ServerError) {
                        errorTitleResource = R.string.server_error_title;
                        errorMessageResource = R.string.server_error;
                    }

                    hideProgressBar();

                    AlertDialogFragment dialog = AlertDialogFragment.newInstance(errorTitleResource, errorMessageResource);
                    if (dialog != null)
                        dialog.show(getFragmentManager(), "modifyIOErrorDialog");

                    Log.e("vegetarguide", "Error during product modify request", cause);
                }
            };
        }
    }


}
