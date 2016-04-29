package com.example.user.planto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    OkHttp okHttp = new OkHttp();
    String url;
    public String respWheather;
    public String respPlant;
    public String name;
    public Integer wasser;
    public Integer licht;
    public Integer temperatur;
    public Integer duengung;
    double minTemp;
    double maxTemp;
    double temp;

    TextView nameText;
    TextView wasserText;
    TextView lichtText;
    TextView temperaturText;
    TextView duengungText;
    TextView anweisung;
    Button checkButton;

    String wheaterIP = "http://api.openweathermap.org/data/2.5/weather?lat=50.78&lon=7.28&APPID=ef953dab59421b03e9f978bb389bde56";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        nameText = (TextView) findViewById(R.id.namePlant);
        wasserText = (TextView) findViewById(R.id.wasserText);
        lichtText = (TextView) findViewById(R.id.lichtText);
        temperaturText = (TextView) findViewById(R.id.temperaturText);
        duengungText = (TextView) findViewById(R.id.duengungText);
        anweisung = (TextView) findViewById(R.id.anweisung);
        checkButton = (Button) findViewById(R.id.checkAgain);
        checkButton.setOnClickListener(this);

        String ip = getIntent().getExtras().getString("IP", "defaultKey");
        url = "http://"+ip+":8888/plant/";

        try {
            // HTTP Get on Plant Ressource
            Call get = okHttp.doGetRequest(url, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respPlant = response.body().string();
                    try {

                        JSONObject jsonObjectPlant = new JSONObject(respPlant);
                        JSONArray jsonArray = jsonObjectPlant.getJSONArray("plant");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            name = obj.getString("name");
                            wasser = obj.getInt("wasser");
                            licht = obj.getInt("licht");
                            temperatur = obj.getInt("temperatur");
                            duengung = obj.getInt("duengung");
                            System.out.println("wasser: " + wasser);
                            System.out.println("licht: " + licht);
                            System.out.println("temperatur: " + temperatur);
                            System.out.println("duengung: " + duengung);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            // HTTP Get on Wheater API Ressource
            Call get = okHttp.doGetRequest(wheaterIP, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respWheather = response.body().string();
                    try {
                        JSONObject jsonObjectWheather = new JSONObject(respWheather);
                        JSONObject mainWeather = jsonObjectWheather.getJSONObject("main");
                        minTemp = mainWeather.getDouble("temp_min");
                        maxTemp = mainWeather.getDouble("temp_max");
                        temp = mainWeather.getDouble("temp");

                        //Umrechnung von Kelvin in Grad Celsius
                        minTemp = minTemp - 273.15;
                        maxTemp = maxTemp - 273.15;
                        temp = temp - 273.15;

                        //System.out.println("minTemp: " + minTemp);
                        //System.out.println("maxTemp: " + maxTemp);
                        System.out.println("temp: " + temp);
                        //System.out.println(jsonObjectWheather);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        nameText.setText("Name: "+ name );
        wasserText.setText("Wasser: "+ wasser + "%");
        lichtText.setText("Licht: "+ licht + "%");
        temperaturText.setText("Temperatur: "+ temperatur + " Grad");
        duengungText.setText("Duengung: "+ duengung + "%");


        if(temp > (temperatur+5)){
            anweisung.setText("Bitte verringern Sie die Temperatur!");
        }

        if(temp < (temperatur-5)){
            anweisung.setText("Bitte erhöhen Sie die Temperatur!");
        }


    }
}
