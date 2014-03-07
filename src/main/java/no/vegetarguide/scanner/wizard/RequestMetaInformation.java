package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import no.vegetarguide.scanner.Application;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.Product;

import static no.vegetarguide.scanner.Application.PRODUCT_DETAILS_KEY;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class RequestMetaInformation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metainformation);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(Application.PRODUCT_DETAILS_KEY);
        Product product = (Product) obj;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, MetaInformationFragment.newInstance(product))
                    .commit();
        }

    }

    public static class MetaInformationFragment extends Fragment {

        private Product product;
        private EditText brand_edit;
        private TextView gtin_value;
        private EditText title_edit;
        private EditText subtitle_edit;
        private EditText comment;

        public MetaInformationFragment() {

        }

        public static MetaInformationFragment newInstance(Product productDetails) {
            MetaInformationFragment frag = new MetaInformationFragment();
            Bundle args = new Bundle();
            args.putParcelable(PRODUCT_DETAILS_KEY, productDetails);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_metainformation, container, false);
            Bundle arguments = getArguments();
            if (arguments == null) {
                throw new IllegalStateException("Missing required state arguments bundle");
            }
            product = arguments.getParcelable(PRODUCT_DETAILS_KEY);

            createNextButton(rootView, product);
            createCancelButton(rootView);
            createTextEdits(rootView, product);

            return rootView;
        }

        private void createTextEdits(View rootView, Product product) {
            gtin_value = (TextView) rootView.findViewById(R.id.gtin_value);
            gtin_value.setText(product.getGtin());

            title_edit = (EditText) rootView.findViewById(R.id.title_edit);
            title_edit.setText(product.getTitle());
            subtitle_edit = (EditText) rootView.findViewById(R.id.subtitle_edit);
            subtitle_edit.setText(product.getSubtitle());
            brand_edit = (EditText) rootView.findViewById(R.id.brand_edit);
            brand_edit.setText(product.getBrand());

            comment = (EditText) rootView.findViewById(R.id.commentary_edit);
            comment.setText(product.getGeneralComment());
        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.metainformation_cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.metainformation_next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchNext = new Intent(getActivity(), CheckIfNotVegetarianAtAll.class);

                    mergeProductValues();
                    if (missingValues()) {
                        //TODO show popup saying what is wrong
                    } else {
                        launchNext.putExtra(PRODUCT_DETAILS_KEY, product);
                        startActivity(launchNext);
                    }
                }
            });
        }

        private boolean missingValues() {
            // validate input, check for required fields
            return false;
        }

        private void mergeProductValues() {
            String trimmedComment = trimToNull(comment.getText().toString());
            product.setGeneralComment(trimmedComment); // overwrite previous value always

            String trimmedTitle = trimToNull(title_edit.getText().toString());
            product.setTitle(trimmedTitle == null ? product.getTitle() : trimmedTitle); // keep previous value if missing

            String trimmedSubtitle = trimToNull(subtitle_edit.getText().toString());
            product.setSubtitle(trimmedSubtitle == null ? product.getSubtitle() : trimmedSubtitle); // keep previous value if missing

            String trimmedBrand = trimToNull(brand_edit.getText().toString());
            product.setBrand(trimmedBrand == null ? product.getBrand() : trimmedBrand); // keep previous value if missing
        }
    }
}
