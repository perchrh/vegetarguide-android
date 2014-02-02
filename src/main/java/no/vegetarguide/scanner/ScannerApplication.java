package no.vegetarguide.scanner;

import android.app.Application;

public class ScannerApplication extends Application {

    private static Configuration configuration;

    @Override
    public void onCreate() {
        super.onCreate();
        configuration = new Configuration(this);
    }

    public static Configuration getConfiguration() {
        return configuration; // guaranteed to be initialized when first Activity loads
    }

}
