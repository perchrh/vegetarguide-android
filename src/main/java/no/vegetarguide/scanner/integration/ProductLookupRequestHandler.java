package no.vegetarguide.scanner.integration;

import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import no.vegetarguide.scanner.ScannerApplication;
import no.vegetarguide.scanner.model.ProductLookupResponse;

public class ProductLookupRequestHandler {

    private final String gtin;
    private final String formatName;

    public ProductLookupRequestHandler(String gtin, String formatName) {
        this.gtin = gtin;
        this.formatName = formatName;
    }

    private String getEndpoint() {
        String lookupProductEndPoint = ScannerApplication.getConfiguration().getLookupProductEndPoint();
        Uri.Builder uriBuilder = Uri.parse(lookupProductEndPoint).buildUpon();
        uriBuilder.appendQueryParameter("gtin", gtin);
        uriBuilder.appendQueryParameter("format", formatName);

        Uri url = uriBuilder.build();
        //noinspection ConstantConditions
        return url.toString();
    }

    public Request execute(RequestQueue queue,
                           Response.Listener<ProductLookupResponse> listener,
                           Response.ErrorListener errorListener) {
        GsonRequest<ProductLookupResponse> productLookup = new GsonRequest<>(Request.Method.GET,
                getEndpoint(),
                ProductLookupResponse.class,
                listener,
                errorListener);

        productLookup.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 3, 1.2f));

        return queue.add(productLookup);
    }

}