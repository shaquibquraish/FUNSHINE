package com.shaquibquraishi.funshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView weatherDay;
    TextView weatherDate;
    TextView currentTemp;
    TextView minTemp;
    TextView weatherDesc;
    TextView humidity;
    TextView pressure;
    TextView wind;
    ImageView weatherIcon;
    TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar=findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        weatherDay=findViewById(R.id.weatherDayd);
        weatherDate=findViewById(R.id.weatherDated);
        currentTemp=findViewById(R.id.currentTempd);
        minTemp=findViewById(R.id.minTempd);
        weatherDesc=findViewById(R.id.weatherTyped);
        humidity=findViewById(R.id.weatherHumidity);
        pressure=findViewById(R.id.weatherPressure);
        wind=findViewById(R.id.weatherWind);
        weatherIcon=findViewById(R.id.weatherIcond);
        city=findViewById(R.id.cityCountryNamed);
        DailyWeatherReport reportDetail=(DailyWeatherReport)getIntent().getSerializableExtra("Detail");
        String[]split=reportDetail.getFormattedDate().split(",");
        weatherDay.setText(split[0]);
        weatherDate.setText(split[1]);
        currentTemp.setText(String.valueOf(reportDetail.getMaxTemp()));
        minTemp.setText(String.valueOf(reportDetail.getMinTemp()));
        weatherDesc.setText(reportDetail.getWeather());
        city.setText(reportDetail.getCityName()+","+reportDetail.getCountry());
        switch (reportDetail.getWeather()){
            case "Clear":
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_clear));
                break;
            case "Clouds":
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_clouds));
                break;
            case "Rain":
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_rain));
                break;
            case "Snow":
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_snow));
                break;
            case "Drizzle":
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_light_rain));
                break;
            default:
                weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.art_clear));


        }


    }
}
