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

public class devicesFragment extends Fragment {

    ListView listViewDevices;
    View page;
    AddressAdaptor addressAdaptor;
    Button addDeviceButton;
    ArrayList<String> listDevices;
    Toast rqErrorToast;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = inflater.inflate(R.layout.fragment_devices, container, false);
        addDeviceButton = page.findViewById(R.id.buttonAddDevice);
        listViewDevices = page.findViewById(R.id.listViewDevices);
        listDevices = new ArrayList<>();
        rqErrorToast = Toast.makeText(this.getActivity(), "Router Query Error", Toast.LENGTH_LONG);
        addDeviceButton.setOnClickListener(e-> startActivity(new Intent(getActivity(), generatePSK.class)));

        addressAdaptor = new AddressAdaptor(getActivity().getApplicationContext());
        SwipeRefreshLayout pullToRefresh = page.findViewById(R.id.pullToRefreshDevices);
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(true);
            if (pullToRefresh.isRefreshing()){
                pullToRefresh.setRefreshing(false);
            }});

        listViewDevices.setAdapter(addressAdaptor);
        addressAdaptor.notifyDataSetChanged();
        listViewDevices.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle_message = new Bundle();
            bundle_message.putString("mac", listViewDevices.getItemAtPosition(i).toString());
            Intent intent = new Intent(getActivity(), deleteActivity.class);
            intent.putExtra("mac", listViewDevices.getItemAtPosition(i).toString());
            startActivityForResult(intent, 500);
        });
        return page;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == 600) {
            Bundle extras = data.getExtras();
            listDevices.clear();
            addressAdaptor.notifyDataSetChanged();
        } else {
            rqErrorToast.show();
        }
    }

    private class AddressAdaptor extends ArrayAdapter<String> {

        AddressAdaptor(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return listDevices.size();
        }
        public String getItem(int position){
            return listDevices.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.connected_devices_row, null);
            TextView address = result.findViewById(R.id.device_mac);
            address.setText(   getItem(position)  ); // get the string at position

            return result;
        }

        public long getId(int position){
            return position;
        }
    }

}
