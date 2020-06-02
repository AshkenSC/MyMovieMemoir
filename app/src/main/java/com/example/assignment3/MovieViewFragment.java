package com.example.assignment3;

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
import com.example.assignment3.model.MemoirEntry;
import com.example.assignment3.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class MovieViewFragment extends Fragment {
    NetworkConnection networkConnection = null;

    // current view
    View view;

    // user's info and data
    String imdbId;
    String firstName;
    int userId;
    JSONObject dataObject = null;

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
        Button addToWatchList = view.findViewById(R.id.view_add_to_watchlist);
        addToWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use bundle to pass fetched JSON data
                startToFragment(getActivity(), R.id.content_frame, new MovieMemoirFragment());
            } });


        Button addToMemoir = view.findViewById(R.id.menu_movie_memoir);
        addToMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            } });

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
                dataObject = new JSONObject(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvTitile = view.findViewById(R.id.view_name);
            try {
                tvTitile.setText(dataObject.getString("Title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvReleaseDate = view.findViewById(R.id.view_release_date);
            try {
                tvReleaseDate.setText(dataObject.getString("Released"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvImdbId = view.findViewById(R.id.view_imdb_id);
            tvImdbId.setText("IMDb id: " + imdbId);

            RatingBar ratingBar = view.findViewById(R.id.view_rating_bar);
            try {
                ratingBar.setRating(Float.parseFloat(dataObject.getString("imdbRating")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageView image = view.findViewById(R.id.view_image);
            try {
                Picasso.get().load(dataObject.getString("Poster")).into(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvGenre = view.findViewById(R.id.view_genre);
            try {
                tvGenre.setText("Genre: " + dataObject.getString("Genre"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvCountry = view.findViewById(R.id.view_country);
            try {
                tvCountry.setText("Country: " + dataObject.getString("Country"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvDirector = view.findViewById(R.id.view_director);
            try {
                tvDirector.setText("Director: " + dataObject.getString("Director"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvCast = view.findViewById(R.id.view_cast);
            try {
                tvCast.setText("Cast: " + dataObject.getString("Actors"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tvPlotSummary = view.findViewById(R.id.view_plot_summary);
            try {
                tvPlotSummary.setText("Plot summary: " + dataObject.getString("Plot"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
