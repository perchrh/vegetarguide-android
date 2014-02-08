package no.vegetarguide.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import no.vegetarguide.scanner.integration.ModifyProductRequest;
import no.vegetarguide.scanner.integration.ModifyProductRequestHandler;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.AdditivesDetails;
import no.vegetarguide.scanner.model.ModifyProductResponse;
import no.vegetarguide.scanner.model.ProductLookupResponse;

import static no.vegetarguide.scanner.SuperScan.MODIFY_PRODUCT_SUCCESS;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ModifyProductActivity extends Activity {

    public static final int REQUEST_CODE_VERIFY_ADDITIVES = 100;
    private ProductLookupResponse productDetails;
    private EditText title;
    private EditText subtitle;
    private EditText brand;
    private CheckBox otherwise_animal_derived;
    private EditText otherwise_animal_derived_detail;
    private TextView gtin;
    private ViewGroup otherwise_animal_derived_group;
    private CheckBox contains_animal_additives;
    private CheckBox contains_animal_milk;
    private CheckBox contains_bodyparts;
    private CheckBox contains_eggs;
    private CheckBox contains_honey;
    private CheckBox tested_on_animals;
    private int greenColor;
    private int redColor;

    private boolean additivesAreVerified = false;
    private AdditivesDetails additivesDetails;
    private EditText user_commentary;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ProductLookupResponse.class.getSimpleName());
        productDetails = (ProductLookupResponse) obj;

        greenColor = getResources().getColor(R.color.holo_green_light);
        redColor = getResources().getColor(R.color.holo_red_light);

        initViews();
    }

    private void initViews() {
        initReadOnlyFields();
        initEditTexts();
        initCheckBoxes();
        initSubmitButton();
        initProgressBar();
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progressbar);
    }

    private void initReadOnlyFields() {
        gtin = (TextView) findViewById(R.id.gtin_value);
        gtin.setText(productDetails.getGtin());
    }

    private void initSubmitButton() {
        View submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userSpecifiedNoAnimalAdditives() && !additivesAreVerified) {
                    // start verification activity, block, then proceed
                    Intent verificationActivity = new Intent(ModifyProductActivity.this, VerifyAdditivesActivity.class);
                    startActivityForResult(verificationActivity, REQUEST_CODE_VERIFY_ADDITIVES);
                } else if (additivesAreVerified || contains_animal_additives.isChecked()) {
                    performRequest();
                } else if (noChangeToAnimalAdditives()) {
                    performRequest();
                } else {
                    //TODO cleanup, log errors server side
                    String errorMessage = "Programmeringsfeil. Appen vet ikke hva den skal gjøre. Rapporter denne feilen!";
                    AlertDialogFragment dialogFragment =
                            AlertDialogFragment.newInstance(R.string.error_product_not_submitted_format, errorMessage);
                    dialogFragment.show(getFragmentManager(), "programmingError");
                }
            }

        });
    }

    private boolean noChangeToAnimalAdditives() {
        Boolean originalValue = productDetails.getContains_animal_additives();
        Boolean currentValue = contains_animal_additives.isChecked();
        return currentValue.equals(originalValue);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean userSpecifiedNoAnimalAdditives() {
        // Originally true or null, and user selected false
        Boolean originalValue = productDetails.getContains_animal_additives();
        boolean noAnimalAdditivesSelected = !contains_animal_additives.isChecked();
        if (productDetails.get_id() == null) {
            return noAnimalAdditivesSelected;
        } else {
            return noAnimalAdditivesSelected && !Boolean.FALSE.equals(originalValue);
        }
    }

    private void performRequest() {
        showProgressBar();

        ModifyProductRequest requestObject = new ModifyProductRequest(
                title.getText().toString(),
                subtitle.getText().toString(),
                brand.getText().toString(),
                otherwise_animal_derived_detail.getText().toString(),
                user_commentary.getText().toString(),
                otherwise_animal_derived.isChecked(),
                contains_animal_additives.isChecked(),
                contains_animal_milk.isChecked(),
                contains_bodyparts.isChecked(),
                contains_eggs.isChecked(),
                contains_honey.isChecked(),
                tested_on_animals.isChecked(),
                additivesDetails,
                productDetails);

        ModifyProductRequestHandler requestHandler = new ModifyProductRequestHandler(requestObject);

        requestHandler.execute(VolleySingleton.getInstance(ModifyProductActivity.this).getRequestQueue(),
                createModifyResponseListener(),
                createModifyErrorListener());
    }

    private Response.Listener<ModifyProductResponse> createModifyResponseListener() {
        return new Response.Listener<ModifyProductResponse>() {
            @Override
            public void onResponse(ModifyProductResponse response) {
                if (response.isSuccess()) {
                    Intent result = new Intent();
                    result.putExtra(MODIFY_PRODUCT_SUCCESS, true);
                    setResult(Activity.RESULT_OK, result);
                    hideProgressBar();

                    ModifyProductActivity.this.finish();
                } else {
                    hideProgressBar();

                    String errorMessage = String.format(getString(R.string.error_product_not_submitted_format), response.getMessage());
                    AlertDialogFragment dialogFragment =
                            AlertDialogFragment.newInstance(R.string.error_product_not_submitted_format, errorMessage);
                    dialogFragment.show(getFragmentManager(), "errorResponse");
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
                dialog.show(getFragmentManager(), "modifyIOErrorDialog");

                Log.e("superscan", "Error during product modify request", cause);
            }
        };
    }

    private void initEditTexts() {
        title = createEditText(R.id.title_edit, productDetails.getTitle());
        subtitle = createEditText(R.id.subtitle_edit, productDetails.getSubtitle());
        brand = createEditText(R.id.brand_edit, productDetails.getBrand());

        otherwise_animal_derived_group = (ViewGroup) findViewById(R.id.other_animal_derived_row);
        otherwise_animal_derived_detail = createEditText(R.id.product_other_animal_derived_edit, productDetails.getOtherwise_animal_derived_detail());
        user_commentary = (EditText) findViewById(R.id.commentary_edit);
    }

    private EditText createEditText(int resourceId, String textValue) {
        EditText inputField = (EditText) findViewById(resourceId);
        if (isNotEmpty(textValue)) {
            inputField.setText(textValue);
        }
        return inputField;
    }

    private void initCheckBoxes() {
        contains_animal_additives = createCheckBox(R.id.product_contains_animal_additives,
                productDetails.getContains_animal_additives(), true);
        contains_animal_additives.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user_commentary.setHint(R.string.user_commentary_hint_with_animal_additives);
                } else {
                    user_commentary.setHint(R.string.user_commentary_hint);
                }
            }
        });

        contains_animal_milk = createCheckBox(R.id.product_contains_animal_milk,
                productDetails.getContains_animal_milk(), true);

        contains_bodyparts = createCheckBox(R.id.product_contains_bodyparts,
                productDetails.getContains_bodyparts(), true);

        contains_eggs = createCheckBox(R.id.product_contains_eggs,
                productDetails.getContains_eggs(), true);

        contains_honey = createCheckBox(R.id.product_contains_honey,
                productDetails.getContains_honey(), true);

        tested_on_animals = createCheckBox(R.id.product_is_animal_tested,
                productDetails.getAnimal_tested(), true);

        otherwise_animal_derived = createCheckBox(R.id.product_contains_other_animal_derived,
                productDetails.getOtherwise_animal_derived(), false);
        otherwise_animal_derived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    otherwise_animal_derived_group.setVisibility(View.VISIBLE);
                } else {
                    otherwise_animal_derived_group.setVisibility(View.GONE);
                }
            }
        });
    }

    private CheckBox createCheckBox(int resourceId, Boolean checkedInDatabase, boolean defaultValue) {
        CheckBox box = (CheckBox) findViewById(resourceId);
        if (checkedInDatabase != null) {
            box.setChecked(checkedInDatabase);
            if (checkedInDatabase) {
                box.setTextColor(redColor);
            } else {
                box.setTextColor(greenColor);
            }
        } else {
            box.setChecked(defaultValue);
        }
        return box;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_VERIFY_ADDITIVES:
                if (resultCode == Activity.RESULT_OK) {
                    additivesDetails = data.getParcelableExtra(VerifyAdditivesActivity.VERIFICATION_DATA_KEY);
                    additivesAreVerified = additivesDetails.allVerified();
                    if (additivesAreVerified) {
                        performRequest();
                    } else {
                        contains_animal_additives.setChecked(true);
                        Toast.makeText(ModifyProductActivity.this, R.string.error_additives_not_verified, Toast.LENGTH_LONG).show();
                    }

                }
        }
    }
}
