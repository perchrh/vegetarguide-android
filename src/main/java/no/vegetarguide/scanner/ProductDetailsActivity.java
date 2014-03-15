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

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.vegetarguide.scanner.integration.VolleySingleton;
import no.vegetarguide.scanner.model.Ingredients;
import no.vegetarguide.scanner.model.ProductLookupResponse;
import no.vegetarguide.scanner.model.ResultType;
import no.vegetarguide.scanner.model.StatusType;
import no.vegetarguide.scanner.wizard.RequestMetaInformation;

import static no.vegetarguide.scanner.Application.MODIFY_PRODUCT_REQUEST_CODE;
import static no.vegetarguide.scanner.Application.START_SCANNING;

public class ProductDetailsActivity extends Activity {

    private ProductLookupResponse lookupResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ProductLookupResponse.class.getSimpleName());
        lookupResponse = (ProductLookupResponse) obj;

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
        URL imageUrl = lookupResponse.getProduct().getImageurl();
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
                Intent modifyProduct = new Intent(ProductDetailsActivity.this, RequestMetaInformation.class);
                modifyProduct.putExtra(ProductLookupResponse.class.getSimpleName(), lookupResponse);
                startActivityForResult(modifyProduct, MODIFY_PRODUCT_REQUEST_CODE);
            }
        });

        switch (lookupResponse.getResult()) {
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

        ResultType result = lookupResponse.getResult();
        if (result == ResultType.KNOWN_STATUS) {
            StatusType statusType = lookupResponse.getStatus();
            status.setText(statusType.getDescriptionResource());
            statusLine.setBackgroundColor(getResources().getColor(statusType.getColorResource()));
        } else {
            status.setText(result.getDescriptionResource());
        }

    }

    private void initDescription() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(lookupResponse.getProduct().getTitle());

        TextView subtitle = (TextView) findViewById(R.id.subtitle);
        subtitle.setText(lookupResponse.getProduct().getSubtitle());

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

            Ingredients ingredients = lookupResponse.getProduct().getIngredients();
            if (ingredients.getContains_eggs() == null) {
                unknownIngredients.add(getString(R.string.product_contains_eggs));
            }
            if (ingredients.getContains_animal_milk() == null) {
                unknownIngredients.add(getString(R.string.product_contains_animal_milk));
            }
            if (ingredients.getContains_insect_excretions() == null) {
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
        return lookupResponse.getProduct().isVegetarian() &&
                lookupResponse.getProduct().isMaybeVegan();
    }

    private List<String> createAnimalIngredientList() {
        List<String> animalIngredients = new ArrayList<>();
        Ingredients ingredients = lookupResponse.getProduct().getIngredients();
        if (Boolean.TRUE.equals(ingredients.getContains_animal_additives())) {
            animalIngredients.add(getString(R.string.product_contains_animal_additives));
        }

        if (Boolean.TRUE.equals(ingredients.getContains_animal_milk())) {
            animalIngredients.add(getString(R.string.product_contains_animal_milk));
        }

        if (Boolean.TRUE.equals(ingredients.getContains_bodyparts())) {
            animalIngredients.add(getString(R.string.product_contains_bodyparts));
        }

        if (Boolean.TRUE.equals(ingredients.getContains_eggs())) {
            animalIngredients.add(getString(R.string.product_contains_eggs));
        }

        if (Boolean.TRUE.equals(ingredients.getContains_insect_excretions())) {
            animalIngredients.add(getString(R.string.product_contains_honey));
        }

        return animalIngredients;
    }

}
