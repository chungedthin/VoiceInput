package com.example.chung.voiceinput;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        registerBtn = (Button) findViewById(R.id.registerbtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void validate (String username, String password){
        if(true) {
            // login success
            Intent intent = new Intent(login.this, menu.class);
            startActivity(intent);
        }else
        {
            // login fail

        }
    }

    private void register (){
        if(true) {
            // register success
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        }else
        {
            // register fail

        }
    }

   /* public void postLoginData(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://awch.myqnapcloud.com/jj_homepage/an_fyp/OOP/api/login.php");

        try{
        //ADD username + pw
         String uname = username.getText().toString();
         String pword = password.getText().toString();

         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
         nameValuePairs.add(new BasicNameValuePair("username", uname));
         nameValuePairs.add(new BasicNameValuePair("password", pword));
         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            String str = inputStreamToString(response.getEntity().getContent()).toString();

            if(str.toString().equalsIgnoreCase("true"))
            {
             result.setText("Login successful");
            }else
            {
             result.setText(str);
            }

        } catch (ClientProtocolException e) {
         e.printStackTrace();
        } catch (IOException e) {
         e.printStackTrace();
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
     String line = "";
     StringBuilder total = new StringBuilder();
     // Wrap a BufferedReader around the InputStream
     BufferedReader rd = new BufferedReader(new InputStreamReader(is));
     // Read response until the end
     try {
      while ((line = rd.readLine()) != null) {
        total.append(line);
      }
     } catch (IOException e) {
      e.printStackTrace();
     }
     // Return full string
     return total;
    }

    @Override
    public void onClick(View view) {
      if(view == ok){
        postLoginData();
      }
    }

    }


    } */







}
