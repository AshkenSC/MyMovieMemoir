package com.example.assignment3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONObject;

public class MovieMemoirFragment extends Fragment{

    JSONObject dataObject = null;
    NetworkConnection networkConnection = null;
    // used to pass to GetAllPersonsTask
    View view2 = null;

    public MovieMemoirFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.movie_memoir_fragment, container, false);
        networkConnection = new NetworkConnection();

        // get inflater and container, ready to pass to GetAllPersonsTask
        view2 = view;

        // find all persons button
        Button findAllStudentsBtn = view.findViewById(R.id.btnFindAll);
        findAllStudentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllPersonsTask getAllStudentsTask = new GetAllPersonsTask();
                getAllStudentsTask.execute();
            }
        });

        // TODO: SPINNIER 1: 3 options to list stored movies spinner


        // TODO: SPINNIER 2: filters of movies (e.g. different genre)

        return view;
    }

    private class GetAllPersonsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return networkConnection.getAllPersons();
        }
        @Override
        protected void onPostExecute(String persons) {
            TextView resultTextView = view2.findViewById(R.id.tvResult);
            resultTextView.setText(persons);
        }
    }
}
