package com.example.assignment3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.assignment3.adapter.RecyclerViewAdapter;
import com.example.assignment3.adapter.WatchlistRecyclerViewAdapter;
import com.example.assignment3.localDB.dao.WatchlistEntryDAO;
import com.example.assignment3.localDB.database.WatchlistEntryDatabase;
import com.example.assignment3.localDB.entity.WatchlistEntry;
import com.example.assignment3.model.MemoirEntry;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WatchlistRecyclerViewAdapter adapter;
    private List<WatchlistEntry> watchlist;

    public WatchlistFragment() {
        // public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);

        // recycler view
        recyclerView = view.findViewById(R.id.watchlist_recycler_view);
        watchlist = new ArrayList<>();

        // load entries in database to RecyclerView
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();


        return view;
    }

    private class ReadDatabase extends AsyncTask<Void, Void, List<WatchlistEntry>> {
        @Override
        protected List<WatchlistEntry> doInBackground(Void... params) {
            List<WatchlistEntry> entries = HomeActivity.db.WatchlistEntryDao().getAll();
            return entries;
//            if (!(entries.isEmpty() || entries == null)) {
//                String allEntries = "";
//                for (WatchlistEntry temp : entries) {
//                    // TODO 向recycler view中添加条目。当前条目为temp
//                    temp
//                }
//                return allEntries;
//            } else
//                return "";
        }

        @Override
        protected void onPostExecute(List<WatchlistEntry> watchListEntries) {
            // display memoir
            // initiate adapter
            adapter = new WatchlistRecyclerViewAdapter(watchlist);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            adapter.addUnits(watchListEntries);
        }
    }


}
