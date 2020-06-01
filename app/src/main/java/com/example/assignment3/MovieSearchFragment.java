package com.example.assignment3;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.adapter.SearchRecyclerViewAdapter;
import com.example.assignment3.model.SearchResult;
import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieSearchFragment extends Fragment {
    NetworkConnection networkConnection = null;

    // widgets
    private EditText inputKeyword;
    private Button movieSearchBtn;

    // user's info and data
    String firstName;
    int userId;
    String movieSearchData = new String();

    // input keyword
    String keyword;

    // declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchRecyclerViewAdapter adapter;
    private List<SearchResult> searchResults;

    public MovieSearchFragment(int userId, String firstName) {
        // Required empty public constructor
        this.userId = userId;
        this.firstName = firstName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        final View view = inflater.inflate(R.layout.movie_search_fragment, container, false);

        /* display search bar */
        inputKeyword = view.findViewById(R.id.search_movie_bar);
        movieSearchBtn = view.findViewById(R.id.search_button);
        movieSearchBtn.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View v) {
                 // network connection
                 networkConnection = new NetworkConnection();

                 // get input keyword
                 keyword = inputKeyword.getText().toString();

                 /* recycler view to display search result */
                 recyclerView = view.findViewById(R.id.search_recycler_view);

                 searchResults = new ArrayList<>();
                 searchResults = SearchResult.createEntryList();

                 // execute search
                 GetMovieSearchResult getMovieSearchResult = new GetMovieSearchResult();
                 getMovieSearchResult.execute(keyword);
                 // get result string
                 try {
                     movieSearchData = getMovieSearchResult.get();
                 } catch (ExecutionException e) {
                     e.printStackTrace();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 // get search result list
                 ArrayList<ArrayList<String>> movieSearchList = new ArrayList<>();
                 try {
                     movieSearchList = parseData(movieSearchData);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

                 // display search result
                 // initiate adapter
                 adapter = new SearchRecyclerViewAdapter(searchResults);
                 recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                         LinearLayoutManager.VERTICAL));
                 recyclerView.setAdapter(adapter);
                 layoutManager = new LinearLayoutManager(getActivity());
                 recyclerView.setLayoutManager(layoutManager);

                 // add entries to entry list
                 for (int i = 0; i < movieSearchList.size(); i++) {
                     SearchResult SearchResult = new SearchResult(movieSearchList.get(i).get(0),
                             movieSearchList.get(i).get(1),
                             movieSearchList.get(i).get(2));
                     searchResults.add(SearchResult);
                 }
                 adapter.addUnits(searchResults);
             }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<ArrayList<String>> parseData(String movieSearchData) throws JSONException {
        ArrayList<ArrayList<String>> entryList = new ArrayList<>();
        JSONObject dataObject = new JSONObject(movieSearchData);
        JSONArray dataList = new JSONArray(dataObject.getString("Search"));
        for (int i = 0; i < dataList.length(); i++) {
            JSONObject entryObject = dataList.getJSONObject(i);
            ArrayList<String> entry = new ArrayList<>();
            entry.add(entryObject.getString("Title"));
            entry.add(entryObject.getString("Year"));
            entry.add(entryObject.getString("Poster"));
            entryList.add(entry);
        }

        return entryList;
    }

    private class GetMovieSearchResult extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... keywords) {
            return networkConnection.getMovieSearchResult(keywords[0]);
        }

        @Override
        protected void onPostExecute(String results) {
            movieSearchData = results;
        }
    }
}