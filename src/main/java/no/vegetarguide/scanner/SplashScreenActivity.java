package no.vegetarguide.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;


public class SplashScreenActivity extends Activity {

    private final static long SPLASH_DELAY = 2000L;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);
        registerClickHandler();
        removeSplashScreenAfterDelay();
    }

    private void registerClickHandler() {
        ViewGroup screen = (ViewGroup) findViewById(R.id.screen);
        screen.setOnClickListener(new SingleClickListener() {
            public void onSingleClick(View v) {
                startMainActivity();
            }
        });
    }

    private void removeSplashScreenAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startMainActivity();
            }
        }, SPLASH_DELAY);
    }

    private void startMainActivity() {
        if (isFinishing()) {
            return;
        }
        finish(); //makes back button not return to this activity
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

}