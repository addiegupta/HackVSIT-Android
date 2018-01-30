package com.example.android.hackvsit.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public final class QueryUtils {


    public static final String BASE_URL="http://192.168.136.213:3000/api/getData";

    public static RequestQueue addVolleyHttpRequest(RequestQueue queue, boolean isGetRequest,
                                                    String volleyUrl,String id){
        int requestMethod;
        if (isGetRequest){
            requestMethod = Request.Method.GET;
        }
        else {
            requestMethod = Request.Method.POST;
        }
        StringRequest request = new StringRequest(requestMethod,volleyUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(),response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(),error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(request);
        return queue;
    }

    public static RequestQueue postHttpRequest(RequestQueue queue, final String id){
        StringRequest request = new StringRequest(Request.Method.POST,BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(QueryUtils.class.getSimpleName(),response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(QueryUtils.class.getSimpleName(),error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("machineId",id);
                return params;}
        };
        queue.add(request);
        return queue;
    }

}
