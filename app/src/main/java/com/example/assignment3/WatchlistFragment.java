package com.example.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
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
    static View view;

    // declare RecyclerView
    private static RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static WatchlistRecyclerViewAdapter adapter;
    private List<WatchlistEntry> watchlist;

    // currently selected entry
    private static int selectedIndex = -1;

    // delete confirm
    private static boolean confirmDelete = false;

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

        // set up buttons
        // view button
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

        // delete button
        Button btnDelete = view.findViewById(R.id.watch_delete_button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex == -1) {
                    Toast.makeText(getActivity(), "Select an entry first", Toast.LENGTH_LONG).show();
                    return;
                }

                DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                deleteDialogFragment.show(getFragmentManager(), "Confirm delete");
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

    private static class DeleteWatchlist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... imdbId) {
            HomeActivity.db.WatchlistEntryDao().deleteSelected(imdbId[0]);
            return "Selected movie has been removed from your Watchlist.";
        }

        @Override
        protected void onPostExecute(String message) {

        }
    }

    public static void paintEntry(int widgetId, int colorId) {
        LinearLayout currentEntry =
                recyclerView.getChildAt(selectedIndex).
                        findViewById(R.id.watchlist_entry);
        currentEntry.setBackgroundColor(colorId);
    }

    public static void paintButton(int widgetId, int colorId) {
        Button currentButton =
                view.findViewById(widgetId);
        currentButton.setBackgroundColor(colorId);
    }

    // fragment stack, used for press back button to return to previous fragment
    public void startToFragment(Context context, int container, Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, newFragment);
        transaction.addToBackStack(context.getClass().getName());
        transaction.commit();
    }

    // delete confirm dialog
    public static class DeleteDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
            builder.setTitle("Attention")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmDelete = true;

                            if(confirmDelete == true) {
                                if (selectedIndex == -1) {
                                    Toast.makeText(getActivity(), "Select an entry first", Toast.LENGTH_LONG).show();
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
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmDelete = false;
                        }
                    })
                    .setCancelable(false);
            //builder.show(); // show() cannot be used here
            return builder.create();
        }
    }

}
