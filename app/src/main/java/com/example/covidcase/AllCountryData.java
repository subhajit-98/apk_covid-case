package com.example.covidcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCountryData extends Fragment {

    AdapterViewFlipper show_content_table;
    TableLayout tl_show_all_statics;
    ArrayList<SetGetCaseList> all_case_data = new ArrayList<SetGetCaseList>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_country_data, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // show_content_table = getActivity().findViewById(R.id.show_content_table);
        tl_show_all_statics = getActivity().findViewById(R.id.tl_show_all_statics);
        GetDataFromServer("https://disease.sh/v3/covid-19/countries");
    }

    private void GetDataFromServer(String url){
        JsonArrayRequest all_covid_data = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        /*SetGetCaseList case_details = new SetGetCaseList();
                        case_details.setCountryName(obj.getString("country"));
                        case_details.setConfirm(obj.getString("cases"));
                        case_details.setDeath(obj.getString("deaths"));
                        case_details.setRecover(obj.getString("recovered"));*/
                        JSONObject flag_data = obj.getJSONObject("countryInfo");
                        /*case_details.setFlagUrl(flag_data.getString("flag"));
                        all_case_data.add(case_details);*/
                        ImageView iv_showFlag = new ImageView(getActivity());
                        Glide.with(getActivity()).load(flag_data.getString("flag")).into(iv_showFlag);
                        iv_showFlag.setMaxWidth(30);
                        iv_showFlag.setMaxHeight(20);
                        iv_showFlag.setPadding(0,0,30,0);
                        TableRow tr = new TableRow(getActivity());
                        TextView tv_country = new TextView(getActivity());
                        tv_country.setText(obj.getString("country"));
                        tv_country.setWidth(80);
                        TextView tv_active = new TextView(getActivity());
                        tv_active.setText(obj.getString("cases"));
                        tv_active.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv_active.setTextColor(Color.parseColor("#E3CC08"));
                        TextView tv_dead = new TextView(getActivity());
                        tv_dead.setText(obj.getString("deaths"));
                        tv_dead.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv_dead.setTextColor(Color.parseColor("#EC2011"));
                        TextView tv_recover = new TextView(getActivity());
                        tv_recover.setText(obj.getString("recovered"));
                        tv_recover.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv_recover.setTextColor(Color.parseColor("#FF0FB616"));
                        tr.addView(iv_showFlag);
                        tr.addView(tv_country);
                        tr.addView(tv_active);
                        tr.addView(tv_dead);
                        tr.addView(tv_recover);
                        tr.setPadding(0, 0, 0, 30);
                        // tr.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.table_border, null));
                        tl_show_all_statics.addView(tr, i+1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // show_content_table.setAdapter(new ShowCaseDetails());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(tl_show_all_statics, "You are offline!", Snackbar.LENGTH_SHORT)
                        .setAction("Turn On", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); // Settings.ACTION_WIFI_SETTINGS
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(all_covid_data);
    }

    class ShowCaseDetails extends BaseAdapter{

        @Override
        public int getCount() {
            return all_case_data.size();
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
            LayoutInflater i =getLayoutInflater();
            View v =i.inflate(R.layout.show_country_list_spiner, null);
            TextView t1 =v.findViewById(R.id.tv_show_country_name);
            ImageView flag = v.findViewById(R.id.iv_show_flag);
            String img_url = all_case_data.get(position).getFlagUrl();
            Glide.with(getActivity()).load(img_url).into(flag);
            t1.setText(all_case_data.get(position).getCountryName());
            return v;
        }
    }
}

/*
* tl_show_all_statics = getActivity().findViewById(R.id.tl_show_all_statics);
        for(int i=0; i<10; i++){
            TableRow tr = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(lp);
            TextView abc = new TextView(getActivity());
            abc.setText("Data ");
            abc.setWidth(120);
            TextView abcd = new TextView(getActivity());
            abcd.setText("Data");
            tr.addView(abc);
            tr.addView(abcd);
            tl_show_all_statics.addView(tr, i);
            } */