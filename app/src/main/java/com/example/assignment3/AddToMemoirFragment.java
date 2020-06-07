package com.example.assignment3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddToMemoirFragment extends Fragment {

    // view and network
    View view;
    NetworkConnection networkConnection;

    // movie data and user info
    int userId;
    String imdbId;
    JSONObject entryJSONObject = null;

    // widgets in the layout
    // movie info
    TextView tvName;
    TextView tvReleaseDate;
    TextView tvImdbId;
    ImageView image;
    // memoir input
    DatePicker watchDate;
    TimePicker watchTime;
    EditText cinemaPostcode;
    EditText comment;
    // Button
    Button btnSubmit;
    // rating bar
    RatingBar ratingBar;
    float ratingScore;
    boolean isRated = false;

    // packed data
    Map<String, String> memoirData = new HashMap<>();
    Map<String, String> cinemaData = new HashMap<>();

    public AddToMemoirFragment(int userId, String imdbId) {
        // Required empty public constructor
        this.userId = userId;
        this.imdbId = imdbId;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.add_to_memoir_fragment, container, false);

        // establish network connection
        networkConnection = new NetworkConnection();

        /* bind widgets */
        tvName = view.findViewById(R.id.add_to_memoir_name);
        tvReleaseDate = view.findViewById(R.id.add_to_memoir_release_date);
        tvImdbId = view.findViewById(R.id.add_to_memoir_imdb_id);
        image = view.findViewById(R.id.add_to_memoir_image);

        // set up rating bar
        ratingBar = view.findViewById(R.id.add_to_memoir_rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingScore = rating * 2;
                isRated = true;
            }
        });

        watchDate = view.findViewById(R.id.add_to_memoir_date_Picker);
        watchTime = view.findViewById(R.id.add_to_memoir_time_Picker);
        cinemaPostcode = view.findViewById(R.id.add_to_memoir_postcode);
        comment = view.findViewById(R.id.add_to_memoir_comment);

        /* down load movie info using API */
        GetMovieDetail getMovieDetail = new GetMovieDetail();
        getMovieDetail.execute(imdbId);

        /* submit input info */
        btnSubmit = view.findViewById(R.id.add_to_memoir_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // pack input data
                 if(!isRated) {
                     ratingScore = ratingBar.getRating() * 2;
                 }

                 memoirData.put("movieName", tvName.getText().toString());
                 memoirData.put("movieReleaseDate", tvReleaseDate.getText().toString());
                 memoirData.put("watchDate", DateTimeFormat.dateStrAddTail(DateTimeFormat.getDateStr(watchDate)));
                 memoirData.put("watchTime", DateTimeFormat.getTimeStr(watchTime));
                 memoirData.put("comment", comment.getText().toString());
                 memoirData.put("score", String.valueOf(ratingScore));
                 memoirData.put("personId", String.valueOf(userId));
                 memoirData.put("imdbId", tvImdbId.getText().toString());
                 memoirData.put("cinemaPostcode", cinemaPostcode.getText().toString());

                 cinemaData.put("cinemaName", tvImdbId.getText().toString());
                 cinemaData.put("cinemaLocation", "Australia");
                 cinemaData.put("cinemaPostcode", cinemaPostcode.getText().toString());

                 // put cinema and memoir data into a list
                 List<Map<String, String>> cinemaAndMemoir = new ArrayList<>();
                 cinemaAndMemoir.add(cinemaData);
                 cinemaAndMemoir.add(memoirData);

                 // upload memoir to database
                 SubmitMemoir submitMemoir = new SubmitMemoir();
                 submitMemoir.execute(cinemaAndMemoir);
             }});

        return view;
    }

    // get movie info
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

            // set movie info
            tvImdbId.setText(imdbId);
            try {
                tvName.setText(entryJSONObject.getString("Title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tvReleaseDate.setText(entryJSONObject.getString("Released"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Picasso.get().load(entryJSONObject.getString("Poster")).into(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // submit memoir
    private class SubmitMemoir extends AsyncTask<List<Map<String, String>>, Void, String> {
        @Override
        protected String doInBackground(List<Map<String, String>>... sourceData) {
            return networkConnection.submitMemoir(sourceData[0]);
        }

        @Override
        protected void onPostExecute(String message) {
            if(message == "") {
                Toast.makeText(getActivity(), "The entry has been successfully uploaded.", Toast.LENGTH_LONG).show();
                startToFragment(getActivity(), R.id.content_frame, new MovieMemoirFragment(userId));
            }
            else {
                Toast.makeText(getActivity(), "An error occurred. Upload failed.", Toast.LENGTH_LONG).show();
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

/*

{"cinemaId":{"cinemaId":4,"cinemaLocation":"Australia","cinemaName":"tt9590742","cinemaPostcode":"8007"},"cinemaPostcode":"8007","comment":"ok;;;lll;;;8007;;;lll;;;tt9590742","imdbId":"tt9590742","memoirId":7,"movieName":"Return to Zootopia","movieReleaseDate":"2017-10-01T00:00:00+08:00","personId":{"address":"Long Street, Wuhu","dob":"1998-01-27","firstName":"Ashken","gender":"Male","personId":1,"postcode":"8002","stateName":"Anhui","surname":"Bear"},"score":40,"watchDate":"2020-06-05T00:00:00+08:00","watchTime":"1970-01-01T22:14:00"}

{"cinemaId":{"cinemaId":12,"cinemaLocation":"Australia","cinemaName":"tt2380307","cinemaPostcode":"8007"},"cinemaPostcode":"8007","comment":"ok;;;lll;;;8007;;;lll;;;tt2380307","imdbId":"tt2380307","memoirId":7,"movieName":"Coco","movieReleaseDate":"2017-11-22T00:00:00+08:00","personId":{"address":"Long Street, Wuhu","dob":"1998-01-27T00:00:00+08:00","firstName":"Ashken","gender":"Male","personId":1,"postcode":"8002","stateName":"Anhui","surname":"Bear"},"score":30,"watchDate":"2020-06-05T00:00:00+08:00","watchTime":"1970-01-01T22:52:00"}
 */