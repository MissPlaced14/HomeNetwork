package com.example.rajatkumar.homenetwork;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class devicesFragment extends Fragment {

    ListView listViewDevices;
    View page;
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = null;
    Object obj = null;
    String url="http://192.168.1.1/ubus";
    ArrayList<String> listDevices = new ArrayList<>();
    String [] output;
    AddressAdaptor addressAdaptor;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = inflater.inflate(R.layout.fragment_devices, container, false);
        listViewDevices = (ListView)page.findViewById(R.id.listViewDevices);
        RouterQuery rq = new RouterQuery();
        addressAdaptor = new AddressAdaptor(getActivity().getApplicationContext());
        rq.execute(url);

        SwipeRefreshLayout pullToRefresh = page.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
            }
        });

        listViewDevices.setAdapter(addressAdaptor);
        return page;
    }


    public class RouterQuery extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... args) {

            String URL1 = args[0];
            RouterQuery rq = new RouterQuery();
            String[] test = rq.getMacAddresses(rq.getUbusKey());
            listDevices = new ArrayList<>();
            while(true) {

                for (int i = 0; i < test.length; i++) {
                    Log.i("this is ", test[i]);
                    listDevices.add(test[i]);
                }

                return "finished";

            }
        }

        @SuppressWarnings("unchecked")
        public String [] getMacAddresses(String key) {
            System.out.println(key);
            //curl -d '{ "jsonrpc": "2.0", "id": 1, "method": "call", "params": [ "70235bc972c396ced14ff6eec1a6fa4d", "hostapd.wlan0", "get_clients", {} ] }'  http://192.168.1.1/ubus
            String[] command = {"curl", "-d", "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": [ \""+key+"\", \"hostapd.wlan0\", \"get_clients\", {} ] }",  url};
            ProcessBuilder process = new ProcessBuilder(command);
            Process p;
            try
            {
                p = process.start();
                BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
                String result = builder.toString();
                    Log.i("result", result);


                try {
                    obj = parser.parse(result);
                } catch (ParseException e) {
                        Log.i("Error","there was an error parsing the JSON file");
                }
                jsonObject = (JSONObject) obj;
                JSONArray resultJson = (JSONArray) jsonObject.get("result");
                Log.i("Result Json",resultJson.toJSONString());
                JSONObject dataJSON = (JSONObject) resultJson.get(1);
                Log.i("Data json",dataJSON.toJSONString());
                JSONObject clientsJSON = (JSONObject) dataJSON.get("clients");
                Log.i("Client Json",clientsJSON.toJSONString());
                String[] test = (String[]) clientsJSON.keySet().toArray(new String[clientsJSON.size()]);
                return test;
                //			return keyJSON;



            }
            catch (IOException e)
            {   System.out.print("error");
                e.printStackTrace();
            }
            return null;
        }






        public String getUbusKey(){
            String[] command = {"curl", "-d", "{ 'jsonrpc': '2.0', 'id': 1, 'method': 'call', 'params': [ '00000000000000000000000000000000', 'session', 'login', { 'username': 'root', 'password': 'password'  } ] }",  url};
            ProcessBuilder process = new ProcessBuilder(command);
            Process p;
            try
            {
                p = process.start();
                BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
                String result = builder.toString();
                Log.e("result","????????");
                Log.e("result",result);


                try {
                    obj = parser.parse(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("second there was an error parsing the JSON file");
                }
                jsonObject = (JSONObject) obj;
                JSONArray resultJson = (JSONArray) jsonObject.get("result");
                System.out.println(resultJson.toJSONString());
                JSONObject dataJSON = (JSONObject) resultJson.get(1);
                System.out.println(dataJSON.toJSONString());
                String keyJSON = (String) dataJSON.get("ubus_rpc_session");
                return keyJSON;


//			   JSONObject radio1 = (JSONObject) jsonObject.get("radio1");
//		        JSONArray interfaces = (JSONArray) radio1.get("interfaces");
//		        JSONObject interface0 = (JSONObject) interfaces.get(0);
//		        JSONObject config = (JSONObject) interface0.get("config");

            }
            catch (IOException e)
            {   System.out.print("error");
            Log.e("IOError","Error retrieving data");
                e.printStackTrace();
            }
            return "";
        }
        public void onProgressUpdate(Integer... data) {

        }

        public void onPostExecute(String result) {
            addressAdaptor.notifyDataSetChanged();
        }
    }

    private class AddressAdaptor extends ArrayAdapter<String> {

        public AddressAdaptor(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return listDevices.size();
        }
        public String getItem(int position){
            return listDevices.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.connected_devices, null);
            TextView address = (TextView)result.findViewById(R.id.device_mac);
            address.setText(   getItem(position)  ); // get the string at position
            return result;
        }

        public long getId(int position){
            return position;
        };
    }

}
