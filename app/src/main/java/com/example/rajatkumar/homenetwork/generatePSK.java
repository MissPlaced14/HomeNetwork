package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class generatePSK extends Activity {
    EditText editTextPSK;
    Button buttonGeneratePSk;
    Button buttonSavePSk;
    Button buttonCancel;
    String randomLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //Creating a Random object to create random PSKs to be stored
    Random random;
    StringBuilder sb;

    final String DB_URL= "jdbc:mysql://192.168.1.240:3306/radius";
    final String USER = "user";
    final String PASS = "P@$$w0rd";
    String value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_psk);
        editTextPSK = (EditText)findViewById(R.id.editTextPSK);
        buttonGeneratePSk = (Button)findViewById(R.id.buttonGeneratePSK);
        buttonSavePSk = (Button)findViewById(R.id.buttonSavePSK);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        random = new Random();
        sb = new StringBuilder();

        buttonCancel.setOnClickListener(e->{
            finish();
        });
    }

    public void generatePSKClicked(View view) {

        sb.setLength(0);
        for (int i =0; i<8; i++){
            sb.append(randomLetters.charAt(random.nextInt(randomLetters.length())));
        }
        editTextPSK.setText(sb);
    }


    public void savePSKClicked(View view) {

        if(editTextPSK.getText().length()<8){
            Toast toast = Toast.makeText(this, "Password shoud be at least 8 digits/alphabets long", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public class RouterQuery extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... args) {

            return value;
        }

        public void onPostExecute(String result) {

        }

    }
}
