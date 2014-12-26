package eu.execom.dry.petclinic.api;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> responseClass;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final String requestBody;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url           URL of the request to make
     * @param responseClass Relevant class object, for Gson's reflection
     * @param headers       Map of request headers
     */
    public GsonRequest(Integer method, String url, Map<String, String> headers, Object request, Class<T> responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.responseClass = responseClass;
        this.headers = headers;
        this.listener = listener;

        if (request != null) this.requestBody = gson.toJson(request);
        else this.requestBody = null;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            if (response.data.length != 0) {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(gson.fromJson(json, responseClass), HttpHeaderParser.parseCacheHeaders(response));
            }else {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    public byte[] getBody() {
        try {
            return this.requestBody == null ? null : this.requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException var2) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{this.requestBody, "utf-8"});
            return null;
        }
    }
}
