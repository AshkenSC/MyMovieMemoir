package com.example.assignment3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.networkconnection.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment {

    // network connection
    NetworkConnection networkConnection = null;
    // view
    View view;
    // user id in database
    int userId;
    String firstName;
    // user coord
    String userCoord;

    // buttons
    Button btnLoadMap;
    Button btnReturn;

    public MapFragment(int userId, String firstName) {
        this.userId = userId;
        this.firstName = firstName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.map_fragment, container, false);
        // network connection
        networkConnection = new NetworkConnection();

        // convert user's location to geo coordinate
        GetCoord getCoord = new GetCoord();
        getCoord.execute(userId);
        try {
            userCoord = getCoord.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // load map button
        btnLoadMap = view.findViewById(R.id.map_load);
        btnLoadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load google map
                Intent intent = new Intent(getActivity(), GoogleMapsActivity.class);
                // pass data
                intent.putExtra("userCoord", userCoord);
                Log.i(" userCoord ", userCoord);
                // load activity
                startActivity(intent);
            }
        });

        // return button
        btnReturn = view.findViewById((R.id.map_return));
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToFragment(getActivity(), R.id.content_frame, new HomeFragment(userId, firstName));
            }
        });

        return view;
    }

    private class GetCoord extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... userId) {

            String sourceData = "";
            String longtitude = null;
            String latitude = null;

            try {
                sourceData = networkConnection.getCoordinate(userId[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(sourceData);
                JSONObject feature = (JSONObject) jsonObject.getJSONArray("features").get(0);
                longtitude = feature.getJSONObject("properties").getString("lon");
                latitude = feature.getJSONObject("properties").getString("lat");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return latitude + ", " + longtitude;
        }

        @Override
        protected void onPostExecute(String results) {
            userCoord = results;
        }
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
