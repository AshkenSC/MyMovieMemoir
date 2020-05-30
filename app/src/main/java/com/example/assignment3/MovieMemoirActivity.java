package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment3.networkconnection.NetworkConnection;

public class MovieMemoirActivity extends AppCompatActivity {

    NetworkConnection networkConnection=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_memoir);
        networkConnection=new NetworkConnection();

        // find all persons button
        Button findAllStudentsBtn = findViewById(R.id.btnFindAll);
        findAllStudentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllPersonsTask getAllStudentsTask = new GetAllPersonsTask();
                getAllStudentsTask.execute();
            }
        });

        // TODO: SPINNIER 1: 3 options to list stored movies spinner


        // TODO: SPINNIER 2: filters of movies (e.g. different genre)

    }

    private class GetAllPersonsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return networkConnection.getAllPersons();
        }
        @Override
        protected void onPostExecute(String persons) {
            TextView resultTextView = findViewById(R.id.tvResult);
            resultTextView.setText(persons);
        }
    }

}
