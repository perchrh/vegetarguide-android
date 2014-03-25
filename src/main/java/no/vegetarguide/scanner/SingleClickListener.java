package no.vegetarguide.scanner;

import android.view.View;

/**
 * Avoids the user double-clicking buttons resulting in possible double submits or similar
 */
public abstract class SingleClickListener implements View.OnClickListener {
    private boolean enabled = true;

    @Override
    public final void onClick(View v) {
        doOnClick(v);
    }

    private synchronized void doOnClick(View v) {
        if (enabled) {
            disableClicks(v);
            onSingleClick(v);
            enableClicks(v);
        }
    }

    private void disableClicks(View v) {
        enabled = false;
        v.setEnabled(false);
    }

    public abstract void onSingleClick(View v);

    private void enableClicks(View v) {
        enabled = true;
        v.setEnabled(true);
    }
}