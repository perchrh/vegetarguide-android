package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ManualInputActivity extends BaseActivity {

    public static final int GTIN_AVAILABLE_RETURN_CODE = 1005;
    public static final String GTIN_EXTRA = "gtin";
    public static final int REQUEST_MANUAL_INPUT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ManualInputFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ManualInputFragment extends Fragment {

        public ManualInputFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View inflatedView = inflater.inflate(R.layout.fragment_manual_input, container, false);

            final TextView inputField = (TextView) inflatedView.findViewById(R.id.manual_input_gtin_edittext);
            inputField.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_ENTER) {

                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            // do nothing yet
                        } else if (event.getAction() == KeyEvent.ACTION_UP) {
                            returnResult(inputField.getText().toString());
                        }
                        // don't propagate the Enter key up
                        // since it was our task to handle it.
                        return true;

                    } else {
                        // it is not an Enter key - let others handle the event
                        return false;
                    }
                }

            });
            View searchButton = inflatedView.findViewById(R.id.manual_gtin_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnResult(inputField.getText().toString());
                }

            });

            return inflatedView;
        }

        private void returnResult(final String gtin) {
            Activity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                Intent returnValue = new Intent();
                returnValue.putExtra(GTIN_EXTRA, gtin);

                activity.setResult(GTIN_AVAILABLE_RETURN_CODE, returnValue);
                activity.finish();
            }
        }

    }


}
