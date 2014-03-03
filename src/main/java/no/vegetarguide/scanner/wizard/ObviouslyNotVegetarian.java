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

public class ObviouslyNotVegetarian extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obviously_not_vegetarian);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ObviouslyNotVegetarianFragment.newInstance(product))
                    .commit();
        }

    }

    public static class ObviouslyNotVegetarianFragment extends Fragment {

        private Product product;
        private CheckBox animal_bodies;
        private CheckBox red_listed_additives;
        private CheckBox major_unspecified_additives;
        private EditText comment;
        private CheckBox manufacturer_confirms_vegetarian;
        private EditText confirmed_vegetarian_comment;

        public ObviouslyNotVegetarianFragment() {

        }

        public static ObviouslyNotVegetarianFragment newInstance(Product productDetails) {
            ObviouslyNotVegetarianFragment frag = new ObviouslyNotVegetarianFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_obviously_not_vegetarian, container, false);
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
            comment = (EditText) rootView.findViewById(R.id.obviously_not_vegetarian_comment);
            comment.setText(product.getNotLactoOvoVegetarianComment());

            animal_bodies = (CheckBox) rootView.findViewById(R.id.animal_bodies);
            if (product.getContainsBodyParts() != null) {
                animal_bodies.setChecked(product.getContainsBodyParts());
            }
            red_listed_additives = (CheckBox) rootView.findViewById(R.id.red_listed_additives);
            if (product.getContainsRedListedAdditives() != null) {
                red_listed_additives.setChecked(product.getContainsRedListedAdditives());
            }
            major_unspecified_additives = (CheckBox) rootView.findViewById(R.id.major_unspecified_additives);
            if (product.getContainsMajorUnspecifiedAdditives() != null) {
                red_listed_additives.setChecked(product.getContainsMajorUnspecifiedAdditives());
            }
            CompoundButton.OnCheckedChangeListener showCommentFieldIfAnyChecked = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (animal_bodies.isChecked()
                            || red_listed_additives.isChecked()
                            || StringUtils.isNotEmpty(comment.getText())) {
                        comment.setVisibility(View.VISIBLE);
                    } else {
                        comment.setVisibility(View.GONE);
                    }
                }
            };
            animal_bodies.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);
            red_listed_additives.setOnCheckedChangeListener(showCommentFieldIfAnyChecked);

            confirmed_vegetarian_comment = (EditText) rootView.findViewById(R.id.confirmed_vegetarian_comment);
            confirmed_vegetarian_comment.setText(product.getConfirmedLactoOvoVegetarianComment());
            manufacturer_confirms_vegetarian = (CheckBox) rootView.findViewById(R.id.manufacturer_confirms_vegetarian);
            if (product.getManufacturerConfirmsProductIsLactoOvoVegetarian() != null) {
                manufacturer_confirms_vegetarian.setChecked(product.getManufacturerConfirmsProductIsLactoOvoVegetarian());
            }

            confirmed_vegetarian_comment.setVisibility(StringUtils.isEmpty(product.getConfirmedLactoOvoVegetarianComment())
                    ? View.GONE : View.VISIBLE); // initial value. TODO add initial value to other edittext comment-fields

            major_unspecified_additives.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    if (isChecked || StringUtils.isNotEmpty(product.getConfirmedLactoOvoVegetarianComment())) {
                        confirmed_vegetarian_comment.setVisibility(View.VISIBLE);
                    } else {
                        confirmed_vegetarian_comment.setVisibility(View.GONE);
                    }
                }
            });

        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.obviously_not_vegetarian_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.obviously_not_vegetarian_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mergeProductValues();

                    Intent launchNext;

                    if (product.isMaybeLactoOvoVegetarian()) {
                        launchNext = new Intent(getActivity(), MaybeVegan.class);
                    } else {
                        launchNext = new Intent(getActivity(), EnoughInformation.class);
                    }
                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues() {
            // TODO update with new fields
            product.setContainsBodyParts(animal_bodies.isChecked());
            product.setContainsRedListedAdditives(red_listed_additives.isChecked());
            product.setContainsMajorUnspecifiedAdditives(major_unspecified_additives.isChecked());
        }
    }
}
