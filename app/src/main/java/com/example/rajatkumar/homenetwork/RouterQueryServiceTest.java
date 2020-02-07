package com.example.rajatkumar.homenetwork;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class is intended to mock the behaviour of the RouterQueryService class.
 * It's purpose is for testing the GUI & front end controls for the app without having a Router Device setup
 */
public class RouterQueryServiceTest extends IntentService {

    // JSON for some fake vlans
    private String vlansJson = "{\"vlans\": \" vlan1 \", \" vlan2 \", \" vlan3 \" }";
    private ArrayList<String> vlansAL = new ArrayList<>();
    public RouterQueryServiceTest() { super("FakeRouterQuery"); }

    /**
     * Invoked when RouterQueryServiceTest is invoked in an intent
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Toast.makeText(this, "Connecting to Router", Toast.LENGTH_LONG).show();
        //gets the value assigned to intent action
        String action="";
        if (intent.getStringExtra("action")!=null) action = intent.getStringExtra("action");
        //executes the matching action
        switch (action){
            case "addVlan" :
                addVlan(intent.getStringExtra("ssid"), intent.getStringExtra("pw"));
                break;
            case "removeVlan" :
                removeVlan(intent.getStringExtra("ssid"));
                break;
            case "getVlans" :
                getVlans();
                break;
            case "getVlansAL" :
                getVlansAL();
            default :
                Log.i("RouterQueryService", "Invalid Query");
                break;
        }

    }

    /**
     * Adds a new VLAN to the Router
     * @param ssid the SSID for the new VLAN
     * @param pw password for the new VLAN
     */
    private void addVlan(String ssid, String pw){
        System.out.println("addVlan called");
        //TODO Check SSID is unique
        System.out.println(vlansJson);
        System.out.println("Adding ssid: " + ssid + " pw:" + pw);
        int endof = vlansJson.length()-1;
        vlansJson = vlansJson.substring(0,endof)+ ", \"ssid\"" + " }";
        System.out.println("new: " +vlansJson);
    }

    /**
     * Removes VLAN from Router
     * @param ssid SSID of the VLAN to remove
     */
    private void removeVlan(String ssid){
        System.out.println("removeVlan called " + ssid );

    }
    private String getVlans(){
        System.out.println("getVlans called");
        return (vlansJson);
    }
    private void setVlanAL(String vlans){
        String v = getVlans();

    }
    private ArrayList<String> getVlansAL(){
        return vlansAL;
    }

    /**
     * Not implimented
     * @param ssid the ssid of the VLAN to disable
     */
    private void disableVlan(String ssid){
        //TODO impliment disable VLAN
        System.out.println("Disable Vlan " + ssid + " called");
    }

}
