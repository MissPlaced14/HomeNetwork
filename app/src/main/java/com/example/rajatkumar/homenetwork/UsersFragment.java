package com.example.rajatkumar.homenetwork;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class UsersFragment extends Fragment {
    ListView listViewUsers;
    ArrayList<String> listUsers = new ArrayList<>();
    UsersAdaptor usersAdaptor;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View page2;

        listUsers.add("Carlos");
        listUsers.add("Dean");
        listUsers.add("Urvesh");
        listUsers.add("Ankit");
        listUsers.add("Rajat");
        page2 = inflater.inflate(R.layout.fragment_users, container, false);
        listViewUsers = (ListView)page2.findViewById(R.id.listViewDevices);
        listViewUsers.setAdapter(usersAdaptor);
        return page2;
    }


    private class UsersAdaptor extends ArrayAdapter<String> {

        public UsersAdaptor(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return listUsers.size();
        }
        public String getItem(int position){
            return listUsers.get(position);
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