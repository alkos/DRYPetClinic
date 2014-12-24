package eu.execom.dry.petclinic.api;

import android.content.Context;
import android.text.TextUtils;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import eu.execom.dry.petclinic.api.dto.*;
import eu.execom.dry.petclinic.api.enumeration.*;
import java.util.HashMap;
import java.util.Map;

class ApiService {

    private final String TAG = "";
    private final String SECURITY_TOKEN = "X-PETCLINIC-AUTH";
    private final String serverHost;
    private final Context context;
    private RequestQueue mRequestQueue;
    private String securityToken = null;

    public ApiService(String serverHost, Context context) {
        this.serverHost = serverHost;
        this.context = context;
    }

    public String setSecurityToken() {
        return securityToken;
    }

    public void createUser(CreateUserDto requestDto, Response.Listener<ReadUserResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "users";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        post(url, headers, requestDto, ReadUserResponseDto.class, listener, errorListener, tag);
    }

    public void readUser(ReadUserDto requestDto, Response.Listener<ReadUserResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "users/" + requestDto.getId() + "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        get(url, headers, ReadUserResponseDto.class, listener, errorListener, tag);
    }

    public void updateUser(UpdateUserDto requestDto, Response.Listener<ReadUserResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "users/" + requestDto.getId() + "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        UpdateUserBodyDTO bodyDto = new UpdateUserBodyDTO(requestDto.getRole(), requestDto.getPassword());

        put(url, headers, bodyDto, ReadUserResponseDto.class, listener, errorListener, tag);
    }

    public void deleteUser(ReadUserDto requestDto, Response.Listener<Void> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "users/" + requestDto.getId() + "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        delete(url, headers, Void.class, listener, errorListener, tag);
    }

    public void users(UsersDto requestDto, Response.Listener<UsersResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "users";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        get(url, headers, UsersResponseDto.class, listener, errorListener, tag);
    }

    public void adminUsers(AdminUsersDto requestDto, Response.Listener<AdminUsersResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "adminUsers";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        get(url, headers, AdminUsersResponseDto.class, listener, errorListener, tag);
    }

    public void signUp(SignUpDto requestDto, Response.Listener<AuthenticationResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "signUp";

        post(url, null, requestDto, AuthenticationResponseDto.class, listener, errorListener, tag);
    }

    public void signIn(SignInDto requestDto, Response.Listener<AuthenticationResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "signIn";

        post(url, null, requestDto, AuthenticationResponseDto.class, listener, errorListener, tag);
    }

    public void signOut(Response.Listener<Void> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "signOut";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(SECURITY_TOKEN, securityToken);

        post(url, headers, null, Void.class, listener, errorListener, tag);
    }

    public void authenticate(AuthenticationCodeDto requestDto, Response.Listener<AuthenticationResponseDto> listener, Response.ErrorListener errorListener, String tag) {
        String url = serverHost + "/" + "authenticate";

        post(url, null, requestDto, AuthenticationResponseDto.class, listener, errorListener, tag);
    }

    public void cancelPendingRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    protected <T> void get(String url, Map<String, String> headers, Class<T> responseClass,
                           Response.Listener<T> listener, Response.ErrorListener errorListener, String tag) {
        addToRequestQueue(new GsonRequest<T>(Request.Method.GET, url, headers, null, responseClass, listener, errorListener), tag);
    }

    protected <T> void post(String url, Map<String, String> headers, Object request, Class<T> responseClass,
                            Response.Listener<T> listener, Response.ErrorListener errorListener, String tag) {
        addToRequestQueue(new GsonRequest<T>(Request.Method.POST, url, headers, request, responseClass, listener, errorListener), tag);
    }

    protected <T> void put(String url, Map<String, String> headers, Object request, Class<T> responseClass,
                           Response.Listener<T> listener, Response.ErrorListener errorListener, String tag) {
        addToRequestQueue(new GsonRequest<T>(Request.Method.PUT, url, headers, request, responseClass, listener, errorListener), tag);
    }

    protected <T> void delete(String url, Map<String, String> headers, Class<T> responseClass,
                              Response.Listener<T> listener, Response.ErrorListener errorListener, String tag) {
        addToRequestQueue(new GsonRequest<T>(Request.Method.DELETE, url, headers, null, responseClass, listener, errorListener), tag);
    }

    private RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    protected <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);        // set the default tag if tag is empty

        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

}
