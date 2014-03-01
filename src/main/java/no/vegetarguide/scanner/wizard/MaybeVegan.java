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

public class MaybeVegan extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maybe_vegan);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, MaybeVeganFragment.newInstance(product))
                    .commit();
        }

    }

    public static class MaybeVeganFragment extends Fragment {
        private CheckBox contains_animal_milk;
        private CheckBox contains_eggs;
        private CheckBox contains_honey;
        private Product product;

        public static MaybeVeganFragment newInstance(Product productDetails) {
            MaybeVeganFragment frag = new MaybeVeganFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        public MaybeVeganFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_maybe_vegan, container, false);
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
            if (product.isContainsAnimalMilk() != null) {
                contains_animal_milk.setChecked(product.isContainsAnimalMilk());
            }
            contains_eggs = (CheckBox) rootView.findViewById(R.id.contains_eggs);
            if (product.isContainsEggs() != null) {
                contains_eggs.setChecked(product.isContainsEggs());
            }
            contains_honey = (CheckBox) rootView.findViewById(R.id.contains_honey);
            if (product.isContainsHoney() != null) {
                contains_honey.setChecked(product.isContainsHoney());
            }

        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.maybe_vegan_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createNextButton(View rootView) {
            View nextButton = rootView.findViewById(R.id.maybe_vegan_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchNext = new Intent(getActivity(), UncertainIngredients.class);

                    mergeProductValues(product);

                    // TODO if product is non-vegetarian, goto different activity

                    launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                    startActivity(launchNext);
                }
            });
        }

        private void mergeProductValues(Product product) {
            // TODO støtte tri-state? yes, no, undecided?
            product.setContainsAnimalMilk(contains_animal_milk.isChecked());
            product.setContainsEggs(contains_eggs.isChecked());
            product.setContainsHoney(contains_honey.isChecked());
        }

    }
}
