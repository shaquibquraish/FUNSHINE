package com.shaquibquraishi.funshine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {


    Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION=111;
    private ArrayList<DailyWeatherReport> arrayList=new ArrayList<>();
    private ArrayList<DailyWeatherReport> arrayList2=new ArrayList<>();
    ImageView weatherIcon;
    TextView cityCountryName;
    TextView date;
    TextView currentTemp;
    TextView minTemp;
    TextView weatherType;
    WeatherAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        madapter=new WeatherAdapter(arrayList2);
        recyclerView.setAdapter(madapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        weatherIcon=findViewById(R.id.weatherIcon);
        cityCountryName=findViewById(R.id.cityCountryName);
        date=findViewById(R.id.weatherDate);
        currentTemp=findViewById(R.id.currentTemp);
        minTemp=findViewById(R.id.minTemp);
        weatherType=findViewById(R.id.weatherType);
       mGoogleApiClient=new GoogleApiClient.Builder(this)
               .addApi(LocationServices.API)
               .enableAutoManage(this,this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .build();



    }
    public void downloadWeatherData(Location location){
        String lat=String.valueOf(location.getLatitude());
        String lon= String.valueOf(location.getLongitude());
        final String url="https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&units=metric&appid=0bf257ad991546fdab6f62ce69f5eec7";
        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject city=response.getJSONObject("city");
                    String cityName=city.getString("name");
                    String country=city.getString("country");

                    JSONArray list=response.getJSONArray("list");
                    String[] str=new String[40];
                    for(int i=0;i<40;i++){
                        JSONObject dateObj=list.getJSONObject(i);
                        str[i]=dateObj.getString("dt_txt").substring(0,10);
                    }
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                    String TodaysDate = df.format(c);
                    int tdate=Integer.parseInt(TodaysDate.substring(8));
                    for(int i=0;i<40;i++){
                        if(TodaysDate.matches(str[i])) {

                            tdate=tdate+1;
                            String s=String.format("%02d",tdate);
                            TodaysDate=TodaysDate.substring(0,8)+s;

                            JSONObject obj = list.getJSONObject(i);
                            JSONObject main = obj.getJSONObject("main");
                            Double currentTemp = main.getDouble("temp");
                            Double maxTemp = main.getDouble("temp_max");
                            Double minTemp = main.getDouble("temp_min");

                            JSONArray weatherArr = obj.getJSONArray("weather");
                            JSONObject weather = weatherArr.getJSONObject(0);
                            String weatherType = weather.getString("main");

                            String rawDate = obj.getString("dt_txt");

                            DailyWeatherReport report = new DailyWeatherReport(cityName, country, currentTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), weatherType, rawDate);
                            Log.i("FROM CLASS", report.getWeather());
                            arrayList.add(report);
                        }
                    }
                    for(int i=1;i<arrayList.size();i++){
                        arrayList2.add(arrayList.get(i));
                    }



                }catch (JSONException e){
                    Log.i("JSON EXC",e.getLocalizedMessage());
                }
                updateUI();
                madapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("FUN",error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }
    public void updateUI(){
        if(arrayList.size()>0){
            DailyWeatherReport report=arrayList.get(0);
            switch (report.getWeather()){
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
            currentTemp.setText(String.valueOf(report.getCurrentTemp()));
            minTemp.setText(String.valueOf(report.getMinTemp()));
            weatherType.setText(report.getWeather());
            cityCountryName.setText(report.getCityName()+","+report.getCountry());
            date.setText(report.getFormattedDate());



        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION);

        }else{
            startLocationServices();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherData(location);

    }
    public void startLocationServices(){
        try{
            LocationRequest request=LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,request,this);

        }catch (SecurityException e){
            e.printStackTrace();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_LOCATION :{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                }else {
                    Toast.makeText(this, "You denied Permission", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
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
    public class WeatherReportViewHolder extends RecyclerView.ViewHolder{
        private ImageView weatherMini_icon;
        private TextView weatherDay;
        private TextView weatherDescription;
        private TextView weatherTempMax;
        private TextView weatherTempMin;

        public WeatherReportViewHolder(View itemView) {
            super(itemView);
            weatherMini_icon=itemView.findViewById(R.id.weatherMini_icon);
            weatherDay=itemView.findViewById(R.id.weatherDay);
            weatherDescription=itemView.findViewById(R.id.weatherDescription);
            weatherTempMax=itemView.findViewById(R.id.weatherTempMax);
            weatherTempMin=itemView.findViewById(R.id.weatherTempMin);
        }
        public void updateUI(DailyWeatherReport report){
            switch (report.getWeather()){
                case "Clear":
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_sunny));
                    break;
                case "Clouds":
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_cloudy));
                    break;
                case "Rain":
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_rainy));
                    break;
                case "Snow":
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_snowy));
                    break;
                case "Drizzle":
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_drizzle));
                    break;
                default:
                    weatherMini_icon.setImageDrawable(getResources().getDrawable(R.drawable.weather_sunny));


            }
            String [] x=report.getFormattedDate().split(",");
            weatherDay.setText(x[0]);
            weatherDescription.setText(report.getWeather() );
            weatherTempMax.setText(String.valueOf(report.getMaxTemp()));
            weatherTempMin.setText(String.valueOf(report.getMinTemp()));

        }
    }
}
