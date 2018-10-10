package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class StartActivity extends Activity {

    private TextView textView;
    private Button buttonStart;
    private CheckBox checkBoxTerms;
    private boolean agreed;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textView = (TextView)findViewById(R.id.welcomeText);
        buttonStart = (Button)findViewById(R.id.buttonStart);
        checkBoxTerms = (CheckBox)findViewById(R.id.checkBoxTerms);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        agreed = sharedPreferences.getBoolean("agreed",false);

        if(agreed){
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonStart.setEnabled(true);
                } else {
                    buttonStart.setEnabled(false);
                }

            }});

    }

    public void buttonStartClicked(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("agreed", true);
        editor.commit();
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
