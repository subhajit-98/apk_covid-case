package com.example.covidcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleCountry extends Fragment {

    Spinner spin_show_country;
    ArrayList<SetGetCountryList> CountryList = new ArrayList<SetGetCountryList>();
    ListView lv_show_country_details;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_country, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spin_show_country = getActivity().findViewById(R.id.spin_show_country);
        spin_show_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetSingleCountryDetails(CountryList.get(position).getCountry_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv_show_country_details = getActivity().findViewById(R.id.lv_show_country_details);
        dialog = dialog.show(getActivity(), "Fetching data","Loading. Please wait...", true);
        GetAllCountryList("https://api.covid19api.com/countries");
    }

    public void GetAllCountryList(String url){
        JsonArrayRequest obj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Set blank data
                SetGetCountryList default_list = new SetGetCountryList();
                default_list.setCountry_name("Select Country");
                default_list.setCountry_slug("");
                default_list.setCountry_code("");
                CountryList.add(default_list);
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject country_data = response.getJSONObject(i);
                        SetGetCountryList list = new SetGetCountryList();
                        list.setCountry_name(country_data.getString("Country"));
                        list.setCountry_slug(country_data.getString("Slug"));
                        list.setCountry_code(country_data.getString("ISO2"));
                        CountryList.add(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(CountryList.size() > 0){
                    spin_show_country.setAdapter(new CountryListSpinner());
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(spin_show_country, "You are offline!", Snackbar.LENGTH_SHORT)
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

    class CountryListSpinner extends BaseAdapter{

        @Override
        public int getCount() {
            return CountryList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.show_country_list_spiner, null);
            TextView ShowCountryName = v.findViewById(R.id.tv_show_country_name);
            ShowCountryName.setText(CountryList.get(position).getCountry_name());
            ImageView ShowCountryFlag = v.findViewById(R.id.iv_show_flag);
            if(CountryList.get(position).getCountry_code() != ""){
                Glide.with(getActivity()).load("https://www.countryflags.io/"+CountryList.get(position).getCountry_code()+"/shiny/64.png").into(ShowCountryFlag);
            }
            else{
                ShowCountryFlag.setImageResource(R.drawable.global);
            }
            return v;
        }
    }

    private void GetSingleCountryDetails(String countrySlug){
        // Toast.makeText(getActivity(), countrySlug, Toast.LENGTH_LONG).show();
        if(countrySlug != "" && countrySlug != "Select Country"){
            dialog = dialog.show(getActivity(), "Fetching data","Loading. Please wait...", true);
            String RequestUrl = "https://disease.sh/v3/covid-19/countries/"+countrySlug;
            JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, RequestUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String Country = response.getString("country");
                        String Cases = response.getString("cases");
                        String TodayCases = response.getString("todayCases");
                        String Death = response.getString("deaths");
                        String TodayDeath = response.getString("todayDeaths");
                        String Recovered = response.getString("recovered");
                        String TodayRecovered = response.getString("todayRecovered");
                        String Critical = response.getString("critical");
                        JSONObject flag_obj = response.getJSONObject("countryInfo");
                        String FlagUrl = flag_obj.getString("flag");
                        lv_show_country_details.setAdapter(new SingleCountryDetailsShow(
                                Country, FlagUrl, Cases, TodayCases, Death, TodayDeath, Recovered, TodayRecovered, Critical
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            Volley.newRequestQueue(getActivity()).add(obj);
        }
    }

    class SingleCountryDetailsShow extends BaseAdapter{
        String Country, FlagUrl, ActiveCase, TodayActiveCase, Death, TodayDeath, Recovered, TodayRecovered, Critical;

        public SingleCountryDetailsShow(String country, String FlagUrl, String cases, String todayCases, String death, String todayDeath, String recovered, String todayRecovered, String critical) {
            this.Country = country;
            this.FlagUrl = FlagUrl;
            this.ActiveCase = cases;
            this.TodayActiveCase = todayCases;
            this.Death = death;
            this.TodayDeath = todayDeath;
            this.Recovered = recovered;
            this.TodayRecovered = todayRecovered;
            this.Critical = critical;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();
            View v =li.inflate(R.layout.layoutshow_single_country_record_layout, null);
            ImageView iv_country_flag = v.findViewById(R.id.iv_country_flag);
            Glide.with(getActivity()).load(this.FlagUrl).into(iv_country_flag);
            TextView tv_show_country_name = v.findViewById(R.id.tv_show_country_name);
            tv_show_country_name.setText(this.Country);
            TextView tv_active_case = v.findViewById(R.id.tv_active_case);
            tv_active_case.setText(this.ActiveCase);
            TextView tv_dead_case = v.findViewById(R.id.tv_dead_case);
            tv_dead_case.setText(this.Death);
            TextView tv_recovary_case = v.findViewById(R.id.tv_recovary_case);
            tv_recovary_case.setText(this.Recovered);
            TextView tv_total_active = v.findViewById(R.id.tv_total_active);
            tv_total_active.setText(this.TodayActiveCase);
            TextView tv_death_case = v.findViewById(R.id.tv_death_case);
            tv_death_case.setText(this.TodayDeath);
            TextView tv_total_recovary_case = v.findViewById(R.id.tv_total_recovary_case);
            tv_total_recovary_case.setText(this.TodayRecovered);
            return v;
        }
    }
}