package com.example.covidcase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{
    public ArrayList<SetGetCaseList> country_data;

    public RecycleAdapter(ArrayList<SetGetCaseList> total_list) {
        this.country_data = total_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li =LayoutInflater.from(parent.getContext());
        View v =li.inflate(R.layout.layout_tests_all_country, parent,false);
        ViewHolder viewholder = new ViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.country_name.setText(this.country_data.get(position).getCountryName());
        holder.tv_total_population.setText(this.country_data.get(position).getPopulation());
        holder.tv_test_data.setText(this.country_data.get(position).getTest());
        holder.d.load(this.country_data.get(position).getFlagUrl()).into(holder.country_flag_image);
    }

    @Override
    public int getItemCount() {
        return this.country_data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView country_name, tv_total_population, tv_test_data;
        ImageView country_flag_image;
        RequestManager d;

        public ViewHolder(View itemView) {
            super(itemView);

            country_name = itemView.findViewById(R.id.country_name);
            country_flag_image = itemView.findViewById(R.id.country_flag_image);
            d = Glide.with(itemView);
            tv_total_population = itemView.findViewById(R.id.tv_total_population);
            tv_test_data = itemView.findViewById(R.id.tv_test_data);
        }
    }
}
