package thamizh.andro.org.diglossia.net.api;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arnald on 4/6/16.
 */
public class SignIn implements Response.Listener<String>, Response.ErrorListener {

    public interface Callback {
        public void onSuccess(String result);
        public void onFailure(String message);
    }

    private Callback callback;
    private String phone;
    private String msgid;

    public void checkUser(Callback callback, final HashMap<String,String> params, boolean isTaxi, String server) {
        this.callback = callback;
        this.phone = params.get("mobileno");
        this.msgid = params.get("msgid");
        String url;

        if (isTaxi) {
            url = server+"track/pages/trk_check_driver_details.php";
        } else {
            url = server+"track/pages/trk_check_emp_details.php";
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, this, this) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            };
        };

        VolleyQueue.getQueue().add(request);
    }

    @Override
    public void onResponse(String response) {
        // FIXME: proper validation
        if (response.contains("exists") || response.contains("~" + phone + "~")) {
            callback.onSuccess(response);
        } else {
            callback.onFailure(response.replace("Error ",""));
       }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callback.onFailure(error.getMessage());
    }
}
