package com.example.android.hackvsit.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.hackvsit.model.Machine;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public final class QueryUtils {


    private static QueryUtilsCallback mCallback;
    public static final String BASE_URL = "http://192.168.136.210:3000/api/getData";
    public static final String BASE_PAY_URL = "http://192.168.136.210:3000/api/transaction/";

    public static RequestQueue addVolleyHttpRequest(RequestQueue queue, boolean isGetRequest,
                                                    String volleyUrl, String id) {
        int requestMethod;
        if (isGetRequest) {
            requestMethod = Request.Method.GET;
        } else {
            requestMethod = Request.Method.POST;
        }
        StringRequest request = new StringRequest(requestMethod, volleyUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(), response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(), error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(request);
        return queue;
    }

    public static RequestQueue fetchMachineData(RequestQueue queue, final String id, QueryUtilsCallback callback) {
        mCallback = callback;
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(), response);
                        mCallback.setupMachine(
                                JSONUtils.getMachineDetails(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(), error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("machineId", id);
                return params;
            }
        };
        queue.add(request);
        return queue;
    }
    public static RequestQueue makePayment(RequestQueue queue, final String vendId, final JSONObject object,
                                           QueryUtilsCallback callback){
        mCallback = callback;
        String PAY_URL = BASE_PAY_URL + vendId;
        StringRequest request = new StringRequest(Request.Method.POST, PAY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(), response);
                        mCallback.launchPayPortal();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(), error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("json", object.toString());
                return params;
            }
        };
        queue.add(request);
        return queue;
    }

    public interface QueryUtilsCallback {
        void setupMachine(Machine machine);
        void launchPayPortal();
    }

}
