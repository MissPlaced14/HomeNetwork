package com.example.rajatkumar.homenetwork;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class devicesFragment extends Fragment {

    ListView listViewDevices;
    View page;
    AddressAdaptor addressAdaptor;
    Button addDeviceButton;
    ArrayList<String> listDevices;
    Toast rqErrorToast;

    // URL to send UBUS calls to



    ArrayList<String> arraylist2;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = inflater.inflate(R.layout.fragment_devices, container, false);
        addDeviceButton = page.findViewById(R.id.buttonAddDevice);
        listViewDevices = page.findViewById(R.id.listViewDevices);
        listDevices = new ArrayList<String>();
        rqErrorToast = Toast.makeText(this.getActivity(), "Router Query Error", Toast.LENGTH_LONG);
        addDeviceButton.setOnClickListener(e->{
            startActivity(new Intent(getActivity(), generatePSK.class));

        });
        RouterQuery rq = new RouterQuery();
        rq.execute();
        addressAdaptor = new AddressAdaptor(getActivity().getApplicationContext());
        SwipeRefreshLayout pullToRefresh = page.findViewById(R.id.pullToRefreshDevices);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                               @Override
                                               public void onRefresh() {

                                                   pullToRefresh.setRefreshing(true);
                                                   if (pullToRefresh.isRefreshing()){
                                                       pullToRefresh.setRefreshing(false);
                                                   }
                                               }

                                           }

        );


        listViewDevices.setAdapter(addressAdaptor);
        addressAdaptor.notifyDataSetChanged();
        listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle_message = new Bundle();
                bundle_message.putString("mac", listViewDevices.getItemAtPosition(i).toString());
                Intent intent = new Intent(getActivity(), deleteActivity.class);
                intent.putExtra("mac", listViewDevices.getItemAtPosition(i).toString());
                startActivityForResult(intent, 500);
            }
        });
        return page;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == 600) {

            Bundle extras = data.getExtras();
            String comingID = extras.getString("SendingID");
//            Log.i("id", comingID);
            RouterQuery rq = new RouterQuery();
            if (rq != null) {
                rq.deletePassword(comingID);
                rq.execute();
                listDevices.clear();
                addressAdaptor.notifyDataSetChanged();
            } else {
                rqErrorToast.show();
            }
        }
    }


    /**
     * Class for making UBUS/JSON-RPC calls to the router
     *
     **/
    public class RouterQuery extends AsyncTask<String, Integer, String> {
        /** Router URL */
        private static final String UBUS_URL ="http://192.168.1.1/ubus";
        /** VM URL */
//        private static final String UBUS_URL ="http://192.168.56.2/ubus";

        /** Login params for router -- should be encrypted & in shared pref. */
        private static final String USRNM_PW = "{ \"username\": \"root\", \"password\": \"openwrt\"  }] }'";

        /** String value of JSON-RPC call to log into router. */
        private static final String GET_SESSION = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", " +
                "\"params\": [ \"00000000000000000000000000000000\", \"session\", \"login\", " + USRNM_PW + UBUS_URL;

        /** First half of a call to a UBUS call (upto, but not including token)
         * Constructing the JSON to send the commands is finnicky and difficult to proofread. */
        private static final String UBUS_CALL = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": ";

        /** Second half of UBUS call to write text to a file for test method */
        private static final String APPEND_FILE = "\", \"file\", \"write\", { \"path\" : " +
                "\"/test/write.txt\", \"data\" : \"\\ntesting\\n\" } ] }' " +UBUS_URL;

        protected String doInBackground(String... args) {
            //message
            String result = "failed";

            RouterQuery rq = new RouterQuery();

            if(rq.getToken(UBUS_URL, GET_SESSION)!=null) {
                String token = rq.getToken(UBUS_URL, GET_SESSION);
                Log.i("auth token:", token);
                rq.testFile(token);

//                rq.updateFile(token);
//                rq.getDevicesText(token);
                result="completed!";
            }else{
                rqErrorToast.show();
                Log.i("Auth", "Token null");
            }
            return result;
        }
        private String testFile (String token){
            try {

//                String json = UBUS_CALL + token + APPEND_FILE;
                String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": [ " + token +
                        "\", \"file\", \"write\", { \"path\" : \"/test/write.txt\", \"data\" : \"\\nblafdaaoildnfv\n\" } ] }'  http://192.168.1.1/ubus";
                URL url = new URL(UBUS_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                System.out.println(result);
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
        private  String updateFile(String token) {
            try {
                String json = "{ \"jsonrpc\": \"2.0\", \"id\": 2005, \"method\": \"call\", \"params\": [ \""+token+"\", \"file\", \"exec\", { \"command\": \"selectDevices\" } ] }";

                URL url = new URL(UBUS_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                System.out.println(result);
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
        private  String deletePassword(String mac) {
            try {
                String query_urlt = "http://192.168.1.1/ubus";
                String jsont = "{ \"jsonrpc\": \"2.0\", \"id\": 1, \"method\": \"call\", \"params\": [ \"00000000000000000000000000000000\", \"session\", \"login\", { \"username\": \"root\", \"password\": \"algonquin\"  } ] }";

                String token = getToken(query_urlt, jsont);
                String query_url = "http://192.168.1.1/ubus";
                Log.i("token3", token);
                String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1524, \"method\": \"call\", \"params\": [ \""+token+"\", \"file\", \"exec\", { \"command\": \"deleteScript\",\"params\": [ \""+mac+"\" ] } ] }";

                URL url = new URL(query_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");

                System.out.println("testing123");
                System.out.println(result);

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
        private String getDevicesText(String token) {
            try {
                String json = "{ \"jsonrpc\": \"2.0\", \"id\": 1000, \"method\": \"call\", \"params\": [ \""+token+"\", \"file\", \"read\", { \"path\": \"/root/newfile.txt\"} ] }";

                URL url = new URL(UBUS_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                //		System.out.println(result);

                //		System.out.println("result after Reading JSON Response");


                JSONObject myResponse = new JSONObject(result);



                in.close();
                conn.disconnect();
                System.out.println(myResponse.toString());

                String fullText = myResponse.toString();
                fullText = fullText.substring(22);
                fullText = fullText.split("\\}",2)[0];
                fullText = fullText.substring(0, fullText.length()-3);
                String[] rows = fullText.split(" ", 0);
                ArrayList<String> rowAl = new ArrayList<String>(Arrays.asList(rows));
                ArrayList<String> cell = new ArrayList<String>();
                for(int i = 0; i<rowAl.size(); i++) {
                    cell.addAll(Arrays.asList(rowAl.get(i).split("\\|")));
                }
                for(int i = 0; i<cell.size(); i+=5) {
                    listDevices.add(cell.get(i+1));
                    //new Device(Integer.parseInt(cell.get(i)),cell.get(i+1),cell.get(i+2),cell.get(i+3),cell.get(i+4)));
                }
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
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");


                //			System.out.println(result);

                //			System.out.println("result after Reading JSON Response");


                JSONObject myResponse = new JSONObject(result);

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
            View result = inflater.inflate(R.layout.connected_devices_row, null);
            TextView address = result.findViewById(R.id.device_mac);
            address.setText(   getItem(position)  ); // get the string at position

//            if(arraylist2.contains(getItem(position) )){
//                address.setTextColor(Color.GREEN);
//            }
            return result;
        }

        public long getId(int position){
            return position;
        }
    }

}
