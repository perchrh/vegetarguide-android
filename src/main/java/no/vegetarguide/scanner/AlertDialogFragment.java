package no.vegetarguide.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

    private static final String MESSAGE_KEY = "message";
    private static final String TITLE_KEY = "title";
    private static final String MESSAGESTRING_KEY = "messageString";

    public AlertDialogFragment() {
        super();
    }

    public static AlertDialogFragment newInstance(int title, String message) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE_KEY, title);
        args.putString(MESSAGESTRING_KEY, message);
        frag.setArguments(args);
        return frag;
    }

    public static AlertDialogFragment newInstance(int title, int message) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE_KEY, title);
        args.putInt(MESSAGE_KEY, message);
        frag.setArguments(args);
        return frag;
    }

    /**
     * @param savedInstanceState -
     * @return null if activity is missing
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new IllegalStateException("Missing required state arguments bundle");
        }

        String messageString = arguments.getString(MESSAGESTRING_KEY, null);
        int title = arguments.getInt(TITLE_KEY, -1);
        int message = arguments.getInt(MESSAGE_KEY, -1);

        if ((messageString == null && message < 0) || title < 0) {
            throw new IllegalStateException("Missing required state argument");
        }

        Activity activity = getActivity();

        if (activity == null || activity.isFinishing()) {
            return null;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        if (messageString != null) {
            dialogBuilder.setMessage(messageString);
        } else {
            dialogBuilder.setMessage(message);
        }

        return dialogBuilder.create();
    }

}
