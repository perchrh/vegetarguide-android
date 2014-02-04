package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.lang3.StringUtils;

import no.vegetarguide.scanner.integration.ProductLookupRequestHandler;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.LookupErrorType;
import no.vegetarguide.scanner.model.ProductLookupResponse;

import static no.vegetarguide.scanner.SuperScan.MODIFY_PRODUCT_SUCCESS;
import static no.vegetarguide.scanner.SuperScan.PRODUCT_DETAILS_REQUEST_CODE;
import static no.vegetarguide.scanner.SuperScan.START_SCANNING;

public class MainActivity extends Activity {

    private View progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        View scan = findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    startScan();
                } else {
                    DialogFragment newFragment = AlertDialogFragment.newInstance(
                            R.string.network_error_title, R.string.no_network_connection);
                    newFragment.show(getFragmentManager(), "networkError");
                }
            }
        });

        progressBar = findViewById(R.id.progressbar);

        View inputNumber = findViewById(R.id.input_code);
        inputNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO make this a separate activity
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.title_user_input_barcode);

                final EditText input = new EditText(MainActivity.this);
                input.setLines(1);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        performRequest(StringUtils.strip(value), "manual input");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();

                input.setOnKeyListener(new View.OnKeyListener() {
                    boolean hasSubmitted = false;

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (hasSubmitted) {
                            return true; // Don't send multiple requests if multiple enter characters are received
                        }
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                            String value = input.getText().toString();
                            performRequest(StringUtils.strip(value), "manual");
                            hasSubmitted = true;
                            dialog.dismiss();

                            return true;
                        }
                        return false;
                    }
                });

                dialog.show();
            }
        });

    }

    private void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return; // back button pressed
        }

        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {
                    performRequest(scanResult.getContents(), scanResult.getFormatName());
                } else {
                    Toast.makeText(this, R.string.scan_error, Toast.LENGTH_LONG).show();
                }
                break;
            case PRODUCT_DETAILS_REQUEST_CODE:
                if (intent == null) {
                    break;
                } else if (intent.getBooleanExtra(START_SCANNING, false)) {
                    startScan();
                } else if (intent.getBooleanExtra(MODIFY_PRODUCT_SUCCESS, false)) {
                    Toast.makeText(this, R.string.success_product_submitted, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private Request performRequest(String productCode, String formatName) {
        showProgressBar();

        ProductLookupRequestHandler requestHandler = new ProductLookupRequestHandler(productCode, formatName);
        //TODO do this in an intentservice
        return requestHandler.execute(VolleySingleton.getInstance(this).getRequestQueue(),
                createLookupResponseListener(),
                createLookupErrorListener());
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private Response.Listener<ProductLookupResponse> createLookupResponseListener() {
        return new Response.Listener<ProductLookupResponse>() {
            @Override
            public void onResponse(ProductLookupResponse response) {
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                intent.putExtra(ProductLookupResponse.class.getSimpleName(), response);

                hideProgressBar();

                if (response.hasError()) {
                    showErrorMessage(response.getError());
                } else {
                    startActivityForResult(intent, PRODUCT_DETAILS_REQUEST_CODE);
                }
            }

            private void showErrorMessage(LookupErrorType error) {
                DialogFragment newFragment = AlertDialogFragment.newInstance(
                        R.string.lookup_error_title, error.getDescriptionResource());
                newFragment.show(getFragmentManager(), "lookupErrorDialog");
            }
        };
    }

    private Response.ErrorListener createLookupErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Throwable cause = error.getCause();

                int errorTitleResource = R.string.network_error_title;
                int errorMessageResource = R.string.error_generic_maybe_network;
                if (error instanceof NoConnectionError ||
                        error instanceof TimeoutError
                        || error instanceof NetworkError) {
                    errorMessageResource = R.string.network_route_error;
                } else if (error instanceof ServerError) {
                    errorTitleResource = R.string.server_error_title;
                    errorMessageResource = R.string.server_error;
                }

                hideProgressBar();

                DialogFragment newFragment = AlertDialogFragment.newInstance(
                        errorTitleResource, errorMessageResource);
                newFragment.show(getFragmentManager(), "lookupIOErrorDialog");

                Log.e("superscan", "Error during product lookup request", cause);
            }
        };
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance(int title, int message) {
            AlertDialogFragment frag = new AlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("message", message);
            frag.setArguments(args);
            return frag;
        }

        public AlertDialogFragment() {
            super();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            int message = getArguments().getInt("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    ).create();
        }
    }

}
