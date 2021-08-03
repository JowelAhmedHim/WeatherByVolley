package com.example.weatherappbyvolly;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search?query=";
    public static final String QUERY_FOR_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    Context context;
    String cityId;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface  VolleyResponseListener{
        void  onError(String message);
        void  onResponse(String cityId);
    }

    public void getCityID(String cityName,VolleyResponseListener volleyResponseListener){

        String url = QUERY_FOR_CITY_ID +cityName;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                cityId="";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");

                }catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(context, "City ID = "+cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Error");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(arrayRequest);


    }


    public interface  ForCastByIdResponse{
        void  onError(String message);
        void  onResponse(List<WeatherReportModel> weatherReportModels);
    }


    public void getCityForecastByID(String cityID,ForCastByIdResponse forCastByIdResponse){

        List<WeatherReportModel>  weatherReportModels = new ArrayList<>();
        String url = QUERY_FOR_WEATHER_BY_ID+cityID;
        //get the json object;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");
                    //get the first item from the array

                    for (int i=0; i<consolidated_weather_list.length();i++){
                        WeatherReportModel one_day_weather = new WeatherReportModel();

                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        one_day_weather.setId(first_day_from_api.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        one_day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day_weather.setMax_temp(first_day_from_api.getLong("max_temp"));
                        one_day_weather.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day_weather.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        one_day_weather.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        one_day_weather.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        one_day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day_weather.setVisibility(first_day_from_api.getLong("visibility"));
                        one_day_weather.setPredictability(first_day_from_api.getInt("predictability"));
                        weatherReportModels.add(one_day_weather);


                    }

                    forCastByIdResponse.onResponse(weatherReportModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }


    public interface  GetCityForecastByNameCallBack{
        void  onError(String message);
        void  onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName, GetCityForecastByNameCallBack getCityForecastByNameCallBack){
       getCityID(cityName, new VolleyResponseListener() {
           @Override
           public void onError(String message) {

           }

           @Override
           public void onResponse(String cityId) {

               getCityForecastByID(cityId, new ForCastByIdResponse() {
                   @Override
                   public void onError(String message) {

                   }

                   @Override
                   public void onResponse(List<WeatherReportModel> weatherReportModels) {

                       getCityForecastByNameCallBack.onResponse(weatherReportModels);

                   }
               });

           }
       });
    }




}
