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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class generatePSK extends AppCompatActivity{
    TextView textPassword;
    Button buttonGeneratePSk;
    FloatingActionButton buttonSavePSk;
    FloatingActionButton  buttonCancel;
    private Spinner spinner;
    private String randomLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //Creating a Random object to create random PSKs to be stored
    Random random = new Random();
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
        buttonSavePSk = (FloatingActionButton )findViewById(R.id.buttonSavePSK);
        buttonCancel = (FloatingActionButton )findViewById(R.id.buttonCancel);
        spinner = (Spinner)findViewById(R.id.spinnerDeviceType);
        sb = new StringBuilder();
        String[] type = new String[]{
                "Personal",
                "Guest",
                "Smart Device"
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, type
        );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                Log.i("selection",parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });

        randomGenerator();
        buttonCancel.setOnClickListener(e->{
            finish();
        });
    }

    public void randomGenerator(){
        sb.setLength(0);
        for (int i =0; i<8; i++){
            sb.append(randomLetters.charAt(random.nextInt(randomLetters.length())));
        }
        textPassword.setText(sb);
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

                if(editText.getText().length()<8){
                    Toast toast = Toast.makeText(generatePSK.this, "Password should be at least of 8 Characters", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    textPassword.setText(editText.getText().toString());
                }
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

    public void savePSKClicked(View view) {
        if(textPassword.getText().length()<8){
            Toast toast = Toast.makeText(this, "Password shoud be at least 8 digits/alphabets long", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            InsertPassword insert = new InsertPassword();
            insert.execute(textPassword.getText().toString());
        }
    }



    public class InsertPassword extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... args) {
            String newPassword = args[0];
            String query_url = "http://192.168.1.1/ubus";
            String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": [ \"00000000000000000000000000000000\", \"session\", \"login\", { \"username\": \"root\", \"password\": \"algonquin\"  } ] }";
            String token = getToken(query_url, json);

            insertPassword(token, newPassword);
            return "";
        }

        private  String insertPassword(String token, String password) {
            try {
                String query_url = "http://192.168.1.1/ubus";
                String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1524, \"method\": \"call\", \"params\": [ \""+token+"\", \"file\", \"exec\", { \"command\": \"/root/insertScript\",\"params\": [ \""+password+"\" ] } ] }";

                URL url = new URL(query_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                System.out.println(result);

                System.out.println("result after Reading JSON Response");


                JSONObject myResponse = new JSONObject(result);
                value = "New Password created as "+password;
                in.close();
                conn.disconnect();
                return myResponse.toString();



            } catch (Exception e) {
                System.out.println(e);
                value ="Connection went wrong";

                return null;
            }

        }



        public String getToken(String query_url, String json) {


            try {




                URL url = new URL(query_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                System.out.println(result);

                System.out.println("result after Reading JSON Response");


                JSONObject myResponse = new JSONObject(result);
//			System.out.println("jsonrpc- "+myResponse.getString("result"));
//			System.out.println("id- "+myResponse.getInt("id"));
//			System.out.println("result- "+myResponse.getString("result"));


                in.close();
                conn.disconnect();
//			System.out.println(myResponse.get("result")["ubus_rpc_session"]);
                JSONArray obj2 = (JSONArray) myResponse.get("result");
                System.out.println(obj2.get(1).toString());
                JSONObject obj3 = (JSONObject) obj2.get(1);
                System.out.println(obj3.get("ubus_rpc_session").toString());
                return 	obj3.get("ubus_rpc_session").toString();

                //myResponse.getString("ubus_rpc_session");



            } catch (Exception e) {
                System.out.println(e);
                return null;
            }

        }
        public void onPostExecute(String result) {
            randomGenerator();
            Toast toast = Toast.makeText(generatePSK.this, value, Toast.LENGTH_LONG);
            toast.show();
        }

    }

//    public class Insert extends AsyncTask<String, Integer, String> {
//
//
//        protected String doInBackground(String... args) {
//            PSK = args[0];
//            try
//            {
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
//
//                if(conn == null) {
//                    value ="Connection goes wrong";
//                } else {
//                    String query = "INSERT INTO radreply VALUES (null, '00-00-00-00-00-00', 'Tunnel-Password', ':=', '"+PSK +"') ";
//                    Statement stmt = conn.createStatement();
//                    stmt.executeUpdate(query);
//                    value = "Inserted successfully";
//                    System.out.println(value);
//                }
//
//                conn.close();
//            }
//            catch (Exception e)
//            {
//                value ="Connection goes wrong";
//                e.printStackTrace();
//            }
//            return value;
//        }

//        public void onPostExecute(String result) {
//            textPassword.setText("");
//            Toast toast = Toast.makeText(generatePSK.this, value, Toast.LENGTH_LONG);
//            toast.show();
//        }
//
//    }

}
