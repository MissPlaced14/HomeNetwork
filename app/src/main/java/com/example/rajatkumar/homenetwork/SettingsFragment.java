package com.example.rajatkumar.homenetwork;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends Fragment {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View page;
        page = inflater.inflate(R.layout.fragment_settings, container, false);
        return page;
    }
}