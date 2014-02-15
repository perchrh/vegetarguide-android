package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class ManualInputActivity extends Activity {

    private static final int GTIN_AVAILABLE_RETURN_CODE = 1005;
    public static final String GTIN_EXTRA = "gtin";
    public static final int REQUEST_MANUAL_INPUT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View inflatedView = inflater.inflate(R.layout.fragment_manual_input, container, false);

            final TextView inputField = (TextView) inflatedView.findViewById(R.id.manual_input_gtin_edittext);
            View searchButton = inflatedView.findViewById(R.id.manual_gtin_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        String gtin = inputField.getText().toString();
                        Intent returnValue = new Intent();
                        returnValue.putExtra(GTIN_EXTRA, gtin);

                        activity.setResult(GTIN_AVAILABLE_RETURN_CODE, returnValue);
                        activity.finish();
                    }
                }
            });

            return inflatedView;
        }
    }


}
