package com.shaquibquraishi.funshine;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherReportViewHolder extends RecyclerView.ViewHolder{
    private ImageView weatherMini_icon;
    private TextView weatherDay;
    private TextView weatherDescription;
    private TextView weatherTempMax;
    private TextView weatherTempMin;
    public LinearLayout linearLayout;

    public WeatherReportViewHolder(View itemView) {
        super(itemView);
        weatherMini_icon=itemView.findViewById(R.id.weatherMini_icon);
        weatherDay=itemView.findViewById(R.id.weatherDay);
        weatherDescription=itemView.findViewById(R.id.weatherDescription);
        weatherTempMax=itemView.findViewById(R.id.weatherTempMax);
        weatherTempMin=itemView.findViewById(R.id.weatherTempMin);
        linearLayout=itemView.findViewById(R.id.recyclerItem_id);
    }
    public void updateUI(DailyWeatherReport report){
        switch (report.getWeather()){
            case "Clear":
                weatherMini_icon.setImageResource(R.drawable.weather_sunny);
                break;
            case "Clouds":
                weatherMini_icon.setImageResource(R.drawable.weather_cloudy);
                break;
            case "Rain":
                weatherMini_icon.setImageResource(R.drawable.weather_rainy);
                break;
            case "Snow":
                weatherMini_icon.setImageResource(R.drawable.weather_snowy);
                break;
            case "Drizzle":
                weatherMini_icon.setImageResource(R.drawable.weather_drizzle);
                break;
            default:
                weatherMini_icon.setImageResource(R.drawable.weather_sunny);


        }
        String [] x=report.getFormattedDate().split(",");
        weatherDay.setText(x[0]);
        weatherDescription.setText(report.getWeather() );
        weatherTempMax.setText(String.valueOf(report.getMaxTemp()));
        weatherTempMin.setText(String.valueOf(report.getMinTemp()));

    }
}
