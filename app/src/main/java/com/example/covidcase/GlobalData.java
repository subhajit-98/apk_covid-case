package com.example.covidcase;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GlobalData extends Fragment {

    BarChart bar_ch_new_case;
    ArrayList<BarEntry> BARENTRY = new ArrayList<>() ;
    ArrayList<String> BarEntryLabels = new ArrayList<String>();
    ArrayList xAxisName, graphDataSet;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    TextView tv_total_recover, tv_total_death, tv_total;
    ProgressBar pb_affect, pb_death, pb_recover;
    ProgressDialog graph_dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // https://medium.com/@mobindustry/how-to-quickly-implement-beautiful-charts-in-your-android-app-cf4caf050772
        // https://github.com/PhilJay/MPAndroidChart
        // https://www.youtube.com/watch?v=pi1tq-bp7uA

    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));

    }
    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");

    }

    private ArrayList<IBarDataSet> getDataSet() {
        /*ArrayList<IBarDataSet> dataSets = null;

        ArrayList valueSet1 = new ArrayList();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList valueSet2 = new ArrayList();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        // dataSets.add(barDataSet2);
        return dataSets;*/

        ArrayList<IBarDataSet> dataSets = null;
        dataSets = new ArrayList();
        BarDataSet barDataSet1 = new BarDataSet(graphDataSet, "Confirm cases");
        barDataSet1.setColor(Color.rgb(210, 57, 57));
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList getXAxisValues() {
        /*ArrayList xAxis = new ArrayList();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;*/
        return xAxisName;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_total = getActivity().findViewById(R.id.tv_total);
        tv_total_death = getActivity().findViewById(R.id.tv_total_death);
        tv_total_recover = getActivity().findViewById(R.id.tv_total_recover);

        bar_ch_new_case = getActivity().findViewById(R.id.bar_ch_new_case);
        bar_ch_new_case.setNoDataText("Loading data...");

        pb_affect = getActivity().findViewById(R.id.pb_affect);
        pb_death = getActivity().findViewById(R.id.pb_death);
        pb_recover = getActivity().findViewById(R.id.pb_recover);

        // getDataFromServer("https://api.covid19api.com/summary");
        getDataFromServer_new_link("https://disease.sh/v3/covid-19/countries");
        getNewConfirmCase("https://covidtracking.com/api/states/daily");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global_data, container, false);
    }

    /*private void getDataFromServer(String url){
        JsonObjectRequest requestObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pb_affect.setVisibility(View.GONE);
                    pb_death.setVisibility(View.GONE);
                    pb_recover.setVisibility(View.GONE);
                    JSONObject allData = response.getJSONObject("Global");
                    tv_total.setText(allData.getString("TotalConfirmed"));
                    tv_total_death.setText(allData.getString("TotalDeaths"));
                    tv_total_recover.setText(allData.getString("TotalRecovered"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(requestObj);
    }*/

    private void getDataFromServer_new_link(String url){
        JsonArrayRequest requestObj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    pb_affect.setVisibility(View.GONE);
                    pb_death.setVisibility(View.GONE);
                    pb_recover.setVisibility(View.GONE);
                    JSONObject allData = response.getJSONObject(0);
                    tv_total.setText(allData.getString("cases"));
                    tv_total_death.setText(allData.getString("deaths"));
                    tv_total_recover.setText(allData.getString("recovered"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(requestObj);
    }

    private void getNewConfirmCase(String url){
        xAxisName = new ArrayList();
        graphDataSet = new ArrayList();
        JsonArrayRequest requestObj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i< response.length()/500; i++) {
                        JSONObject get_array = response.getJSONObject(i);
                        xAxisName.add(get_array.getString("state"));
                        String positiveCase;
                        if(get_array.getString("positive") == "null"){
                            positiveCase = "0";
                        }
                        else{
                            positiveCase = get_array.getString("positive");
                        }
                        graphDataSet.add(new BarEntry(Float.parseFloat(positiveCase), i));
                        // Log.i("Data: ", String.valueOf(Float.parseFloat(get_array.getString("positive"))));
                        // Log.i("Data", get_array.getString("positive"));
                    }
                    GetConfirmCaseBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(requestObj);
    }

    private void GetConfirmCaseBar(){
        BarData data = new BarData(getXAxisValues(), getDataSet());
        bar_ch_new_case.setNoDataText("Loading...");
        bar_ch_new_case.setData(data);
        bar_ch_new_case.setDescription("Covid-19 new confirm case data");
        bar_ch_new_case.animateXY(2000, 2000);
        bar_ch_new_case.invalidate();
        bar_ch_new_case.setTouchEnabled(true);
        bar_ch_new_case.setDragEnabled(true);
        bar_ch_new_case.setScaleEnabled(true);
        bar_ch_new_case.setScaleMinima(8f, 0f);
    }
}