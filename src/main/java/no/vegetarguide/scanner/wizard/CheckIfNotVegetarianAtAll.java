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

import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.integration.ModifyProductRequest;
import no.vegetarguide.scanner.model.Product;

public class CheckIfNotVegetarianAtAll extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_if_not_vegetarian_at_all);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ModifyProductRequest.class.getSimpleName());
        ModifyProductRequest modifyRequest = (ModifyProductRequest) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CheckIfNotVegetarianAtAllFragment.newInstance(modifyRequest))
                    .commit();
        }

    }

    public static class CheckIfNotVegetarianAtAllFragment extends Fragment {

        private CheckBox contains_bodyparts;
        private CheckBox contains_animal_additives;
        private CheckBox contains_unspecified_possibly_animal_additives;
        private CheckBox manufacturer_confirms_vegetarian;
        private EditText confirmed_vegetarian_comment;
        private ModifyProductRequest modifyRequest;

        public CheckIfNotVegetarianAtAllFragment() {

        }

        public static CheckIfNotVegetarianAtAllFragment newInstance(ModifyProductRequest modifyRequest) {
            CheckIfNotVegetarianAtAllFragment frag = new CheckIfNotVegetarianAtAllFragment();
            Bundle args = new Bundle();
            args.putParcelable(ModifyProductRequest.class.getSimpleName(), modifyRequest);
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
            modifyRequest = arguments.getParcelable(ModifyProductRequest.class.getSimpleName());

            createNextButton(rootView, modifyRequest.getProduct());
            createCancelButton(rootView);
            createCheckBoxes(rootView, modifyRequest.getProduct());

            return rootView;
        }

        private void createCheckBoxes(final View rootView, final Product product) {
            contains_bodyparts = (CheckBox) rootView.findViewById(R.id.contains_bodyparts);
            if (product.getIngredients().getContains_bodyparts() != null) {
                contains_bodyparts.setChecked(product.getIngredients().getContains_bodyparts());
            }
            contains_animal_additives = (CheckBox) rootView.findViewById(R.id.contains_animal_additives);
            if (product.getIngredients().getContains_animal_additives() != null) {
                contains_animal_additives.setChecked(product.getIngredients().getContains_animal_additives());
            }
            contains_unspecified_possibly_animal_additives = (CheckBox) rootView.findViewById(R.id.contains_unspecified_possibly_animal_additives);
            if (product.getIngredients().getContains_unspecified_possibly_animal_additives() != null) {
                contains_unspecified_possibly_animal_additives.setChecked(product.getIngredients().getContains_unspecified_possibly_animal_additives());
            }

            confirmed_vegetarian_comment = (EditText) rootView.findViewById(R.id.confirmed_vegetarian_comment);
            confirmed_vegetarian_comment.setText(product.getConfirmed_vegetarian_comment());

            manufacturer_confirms_vegetarian = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegetarian);
            if (product.getManufacturer_confirms_vegetarian() != null) {
                manufacturer_confirms_vegetarian.setChecked(product.getManufacturer_confirms_vegetarian());
            }

            confirmed_vegetarian_comment.setVisibility(StringUtils.isEmpty(product.getConfirmed_vegetarian_comment())
                    ? View.GONE : View.VISIBLE); // initial value

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
                    if (isChecked || StringUtils.isNotEmpty(product.getConfirmed_vegetarian_comment())) {
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

                    mergeProductValues(product);

                    Intent launchNext;

                    if (product.isMaybeVegetarian()) {
                        launchNext = new Intent(getActivity(), CheckIfVegetarian.class);
                    } else {
                        launchNext = new Intent(getActivity(), EnoughInformation.class);
                    }
                    launchNext.putExtra(ModifyProductRequest.class.getSimpleName(), modifyRequest);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues(Product product) {
            product.getIngredients().setContains_bodyparts(contains_bodyparts.isChecked());
            product.getIngredients().setContains_animal_additives(contains_animal_additives.isChecked());
            product.getIngredients().setContains_unspecified_possibly_animal_additives(contains_unspecified_possibly_animal_additives.isChecked());

            if (manufacturer_confirms_vegetarian.getVisibility() == View.VISIBLE) {
                product.setManufacturer_confirms_vegetarian(manufacturer_confirms_vegetarian.isChecked());
            } else {
                product.setManufacturer_confirms_vegetarian(null);
            }

            if (confirmed_vegetarian_comment.getVisibility() == View.VISIBLE) {
                product.setConfirmed_vegetarian_comment(StringUtils.trimToNull(confirmed_vegetarian_comment.getText().toString()));
            } else {
                product.setConfirmed_vegetarian_comment(null);
            }
        }
    }
}
