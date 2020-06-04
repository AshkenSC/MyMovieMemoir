package com.example.assignment3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.adapter.RecyclerViewAdapter;
import com.example.assignment3.localDB.database.WatchlistEntryDatabase;
import com.example.assignment3.localDB.entity.WatchlistEntry;
import com.example.assignment3.model.MemoirEntry;
import com.example.assignment3.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieViewFragment extends Fragment {
    NetworkConnection networkConnection = null;

    // current view and widgets
    View view;

    // user's info and data
    String imdbId;
    String firstName;
    int userId;
    JSONObject entryJSONObject = null;

    // whether current entry exists in db
    boolean isExisted;

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private List<MemoirEntry> memoirEntries;

    public MovieViewFragment(String imdbId, int userId, String firstName) {
        // Required empty public constructor
        this.imdbId = imdbId;
        this.userId = userId;
        this.firstName = firstName;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.movie_view_fragment, container, false);

        // network connection
        networkConnection = new NetworkConnection();

        /* load data into view */
        GetMovieDetail getMovieDetail = new GetMovieDetail();
        getMovieDetail.execute(imdbId);

        /* set buttons */
        final Button btnAddToWatchList = view.findViewById(R.id.view_add_to_watchlist);
        // if current movie is already in the list, disable this button
        CheckExistance checkExistance = new CheckExistance();
        checkExistance.execute(imdbId);
        try {
            isExisted = checkExistance.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isExisted) {
            // change button layout and disable it
            btnAddToWatchList.setText("Movie already in Watchlist");
        }

        btnAddToWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use bundle to pass fetched JSON data
                if (isExisted) {
                    Toast.makeText(getActivity(),"This film is already in your Watchlist!",Toast.LENGTH_LONG).show();
                }
                else {
                    AddToWatchlist addToWatchlist = new AddToWatchlist();
                    addToWatchlist.execute(imdbId);
                }
            } });

//        Button addToMemoir = view.findViewById(R.id.menu_movie_memoir);
//        addToMemoir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO
//            } });

        return view;
    }

    private class GetMovieDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... imdbId) {
            return networkConnection.getMovieDetail(imdbId[0]);
        }

        @Override
        protected void onPostExecute(String results) {
            try {
                entryJSONObject = new JSONObject(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvTitile = view.findViewById(R.id.view_name);
            try {
                tvTitile.setText(entryJSONObject.getString("Title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvReleaseDate = view.findViewById(R.id.view_release_date);
            try {
                tvReleaseDate.setText(entryJSONObject.getString("Released"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvImdbId = view.findViewById(R.id.view_imdb_id);
            tvImdbId.setText("IMDb id: " + imdbId);

            RatingBar ratingBar = view.findViewById(R.id.view_rating_bar);
            try {
                ratingBar.setRating(Float.parseFloat(entryJSONObject.getString("imdbRating")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageView image = view.findViewById(R.id.view_image);
            try {
                Picasso.get().load(entryJSONObject.getString("Poster")).into(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvGenre = view.findViewById(R.id.view_genre);
            try {
                tvGenre.setText("Genre: " + entryJSONObject.getString("Genre"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvCountry = view.findViewById(R.id.view_country);
            try {
                tvCountry.setText("Country: " + entryJSONObject.getString("Country"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvDirector = view.findViewById(R.id.view_director);
            try {
                tvDirector.setText("Director: " + entryJSONObject.getString("Director"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvCast = view.findViewById(R.id.view_cast);
            try {
                tvCast.setText("Cast: " + entryJSONObject.getString("Actors"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvPlotSummary = view.findViewById(R.id.view_plot_summary);
            try {
                tvPlotSummary.setText("Plot summary: " + entryJSONObject.getString("Plot"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddToWatchlist extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... imdbId) {
            WatchlistEntry newEntry = null;

            // TODO check if the movie already exists in Room DB

            // if not existed, add a new entry
            try {
                String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                newEntry = new WatchlistEntry(imdbId[0],
                        entryJSONObject.getString("Title"),
                        entryJSONObject.getString("Released"),
                        dateToday);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HomeActivity.db.WatchlistEntryDao().insert(newEntry);

            String message = newEntry.getMovieName() + " has been added to your Watchlist!";
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            // prompt message
            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        }
    }

    private class CheckExistance extends AsyncTask<String, Void, Boolean> {
        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... imdbId) {
            WatchlistEntry newEntry = null;

            // TODO check if the movie already exists in Room DB
            if (HomeActivity.db.WatchlistEntryDao().findById(imdbId[0]) != null) {
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean existance) {
            isExisted = existance;
        }
    }

    public void startToFragment(Context context, int container, Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, newFragment);
        transaction.addToBackStack(context.getClass().getName());
        transaction.commit();
    }
}
