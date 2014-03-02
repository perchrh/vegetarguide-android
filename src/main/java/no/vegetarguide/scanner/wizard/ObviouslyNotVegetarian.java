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

        public static ObviouslyNotVegetarianFragment newInstance(Product productDetails) {
            ObviouslyNotVegetarianFragment frag = new ObviouslyNotVegetarianFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        public ObviouslyNotVegetarianFragment() {

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

        private void createCheckBoxes(View rootView, Product product) {
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
                    Intent launchNext = new Intent(getActivity(), MaybeVegan.class);

                    mergeProductValues();

                    // TODO if product is non-vegan, goto different activity

                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues() {
            product.setContainsBodyParts(animal_bodies.isChecked());
            product.setContainsRedListedAdditives(red_listed_additives.isChecked());
            product.setContainsMajorUnspecifiedAdditives(major_unspecified_additives.isChecked());
        }
    }
}
