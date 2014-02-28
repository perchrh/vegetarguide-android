package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.model.ProductLookupResponse;

public class ObviouslyNotVegetarian extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obviously_not_vegetarian);

//        Bundle b = getIntent().getExtras();
//        Parcelable obj = b.getParcelable(ProductLookupResponse.class.getSimpleName());
//        ProductLookupResponse productDetails = (ProductLookupResponse) obj;
        ProductLookupResponse productDetails = new ProductLookupResponse();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ObviouslyNotVegetarianFragment.newInstance(productDetails))
                    .commit();
        }

    }

    public static class ObviouslyNotVegetarianFragment extends Fragment {
        private static final String PRODUCT_DETAILS_KEY = "product_details";
        private String[] redList;

        public static ObviouslyNotVegetarianFragment newInstance(ProductLookupResponse productDetails) {
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

            return rootView;
        }
    }
}
