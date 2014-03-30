package no.vegetarguide.scanner.integration;

import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import no.vegetarguide.scanner.ScannerApplication;

public class ModifyProductRequestHandler {

    private final ModifyProductRequest request;

    public ModifyProductRequestHandler(ModifyProductRequest request) {
        this.request = request;
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
        GsonObjectRequest<ModifyProductRequest, ModifyProductResponse> modifyRequest =
                new GsonObjectRequest<>(Request.Method.POST,
                        getEndpoint(),
                        request,
                        ModifyProductRequest.class,
                        ModifyProductResponse.class,
                        listener,
                        errorListener);

        modifyRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));

        return queue.add(modifyRequest);
    }

}