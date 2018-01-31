package com.example.android.hackvsit.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public final class QueryUtils {


    private static QueryUtilsCallback mCallback;
    public static final String BASE_URL = "http://192.168.136.210:3000/api/getData";
    public static final String BASE_PAY_URL = "http://192.168.136.210:3000/api/transaction/";
    public static final String BASE_IMG_URL = "http://192.168.136.217:5000/getImage/?q=\"";


    public static RequestQueue fetchMachineData(RequestQueue queue, final String id, QueryUtilsCallback callback) {
        mCallback = callback;
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(), response);
                        mCallback.returnResponse(response);
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
    public static RequestQueue sendImageData(RequestQueue queue, final String image, QueryUtilsCallback callback) {
        mCallback = callback;
        StringRequest request = new StringRequest(Request.Method.GET, BASE_IMG_URL+image+"\"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(), response);
                        mCallback.returnResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(), error.toString());
            }
        });
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
                        try{

                            JSONObject parent = new JSONObject(response);
                            String price = parent.getString("price");
                            String time = parent.getString("time");
                            mCallback.launchPayPortal(price,time);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

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
        void returnResponse(String response);
        void launchPayPortal(String price,String time);
    }

}
