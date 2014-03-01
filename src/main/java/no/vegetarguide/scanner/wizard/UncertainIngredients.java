package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        Parcelable obj = b.getParcelable(Product.class.getSimpleName());
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, UncertainIngredientsFragment.newInstance(product))
                    .commit();
        }

    }

    public static class UncertainIngredientsFragment extends Fragment {

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
            final Parcelable product = getArguments().getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView, (Product) product);
            createCancelButton(rootView);

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

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.uncertain_ingredients_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send productDetails over network, wait for result

                    Intent gotoStart = new Intent(getActivity(), MainActivity.class);
                    gotoStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoStart);
                }
            });
        }
    }
}
