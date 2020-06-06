package com.example.assignment3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.assignment3.model.MemoirEntry;
import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MovieMemoirFragment extends Fragment{

    // user id and memoir data
    private int userId;
    String memoirData;
    JSONArray dataArray = null;
    String cacheImageURLandRating;

    // internet connection
    NetworkConnection networkConnection = null;

    // view
    View view;

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MemoirRecyclerViewAdapter adapter;
    public ArrayList<MemoirEntry> memoirEntries = new ArrayList<>();
    public List<MemoirEntry> sortedEntries = new ArrayList<>();

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
            float imdbRaing = 0;
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

        // load image and imdb rating from API
        for (int i = 0; i < memoirEntries.size(); i++) {
            GetImageURLandRating getImageURLandRating = new GetImageURLandRating();
            getImageURLandRating.execute(memoirEntries.get(i).getImdbId());
            try {
                cacheImageURLandRating = getImageURLandRating.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // parse string to get imdb rating and image url
            String imdbRating = cacheImageURLandRating.split(";;;lll;;;")[0];
            String imageURL = cacheImageURLandRating.split(";;;lll;;;")[1];
            memoirEntries.get(i).setImdbRating(Float.parseFloat(imdbRating));
            memoirEntries.get(i).setImageURL(imageURL);
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
        // add entries to recycler view
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

        // sort by watch date
        Button btnSortByWatchDate = view.findViewById(R.id.memoir_date_sort);
        btnSortByWatchDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // sort entries
                List<MemoirEntry> sortedEntries = sortedEntriesByWatchDate();
                // remove all units from current recycler view
                adapter.removeAllUnits();
                // reload all units
                adapter.addUnits(sortedEntries);
                // clone sorted entries to entry list
                memoirEntries = new ArrayList<>(sortedEntries);
            }
        });

        // sort by user's rating
        Button btnSortByUserRating = view.findViewById(R.id.memoir_user_sort);
        btnSortByUserRating.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // sort entries
                List<MemoirEntry> sortedEntries = sortedEntriesByUserRating();
                // remove all units from current recycler view
                adapter.removeAllUnits();
                // reload all units
                adapter.addUnits(sortedEntries);
                // clone sorted entries to entry list
                memoirEntries = new ArrayList<>(sortedEntries);
            }
        });

        // sort by public rating
        Button btnSortByPublicRating = view.findViewById(R.id.memoir_public_sort);
        btnSortByPublicRating.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // sort entries
                List<MemoirEntry> sortedEntries = sortedEntriesByPublicRating();
                // remove all units from current recycler view
                adapter.removeAllUnits();
                // reload all units
                adapter.addUnits(sortedEntries);
                // clone sorted entries to entry list
                memoirEntries = new ArrayList<>(sortedEntries);
            }
        });


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

    private class GetImageURLandRating extends AsyncTask<String, Void, String> {
        private OnTaskCompleted listener;

        public GetImageURLandRating(OnTaskCompleted listener) {
            this.listener = listener;
        }

        public GetImageURLandRating() {

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
                url = jsonObject.getString("imdbRating");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                url += ";;;lll;;;";
                url += jsonObject.getString("Poster");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // return string format: rating;;;lll;;;url
            return url;
        }
        @Override
        protected void onPostExecute(String imageURL) {
            //listener.onTaskCompleted(url);
            cacheImageURLandRating = imageURL;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MemoirEntry> sortedEntriesByWatchDate() {
        memoirEntries.sort(new Comparator<MemoirEntry>() {
            @Override
            public int compare(MemoirEntry o1, MemoirEntry o2) {
                int year1 = Integer.parseInt(o1.getWatchDate().split("T")[0].split("-")[0]);
                int month1 = Integer.parseInt(o1.getWatchDate().split("T")[0].split("-")[1]);
                int day1 = Integer.parseInt(o1.getWatchDate().split("T")[0].split("-")[2]);

                int year2 = Integer.parseInt(o2.getWatchDate().split("T")[0].split("-")[0]);
                int month2 = Integer.parseInt(o2.getWatchDate().split("T")[0].split("-")[1]);
                int day2 = Integer.parseInt(o2.getWatchDate().split("T")[0].split("-")[2]);

                if (year1 != year2) {
                    if (year1 > year2) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
                else if (month1 != month2){
                    if (month1 > month2) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
                else {
                    if (day1 > day2) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        // keep a copy of sorted entries
        sortedEntries = (List<MemoirEntry>) memoirEntries.clone();
        return sortedEntries;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<MemoirEntry> sortedEntriesByUserRating() {
        memoirEntries.sort(new Comparator<MemoirEntry>() {
            @Override
            public int compare(MemoirEntry o1, MemoirEntry o2) {
                if (o1.getUserRating() > o2.getUserRating()) {
                    return -1;
                }
                else if (o1.getUserRating() < o2.getUserRating()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        // keep a copy of sorted entries
        sortedEntries = (List<MemoirEntry>) memoirEntries.clone();
        return sortedEntries;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    List<MemoirEntry> sortedEntriesByPublicRating() {
        memoirEntries.sort(new Comparator<MemoirEntry>() {
            @Override
            public int compare(MemoirEntry o1, MemoirEntry o2) {
                if(o1.getImdbRating() > o2.getImdbRating()) {
                    return -1;
                }
                else if(o1.getImdbRating() < o2.getImdbRating()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        // keep a copy of sorted entries
        sortedEntries = (List<MemoirEntry>) memoirEntries.clone();
        return sortedEntries;
    }
}
