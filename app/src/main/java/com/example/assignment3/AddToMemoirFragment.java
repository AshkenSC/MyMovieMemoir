package com.example.assignment3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class AddToMemoirFragment extends Fragment {

    View view;
    String imdbId;


    public AddToMemoirFragment(String imdbId) {
        // Required empty public constructor
        this.imdbId = imdbId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.add_fragment, container, false);




        return view;
    }
}
