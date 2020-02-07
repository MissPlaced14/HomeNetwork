package com.example.rajatkumar.homenetwork;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class has methods connected to the fragment_devices.xml layout which is loaded
 * into the center frame of the app when the devices menu item is selected from the bottom nav bar
 * It loads a ListView of the SSIDs, provides option to add a new VLAN, or delete an VLAN.
 */
public class vlansFragment extends Fragment {

    ListView vlansLV;
    View page;
    AddressAdaptor addressAdaptor;
    Button addVlanBtn;
    ArrayList<String> vlansAL;
    Toast rqErrorToast;
    Intent rqIntent;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = inflater.inflate(R.layout.fragment_devices, container, true);
        addVlanBtn = page.findViewById(R.id.addVlanBtn);
        vlansLV = page.findViewById(R.id.listViewDevices);
        vlansAL = new ArrayList<>();
        rqErrorToast = Toast.makeText(this.getActivity(), "Router Query Error", Toast.LENGTH_LONG);
        rqIntent = new Intent(getActivity(), RouterQueryServiceTest.class);

        addVlanBtn.setOnClickListener(e-> startActivity(new Intent(getActivity(), generatePSK.class)));
        addressAdaptor = new AddressAdaptor(getActivity().getApplicationContext());
        SwipeRefreshLayout pullToRefresh = page.findViewById(R.id.pullToRefreshDevices);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(true);
            if (pullToRefresh.isRefreshing()){
                rqIntent.putExtra("action", "getVlans");
                //TODO reload ListView with results
                pullToRefresh.setRefreshing(false);
            }});

        vlansLV.setAdapter(addressAdaptor);
        addressAdaptor.notifyDataSetChanged();
        vlansLV.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle_message = new Bundle();
            bundle_message.putString("ssid", vlansLV.getItemAtPosition(i).toString());
            Intent intent = new Intent(getActivity(), deleteActivity.class);
            intent.putExtra("ssid", vlansLV.getItemAtPosition(i).toString());
            startActivityForResult(intent, 500);
        });
        return page;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == 600) {
//            vlansAL.clear();
            addressAdaptor.notifyDataSetChanged();
        } else {
            rqErrorToast.show();
        }
    }

    /**
     * Adapter to populate ListView containing the VLANs' SSIDs
     */
    private class AddressAdaptor extends ArrayAdapter<String> {

        AddressAdaptor(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return vlansAL.size();
        }
        public String getItem(int position){
            return vlansAL.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.connected_devices_row, null);
            TextView address = result.findViewById(R.id.ssid);
            address.setText(   getItem(position)  ); // get the string at position

            return result;
        }

    }

}
