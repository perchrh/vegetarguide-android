package no.vegetarguide.scanner.integration;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.CharEncoding;

import java.io.UnsupportedEncodingException;

import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;
import static java.lang.String.format;

public class GsonObjectRequest<R, T> extends Request<T> {

    private static final Gson FILTERING_GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final Listener<T> listener;
    private final Class<R> inputType;
    private final Class<T> outputType;
    private final R object;

    private static final String PROTOCOL_CONTENT_TYPE = format("application/json; charset=%s", CharEncoding.UTF_8);

    public GsonObjectRequest(int method, String url, R postObject, Class<R> inputType, Class<T> outputType,
                             Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.inputType = inputType;
        this.outputType = outputType;
        this.object = postObject;
    }

    @Override
    public byte[] getBody() {
        String jsonString = FILTERING_GSON.toJson(object, inputType);
        try {
            return jsonString.getBytes(CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e); // cannot happen unless VM error
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, parseCharset(response.headers));
            return Response.success(FILTERING_GSON.fromJson(json, outputType), parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
