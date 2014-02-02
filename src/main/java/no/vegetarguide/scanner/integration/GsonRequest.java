package no.vegetarguide.scanner.integration;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;

public class GsonRequest<T> extends Request<T> {
    private static final Gson GSON = new Gson();
    private final Class<T> outputType;
    private final Listener<T> listener;

    public GsonRequest(int method, String url, Class<T> outputType, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.outputType = outputType;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, parseCharset(response.headers));
            return Response.success(GSON.fromJson(json, outputType), parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}