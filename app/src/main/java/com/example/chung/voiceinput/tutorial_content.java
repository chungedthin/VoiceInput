package com.example.chung.voiceinput;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class tutorial_content extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_content);

        TextView tv = (TextView)findViewById(R.id.content);

        try {
            String tut_content = getIntent().getStringExtra("ITEM_CONTENT");
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(tut_content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
