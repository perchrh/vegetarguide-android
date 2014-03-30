package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import no.vegetarguide.scanner.integration.ProductLookupRequestHandler;
import no.vegetarguide.scanner.integration.ProductLookupResponse;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.LookupErrorType;

import static no.vegetarguide.scanner.Application.MODIFY_PRODUCT_SUCCESS;
import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_REQUEST_CODE;
import static no.vegetarguide.scanner.Application.START_SCANNING;

public class MainActivity extends BaseActivity {

    private View progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        View scan = findViewById(R.id.scan_button);
        scan.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (isNetworkAvailable(getBaseContext())) {
                    startScan();
                } else {
                    DialogFragment dialog = AlertDialogFragment.newInstance(
                            R.string.network_error_title, R.string.no_network_connection);
                    if (dialog != null)
                        dialog.show(getFragmentManager(), "networkError");
                }
            }
        });

        progressBar = findViewById(R.id.progressbar);

        View inputNumber = findViewById(R.id.input_code);
        inputNumber.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isNetworkAvailable(getBaseContext())) {
                    Intent launchActivity = new Intent(MainActivity.this, ManualInputActivity.class);
                    startActivityForResult(launchActivity, ManualInputActivity.REQUEST_MANUAL_INPUT);
                } else {
                    DialogFragment dialog = AlertDialogFragment.newInstance(
                            R.string.network_error_title, R.string.no_network_connection);
                    if (dialog != null)
                        dialog.show(getFragmentManager(), "networkError");
                }
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
                    performRequest(scanResult.getContents());
                } else {
                    DialogFragment dialog = AlertDialogFragment.newInstance(
                            R.string.scan_error_title, R.string.scan_error);
                    if (dialog != null)
                        dialog.show(getFragmentManager(), "scanError");
                }
                break;
            case PRODUCT_DETAILS_REQUEST_CODE:
                if (intent.getBooleanExtra(START_SCANNING, false)) {
                    startScan();
                } else if (intent.getBooleanExtra(MODIFY_PRODUCT_SUCCESS, false)) {
                    Toast.makeText(this, R.string.success_product_submitted, Toast.LENGTH_LONG).show();
                }
                break;
            case ManualInputActivity.REQUEST_MANUAL_INPUT:
                String gtin = intent.getStringExtra(ManualInputActivity.GTIN_EXTRA);
                performRequest(gtin);
                break;
            default:
                throw new RuntimeException("Got unknown request code in result " + resultCode);
        }

    }

    private Request performRequest(String productCode) {
        showProgressBar();

        ProductLookupRequestHandler requestHandler = new ProductLookupRequestHandler(productCode);
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
                DialogFragment dialog = AlertDialogFragment.newInstance(
                        R.string.lookup_error_title, error.getDescriptionResource());
                if (dialog != null)
                    dialog.show(getFragmentManager(), "lookupErrorDialog");
            }
        };
    }

    private Response.ErrorListener createLookupErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                DialogFragment dialog = AlertDialogFragment.newInstance(
                        errorTitleResource, errorMessageResource);
                if (dialog != null)
                    dialog.show(getFragmentManager(), "lookupIOErrorDialog");

                Log.e("vegetarguide", "Error during product lookup request", error);
            }
        };
    }


}
