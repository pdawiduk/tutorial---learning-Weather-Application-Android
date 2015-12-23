package com.example.dawiduk.podejscie2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.dawiduk.podejscie2.data.ForecastAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by dawiduk on 11-12-15.
 */
class BackgroundTask extends AsyncTask<String, Void, String[]> {

    private static final String LOG_TAG = BackgroundTask.class.getSimpleName();
    private static final int HOUR_IN_MILISEC=60*60*1000;
    private static final String TIME_FORMAT="EEE MMM dd";
    private ForecastAdapter adapter;
    private Context context;


    public BackgroundTask(ForecastAdapter adapter, Context context){
        this.adapter = adapter;
        this.context = context;

    }

    HttpURLConnection connectUrl;
    BufferedReader reader;
    String JSONline;



    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {
        String[] ids = TimeZone.getAvailableIDs(HOUR_IN_MILISEC);

        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson =  new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        GregorianCalendar dayTime;

        dayTime = new GregorianCalendar();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {

            String day;
            String description;
            StringBuffer highAndLow;


            JSONObject dayForecast = weatherArray.getJSONObject(i);

            dayTime.add(Calendar.DATE,1);
            day = dayTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())+" "+dayTime.get(Calendar.DAY_OF_MONTH);


            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);



        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }
        return resultStrs;

    }

    @Override
    protected String[] doInBackground(String... params) {
        String format="JSON";
        String units="metric";
        int numDays=7;

        final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM="q";
        final String FORMAT_PARAM="mode";
        final String UNITS_PARAM="units";
        final String DAYS_PARAM="cnt";
        final String APPID_PARAM="APPID";



        Uri ApiAdress=Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, params[0])
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, context.getString(R.string.keyApi))
                .build();

        try {
            URL url = new URL(ApiAdress.toString());

            connectUrl = (HttpURLConnection) url.openConnection();//open connection
            connectUrl.setRequestMethod("GET");// set nethod
            connectUrl.connect();

            InputStream input = connectUrl.getInputStream();// set input stream
            StringBuilder builider = new StringBuilder();
            if (input == null) return new String[0] ;

            reader = new BufferedReader(new InputStreamReader(input));

            String line;

            while ((line = reader.readLine()) != null) {
                builider.append(line + '\n');
            }



            JSONline = builider.toString();


        } catch (IOException e) {

            Log.e(LOG_TAG, "You  have an error", e);

            return new String[0];
        } finally {
            if (connectUrl != null) connectUrl.disconnect();

            if (reader != null) {

                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "you have an error", e);
                }
            }
        }

        try{
            return getWeatherDataFromJson(JSONline,numDays);

        }catch(JSONException e){
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();

        }


        return new String[0];
    }


}