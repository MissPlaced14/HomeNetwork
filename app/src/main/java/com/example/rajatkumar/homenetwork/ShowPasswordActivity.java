package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ShowPasswordActivity extends Activity {
    ListView listViewDevices;
    ArrayList<Password> listDevices = new ArrayList<>();
    PasswordAdaptor passwordAdaptor;
    String value="";
    final String DB_URL= "jdbc:mysql://192.168.1.240:3306/radius";
    final String USER = "user";
    final String PASS = "P@$$w0rd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_password);
        listViewDevices = (ListView)findViewById(R.id.listviewPassword);

        Select select = new Select();
        select.execute("");
        passwordAdaptor = new PasswordAdaptor(this);
        listViewDevices.setAdapter(passwordAdaptor);


    }


    private class PasswordAdaptor extends ArrayAdapter<Password> {

        public PasswordAdaptor(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return listDevices.size();
        }
        public Password getItem(int position){
            return listDevices.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ShowPasswordActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.all_passwords_row, null);
            TextView address = (TextView)result.findViewById(R.id.all_address);
            TextView password = (TextView)result.findViewById(R.id.all_pass);
            address.setText(   getItem(position).address  ); // get the string at position
            password.setText(   getItem(position).password  );
            return result;
        }

        public long getId(int position){
            return position;
        };
    }



    public class Password {
        public String address;
        public String password;

        public Password(){
        }
        public Password(String address, String password) {
            this.address = address;
            this.password = password
            ;
        }
    }


    public class Select extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... args) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

                if(conn == null) {
                    value ="Connection goes wrong";
                } else {
                    String query = "select * from radreply";
                    Statement stmt = conn.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    value = "The Data";
                    System.out.println(value);


                    while (resultSet.next()) {
                        Password password = new Password();
                        password.address= resultSet.getString("username");
                        password.password= resultSet.getString("value");
                        listDevices.add(password);

                    }
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
            //   editTextPSK.setText("");
            Toast toast = Toast.makeText(ShowPasswordActivity.this, value, Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
