package com.example.assignment3;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.adapter.RecyclerViewAdapter;
import com.example.assignment3.model.MemoirEntry;
import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    NetworkConnection networkConnection = null;

    // user's info and data
    String firstName;
    int userId;
    String homeMemoirData = new String();

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private List<MemoirEntry> memoirEntries;
    
    public HomeFragment(int userId, String firstName) {
        // Required empty public constructor
        this.userId = userId;
        this.firstName = firstName;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // network connection
        networkConnection = new NetworkConnection();

        /* display welcome string (welcome + username + date) */
        TextView homeWelcome = view.findViewById(R.id.homeWelcome);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String welcomeText = "Welcome, " + firstName + ". Today is " + date;
        homeWelcome.setText(welcomeText);

        /* recycler view to display top 5 films in 2020, using RecyclerView */
        recyclerView = view.findViewById(R.id.homeRecyclerView);

        memoirEntries = new ArrayList<>();
        memoirEntries = MemoirEntry.createEntryList();

        GetMemoir2020 getMemoir2020 = new GetMemoir2020();
        getMemoir2020.execute(userId);
        try {
            homeMemoirData = getMemoir2020.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get top 5 memoir list
        ArrayList<ArrayList<String>> homeMemoirList = new ArrayList<>();
        try {
            homeMemoirList = parseData(homeMemoirData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // display memoir
        // initiate adapter
        adapter = new RecyclerViewAdapter(memoirEntries);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // add entries to entry list
        for (int i = 0; i < min(homeMemoirList.size(), 5); i++) {
            MemoirEntry memoirEntry = new MemoirEntry(homeMemoirList.get(i).get(0),
                                                  homeMemoirList.get(i).get(1),
                                        Integer.parseInt(homeMemoirList.get(i).get(2))/20.0);
            memoirEntries.add(memoirEntry);
        }
        adapter.addUnits(memoirEntries);

//        unitList = view.findViewById(R.id.homeListView);
//        unitListArray = new ArrayList<HashMap<String, String>>();
//        for (int i = 0; i < min(homeMemoirList.size(), 5); i++) {
//            // insert data to the hash map
//            map.put("NAME", homeMemoirList.get(i).get(0));
//            map.put("RELEASE DATE", homeMemoirList.get(i).get(1));
//            map.put("SCORE",homeMemoirList.get(i).get(2));
//            // must use CLONE to add new map element
//            unitListArray.add((HashMap<String, String>) map.clone());
//        }
//        // list adapter
//        myListAdapter = new
//                SimpleAdapter(getActivity(), unitListArray, R.layout.home_list_view, colHEAD, dataCell);
//        unitList.setAdapter(myListAdapter);
        
        return view;
    }

    private int min(int a, int b) {
        if (a < b) {
            return a;
        }
        else {
            return b;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<ArrayList<String>> parseData(String homeMemoirData) throws JSONException {
        ArrayList<ArrayList<String>> entryList = new ArrayList<>();
        JSONArray dataList = new JSONArray(homeMemoirData);
        for (int i = 0; i < dataList.length(); i++) {
            JSONObject entryObject = dataList.getJSONObject(i);
            ArrayList<String> entry = new ArrayList<>();
            entry.add(entryObject.getString("movieName"));
            entry.add(entryObject.getString("movieReleaseDate").split("T")[0]);
            entry.add(String.valueOf(entryObject.getInt("score")));
            entryList.add(entry);
        }

        entryList.sort(new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                if (Integer.parseInt(o1.get(2)) > Integer.parseInt(o2.get(2))) {
                    return -1;
                }
                else if (Integer.parseInt(o1.get(2)) < Integer.parseInt(o2.get(2))){
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        return entryList;
    }

    private class GetMemoir2020 extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... personId) {
            return networkConnection.getMemoirForHome(personId[0]);
        }

        @Override
        protected void onPostExecute(String results) {
            homeMemoirData = results;
        }
    }
}