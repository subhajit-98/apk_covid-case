package com.example.covidcase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard extends Fragment {

    Button btn_all_data, btn_country_data;
    FrameLayout fl_show_data;
    ArrayList<SetGetCaseList> total_list = new ArrayList<SetGetCaseList>();
    RecyclerView rv_show_country_details;
    RecycleAdapter rv_adapter;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_all_data = getActivity().findViewById(R.id.btn_all_data);
        btn_country_data = getActivity().findViewById(R.id.btn_country_data);
        fl_show_data = getActivity().findViewById(R.id.fl_show_data);
        rv_show_country_details = getActivity().findViewById(R.id.rv_show_country_details);

        dialog = dialog.show(getActivity(), "Fetching data","Loading. Please wait...", true);

        GetDataFromServer("https://disease.sh/v3/covid-19/countries");

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_show_data, new AllCountryData());
        ft.commit();


        btn_all_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_country_data.setBackgroundResource(R.drawable.dash_bord_country_choose_button);
                btn_all_data.setBackgroundResource(R.drawable.dash_bord_country_choose_button_select);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_show_data, new AllCountryData());
                ft.commit();
            }
        });

        btn_country_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_all_data.setBackgroundResource(R.drawable.dash_bord_country_choose_button);
                btn_country_data.setBackgroundResource(R.drawable.dash_bord_country_choose_button_select);
                FragmentManager fm2 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.fl_show_data, new SingleCountry());
                ft2.commit();
            }
        });
    }

    private void GetDataFromServer(String url){
        JsonArrayRequest obj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i < response.length(); i++){
                    try {
                        JSONObject data = response.getJSONObject(i);
                        SetGetCaseList case_data = new SetGetCaseList();
                        case_data.setCountryName(data.getString("country"));
                        case_data.setPopulation(data.getString("population"));
                        case_data.setTest(data.getString("tests"));
                        JSONObject flag_obj = data.getJSONObject("countryInfo");
                        case_data.setFlagUrl(flag_obj.getString("flag"));
                        total_list.add(case_data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(total_list.size()> 0){
                    rv_show_country_details.setAdapter(new RecycleAdapter(total_list));
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(fl_show_data, "You are offline!", Snackbar.LENGTH_SHORT)
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
}