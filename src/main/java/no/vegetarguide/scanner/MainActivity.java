package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import no.vegetarguide.scanner.integration.ProductLookupRequestHandler;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.LookupErrorType;
import no.vegetarguide.scanner.model.ProductLookupResponse;
import org.apache.commons.lang3.StringUtils;

import static no.vegetarguide.scanner.SuperScan.*;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
                    Toast.makeText(MainActivity.this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
                }
            }
        });

        View inputNumber = findViewById(R.id.input_code);
        inputNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.title_user_input_barcode);

                final EditText input = new EditText(MainActivity.this);
                input.setLines(1);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        performRequest(StringUtils.strip(value), "manual");
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
        setProgressBarIndeterminateVisibility(true);

        ProductLookupRequestHandler requestHandler = new ProductLookupRequestHandler(productCode, formatName);
        //TODO do this in an intentservice
        return requestHandler.execute(VolleySingleton.getInstance(this).getRequestQueue(),
                createLookupResponseListener(),
                createLookupErrorListener());
    }

    private Response.Listener<ProductLookupResponse> createLookupResponseListener() {
        return new Response.Listener<ProductLookupResponse>() {
            @Override
            public void onResponse(ProductLookupResponse response) {
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                intent.putExtra(ProductLookupResponse.class.getSimpleName(), response);
                setProgressBarIndeterminateVisibility(false);

                if (response.hasError()) {
                    showErrorMessage(response.getError());
                } else {
                    startActivityForResult(intent, PRODUCT_DETAILS_REQUEST_CODE);
                }
            }

            private void showErrorMessage(LookupErrorType error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.error_title).setMessage(error.getDescriptionResource());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
    }

    private Response.ErrorListener createLookupErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, R.string.error_generic_maybe_network, Toast.LENGTH_LONG).show();
                Log.e("superscan", "Error during product lookup request", error);
                setProgressBarIndeterminateVisibility(false);
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

}
