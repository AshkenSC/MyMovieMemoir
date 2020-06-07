package com.example.assignment3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportFragmentBar extends Fragment {

    // network connection
    NetworkConnection networkConnection = null;
    // user id in database
    int userId;

    // view and widgets
    View view;
    Spinner yearSpinner;
    Button btnGoToBarChart;
    BarChart barChart;

    // data string and JSON object
    String reportMemoirData;
    HashMap<String, Integer> filteredEntries = new HashMap<>();
    BarDataSet dataSet;

    public ReportFragmentBar(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.report_fragment_bar, container, false);
        // build network connection
        networkConnection = new NetworkConnection();

        // bar chart
        barChart = view.findViewById(R.id.report_bar_chart);
        // button
        btnGoToBarChart = view.findViewById(R.id.goto_pie_chart);
        btnGoToBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToFragment(getActivity(), R.id.content_frame, new ReportFragment(userId));
            }
        });

        // set up spinner
        yearSpinner = view.findViewById(R.id.year_spinner);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer yearSelected = Integer.parseInt(parent.getItemAtPosition(position).toString());
                if(yearSelected != null) {
                    loadData(yearSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // load data
        loadData(2020);

        return view;
    }

    private void loadData(int year) {

        // get some default display data from 2020.01.01 to 2020.05.30
        ReportFragmentBar.LoadMemoirData loadMemoirData = new ReportFragmentBar.LoadMemoirData();
        loadMemoirData.execute(userId);
        try {
            reportMemoirData = loadMemoirData.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // filter memoir data
        try {
            filteredEntries = filterEntries(reportMemoirData, year);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // load data into bar chart
        List<BarEntry> barEntries = new ArrayList<>();
        // set x-axis value
        final ArrayList<String> xLabel = new ArrayList<>();
        for (String key : filteredEntries.keySet()) {
            xLabel.add(key);
            barEntries.add(new BarEntry(Integer.parseInt(key), filteredEntries.get(key)));
        }

        // load entries defined above into data set
        dataSet = new BarDataSet(barEntries,"Movie Watched Per-month");

        // set up colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        loadColors(colors);
        dataSet.setColors(colors);

        // assign colors to entries in bar chart
        BarData barData = new BarData(dataSet);
        barData.setDrawValues(true);
        barData.setBarWidth(0.9f);

        // set x axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new XAxisValueFormatter());

        barChart.setData(barData);
        barChart.animateXY(1500, 1500);
        barChart.invalidate();
    }

    private HashMap<String, Integer> filterEntries(String reportMemoirData, int yearSelected) throws JSONException {
        HashMap<String, Integer> yearStat = new HashMap<>();
        JSONArray jsonArray = new JSONArray();

        // load into JSON array
        try {
            jsonArray = new JSONArray(reportMemoirData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // traverse JSON array
        for(int i = 0; i < jsonArray.length(); i++) {
            String dateStr = "";

            try {
                dateStr= jsonArray.getJSONObject(i).getString("watchDate");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(parseDate(dateStr)[0] == yearSelected) {
                // if the month already exists, update it
                if(yearStat.containsKey(String.valueOf(parseDate(dateStr)[1]))) {
                    int oldValue = yearStat.get(String.valueOf(parseDate(dateStr)[1]));
                    yearStat.put(String.valueOf(parseDate(dateStr)[1]), oldValue + 1);
                }
                // otherwise insert a new key-value pair
                else {
                    yearStat.put(String.valueOf(parseDate(dateStr)[1]), 1);
                }
            }
        }

        return yearStat;
    }

    private int[] parseDate(String dateFrom) {
        dateFrom = dateFrom.split("T")[0];
        int fromYear = Integer.parseInt(dateFrom.split("-")[0]);
        int fromMonth = Integer.parseInt(dateFrom.split("-")[1]);
        int fromDay = Integer.parseInt(dateFrom.split("-")[2]);

        int dateInt[] = {fromYear, fromMonth, fromDay};
        return dateInt;
    }

    private void loadColors(ArrayList<Integer> colors) {
        colors.add(getResources().getColor(R.color.colorRed));
        colors.add(getResources().getColor(R.color.colorGreen));
        colors.add(getResources().getColor(R.color.colorOrange));
        colors.add(getResources().getColor(R.color.colorMintGreen));
        colors.add(getResources().getColor(R.color.colorYellow));
        colors.add(getResources().getColor(R.color.colorPurple));
        colors.add(getResources().getColor(R.color.colorGray));
        colors.add(getResources().getColor(R.color.colorNavyBlue));
    }

    private class LoadMemoirData extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... userId) {
            return networkConnection.getMemoirData(userId[0]);
        }
        @Override
        protected void onPostExecute(String dataString) {
            reportMemoirData = dataString;
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

    public class XAxisValueFormatter implements IAxisValueFormatter {
        private String[] xStrs = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

        @Override

        public String getFormattedValue(float value, AxisBase axis) {
            int position = (int) value;
//            if (position >= 12) {
//                position = 0;
//            }
            return xStrs[position];
        }
    }

}
