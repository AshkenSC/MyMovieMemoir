package com.example.assignment3;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MovieViewFragment extends Fragment {
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

    public MovieViewFragment(int userId, String firstName) {
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

        return view;
    }
}
