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

import no.vegetarguide.scanner.AlertDialogFragment;
import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;

public class CheckIfLactoOvoVegetarian extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_if_lacto_ovo_vegetarian);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CheckIfLactoOvoVegetarianFragment.newInstance(product))
                    .commit();
        }

    }

    public void showToolTip(View v) {
        AlertDialogFragment tooltip = AlertDialogFragment.newInstance(
                R.string.maybe_vegan_tooltip_headline, R.string.maybe_vegan_tooltip);
        if (tooltip != null) {
            tooltip.show(getFragmentManager(), "tooltip");
        }
    }


    public static class CheckIfLactoOvoVegetarianFragment extends Fragment {
        private CheckBox contains_animal_milk;
        private CheckBox contains_eggs;
        private CheckBox contains_insect_excretions;
        private EditText vegetarian_comment;
        private Product product;

        public CheckIfLactoOvoVegetarianFragment() {

        }

        public static CheckIfLactoOvoVegetarianFragment newInstance(Product productDetails) {
            CheckIfLactoOvoVegetarianFragment frag = new CheckIfLactoOvoVegetarianFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check_if_lacto_ovo_vegetarian, container, false);
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
            vegetarian_comment = (EditText) rootView.findViewById(R.id.vegetarian_comment);
            vegetarian_comment.setText(product.getVegetarian_comment());

            contains_animal_milk = (CheckBox) rootView.findViewById(R.id.contains_animal_milk);
            if (product.getContains_animal_milk() != null) {
                contains_animal_milk.setChecked(product.getContains_animal_milk());
            }
            contains_eggs = (CheckBox) rootView.findViewById(R.id.contains_eggs);
            if (product.getContains_eggs() != null) {
                contains_eggs.setChecked(product.getContains_eggs());
            }
            contains_insect_excretions = (CheckBox) rootView.findViewById(R.id.contains_insect_excretions);
            if (product.getContains_insect_excretions() != null) {
                contains_insect_excretions.setChecked(product.getContains_insect_excretions());
            }

            CompoundButton.OnCheckedChangeListener showCommentFieldIfAnyChecked = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (contains_animal_milk.isChecked()
                            || contains_eggs.isChecked()
                            || contains_insect_excretions.isChecked()
                            || StringUtils.isNotEmpty(vegetarian_comment.getText())) {
                        vegetarian_comment.setVisibility(View.VISIBLE);
                    } else {
                        vegetarian_comment.setVisibility(View.GONE);
                    }
                }
            };
            contains_animal_milk.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);
            contains_eggs.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);
            contains_insect_excretions.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);
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
            product.setVegetarian_comment(StringUtils.trimToNull(vegetarian_comment.getText().toString()));
            product.setContains_animal_milk(contains_animal_milk.isChecked());
            product.setContains_eggs(contains_eggs.isChecked());
            product.setContains_insect_excretions(contains_insect_excretions.isChecked());
        }

    }
}
