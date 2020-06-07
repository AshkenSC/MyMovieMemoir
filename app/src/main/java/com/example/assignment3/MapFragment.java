package com.example.assignment3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.assignment3.networkconnection.NetworkConnection;

public class MapFragment extends Fragment {

    // network connection
    NetworkConnection networkConnection = null;
    // view
    View view;
    // user id in database
    int userId;

    public MapFragment(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.map_fragment, container, false);

        // network connection
        networkConnection = new NetworkConnection();

        return view;
    }
}
