package com.example.assignment3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportFragment extends Fragment {

    // network connection
    NetworkConnection networkConnection = null;
    // user id in database
    int userId;

    // view and widgets
    View view;
    Button btnFrom;
    TextView tvFrom;
    Button btnTo;
    TextView tvTo;
    Button btnGoToBarChart;
    PieChart pieChart;

    // data string and JSON object
    String reportMemoirData;
    HashMap<String, Integer> filteredEntries = new HashMap<>();
    PieDataSet dataSet;

    public ReportFragment(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        view = inflater.inflate(R.layout.report_fragment, container, false);
        // build network connection
        networkConnection = new NetworkConnection();

        // pie chart
        pieChart = view.findViewById(R.id.report_pie_chart);
        // from date and to date text view
        tvFrom = view.findViewById(R.id.pie_from_date);
        tvTo = view.findViewById(R.id.pie_to_date);
        // buttons
        btnGoToBarChart = view.findViewById(R.id.goto_bar_chart);
        btnFrom = view.findViewById(R.id.pie_from_button);
        btnTo = view.findViewById(R.id.pie_to_button);

        // from button
        btnFrom.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //  date numbers of "From"
                int year = 2020, month = 5, day = 1;
                // get "To" date
                final String toStr = tvTo.getText().toString();

                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                // 如果from大于to，则报错
                                String yearStr = String.valueOf(year);
                                String monthStr = String.valueOf(month + 1);
                                if(month < 10) monthStr = "0" + monthStr;
                                String dayStr = String.valueOf(day);
                                if(day < 10) dayStr = "0" + dayStr;

                                String fromStr = yearStr + "-" + monthStr + "-" + dayStr;

                                Integer i = fromStr.compareTo(toStr);
                                if(i < 0) {
                                    tvFrom.setText(fromStr);
                                    reloadChart();
                                }
                                else {
                                    Toast.makeText(getActivity(),"\"From\" date must be earlier than \"To\"" ,Toast.LENGTH_LONG).show();
                                }

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // to button
        btnTo.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //  date numbers of "From"
                int year = 2020, month = 5, day = 1;
                // get "From" date
                final String fromStr = tvFrom.getText().toString();

                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                // 如果from大于to，则报错
                                String yearStr = String.valueOf(year);
                                String monthStr = String.valueOf(month + 1);
                                if(month < 10) monthStr = "0" + monthStr;
                                String dayStr = String.valueOf(day);
                                if(day < 10) dayStr = "0" + dayStr;;

                                String toStr = yearStr + "-" + monthStr + "-" + dayStr;

                                Integer i = fromStr.compareTo(toStr);
                                if(i < 0) {
                                    tvFrom.setText(toStr);
                                    reloadChart();
                                }
                                else {
                                    Toast.makeText(getActivity(),"\"From\" date must be earlier than \"To\"" ,Toast.LENGTH_LONG).show();
                                }

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // switch button
        btnGoToBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToFragment(getActivity(), R.id.content_frame, new ReportFragmentBar(userId));
            }
        });

        // load data
        reloadChart();

        return view;
    }

    // fragment stack, used for press back button to return to previous fragment
    public void startToFragment(Context context, int container, Fragment newFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, newFragment);
        transaction.addToBackStack(context.getClass().getName());
        transaction.commit();
    }

    private void reloadChart() {

        // get some default display data from 2020.01.01 to 2020.05.30
        LoadMemoirData loadMemoirData = new LoadMemoirData();
        loadMemoirData.execute(userId);
        try {
            reportMemoirData = loadMemoirData.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // filter memoir data
        String dateFrom = tvFrom.getText().toString();
        String dateTo = tvTo.getText().toString();
        try {
            filteredEntries = filterEntries(reportMemoirData, dateFrom, dateTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // load data into pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (String key : filteredEntries.keySet()) {
            pieEntries.add(new PieEntry(filteredEntries.get(key), key));
        }

        // load entries defined above into data set
        dataSet = new PieDataSet(pieEntries,"Postcode");

        // set up colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        loadColors(colors);
        dataSet.setColors(colors);

        // assign colors to entries in pie chart
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);

        pieChart.setUsePercentValues(true);     // set percentage display
        pieChart.animate();                     // set animation
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private HashMap<String, Integer> filterEntries(String reportMemoirData, String dateFrom, String dateTo) throws JSONException {
        HashMap<String, Integer> cinemaStat = new HashMap<>();
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

            if(inDateRange(dateFrom, dateTo, dateStr)) {
                String cinemaPostcode = jsonArray.getJSONObject(i).getString("cinemaPostcode");
                // if the postcode already exists, update it
                if(cinemaStat.containsKey(cinemaPostcode)) {
                    Integer oldValue = cinemaStat.get(cinemaPostcode);
                    cinemaStat.put(cinemaPostcode, oldValue + 1);
                }
                // otherwise insert a new key-value pair
                else {
                    cinemaStat.put(cinemaPostcode, 1);
                }
            }
        }

        return cinemaStat;
    }

    private boolean inDateRange(String dateFrom, String dateTo, String dateStr) {
        // parse date
        int dateFromDigit[] = parseDate(dateFrom);
        int dateToDigit[] = parseDate(dateTo);
        int dateDigit[] = parseDate(dateStr.split("T")[0]);
        if(dateDigit[0] >= dateFromDigit[0] && dateDigit[0] <= dateToDigit[0]) {
            if(dateDigit[1] >= dateFromDigit[1] && dateDigit[1] <= dateToDigit[1]) {
                if(dateDigit[2] >= dateFromDigit[2] && dateDigit[2] <= dateToDigit[2]) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] parseDate(String dateFrom) {
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
}
