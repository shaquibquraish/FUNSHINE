package com.shaquibquraishi.funshine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherReportViewHolder>{
    private ArrayList<DailyWeatherReport> mDailyWeatherReport;
    private Context context;
    public WeatherAdapter(Context baseContext, ArrayList<DailyWeatherReport> dailyWeatherReports){
        mDailyWeatherReport=dailyWeatherReports;
        context=baseContext;

    }


    @Override
    public void onBindViewHolder(WeatherReportViewHolder holder, final int position) {
        DailyWeatherReport report=mDailyWeatherReport.get(position);
        holder.updateUI(report);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailActivity.class);
                intent.putExtra("Detail",mDailyWeatherReport.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
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
