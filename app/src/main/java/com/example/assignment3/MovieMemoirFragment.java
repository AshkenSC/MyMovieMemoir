package com.example.assignment3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.adapter.MemoirItemClickListener;
import com.example.assignment3.adapter.MemoirRecyclerViewAdapter;
import com.example.assignment3.adapter.SearchRecyclerViewAdapter;
import com.example.assignment3.model.MemoirEntry;
import com.example.assignment3.model.SearchResult;
import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MovieMemoirFragment extends Fragment{

    // user id and memoir data
    private int userId;
    String memoirData;
    JSONArray dataArray = null;
    String tempURL;

    // internet connection
    NetworkConnection networkConnection = null;

    // view
    View view;

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MemoirRecyclerViewAdapter adapter;
    List<MemoirEntry> memoirEntries = new ArrayList<>();

    public MovieMemoirFragment(int userId) {
        // Required empty public constructor
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.movie_memoir_fragment, container, false);
        networkConnection = new NetworkConnection();

        // load memoir entries
        GetAllMemoirEntries getAllMemoirEntries = new GetAllMemoirEntries();
        getAllMemoirEntries.execute(userId);

        try {
            memoirData = getAllMemoirEntries.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            dataArray = new JSONArray(memoirData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // load memoir entries from JSON data
       for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataEntry = null;
            try {
                dataEntry = (JSONObject) dataArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String movieName = "";
            String imdbId = "";
            String watchDate = "";
            String cinemaPostcode = "";
            String comment = "";
            String releaseDate = "";

            int userRating = 0;

            try {
                comment = dataEntry.getString("comment");
            } catch (JSONException e) {
                e.printStackTrace();
                comment = "";
            }
            cinemaPostcode = comment.split(";;;lll;;;")[1];
            imdbId = comment.split(";;;lll;;;")[2];
            comment = comment.split(";;;lll;;;")[0];

            try {
                movieName = dataEntry.getString("movieName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                watchDate = dataEntry.getString("watchDate");
            } catch (JSONException e) {
                e.printStackTrace();
                watchDate = "";
            }
            try {
                userRating = dataEntry.getInt("score");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                releaseDate = dataEntry.getString("movieReleaseDate");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MemoirEntry memoirEntry = new MemoirEntry(
                    movieName,
                    imdbId,
                    watchDate,
                    releaseDate,
                    cinemaPostcode,
                    comment,
                    userRating
            );
            memoirEntries.add(memoirEntry);
        }

        // load image from API
        for (int i = 0; i < memoirEntries.size(); i++) {
            GetImageURL getImageURL = new GetImageURL();
            getImageURL.execute(memoirEntries.get(i).getImdbId());
            try {
                tempURL = getImageURL.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            memoirEntries.get(i).setImageURL(tempURL);
        }

        // display memoir entries
        // initiate recyclerView and its adapter
        recyclerView = view.findViewById(R.id.memoir_recycler_view);
        adapter = new MemoirRecyclerViewAdapter(memoirEntries);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // add entries to entry list
        adapter.addUnits(memoirEntries);
        
        // recycilerview onClick listener
        recyclerView.addOnItemTouchListener(
                new MemoirItemClickListener(getActivity(), recyclerView ,
                        new MemoirItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override public void onItemClick(View view, int position) {
                                // switch fragment

                                // get imdb id
                                TextView imdbIdView = recyclerView.getChildAt(position).
                                        findViewById(R.id.memoir_imdb_id);
                                String imdbId = imdbIdView.getText().toString();

                                startToFragment(Objects.requireNonNull(getActivity()), R.id.content_frame,
                                        new MovieViewFragment(userId, imdbId));
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                // perform actions
                            }
                        })
        );



        // find all persons button
//        Button findAllStudentsBtn = view.findViewById(R.id.btnFindAll);
//        findAllStudentsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return view;
    }

    private class GetAllMemoirEntries extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... userId) {
            return networkConnection.getMemoirData(userId[0]);
        }
        @Override
        protected void onPostExecute(String dataString) {
            memoirData = dataString;
        }
    }

    private class GetImageURL extends AsyncTask<String, Void, String> {
        private OnTaskCompleted listener;

        public GetImageURL(OnTaskCompleted listener) {
            this.listener = listener;
        }

        public GetImageURL() {

        }

        @Override
        protected String doInBackground(String... userId) {
            String dataString = networkConnection.getMovieDetail(userId[0]);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(dataString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = null;
            try {
                url = jsonObject.getString("Poster");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return url;
        }
        @Override
        protected void onPostExecute(String imageURL) {
            //listener.onTaskCompleted(url);
            tempURL = imageURL;
        }
    }

    // used to get value from AsyncTask
    public interface OnTaskCompleted{
        void onTaskCompleted(String values);
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
