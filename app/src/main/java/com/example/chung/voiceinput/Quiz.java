
package com.example.chung.voiceinput;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Quiz extends AppCompatActivity{
    private TextView quizQuestion;
    private TextView quiz_score;
    private RadioGroup radioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private int currentQuizQuestion;
    private QuizQuestion firstQuestion;
    private int quizCount;
    private List<QuizQuestion> parsedObject;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        quizQuestion = (TextView)findViewById(R.id.quiz_question);
        quiz_score = (TextView)findViewById(R.id.quiz_score);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        optionOne = (RadioButton)findViewById(R.id.radio0);
        optionTwo = (RadioButton)findViewById(R.id.radio1);
        optionThree = (RadioButton)findViewById(R.id.radio2);
        Button previousButton = (Button)findViewById(R.id.previousquiz);
        Button nextButton = (Button)findViewById(R.id.nextquiz);
        AsyncJsonObject asyncObject = new AsyncJsonObject();
        asyncObject.execute("");


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);
                int correctAns = firstQuestion.getcorrectAnswer();
                if(userSelection == correctAns){
                    // correct
                    Toast.makeText(Quiz.this, "You got the answer correct", Toast.LENGTH_LONG).show();
                    score += 1;
                    currentQuizQuestion++;
                    if(currentQuizQuestion >= quizCount){
                        Toast.makeText(Quiz.this, "End of the Quiz Questions", Toast.LENGTH_LONG).show();
                        quiz_score.setText("Your score is"+score);
                        return;
                    }
                    else{
                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText(firstQuestion.getQuestion());
                        uncheckedRadioButton();
                        optionOne.setText(firstQuestion.getAnswerOptions1());
                        optionTwo.setText(firstQuestion.getAnswerOptions2());
                        optionThree.setText(firstQuestion.getAnswerOptions3());
                    }
                }
                else{
                    // failed
                    Toast.makeText(Quiz.this, "Your answer is wrong!!", Toast.LENGTH_LONG).show();
                    return;
                }

            }

        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuizQuestion--;
                if(currentQuizQuestion < 0){
                    return;
                }
                uncheckedRadioButton();
                firstQuestion = parsedObject.get(currentQuizQuestion);
                quizQuestion.setText(firstQuestion.getQuestion());
                optionOne.setText(firstQuestion.getAnswerOptions1());
                optionTwo.setText(firstQuestion.getAnswerOptions2());
                optionThree.setText(firstQuestion.getAnswerOptions3());
            }
        });
    }

    private class AsyncJsonObject extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost(Constants.URL_QUIZ);
            String jsonResult = "";
            try {
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Quiz.this, "Downloading Quiz","Wait....", true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            parsedObject = returnParsedJsonObject(result);
            if(parsedObject == null){
                return;
            }
            quizCount = parsedObject.size();
            firstQuestion = parsedObject.get(0);
            quizQuestion.setText(firstQuestion.getQuestion());
            optionOne.setText(firstQuestion.getAnswerOptions1());
            optionTwo.setText(firstQuestion.getAnswerOptions2());
            optionThree.setText(firstQuestion.getAnswerOptions3());
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {

                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return answer;
        }
    }

    private List<QuizQuestion> returnParsedJsonObject(String result){
        try {
            List<QuizQuestion> jsonObject = new ArrayList<QuizQuestion>();
            QuizQuestion newItemObject = null;
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonChildNode = null;
                try {
                    jsonChildNode = jsonArray.getJSONObject(i);
                    int id = jsonChildNode.getInt("quiz_id");
                    String question = jsonChildNode.getString("question");
                    String answerOptions1 = jsonChildNode.getString("choice_a");
                    String answerOptions2 = jsonChildNode.getString("choice_b");
                    String answerOptions3 = jsonChildNode.getString("choice_c");
                    int answer = jsonChildNode.getInt("answer");
                    int score = jsonChildNode.getInt("score");
                    newItemObject = new QuizQuestion(id, question, answer, answerOptions1, answerOptions2, answerOptions3, score);
                    jsonObject.add(newItemObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }  catch(Exception e){
                e.printStackTrace();
        }
        return  null;
    }

    private int getSelectedAnswer(int radioSelected){

        int answerSelected = 0;
        if(radioSelected == R.id.radio0){
            answerSelected = 1;
        }

        if(radioSelected == R.id.radio1){
            answerSelected = 2;
        }
        if(radioSelected == R.id.radio2){
            answerSelected = 3;
        }
        return answerSelected;
    }

    private void uncheckedRadioButton(){
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
    }

}