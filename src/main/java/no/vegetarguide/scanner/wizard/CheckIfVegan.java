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
import android.widget.CompoundButton;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;

public class CheckIfVegan extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_if_vegan);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CheckIfVeganFragment.newInstance(product))
                    .commit();
        }

    }

    public static class CheckIfVeganFragment extends Fragment {

        private CheckBox possible_animal_derived_additives;
        private CheckBox manufacturer_confirms_vegan;
        private EditText confirmed_vegan_comment;

        public CheckIfVeganFragment() {

        }

        public static CheckIfVeganFragment newInstance(Product product) {
            CheckIfVeganFragment frag = new CheckIfVeganFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, product);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check_if_vegan, container, false);
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
            confirmed_vegan_comment = (EditText) rootView.findViewById(R.id.confirmed_vegan_comment);
            confirmed_vegan_comment.setText(product.getConfirmedVeganComment());

            possible_animal_derived_additives = (CheckBox) rootView.findViewById(R.id.possible_animal_derived_additives);
            if (product.getContainsPossibleAnimalAdditives() != null) {
                possible_animal_derived_additives.setChecked(product.getContainsPossibleAnimalAdditives());
            }
            manufacturer_confirms_vegan = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegan);
            if (product.getManufacturerConfirmsProductIsVegan() != null) {
                manufacturer_confirms_vegan.setChecked(product.getManufacturerConfirmsProductIsVegan());
            }

            CompoundButton.OnCheckedChangeListener showCommentFieldIfAnyChecked = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (possible_animal_derived_additives.isChecked()
                            || StringUtils.isNotEmpty(confirmed_vegan_comment.getText())) {
                        manufacturer_confirms_vegan.setVisibility(View.VISIBLE);
                    } else {
                        manufacturer_confirms_vegan.setVisibility(View.GONE);
                    }

                    if (!manufacturer_confirms_vegan.isChecked() && StringUtils.isEmpty(confirmed_vegan_comment.getText())) {
                        confirmed_vegan_comment.setVisibility(View.GONE);
                    }
                }
            };
            possible_animal_derived_additives.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);
            manufacturer_confirms_vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked || StringUtils.isNotEmpty(confirmed_vegan_comment.getText())) {
                        confirmed_vegan_comment.setVisibility(View.VISIBLE);
                    } else {
                        confirmed_vegan_comment.setVisibility(View.GONE);
                    }

                }
            });
        }

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.uncertain_ingredients_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mergeProductValues(product);

                    Intent launchNext = new Intent(getActivity(), EnoughInformation.class);
                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);

                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues(Product product) {
            if (confirmed_vegan_comment.getVisibility() == View.VISIBLE) {
                product.setConfirmedVeganComment(StringUtils.trimToNull(confirmed_vegan_comment.getText().toString()));
            }
            product.setContainsPossibleAnimalAdditives(possible_animal_derived_additives.isChecked());
            product.setManufacturerConfirmsProductIsVegan(manufacturer_confirms_vegan.isChecked());
        }
    }
}
