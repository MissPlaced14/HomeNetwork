package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class deleteActivity extends Activity {
    Button buttonDelete;
    TextView textView;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        buttonDelete=findViewById(R.id.buttonDeleteMac);
        textView= findViewById(R.id.tvDeleteMac);

        Bundle b = getIntent().getExtras();
        textView.setText(b.getString("mac"));
        String query_url = "http://192.168.1.1/ubus";
        String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": [ \"00000000000000000000000000000000\", \"session\", \"login\", { \"username\": \"root\", \"password\": \"algonquin\"  } ] }";

        token = getToken(query_url, json);
        buttonDelete.setOnClickListener(e->{

            deletePassword(token, textView.getText().toString());

            Intent resultIntent = new Intent();
            setResult(600, resultIntent);
            finish();
        });
    }

    private  String deletePassword(String token, String mac) {
        try {
            String query_url = "http://192.168.1.1/ubus";
            String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1524, \"method\": \"call\", \"params\": [ \""+token+"\", \"file\", \"exec\", { \"command\": \"deleteScript\",\"params\": [ \""+mac+"\" ] } ] }";

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

            JSONObject myResponse = new JSONObject(result);

            in.close();
            conn.disconnect();
            System.out.println(myResponse.toString());
            return myResponse.toString();



        } catch (Exception e) {
            System.out.println(e);
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


            //			System.out.println(result);

            //			System.out.println("result after Reading JSON Response");


            JSONObject myResponse = new JSONObject(result);
            //			System.out.println("jsonrpc- "+myResponse.getString("result"));
            //			System.out.println("id- "+myResponse.getInt("id"));
            //			System.out.println("result- "+myResponse.getString("result"));


            in.close();
            conn.disconnect();
            //			System.out.println(myResponse.get("result")["ubus_rpc_session"]);
            JSONArray obj2 = (JSONArray) myResponse.get("result");
            //			System.out.println(obj2.get(1).toString());
            JSONObject obj3 = (JSONObject) obj2.get(1);


            //			System.out.println(obj3.get("ubus_rpc_session").toString());
            return 	obj3.get("ubus_rpc_session").toString();

            //myResponse.getString("ubus_rpc_session");



        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }
}
