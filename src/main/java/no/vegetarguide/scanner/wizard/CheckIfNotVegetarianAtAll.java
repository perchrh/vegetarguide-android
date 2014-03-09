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

public class CheckIfNotVegetarianAtAll extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_if_not_vegetarian_at_all);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CheckIfNotVegetarianAtAllFragment.newInstance(product))
                    .commit();
        }

    }

    public static class CheckIfNotVegetarianAtAllFragment extends Fragment {

        private CheckBox contains_body_parts;
        private CheckBox contains_animal_additives;
        private CheckBox contains_unspecified_possibly_animal_additives;
        private CheckBox manufacturer_confirms_vegetarian;
        private EditText confirmed_vegetarian_comment;
        private Product product;

        public CheckIfNotVegetarianAtAllFragment() {

        }

        public static CheckIfNotVegetarianAtAllFragment newInstance(Product productDetails) {
            CheckIfNotVegetarianAtAllFragment frag = new CheckIfNotVegetarianAtAllFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check_if_not_vegetarian_at_all, container, false);
            Bundle arguments = getArguments();
            if (arguments == null) {
                throw new IllegalStateException("Missing required state arguments bundle");
            }
            product = arguments.getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView, product);
            createCancelButton(rootView);
            createCheckBoxes(rootView, product);

            return rootView;
        }

        private void createCheckBoxes(final View rootView, final Product product) {
            contains_body_parts = (CheckBox) rootView.findViewById(R.id.contains_body_parts);
            if (product.getIngredients().getContains_body_parts() != null) {
                contains_body_parts.setChecked(product.getIngredients().getContains_body_parts());
            }
            contains_animal_additives = (CheckBox) rootView.findViewById(R.id.contains_animal_additives);
            if (product.getIngredients().getContains_animal_additives() != null) {
                contains_animal_additives.setChecked(product.getIngredients().getContains_animal_additives());
            }
            contains_unspecified_possibly_animal_additives = (CheckBox) rootView.findViewById(R.id.contains_unspecified_possibly_animal_additives);
            if (product.getIngredients().getContains_unspecified_possibly_animal_additives() != null) {
                contains_animal_additives.setChecked(product.getIngredients().getContains_unspecified_possibly_animal_additives());
            }

            confirmed_vegetarian_comment = (EditText) rootView.findViewById(R.id.confirmed_vegetarian_comment);
            confirmed_vegetarian_comment.setText(product.getIngredients().getConfirmed_vegetarian_comment());
            manufacturer_confirms_vegetarian = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegetarian);
            if (product.getIngredients().getManufacturer_confirms_vegetarian() != null) {
                manufacturer_confirms_vegetarian.setChecked(product.getIngredients().getManufacturer_confirms_vegetarian());
            }

            confirmed_vegetarian_comment.setVisibility(StringUtils.isEmpty(product.getIngredients().getConfirmed_vegetarian_comment())
                    ? View.GONE : View.VISIBLE); // initial value. TODO add initial value to other edittext comment-fields

            contains_unspecified_possibly_animal_additives.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        manufacturer_confirms_vegetarian.setVisibility(View.VISIBLE);
                    } else {
                        manufacturer_confirms_vegetarian.setVisibility(View.GONE);
                        confirmed_vegetarian_comment.setText(null);
                        confirmed_vegetarian_comment.setVisibility(View.GONE);
                    }
                }
            });
            manufacturer_confirms_vegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked || StringUtils.isNotEmpty(product.getIngredients().getConfirmed_vegetarian_comment())) {
                        confirmed_vegetarian_comment.setVisibility(View.VISIBLE);
                    } else {
                        confirmed_vegetarian_comment.setVisibility(View.GONE);
                    }
                }
            });

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

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.next_wizard_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mergeProductValues();

                    Intent launchNext;

                    if (product.getIngredients().isMaybeVegetarian()) {
                        launchNext = new Intent(getActivity(), CheckIfVegetarian.class);
                    } else {
                        launchNext = new Intent(getActivity(), EnoughInformation.class);
                    }
                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues() {
            product.getIngredients().setContains_body_parts(contains_body_parts.isChecked());
            product.getIngredients().setContains_animal_additives(contains_animal_additives.isChecked());
            product.getIngredients().setContains_unspecified_possibly_animal_additives(contains_unspecified_possibly_animal_additives.isChecked());

            if (manufacturer_confirms_vegetarian.getVisibility() == View.VISIBLE) {
                product.getIngredients().setManufacturer_confirms_vegetarian(manufacturer_confirms_vegetarian.isChecked());
            } else {
                product.getIngredients().setManufacturer_confirms_vegetarian(null);
            }

            if (confirmed_vegetarian_comment.getVisibility() == View.VISIBLE) {
                product.getIngredients().setConfirmed_vegetarian_comment(StringUtils.trimToNull(confirmed_vegetarian_comment.getText().toString()));
            } else {
                product.getIngredients().setConfirmed_vegetarian_comment(null);
            }
        }
    }
}
