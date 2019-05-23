package com.example.chung.voiceinput;

import android.app.ListActivity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.app.ProgressDialog;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class voiceinput extends ListActivity {
    private TextView txvResult;
    private TextView situation;
    private TextView description;
    private String sitNum;
    private String speech;
    private String reply;
    private String firstSpeech;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voiceinput);
        situation = (TextView) findViewById(R.id.txvSituation);
        description = (TextView) findViewById(R.id.txvDescription);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        setListAdapter(adapter);
        firstSpeech = getIntent().getStringExtra("FIRSTSPEECH");
        listItems.add(firstSpeech);
        adapter.notifyDataSetChanged();

        try{
            String sit = getIntent().getStringExtra("SITUATION");
            String desc = getIntent().getStringExtra("DESCRIPTION");
            String sitNo = getIntent().getStringExtra("SITUATIONNO");
            sitNo = sitNum;
            situation.setText(sit);
            description.setText(desc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSpeechInput(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,10);
        }
        else{
            Toast.makeText(this,"Your device don't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode){
            case 10:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                    speech = txvResult.getText().toString();
                    listItems.add(speech);
                    adapter.notifyDataSetChanged();
                    CallWebService();
                }
                break;
        }
    }

    private void CallWebService(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "http://awch.myqnapcloud.com/fyp/api/keyword/chat_reply.php?s="+ sitNum +"&input="+ speech,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            if (arr != null) {
                                JSONObject obj = arr.getJSONObject(0);
                                reply = obj.getString("reply");
                                listItems.add(reply);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("s", speech);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
