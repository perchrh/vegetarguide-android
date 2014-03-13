package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
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

    public void viewYellowList(View v) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.vegansamfunnet.no/yellow_list_no.html"));
        startActivity(browse);
    }

    public static class CheckIfVeganFragment extends Fragment {

        private CheckBox possible_animal_derived_additives;
        // TODO add animal tested and other animal derived if drinkable (isinglass etc)
        private CheckBox manufacturer_confirms_vegan;
        private EditText confirmed_vegan_comment;
        private Product product;

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
            product = getArguments().getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView);
            createCancelButton(rootView);
            createCheckBoxes(rootView);

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

        private void createCheckBoxes(View rootView) {
            confirmed_vegan_comment = (EditText) rootView.findViewById(R.id.confirmed_vegan_comment);
            confirmed_vegan_comment.setText(product.getIngredients().getConfirmed_vegan_comment());
            confirmed_vegan_comment.setVisibility(StringUtils.isEmpty(product.getIngredients().getConfirmed_vegan_comment())
                    ? View.GONE : View.VISIBLE); // initial value

            possible_animal_derived_additives = (CheckBox) rootView.findViewById(R.id.possible_animal_derived_additives);
            if (product.getIngredients().getContains_possible_animal_additives() != null) {
                possible_animal_derived_additives.setChecked(product.getIngredients().getContains_possible_animal_additives());
            }
            manufacturer_confirms_vegan = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegan);
            if (product.getIngredients().getManufacturer_confirms_vegan() != null) {
                manufacturer_confirms_vegan.setChecked(product.getIngredients().getManufacturer_confirms_vegan());
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

        private void createNextButton(View rootView) {
            View nextButton = rootView.findViewById(R.id.next_wizard_button);
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
                product.getIngredients().setConfirmed_vegan_comment(StringUtils.trimToNull(confirmed_vegan_comment.getText().toString()));
            } else {
                product.getIngredients().setConfirmed_vegan_comment(null);
            }
            product.getIngredients().setContains_possible_animal_additives(possible_animal_derived_additives.isChecked());
            product.getIngredients().setManufacturer_confirms_vegan(manufacturer_confirms_vegan.isChecked());
        }
    }
}
