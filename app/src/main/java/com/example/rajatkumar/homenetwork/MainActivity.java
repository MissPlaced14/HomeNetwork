package com.example.rajatkumar.homenetwork;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainActivity extends AppCompatActivity {

    private static final String file = "userFile";
    FragmentManager fm;
    FragmentTransaction ft;
    //* Bottom Navigation Menu */
    BottomNavigationView navigation;

    String data;
    /**
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        /** navigation menu */
        navigation = findViewById(R.id.navigationMain);
        /** navigation listner */
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        networkFragment nf = new networkFragment();
        ft.replace(R.id.mainFrame, nf);
        ft.commit();

    }

    /**
     * Bottom Nav Menu Listener
     * nav_networks: loads networkFragment (speed test)
     * nav_device: loads devicesFragment (ListView of connected devices, option to add new device)
     * nav_users: loads UsersFragment (ArrayList of current users)
     * nav_setting: loads SettingsFragment(Toggle SSID on/off)
     *
      **/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                // Replace main frame with networkFragment
                // networkFragment contains speed test
                case R.id.nav_networks:

                    if(navigation.getSelectedItemId()!=R.id.nav_networks) {
                        ft = fm.beginTransaction();
                        networkFragment nf = new networkFragment();
                        ft.replace(R.id.mainFrame, nf);
                        ft.commit();
                    }
                    return true;
                // Replaces main frame with deviceFragment
                // devicesFragment contains a LisView of connected devices
                case R.id.nav_devices:
                    ft = fm.beginTransaction();
                    devicesFragment df = new devicesFragment();
                    ft.replace(R.id.mainFrame, df);
                    ft.commit();

                    return true;
                // UsersFragment contains arraylist of users (should be fixed)
                case R.id.nav_users:
                    ft = fm.beginTransaction();
                    UsersFragment uf = new UsersFragment();
                    ft.replace(R.id.mainFrame, uf);
                    ft.commit();
                    return true;
                // SettingsFragment contains option to toggle SSIDs on/off
                case R.id.nav_setting:
                    ft = fm.beginTransaction();
                    SettingsFragment sf = new SettingsFragment();
                    ft.replace(R.id.mainFrame, sf);
                    ft.commit();
                    return true;

                // asks user to confirm logout,
                // returns to login page
                case R.id.nav_logout:
                    SharedPreferences sharedPreferences = getSharedPreferences("userFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title)
                            .setPositiveButton(R.string.ok, (dialog, id) -> {

                                // Cleaning the stored email and password from sharedPreferences
                                editor.clear();
                                editor.apply();

                                //Starting the login activity again after nothing is stored
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            })
                            .show();
                    return true;

            }
            return false;
        }
    };

    /** Top menu bar with "About" and "Options" */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_user:

                Snackbar.make(findViewById(R.id.coordinatorLayout),"Team 4", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d("Toolbar", "Option 1 selected");
                break;

            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            break;

        }

        return true;
    }
//    public class RouterQuery extends AsyncTask<String, Integer, String> {
//
//        protected String doInBackground(String... args) {
//            try {
//
//                java.util.Properties config = new java.util.Properties();
//                config.put("StrictHostKeyChecking", "no");
//                JSch jsch = new JSch();
//                Session session=jsch.getSession(user, host,22);
//                session.setPassword(password);
//                session.setConfig(config);
//                session.connect();
//                System.out.println("Connected");
//
//                Channel channel=session.openChannel("exec");
//                ((ChannelExec)channel).setCommand(args[1]);
//                channel.setInputStream(null);
//                ((ChannelExec)channel).setErrStream(System.err);
//
//                InputStream in=channel.getInputStream();
//                channel.connect();
//                byte[] tmp=new byte[4096];
//                while(true){
//                    while(in.available()>0){
//                        int i=in.read(tmp, 0, 4096);
//                        if(i<0)break;
//                        String jsonString = new String(tmp, 0, i);
//                        System.out.print(jsonString);
//                        Object obj = parser.parse(jsonString);
//                        jsonObject = (JSONObject) obj;
//
//
////	            String hostName = (String) jsonObject.get("hostname");
////	            System.out.printf("SSID is %s", hostName+"\n");
//
//                    }
//                    if(channel.isClosed()){
//                        System.out.println("exit-status: "+channel.getExitStatus());
//                        break;
//                    }
//                    try{Thread.sleep(1000);}catch(Exception ee){}
//                }
//                channel.disconnect();
//                session.disconnect();
//                System.out.println("DONE");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
////
////            JSONObject radio1 = (JSONObject) jsonObject.get("clients");
////         //   JSONArray interfaces = (JSONArray) radio1.get("interfaces");
////            JSONObject interface0 = (JSONObject) radio1.get(0);
////            JSONObject config = (JSONObject) interface0.get("config");
////            data = (String) config.get("ssid");
////            Log.i("SSID", data);
//
//            JSONObject radio1 = (JSONObject) jsonObject.get("radio1");
//            JSONArray interfaces = (JSONArray) radio1.get("interfaces");
//            JSONObject interface0 = (JSONObject) interfaces.get(0);
//            JSONObject config = (JSONObject) interface0.get("config");
//            data = (String) config.get("ssid");
//            Log.i("SSID", data);
//            return "finished";
//        }
//        public void onProgressUpdate(Integer... data) {
//
//        }
//
//        public void onPostExecute(String result) {
//
//        }
//    }

}
