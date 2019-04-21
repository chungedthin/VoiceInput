package com.example.chung.voiceinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class tutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        }

    private void CallWebService(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_KEYWORD ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray arr = new JSONArray(response);
                            if(arr!=null){
                                JSONObject obj = arr.getJSONObject(0);
                                String type = obj.getString("type");
                                txvComment.setText(type);
                            }

                        } catch (JSONException e) {
                            // unexpected response
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("keyword", speech);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
