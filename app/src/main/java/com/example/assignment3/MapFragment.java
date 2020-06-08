package com.example.assignment3;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CaptureRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment {

    // network connection
    NetworkConnection networkConnection = null;
    // view
    View view;
    // user id in database
    int userId;
    String firstName;

    // buttons
    Button btnLoadMap;
    Button btnReturn;

    // cinema-geocode map
    HashMap<String, String> dataPair;

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
            dataPair = getCoord.get();
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
                ArrayList<String> cinemaName = new ArrayList<>();
                ArrayList<String> cinemaLatLng = new ArrayList<>();
                for(String key : dataPair.keySet()) {
                    if(key == "user")
                        continue;
                    cinemaName.add(key);
                    cinemaLatLng.add(dataPair.get(key));
                }
                // load user data
                intent.putExtra("userCoord", dataPair.get("user"));
                //Log.i(" userCoord ", dataPair.get("user"));
                // remove user data from dataPair

                // load cinema data
                // cinema names and coords are stored seperately
                intent.putStringArrayListExtra("cinemaName", cinemaName);
                intent.putExtra("cinemaLatLng", cinemaLatLng);

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

    private class GetCoord extends AsyncTask<Integer, Void, HashMap<String, String>> {
        @Override
        protected HashMap<String, String> doInBackground(Integer... userId) {

            HashMap<String, String> cinemaCoordPair = new HashMap<>();

            try {
                cinemaCoordPair = networkConnection.getCoordinate(userId[0]);
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }

            return cinemaCoordPair;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> results) {
            dataPair = results;
        }
    }

    // get LatLng coord from source data
    private String getLatLng(String sourceData) throws JSONException {
        String longtitude = null;
        String latitude = null;

        JSONObject jsonObject = new JSONObject(sourceData);
        JSONObject feature = (JSONObject) jsonObject.getJSONArray("features").get(0);
        longtitude = feature.getJSONObject("properties").getString("lon");
        latitude = feature.getJSONObject("properties").getString("lat");

        return latitude + ", " + longtitude;
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
