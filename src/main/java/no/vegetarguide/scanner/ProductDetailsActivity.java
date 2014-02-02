package no.vegetarguide.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.ProductLookupResponse;
import no.vegetarguide.scanner.model.ResultType;
import no.vegetarguide.scanner.model.StatusType;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static no.vegetarguide.scanner.SuperScan.MODIFY_PRODUCT_REQUEST_CODE;
import static no.vegetarguide.scanner.SuperScan.START_SCANNING;

public class ProductDetailsActivity extends Activity {

    private ProductLookupResponse productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ProductLookupResponse.class.getSimpleName());
        productDetails = (ProductLookupResponse) obj;

        initViews();
    }

    private void initViews() {
        initDescription();
        initStatusLine();
        initImage();
        initBackButton();
        initModifyButton();
    }

    private void initImage() {
        NetworkImageView image = (NetworkImageView) findViewById(R.id.product_image);
        URL imageUrl = productDetails.getImageurl();
        if (imageUrl != null) {
            image.setVisibility(View.VISIBLE);
            image.setImageUrl(imageUrl.toString(), VolleySingleton.getInstance(this).getImageLoader());
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void initModifyButton() {
        Button modifyButton = (Button) findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyProduct = new Intent(ProductDetailsActivity.this, ModifyProductActivity.class);
                modifyProduct.putExtra(ProductLookupResponse.class.getSimpleName(), productDetails);
                startActivityForResult(modifyProduct, MODIFY_PRODUCT_REQUEST_CODE);
            }
        });

        switch (productDetails.getResult()) {
            case KNOWN_STATUS:
                modifyButton.setText(R.string.label_modify_product);
                break;
            case UNKNOWN_PRODUCT:
                modifyButton.setText(R.string.label_add_product);
                break;
            case UNKNOWN_STATUS:
                modifyButton.setText(R.string.label_add_status);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MODIFY_PRODUCT_REQUEST_CODE) {
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void initBackButton() {
        View backButton = findViewById(R.id.scan_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(START_SCANNING, true);
                ProductDetailsActivity.this.setResult(Activity.RESULT_OK, result);
                ProductDetailsActivity.this.finish();
            }
        });
    }

    private void initStatusLine() {
        ViewGroup statusLine = (ViewGroup) findViewById(R.id.statusline);
        TextView status = (TextView) findViewById(R.id.status);

        ResultType result = productDetails.getResult();
        if (result == ResultType.KNOWN_STATUS) {
            StatusType statusType = productDetails.getStatus();
            status.setText(statusType.getDescriptionResource());
            statusLine.setBackgroundColor(getResources().getColor(statusType.getColorResource()));
        } else {
            status.setText(result.getDescriptionResource());
        }

    }

    private void initDescription() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(productDetails.getTitle());

        TextView subtitle = (TextView) findViewById(R.id.subtitle);
        subtitle.setText(productDetails.getSubtitle());

        TextView knownAnimalIngredients = (TextView) findViewById(R.id.contains_animal_ingredients);

        List<String> animalIngredients = createAnimalIngredientList();

        if (animalIngredients.isEmpty()) {
            knownAnimalIngredients.setVisibility(View.GONE);
        } else {
            knownAnimalIngredients.setVisibility(View.VISIBLE);
            String joined = StringUtils.join(animalIngredients, "\n");
            knownAnimalIngredients.setText(joined);
        }
        //TODO vis også kommentarer om "hvorfor tilsetningsstoffer vegansk" eller omvendt

        TextView missingVeganInformation = (TextView) findViewById(R.id.missing_vegan_information);
        if (isVegetarianMaybeVegan()) {
            List<String> unknownIngredients = new ArrayList<>(3);
            StringBuilder message = new StringBuilder(getString(R.string.enquire_vegan_status));

            if (productDetails.getContains_eggs() == null) {
                unknownIngredients.add(getString(R.string.product_contains_eggs));
            }
            if (productDetails.getContains_animal_milk() == null) {
                unknownIngredients.add(getString(R.string.product_contains_animal_milk));
            }
            if (productDetails.getContains_honey() == null) {
                unknownIngredients.add(getString(R.string.product_contains_honey));
            }

            if (!unknownIngredients.isEmpty()) {
                message.append(StringUtils.join(unknownIngredients, ", ").toLowerCase());
                missingVeganInformation.setText(message);
            }

            missingVeganInformation.setVisibility(View.VISIBLE);
        } else {
            missingVeganInformation.setVisibility(View.GONE);
        }

    }

    private boolean isVegetarianMaybeVegan() {
        return StatusType.VEGETARIAN.equals(productDetails.getStatus())
                && (productDetails.getContains_honey() == null
                || productDetails.getContains_animal_milk() == null
                || productDetails.getContains_eggs() == null);
    }

    private List<String> createAnimalIngredientList() {
        List<String> animalIngredients = new ArrayList<>();
        if (Boolean.TRUE.equals(productDetails.getContains_animal_additives())) {
            animalIngredients.add(getString(R.string.product_contains_animal_additives));
        }

        if (Boolean.TRUE.equals(productDetails.getContains_animal_milk())) {
            animalIngredients.add(getString(R.string.product_contains_animal_milk));
        }

        if (Boolean.TRUE.equals(productDetails.getContains_bodyparts())) {
            animalIngredients.add(getString(R.string.product_contains_bodyparts));
        }

        if (Boolean.TRUE.equals(productDetails.getContains_eggs())) {
            animalIngredients.add(getString(R.string.product_contains_eggs));
        }

        if (Boolean.TRUE.equals(productDetails.getContains_honey())) {
            animalIngredients.add(getString(R.string.product_contains_honey));
        }

        if (Boolean.TRUE.equals(productDetails.getAnimal_tested())) {
            animalIngredients.add(getString(R.string.product_is_animal_tested));
        }

        if (Boolean.TRUE.equals(productDetails.getOtherwise_animal_derived())) {
            animalIngredients.add(getString(R.string.product_contains_other_animal_derived));
        }

        return animalIngredients;
    }

}
