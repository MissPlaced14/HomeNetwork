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

/**
 * This App is designed to server as a user-friendly method for creating and managing VLANs on their home network
 * It requires a companion device (or "router") to serve as the access point, which also needs to be configured with specific software
 *
 * The communication between the App and the router device is defined in the RouterQueryService class
 * classes for various views, intents, and fragments should only use this service class to communicate with the router.
 *
 * @date Feb 2020
 * Currently the App is part-way through a redesign, the structure is overly complex and not entirely functional.
 * I have done my best to add comments and descriptions both of the current functionality and what pieces are missing
 */

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft;

    //* Bottom Navigation Menu */
    BottomNavigationView navigation;

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
     * nav_vlans: loads vlansFragment (ListView of connected devices, option to add new device)
     * nav_setting: loads SettingsFragment(Toggle SSID on/off)
     **/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                // Replace main frame with networkFragment
                // networkFragment contains speed test
                case R.id.nav_networks:

                    if (navigation.getSelectedItemId() != R.id.nav_networks) {
                        ft = fm.beginTransaction();
                        networkFragment nf = new networkFragment();
                        ft.replace(R.id.mainFrame, nf);
                        ft.commit();
                    }
                    return true;

                // Replaces main frame with vlansFragment
                // vlansFragment contains a LisView of connected devices
                case R.id.nav_devices:
                    ft = fm.beginTransaction();
                    vlansFragment df = new vlansFragment();

                    ft.replace(R.id.mainFrame, df);
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

    /**
     * Top menu bar with "About" and "Options"
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_user:

                Snackbar.make(findViewById(R.id.coordinatorLayout), "Team 4", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d("Toolbar", "Option 1 selected");
                break;

            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

        }

        return true;
    }
}