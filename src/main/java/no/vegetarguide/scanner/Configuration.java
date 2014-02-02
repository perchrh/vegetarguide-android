package no.vegetarguide.scanner;

import android.content.Context;

import static java.lang.String.format;

public class Configuration {

    private final String lookupProductEndPoint;
    private final String modifyProductEndPoint;

    Configuration(final Context context) {
        String baseUrl = context.getString(R.string.server_base_url);
        lookupProductEndPoint = format("%s/%s", baseUrl, context.getString(R.string.lookup_action));
        modifyProductEndPoint = format("%s/%s", baseUrl, context.getString(R.string.modify_action));
    }

    public String getLookupProductEndPoint() {
        return lookupProductEndPoint;
    }

    public String getModifyProductEndPoint() {
        return modifyProductEndPoint;
    }
}