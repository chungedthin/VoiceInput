package com.example.chung.voiceinput;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    private String sitNo;
    private String speech;
    private String reply;
    private String keyword;
    private String firstSpeech;
    private String path;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voiceinput);
        voiceBtn = (ImageButton) findViewById(R.id.voice);
        situation = (TextView) findViewById(R.id.txvSituation);
        description = (TextView) findViewById(R.id.txvDescription);
        edittext = (EditText)findViewById(R.id.editText);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);
        firstSpeech = getIntent().getStringExtra("FIRSTSPEECH");
        String sit = getIntent().getStringExtra("SITUATION");
        String desc = getIntent().getStringExtra("DESCRIPTION");
        String sitNo = getIntent().getStringExtra("SITUATIONNO");
        listItems.add(firstSpeech);
        adapter.notifyDataSetChanged();
        path = "http://awch.myqnapcloud.com/fyp/Audio/" + sitNo + "/情景" + sitNo + "-開場白.mp4";
        playSpeech();

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
                situation.setText(sit);
                description.setText(desc);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

        private void CallWebService(){
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    "http://awch.myqnapcloud.com/fyp/api/keyword/chat_reply.php?s="+ sitNo +"&input="+speech,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (arr != null) {
                                    JSONObject obj = arr.getJSONObject(0);
                                    reply = obj.getString("reply");
                                    keyword = obj.getString("keyword");
                                    listItems.add(reply);
                                    adapter.notifyDataSetChanged();
                                    path = "http://awch.myqnapcloud.com/fyp/Audio/"+ sitNo + "/" + keyword + ".MP4";
                                    playSpeech();
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

        public void playSpeech(){
            try {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(path);
            player.prepare();
            player.start();
            }
            catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
