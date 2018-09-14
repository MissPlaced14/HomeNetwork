package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends Activity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textView = (TextView)findViewById(R.id.welcomeText);
    }

    public void welcomeTextClicked(View view) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
