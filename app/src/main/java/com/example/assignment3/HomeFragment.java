package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    // user's first name
    String firstName;

    // declare list view
    List<HashMap<String, String>> unitListArray;
    HashMap<String,String> map = new HashMap<String,String>();
    SimpleAdapter myListAdapter;
    ListView unitList;
    String[] colHEAD = new String[] {"NAME","RELEASE DATE","SCORE"};
    int[] dataCell = new int[] {R.id.movieName,R.id.releaseDate,R.id.score};
    
    public HomeFragment(String firstName) {
        // Required empty public constructor
        this.firstName = firstName;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        /* display welcome string (welcome + username + date) */
        TextView homeWelcome = view.findViewById(R.id.homeWelcome);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String welcomeText = "Welcome, " + firstName + ". Today is " + date;
        homeWelcome.setText(welcomeText);

        /* display top 5 films */
        // instantiate ListView and TaskListArray objects:
        unitList = view.findViewById(R.id.homeListView);
        unitListArray = new ArrayList<HashMap<String, String>>();
        // insert data to the hash map
        map.put("NAME", "The Avengers: Endgame");
        map.put("RELEASE DATE", "2019-04-08");
        map.put("SCORE","7.1");
        // must use CLONE to add new map element
        unitListArray.add((HashMap<String, String>) map.clone());

        map.put("NAME", "Journey to the West");
        map.put("RELEASE DATE", "2019-08-15");
        map.put("SCORE","8.5");
        unitListArray.add((HashMap<String, String>) map.clone());
        // list adapter
        myListAdapter = new
                SimpleAdapter(getActivity(), unitListArray, R.layout.home_list_view, colHEAD, dataCell);
        unitList.setAdapter(myListAdapter);
        
        return view;
    }
}