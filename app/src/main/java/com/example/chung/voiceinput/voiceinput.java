package com.example.chung.voiceinput;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private EditText edittext;
    private TextView situation;
    private TextView description;
    private ImageButton voiceBtn;
    private String sitNum;
    private String speech;
    private String reply;
    private String firstSpeech;
    private TextToSpeech tts;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voiceinput);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("yue", "HK"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        voiceBtn.setEnabled(true);
                    }
                }else{
                        Log.e("TTS","Inirialize failed");
                    }
                }
        });

        voiceBtn = (ImageButton) findViewById(R.id.voice);
        situation = (TextView) findViewById(R.id.txvSituation);
        description = (TextView) findViewById(R.id.txvDescription);
        edittext = (EditText)findViewById(R.id.editText);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);
        firstSpeech = getIntent().getStringExtra("FIRSTSPEECH");
        listItems.add(firstSpeech);
        adapter.notifyDataSetChanged();

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                listItems.add(matches.get(0));
                adapter.notifyDataSetChanged();
                speech = matches.get(0);
                CallWebService();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }

        });
        findViewById(R.id.voice).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        edittext.setHint(" ");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        edittext.setHint("Listening...");
                        break;
                }
                return false;
            }
        });


            try {
                String sit = getIntent().getStringExtra("SITUATION");
                String desc = getIntent().getStringExtra("DESCRIPTION");
                String sitNo = getIntent().getStringExtra("SITUATIONNO");
                sitNum = sitNo;
                situation.setText(sit);
                description.setText(desc);
            } catch (Exception e) {
                e.printStackTrace();
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
                                    tts.speak(reply, TextToSpeech.QUEUE_FLUSH,null);

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
