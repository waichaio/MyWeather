package com.anthony.myweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Created by anthonyo on 22/5/2015.
 */
public class RetrieveWeatherData extends AsyncTask<String,Void,Weather[]>{
    private final String LOG_TAG = RetrieveWeatherData.class.getSimpleName();
    private MyRecyclerViewAdapter mForecastAdapter;
    private Context mContext;
    private ProgressDialog mDialog;
    private String errorText;

    public RetrieveWeatherData(Context context, MyRecyclerViewAdapter fetchAdapter){
        mContext=context;
        mForecastAdapter=fetchAdapter;
    }

    @Override
    protected Weather[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        BufferedReader reader = null;
        String forecastJsonStr = null;
        String mode="json";
        String units="metric";
        int numDays=7;

        try {
            final String CURRENT_BASE_URL="http://api.openweathermap.org/data/2.5/weather?";
            final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM="q";
            final String FORMAT_PARAM="mode";
            final String UNITS_PARAM="units";
            final String DAYS_PARAM="cnt";
            Uri builtUri;

            builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(FORMAT_PARAM, mode)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .build();

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(builtUri.toString());

            try{
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode= statusLine.getStatusCode();
                if(statusCode== 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    forecastJsonStr = builder.toString();
                }else {
                    Log.e(LOG_TAG, statusCode + Resources.getSystem().getResourceName(R.string.download_failed));
                    errorText=statusCode+"- "+Resources.getSystem().getResourceName(R.string.download_failed);

                    return null;
                }

            }catch (ClientProtocolException e){
                Log.e(LOG_TAG, "Error ", e);
                errorText=e.getMessage();
                return null;
            }

        }catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            //Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            errorText=e.getMessage();
            return null;
        } finally{
            //if (urlConnection != null) {
            //    urlConnection.disconnect();
            // }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getWeatherInJson(forecastJsonStr, numDays);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private Weather[] getWeatherInJson(String forecastJsonStr, int numdays)
            throws JSONException {
        Utilities utilities=new Utilities();
        final String LIST="list";
        final String WEATHER="weather";
        final String HUMIDITY="humidity";
        final String TEMPERATURE="temp";
        final String DAY="day";
        final String OWN_NIGHT="night";
        final String OWN_MAX="max";
        final String OWN_MIN="min";
        final String OWM_DATETIME="dt";
        final String OWM_DESCRIPTION="description";
        final String OWM_ICON="icon";
        final String OWM_WINDSPEED="speed";
        final String CITY="city";
        final String LOCATIONNAME="name";
        DecimalFormat df = new DecimalFormat("#.0");

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray= forecastJson.getJSONArray(LIST);

        JSONObject cityObj = forecastJson.getJSONObject(CITY);
        String locationName = cityObj.getString(LOCATIONNAME);
        Weather[] resultStrs= new Weather[numdays];

        for(int i=0;i<weatherArray.length();i++){
            String day;
            String date;
            String description;
            String highAndLow;

            JSONObject dayForecast=weatherArray.getJSONObject(i);
            //Log.v(LOG_TAG, "dayForecast: "+ dayForecast.toString());

            long dateTime= dayForecast.getLong(OWM_DATETIME);
            day=utilities.getDay(dateTime);
            date=utilities.getDate(dateTime);
            //Log.v(LOG_TAG, "day: "+ day);
            int humidity = dayForecast.getInt(HUMIDITY);
            double speed = dayForecast.getDouble(OWM_WINDSPEED);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            //Log.v(LOG_TAG, "weatherObject: "+ weatherObject.toString());
            description = weatherObject.getString(OWM_DESCRIPTION);
            String icon=weatherObject.getString(OWM_ICON);

            JSONObject temperatureObject=dayForecast.getJSONObject(TEMPERATURE);
            double day_Temp= Double.parseDouble(df.format(temperatureObject.getDouble(DAY)));
            double night_Temp= Double.parseDouble(df.format(temperatureObject.getDouble(OWN_NIGHT)));
            double high= temperatureObject.getDouble(OWN_MAX);
            double low=temperatureObject.getDouble(OWN_MIN);


            highAndLow = utilities.formatHighLows(high, low);

            resultStrs[i] = new Weather();
            resultStrs[i].setDay(day);
            resultStrs[i].setDate(date);
            resultStrs[i].setMaxMin_Temp(highAndLow);
            resultStrs[i].setIcon(icon);
            resultStrs[i].setDay_Temp(day_Temp);
            //resultStrs[i].night_Temp=night_Temp;
            resultStrs[i].setHumidity(humidity);
            resultStrs[i].setWindSpeed(speed);
            resultStrs[i].setDesc(description);
            resultStrs[i].setLocationName(locationName);
        }
        for (Weather s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }
        return resultStrs;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mDialog = new ProgressDialog(mContext);
        Resources res =mContext.getResources();
        mDialog.setMessage(res.getString(R.string.status_loading));
        mDialog.show();
    }

    @Override
    protected void onPostExecute(Weather[] result) {

        if(mForecastAdapter!=null)
            mForecastAdapter.clear();

        if (result != null) {

            for (Weather forecast:result) {

//test+="new Weather("+forecast.icon+","+forecast.date+","+forecast.day+","+ forecast.maxMin_Temp+","+forecast.day_Temp+","+ forecast.night_Temp+")";
                mForecastAdapter.add(forecast);
                //mForecastAdapter.insert(forecast,mForecastAdapter.getCount());
                //mForecastAdapter = new WeatherAdapter(mContext, new Weather[]{new Weather(forecast.icon,forecast.date,forecast.day, forecast.maxMin_Temp,forecast.day_Temp, forecast.night_Temp), new Weather(forecast.icon,forecast.date,forecast.day, forecast.maxMin_Temp,forecast.day_Temp, forecast.night_Temp)});
            }
            //WeatherAdapter weatherAdapter = new WeatherAdapter(mContext,weather);
            //mForecastAdapter = weatherAdapter;
            //mForecastAdapter.notifyDataSetChanged();
        }else{
            if(errorText!=null && errorText!="")
                // Toast.makeText(mContext,errorText,Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, Resources.getSystem().getResourceName(R.string.no_internet), Toast.LENGTH_SHORT).show();

        }


        if(mDialog!=null){
            mDialog.dismiss();
        }
    }
}
