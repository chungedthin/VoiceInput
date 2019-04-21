package com.example.chung.voiceinput;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class menu extends AppCompatActivity {

    private Button startBtn;
    private Button reviewBtn;
    private Button howToPlayBtn;
    private Button logoutBtn;
    private Button tutorialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        startBtn = (Button) findViewById(R.id.startbtn);
        reviewBtn = (Button) findViewById(R.id.reviewbtn);
        howToPlayBtn = (Button) findViewById(R.id.howtoplaybtn);
        logoutBtn = (Button) findViewById(R.id.logoutbtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, voiceinput.class);
                startActivity(intent);
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, review.class);
                startActivity(intent);
            }
        });

        howToPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, howtoplay.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(menu.this).logout();
                finish();
                startActivity(new Intent(menu.this,login.class));

            }
        });

        tutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu.this,tutorial.class));

            }
        });
    }
}
