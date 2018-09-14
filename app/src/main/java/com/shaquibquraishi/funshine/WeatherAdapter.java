package com.shaquibquraishi.funshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherReportViewHolder>{
    private ArrayList<DailyWeatherReport> mDailyWeatherReport;
    public WeatherAdapter(ArrayList<DailyWeatherReport> dailyWeatherReports){
        mDailyWeatherReport=dailyWeatherReports;
    }

    @Override
    public void onBindViewHolder(WeatherReportViewHolder holder, int position) {
        DailyWeatherReport report=mDailyWeatherReport.get(position);
        holder.updateUI(report);
    }

    @Override
    public int getItemCount() {
        return mDailyWeatherReport.size();
    }

    @Override
    public WeatherReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new WeatherReportViewHolder(card);
    }
}
