package com.example.weatherappbyvolly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button  getCityId,getWeatherByName,getGetWeatherById;
    EditText input;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv);

        getCityId = findViewById(R.id.cityid);
        getWeatherByName = findViewById(R.id.weatherName);
        getGetWeatherById = findViewById(R.id.weatherId);

        input = findViewById(R.id.input);

        getCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

                weatherDataService.getCityID(input.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                  @Override
                  public void onError(String message) {
                      Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onResponse(String cityId) {
                      Toast.makeText(MainActivity.this, "Returned Id "+cityId, Toast.LENGTH_SHORT).show();
                   }
                });
            }
        });

        getGetWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
                weatherDataService.getCityForecastByID(input.getText().toString(), new WeatherDataService.ForCastByIdResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Failled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModel);
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        });

        getWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
                weatherDataService.getCityForecastByName(input.getText().toString(), new WeatherDataService.GetCityForecastByNameCallBack() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Failled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModel);
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
}