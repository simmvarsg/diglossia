package thamizh.andro.org.diglossia.net.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arnald on 4/6/16.
 */
public class VolleyQueue {
    private static RequestQueue queue;

    private VolleyQueue() {}

    public static void init(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static RequestQueue getQueue() {
        return queue;
    }
}
