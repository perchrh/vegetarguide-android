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

import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.MainActivity;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;

public class UncertainIngredients extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncertain_ingredients);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, UncertainIngredientsFragment.newInstance(product))
                    .commit();
        }

    }

    public static class UncertainIngredientsFragment extends Fragment {

        private CheckBox animal_e_number;
        private CheckBox manufacturer_confirms_vegan;
        private CheckBox other_animal_derived_additives;

        public static UncertainIngredientsFragment newInstance(Product product) {
            UncertainIngredientsFragment frag = new UncertainIngredientsFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, product);
            frag.setArguments(args);
            return frag;
        }

        public UncertainIngredientsFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_uncertain_ingredients, container, false);
            final Product product = getArguments().getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView, product);
            createCancelButton(rootView);
            createCheckBoxes(rootView, product);

            return rootView;
        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.uncertain_ingredients_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createCheckBoxes(View rootView, Product product) {
            animal_e_number = (CheckBox) rootView.findViewById(R.id.animal_e_number);
            if (product.isContainsPossibleAnimalEnumbers() != null) {
                animal_e_number.setChecked(product.isContainsPossibleAnimalEnumbers());
            }
            other_animal_derived_additives = (CheckBox) rootView.findViewById(R.id.other_animal_derived_additives);
            if (product.isContainsPossibleAnimalAdditives() != null) {
                other_animal_derived_additives.setChecked(product.isContainsPossibleAnimalAdditives());
            }
            manufacturer_confirms_vegan = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegan);
            if (product.isManufacturerConfirmsProductIsVegan() != null) {
                manufacturer_confirms_vegan.setChecked(product.isManufacturerConfirmsProductIsVegan());
            }
        }

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.uncertain_ingredients_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send productDetails over network, wait for result

                    mergeProductValues(product);

                    Intent gotoStart = new Intent(getActivity(), MainActivity.class);
                    gotoStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoStart);
                }
            });
        }

        private void mergeProductValues(Product product) {
            product.setContainsPossibleAnimalEnumbers(animal_e_number.isChecked());
            product.setContainsPossibleAnimalAdditives(other_animal_derived_additives.isChecked());
            product.setManufacturerConfirmsProductIsVegan(manufacturer_confirms_vegan.isChecked());
        }
    }
}
