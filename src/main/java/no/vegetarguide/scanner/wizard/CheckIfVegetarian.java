package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;

public class CheckIfVegetarian extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_if_vegetarian);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CheckIfVegetarianFragment.newInstance(product))
                    .commit();
        }

    }

    public static class CheckIfVegetarianFragment extends Fragment {
        private CheckBox contains_animal_milk;
        private CheckBox contains_eggs;
        private CheckBox contains_insect_excretions;
        private EditText vegetarian_comment;
        private Product product;

        public CheckIfVegetarianFragment() {

        }

        public static CheckIfVegetarianFragment newInstance(Product productDetails) {
            CheckIfVegetarianFragment frag = new CheckIfVegetarianFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check_if_vegetarian, container, false);
            Bundle arguments = getArguments();
            if (arguments == null) {
                throw new IllegalStateException("Missing required state arguments bundle");
            }
            product = arguments.getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView);
            createCancelButton(rootView);

            createCheckBoxes(rootView);

            return rootView;
        }

        private void createCheckBoxes(View rootView) {
            contains_animal_milk = (CheckBox) rootView.findViewById(R.id.contains_animal_milk);
            if (product.getIngredients().getContains_animal_milk() != null) {
                contains_animal_milk.setChecked(product.getIngredients().getContains_animal_milk());
            }
            contains_eggs = (CheckBox) rootView.findViewById(R.id.contains_eggs);
            if (product.getIngredients().getContains_eggs() != null) {
                contains_eggs.setChecked(product.getIngredients().getContains_eggs());
            }
            contains_insect_excretions = (CheckBox) rootView.findViewById(R.id.contains_insect_excretions);
            if (product.getIngredients().getContains_insect_excretions() != null) {
                contains_insect_excretions.setChecked(product.getIngredients().getContains_insect_excretions());
            }
        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.cancel_wizard_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createNextButton(View rootView) {
            View nextButton = rootView.findViewById(R.id.next_wizard_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mergeProductValues(product);

                    Intent launchNext;
                    if (product.isMaybeVegan()) {
                        launchNext = new Intent(getActivity(), CheckIfVegan.class);
                    } else {
                        launchNext = new Intent(getActivity(), EnoughInformation.class);
                    }

                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues(Product product) {
            product.getIngredients().setContains_animal_milk(contains_animal_milk.isChecked());
            product.getIngredients().setContains_eggs(contains_eggs.isChecked());
            product.getIngredients().setContains_insect_excretions(contains_insect_excretions.isChecked());
        }

    }
}
