package com.example.rajatkumar.homenetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/** This class is for randomly generating and storing passwords for VLANs
 */
public class generatePSK extends AppCompatActivity {
    TextView textPassword;
    Button buttonGeneratePSk;
    FloatingActionButton buttonSavePSk;
    FloatingActionButton buttonCancel;
    private Spinner spinner;
    private String randomLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //Creating a Random object to create random PSKs to be stored
    Random random = new Random();
    StringBuilder sb;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        textPassword = findViewById(R.id.editTextPSK);
        buttonGeneratePSk = findViewById(R.id.buttonGeneratePSK);
        buttonSavePSk = findViewById(R.id.buttonSavePSK);
        buttonCancel = findViewById(R.id.buttonCancel);
        spinner = findViewById(R.id.spinnerDeviceType);
        sb = new StringBuilder();
        String[] type = new String[]{
                "Personal",
                "Guest",
                "Smart Device"
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, type
        );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                Log.i("selection", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });

        randomGenerator();
        buttonCancel.setOnClickListener(e -> finish());
    }

    /**
     * Generates random password 8 char long
     */
    public void randomGenerator() {
        sb.setLength(0);
        for (int i = 0; i < 8; i++) {
            sb.append(randomLetters.charAt(random.nextInt(randomLetters.length())));
        }
        textPassword.setText(sb);
    }


    /**
     *
     * @param view
     */
    public void editPasswordClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.custom_dialog_layout, null);
        EditText editText = view2.findViewById(R.id.newPassword);

        builder = new AlertDialog.Builder(generatePSK.this);
        builder.setTitle("Create Password");
        builder.setView(view2);
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (editText.getText().length() < 8) {
                Toast toast = Toast.makeText(generatePSK.this, "Password should be at least 8 Characters", Toast.LENGTH_LONG);
                toast.show();
            } else {
                textPassword.setText(editText.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });
        // Create the AlertDialog
        AlertDialog dialog2 = builder.create();
        dialog2.show();

    }

    public void editPassword(View view) {
        if (textPassword.getText().length() < 8) {
            Toast toast = Toast.makeText(this, "Password should be at least 8 digits/alphabets long", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Intent intent = new Intent(this, RouterQueryService.class);
            //action: edit password
            intent.putExtra("action", "addVlan");
            intent.putExtra("ssid","theSSID");
            intent.putExtra("pw", textPassword.getText());
        }
    }


}