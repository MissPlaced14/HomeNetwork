package com.example.rajatkumar.homenetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

public class generatePSK extends AppCompatActivity {
    TextView textPassword;
    Button buttonGeneratePSk;
    Button buttonSavePSk;
    Button buttonCancel;
    private String randomLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //Creating a Random object to create random PSKs to be stored
    Random random;
    StringBuilder sb;

    final String DB_URL= "jdbc:mysql://192.168.1.240:3306/radius";
    final String USER = "user";
    final String PASS = "P@$$w0rd";
    String PSK="";
    String value="";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        textPassword = (TextView)findViewById(R.id.editTextPSK);
        buttonGeneratePSk = (Button)findViewById(R.id.buttonGeneratePSK);
        buttonSavePSk = (Button)findViewById(R.id.buttonSavePSK);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        random = new Random();
        sb = new StringBuilder();
        sb.setLength(0);
        for (int i =0; i<8; i++){
            sb.append(randomLetters.charAt(random.nextInt(randomLetters.length())));
        }
        textPassword.setText(sb);
        buttonCancel.setOnClickListener(e->{
            finish();
        });
    }



    public void savePSKClicked() {
        if(textPassword.getText().length()<8){
            Toast toast = Toast.makeText(this, "Password shoud be at least 8 digits/alphabets long", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Insert insert = new Insert();
            insert.execute(textPassword.getText().toString());

        }
    }

    public void showPSKClicked(View view) {
        startActivity(new Intent(generatePSK.this, ShowPasswordActivity.class));

    }

    public void editPasswordClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.custom_dialog_layout, null);
        EditText editText = (EditText) view2.findViewById(R.id.newPassword);

        builder = new AlertDialog.Builder(generatePSK.this);
        builder.setTitle("Create your own Password");
        builder.setView(view2);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                textPassword.setText(editText.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog2 = builder.create();
        dialog2.show();

    }

    public class Insert extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... args) {
            PSK = args[0];
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

                if(conn == null) {
                    value ="Connection goes wrong";
                } else {
                    String query = "INSERT INTO radreply VALUES (null, '00-00-00-00-00-00', 'Tunnel-Password', ':=', '"+PSK +"') ";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                    value = "Inserted successfully";
                    System.out.println(value);
                }

                conn.close();
            }
            catch (Exception e)
            {
                value ="Connection goes wrong";
                e.printStackTrace();
            }
            return value;
        }

        public void onPostExecute(String result) {
            textPassword.setText("");
            Toast toast = Toast.makeText(generatePSK.this, value, Toast.LENGTH_LONG);
            toast.show();
        }

    }

}
