package com.example.chung.voiceinput;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        registerBtn = (Button) findViewById(R.id.registerbtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in..");
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, menu.class));
            return;
        }
    }

    private void userLogin(){
        final String uname = username.getText().toString();
        final String pword = password.getText().toString();
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        progressDialog.dismiss();
                        try{
                            JSONObject obj = new JSONObject(response);

                            if(obj.has("userId") && obj.has("userName") && obj.has("email")){
                                // successfully login
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("userId"),
                                                obj.getString("userName"),
                                                obj.getString("email")
                                        );
                                startActivity(new Intent(getApplicationContext(),menu.class));
                                finish();
                            } else if(obj.has("message")) {
                                // login failed with message
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                // unexpected response
                                Toast.makeText(getApplicationContext(), obj.getString("Unexpected server response"), Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", uname);
                params.put("password", pword);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void register(){
        if(true) {
            // register success
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);}
        else
        {
            // register fail
        }
    }

    public void onClick(View view){
        if(view==loginBtn){
            userLogin();
        }
    }
}
