package no.vegetarguide.scanner;

import android.app.Application;

public class ScannerApplication extends Application {

    private static Configuration configuration;

    public static Configuration getConfiguration() {
        return configuration; // guaranteed to be initialized when first Activity loads
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configuration = new Configuration(this);
    }

}
