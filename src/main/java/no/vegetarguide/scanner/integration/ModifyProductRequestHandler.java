package no.vegetarguide.scanner.integration;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import no.vegetarguide.scanner.ScannerApplication;
import no.vegetarguide.scanner.model.ModifyProductResponse;

public class ModifyProductRequestHandler {

    private final ModifyProductRequest product;

    public ModifyProductRequestHandler(ModifyProductRequest product) {
        this.product = product;
    }

    private String getEndpoint() {
        String modifyProductEndPoint = ScannerApplication.getConfiguration().getModifyProductEndPoint();
        Uri.Builder uriBuilder = Uri.parse(modifyProductEndPoint).buildUpon();

        Uri url = uriBuilder.build();
        //noinspection ConstantConditions
        return url.toString();
    }

    public Request execute(RequestQueue queue,
                           Response.Listener<ModifyProductResponse> listener,
                           Response.ErrorListener errorListener) {
        GsonObjectRequest<ModifyProductRequest, ModifyProductResponse> productLookup =
                new GsonObjectRequest<>(Request.Method.POST,
                        getEndpoint(),
                        product,
                        ModifyProductRequest.class,
                        ModifyProductResponse.class,
                        listener,
                        errorListener);

        return queue.add(productLookup);
    }

}