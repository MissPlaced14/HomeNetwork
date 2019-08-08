package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private static final String file = "userFile";
    private static final String userName = "userName";
    private static final String passWord = "passWord";
    private Button buttonLogin;
    private EditText editTextUserName, editTextPassword;
    private String adminPassword ="password";
    private CheckBox checkBoxRemember;
    private boolean remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "in onCreate(): ");

        buttonLogin = findViewById(R.id.buttonLogin);
        editTextUserName = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);

        SharedPreferences sharedPref = getSharedPreferences( file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        remember = sharedPref.getBoolean("Remember", false);
        if (remember){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        editTextUserName.setText(sharedPref.getString(userName, "email@domain.com"));
        editTextPassword.setText(sharedPref.getString(passWord, "password"));
        buttonLogin.setOnClickListener(e-> {

            if(editTextPassword.getText().toString().equals(adminPassword)) {
                if(checkBoxRemember.isChecked()) {
                    editor.putString(userName, editTextUserName.getText().toString());
                    editor.putString(passWord, editTextPassword.getText().toString());
                    editor.putBoolean("Remember", true);
                    editor.apply();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast toast = Toast.makeText(this, "Invalid username/Password", Toast.LENGTH_LONG);
                toast.show();
            }
        });

}
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "in onResume(): ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME,  "in onStart(): ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "in onPause(): ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "in onStop(): ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "in onDestroy(): ");
    }


}
