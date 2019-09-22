package com.example.evoke.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.example.evoke.fragments.CameraFragmentRecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Send {
    private static final String TAG = Send.class.getSimpleName();

    public static void SendImageRequest(Bitmap imageBitmap, Context context, CameraFragmentRecyclerViewAdapter cameraRecyclerAdapter) {
        String url = "http://94.182.189.118/api/ai/image/";
        final String[] guessName = {null};

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject object = new JSONObject(new String(response.data));
                        Log.d(TAG, "SendImageRequest: Response " + object);

                        guessName[0] = object.getString("0");
                        String g = object.getString("0");
                        cameraRecyclerAdapter.addToDataSet(g);



                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                },
                error -> {
                    // there is an error
                    // do something about it
                    Log.d(TAG, "onActivityResult: " + error);
                    Log.d(TAG, "onActivityResult: " + error.getMessage());
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", "Amir");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart("image.jpg",
                        getJPGDataFromBitMap(imageBitmap)));

                return params;
            }

        };

        // this policy must be define if the server is low on resource
        // and the server expected to be slow
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    public static byte[] getJPGDataFromBitMap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
