package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.MainActivity;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;

public class EnoughInformation extends Activity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enough_information);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, EnoughInformationFragment.newInstance(product))
                    .commit();
        }
    }

    public static class EnoughInformationFragment extends Fragment {


        public EnoughInformationFragment() {
        }

        public static EnoughInformationFragment newInstance(Product productDetails) {
            EnoughInformationFragment frag = new EnoughInformationFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_enough_information, container, false);
            final Product product = getArguments().getParcelable(PRODUCT_DETAILS_KEY);

            final String format = getString(R.string.product_status_format);
            TextView status = (TextView) rootView.findViewById(R.id.status);
            if (product.getIngredients().isAnimalDerivedForCertain()) {
                status.setText(String.format(format, getString(R.string.status_not_vegetarian)));
            } else if (product.getIngredients().isMaybeVegan()) {
                if (product.getIngredients().isVegan()) {
                    status.setText(String.format(format, getString(R.string.status_vegan)));
                } else if (product.getIngredients().isVegetarian()) {
                    status.setText(String.format(format, getString(R.string.status_vegetarian_and_maybe_vegan)));
                } else if (product.getIngredients().isMaybeVegetarian()) {
                    status.setText(String.format(format, getString(R.string.status_maybe_vegetarian_or_maybe_vegan)));
                }
            } else if (product.getIngredients().isVegetarian()) {
                status.setText(String.format(format, getString(R.string.status_vegetarian)));
            } else {
                status.setText(String.format(format, getString(R.string.status_maybe_vegetarian)));
            }

            View submitButton = rootView.findViewById(R.id.submit_button);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO submit to server
                    Toast.makeText(getActivity(), "Her ville produktet blitt sendt inn til serveren", Toast.LENGTH_LONG).show();

                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                    Intent gotoStart = new Intent(getActivity(), MainActivity.class);
                    gotoStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoStart);
                }
            });

            View cancelButton = rootView.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent gotoStart = new Intent(getActivity(), MainActivity.class);
                    gotoStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoStart);
                }
            });
            return rootView;
        }
    }
}
