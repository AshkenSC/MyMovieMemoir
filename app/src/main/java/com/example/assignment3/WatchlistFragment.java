package com.example.assignment3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.adapter.WatchlistEntryItemClickListener;
import com.example.assignment3.adapter.WatchlistRecyclerViewAdapter;
import com.example.assignment3.localDB.entity.WatchlistEntry;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    // View
    View view;

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WatchlistRecyclerViewAdapter adapter;
    private List<WatchlistEntry> watchlist;

    // currently selected entry
    private int selectedIndex = -1;

    public WatchlistFragment() {
        // public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.watchlist_fragment, container, false);

        // recycler view
        recyclerView = view.findViewById(R.id.watchlist_recycler_view);
        watchlist = new ArrayList<>();

        // load entries in database to RecyclerView
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        // click and select entry in watchlist
        recyclerView.addOnItemTouchListener(
                new WatchlistEntryItemClickListener(getActivity(), recyclerView ,
                        new WatchlistEntryItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                // case 1: no entry selected
                                if (selectedIndex == -1) {
                                    selectedIndex = position;
                                    // paint buttons
                                    paintButton(R.id.watch_view_button,
                                            getResources().getColor(R.color.colorPrimary));
                                    paintButton(R.id.watch_delete_button,
                                            getResources().getColor(R.color.colorPrimary));
                                    // paint entry
                                    paintEntry(R.id.watchlist_entry,
                                            getResources().getColor(R.color.colorSecondary));
                                }
                                // case 2: one entry selected, click on another
                                else if (selectedIndex != position) {
                                    // must un-select previous one first
                                    paintEntry(R.id.watchlist_entry,
                                            getResources().getColor(R.color.colorWhite));
                                    // select another one
                                    selectedIndex = position;
                                    paintEntry(R.id.watchlist_entry,
                                            getResources().getColor(R.color.colorSecondary));
                                }
                                // case 3: re-click on selected entry to cancel selection
                                else {
                                    // un-paint buttons
                                    paintButton(R.id.watch_view_button,
                                            getResources().getColor(R.color.colorDisabledBtn));
                                    paintButton(R.id.watch_delete_button,
                                            getResources().getColor(R.color.colorDisabledBtn));
                                    // un-paint widget
                                    paintEntry(R.id.watchlist_entry,
                                            getResources().getColor(R.color.colorWhite));
                                    selectedIndex = -1;
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                        })
        );

        // button settings
        Button btnView = view.findViewById(R.id.watch_view_button);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedIndex == -1) {
                    Toast.makeText(getActivity(),"Select an entry first",Toast.LENGTH_LONG).show();
                }
                else {
                    TextView imdbIdView = recyclerView.getChildAt(selectedIndex).findViewById(R.id.watchlist_imdb_id);
                    String imdbId = imdbIdView.getText().toString().replace("imdb id:", "");
                    startToFragment(getActivity(), R.id.content_frame,
                            new MovieViewFragment(imdbId));
                }
            }
        });

        Button btnDelete = view.findViewById(R.id.watch_delete_button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedIndex == -1) {
                    Toast.makeText(getActivity(),"Select an entry first",Toast.LENGTH_LONG).show();
                }
                else {
                    // delete entry from database
                    TextView imdbIdView = recyclerView.getChildAt(selectedIndex).findViewById(R.id.watchlist_imdb_id);
                    String imdbId = imdbIdView.getText().toString().replace("imdb id:", "");

                    DeleteWatchlist deleteWatchlist = new DeleteWatchlist();
                    deleteWatchlist.execute(imdbId);
                    // remove entry display on the screen
                    // unpaint entry and buttons
                    adapter.removeEntry(selectedIndex);
                    paintEntry(R.id.watchlist_entry, getResources().getColor(R.color.colorWhite));
                    paintButton(R.id.watch_view_button,
                            getResources().getColor(R.color.colorDisabledBtn));
                    paintButton(R.id.watch_delete_button,
                            getResources().getColor(R.color.colorDisabledBtn));
                    selectedIndex = -1;

                }
            }
        });


        return view;
    }

    private class ReadDatabase extends AsyncTask<Void, Void, List<WatchlistEntry>> {
        @Override
        protected List<WatchlistEntry> doInBackground(Void... params) {
            List<WatchlistEntry> entries = HomeActivity.db.WatchlistEntryDao().getAll();
            return entries;
        }

        @Override
        protected void onPostExecute(List<WatchlistEntry> watchListEntries) {
            // display watchlist
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

    private class DeleteWatchlist extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... imdbId) {
            HomeActivity.db.WatchlistEntryDao().deleteSelected(imdbId[0]);
            return "Selected movie has been removed from your Watchlist.";
        }

        @Override
        protected void onPostExecute(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void paintEntry(int widgetId, int colorId) {
        LinearLayout currentEntry =
                recyclerView.getChildAt(selectedIndex).
                        findViewById(R.id.watchlist_entry);
        currentEntry.setBackgroundColor(colorId);
    }

    public void paintButton(int widgetId, int colorId) {
        Button currentButton =
                view.findViewById(widgetId);
        currentButton.setBackgroundColor(colorId);
    }

    public void paintSelectedEntry(RecyclerView recyclerView) {
        LinearLayout currentEntry =
                recyclerView.getChildAt(selectedIndex).
                        findViewById(R.id.watchlist_entry);
        currentEntry.setBackgroundColor(
                getResources().getColor(R.color.colorSecondary));
    }

    public void unpaintUnselectedEntry(RecyclerView recyclerView) {
        LinearLayout currentEntry =
                recyclerView.getChildAt(selectedIndex).
                        findViewById(R.id.watchlist_entry);
        currentEntry.setBackgroundColor(
                getResources().getColor(R.color.colorWhite));
    }

    // fragment stack, used for press back button to return to previous fragment
    public void startToFragment(Context context, int container, Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, newFragment);
        transaction.addToBackStack(context.getClass().getName());
        transaction.commit();
    }

}
