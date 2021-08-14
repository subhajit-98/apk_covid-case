package com.example.covidcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowGlobalData extends Fragment {
    BarChart total_case_bar_chart;
    PieChart total_death_case_bar_chart;
    ProgressDialog dialog;
    ArrayList CountryList, ActiveCase, DeathCase, CountryCodeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_global_data, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        total_case_bar_chart = getActivity().findViewById(R.id.total_case_bar_chart);
        total_death_case_bar_chart = getActivity().findViewById(R.id.total_death_case_bar_chart);

        GetData("https://disease.sh/v3/covid-19/countries");
    }

    private void GetData(String url){
        dialog = dialog.show(getActivity(), "Fetching data","Loading. Please wait...", true);
        ActiveCase = new ArrayList();
        CountryList = new ArrayList();
        DeathCase = new ArrayList();
        CountryCodeList = new ArrayList();
        JsonArrayRequest obj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject country_details = response.getJSONObject(i);
                        ActiveCase.add(new BarEntry(country_details.getInt("cases"), i));
                        DeathCase.add(new Entry(country_details.getInt("deaths"), i));
                        CountryList.add(country_details.getString("country"));
                        JSONObject country_info = country_details.getJSONObject("countryInfo");
                        CountryCodeList.add(country_info.getString("iso2"));
                        Log.i("Data", String.valueOf(country_details.getInt("deaths")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                MakeGraph();
                PiChart();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(total_case_bar_chart, "You are offline!", Snackbar.LENGTH_SHORT)
                .setAction("Turn On", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); // Settings.ACTION_WIFI_SETTINGS
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                dialog.dismiss();
            }
        });

        Volley.newRequestQueue(getActivity()).add(obj);
    }
    private void MakeGraph(){
        BarDataSet bardataset = new BarDataSet(ActiveCase, "No Of Active Cases");
        total_case_bar_chart.animateY(500);
        BarData data = new BarData(CountryList, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        total_case_bar_chart.setNoDataText("Something Error");
        total_case_bar_chart.setDescription("");
        total_case_bar_chart.setData(data);
        total_case_bar_chart.setTouchEnabled(true);
        total_case_bar_chart.setDragEnabled(true);
        total_case_bar_chart.setScaleEnabled(true);
        total_case_bar_chart.setScaleMinima(10f, -10000000f);
    }

    private void PiChart(){
        PieDataSet dataSet = new PieDataSet(DeathCase, "Number Of DeathCase");
        PieData data = new PieData(CountryCodeList, dataSet);
        total_death_case_bar_chart.setData(data);
        total_death_case_bar_chart.setDescription("");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        total_death_case_bar_chart.animateXY(5000, 5000);
        total_death_case_bar_chart.setMaxAngle(5f);
    }
}