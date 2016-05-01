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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    OkHttp callPlants = new OkHttp();
    OkHttp callWeather = new OkHttp();
    OkHttp callVorhersage = new OkHttp();
    String localurl;
    public String respWheather;
    public String respPlant;
    public String respVorhersage;

    //Initialisieren der Pflanzenvariablen
    public String name;
    public Integer wasser;
    public Integer licht;
    public Integer temperatur;
    public Integer duengung;

    double temp;

    //Variablen der Vorhersage
    List<Double> vorhersagenTemperaturen = new ArrayList<Double>();
    List<Double> vorhersagenFeuchtigkeiten = new ArrayList<Double>();
    List<Double> vorhersagenNiederschlag = new ArrayList<Double>();

    //Initialisieren der Textfelder und Button
    TextView nameText;
    TextView wasserText;
    TextView lichtText;
    TextView temperaturText;
    TextView duengungText;
    TextView anweisung;
    Button checkButton;

    TextView verhersageInfo;
    TextView vorhersageFeuchtigkeit;
    TextView vorhersageTemp;
    TextView vorhersageRegen;
    TextView vorhersageAnweisung;

    //Hier gibt die Wetter-API die Aktuellen Wetterbedingungen zurück.
    /*Daten:
    Datum in Unix-Zeitformat
    Temperatur in Grad Celsius
    Feuchtigkeit
    */
    String wheaterIP = "http://api.openweathermap.org/data/2.5/weather?lat=50.78&lon=7.28&APPID=ef953dab59421b03e9f978bb389bde56&units=metric";

    //Hier gibt die Wetter-API eine Vorhersage der nächsten 5Tage in 3h-Schritten zurück.
    /*Daten:
    Datum in Unix-Zeitformat
    Temperatur in Grad Celsius
    Feuchtigkeit
    durchschnittliche Niederschlagsmenge der letzten jeweils vorangegangenen 3h
     */
    String vorhersageIP ="http://api.openweathermap.org/data/2.5/forecast?q=hennef&appid=ef953dab59421b03e9f978bb389bde56&units=metric";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Textfelder und Button zuordnen
        nameText = (TextView) findViewById(R.id.namePlant);
        wasserText = (TextView) findViewById(R.id.wasserText);
        lichtText = (TextView) findViewById(R.id.lichtText);
        temperaturText = (TextView) findViewById(R.id.temperaturText);
        duengungText = (TextView) findViewById(R.id.duengungText);
        anweisung = (TextView) findViewById(R.id.anweisung);
        checkButton = (Button) findViewById(R.id.checkAgain);
        checkButton.setOnClickListener(this);

        verhersageInfo = (TextView) findViewById(R.id.verhersageInfo);
        vorhersageFeuchtigkeit = (TextView) findViewById(R.id.vorhersageFeuchtigkeit);
        vorhersageTemp = (TextView) findViewById(R.id.vorhersageTemp);
        vorhersageRegen = (TextView) findViewById(R.id.vorhersageRegen);
        vorhersageAnweisung = (TextView) findViewById(R.id.vorhersageAnweisung);


        //Es muss die lokale IP-Addresse angegeben werden
        localurl = "http://192.168.0.103:8888/plant/";

        try {
            // HTTP Get on Plant Ressource
            Call get = callPlants.doGetRequest(localurl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respPlant = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        JSONObject jsonObjectPlant = new JSONObject(respPlant);
                        JSONArray jsonArray = jsonObjectPlant.getJSONArray("plant");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            name = obj.getString("name");
                            wasser = obj.getInt("wasser");
                            licht = obj.getInt("licht");
                            temperatur = obj.getInt("temperatur");
                            duengung = obj.getInt("duengung");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // HTTP Get on Wheater API Ressource
            Call get = callWeather.doGetRequest(wheaterIP, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    respWheather = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        JSONObject jsonObjectWheather = new JSONObject(respWheather);
                        JSONObject mainWeather = jsonObjectWheather.getJSONObject("main");
                        temp = mainWeather.getDouble("temp");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // HTTP Get on Wheater API Ressource forecast
            Call get = callVorhersage.doGetRequest(vorhersageIP, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    respVorhersage = response.body().string();
                    try {

                        /*
                        "list" : [ {    "clouds" : { "all" : 68 },
                                        "dt" : 1462114800,
                                        "dt_txt" : "2016-05-01 15:00:00",
                                        "main" : {          "grnd_level" : 999.67999999999995,
                                                            "humidity" : 95,
                                                            "pressure" : 999.67999999999995,
                                                            "sea_level" : 1037.6300000000001,
                                                            "temp" : 10.779999999999999,
                                                            "temp_kf" : 0,
                                                            "temp_max" : 10.779999999999999,
                                                            "temp_min" : 10.77
                                                    },
                                        "rain" : { "3h" : 0.0025000000000000001 },
                                        "sys" : { "pod" : "d" },
                                        "weather" : [ {     "description" : "light rain",
                                                            "icon" : "10d",
                                                            "id" : 500,
                                                            "main" : "Rain"
                                                       } ],
                                        "wind" : {          "deg" : 8.5031099999999995,
                                                            "speed" : 5.5599999999999996
                                          }
                                        },
                         */
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        JSONObject jsonObjectVorhersagen = new JSONObject(respVorhersage);
                        JSONArray vorhersagen = jsonObjectVorhersagen.getJSONArray("list");
                        for (int i = 0; i < 3; i++) {

                            JSONObject obj = vorhersagen.getJSONObject(i);
                            String date = obj.getString("dt_txt");
                            System.out.println("Datum / Zeit: "+date);
                            JSONObject rain = obj.getJSONObject("rain");
                            Double vorhersagenRain;
                            if (rain.has("3h")) {
                                vorhersagenRain = rain.getDouble("3h");
                            }
                            else {
                                vorhersagenRain = 0.00;
                            }
                            JSONObject mainVorhersagen = obj.getJSONObject("main");
                            Double vorhersagenHumidity = mainVorhersagen.getDouble("humidity");
                            Double vorhersagentemp = mainVorhersagen.getDouble("temp");
                            vorhersagenFeuchtigkeiten.add(new Double(vorhersagenHumidity));
                            vorhersagenTemperaturen.add(new Double(vorhersagentemp));
                            vorhersagenNiederschlag.add(new Double(vorhersagenRain));
                        }
                        System.out.println(vorhersagenFeuchtigkeiten);
                        System.out.println(vorhersagenTemperaturen);
                        System.out.println(vorhersagenNiederschlag);
                        System.out.println("Vorhersagen Angekommen!");


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

        double feuchtigkeitSum = 0;
        double temperaturSum = 0;
        double niederschlagSum = 0;

        //Prüfen ob die Temperatur für diese Pflanze ausreicht
        if(temp > (temperatur+5)){
            anweisung.setText("Bitte verringern Sie die Temperatur!");
        }
        if(temp < (temperatur-5)){
            anweisung.setText("Bitte erhöhen Sie die Temperatur!");
        }

        //Durchschnitt der nächsten 9h berechnen
        for(int i = 0; i < vorhersagenFeuchtigkeiten.size(); i++){
            feuchtigkeitSum += vorhersagenFeuchtigkeiten.get(i).doubleValue();
            temperaturSum += vorhersagenTemperaturen.get(i).doubleValue();
            niederschlagSum += vorhersagenNiederschlag.get(i).doubleValue();
        }
        feuchtigkeitSum = feuchtigkeitSum/3;
        temperaturSum = temperaturSum/3;
        niederschlagSum = niederschlagSum/3;

        System.out.println("Humidity: " + feuchtigkeitSum);
        System.out.println("Temperaturen: " + temperaturSum);
        System.out.println("Niederschlag: " + niederschlagSum);

        nameText.setText("Name: "+ name );
        wasserText.setText("Wasser: " + wasser + "%");
        lichtText.setText("Licht: " + licht + "%");
        temperaturText.setText("Temperatur: " + temperatur + " Grad");
        duengungText.setText("Duengung: " + duengung + "%");

        vorhersageFeuchtigkeit.setText("Feuchtigkeit: " + round(feuchtigkeitSum, 2) + "%");;
        vorhersageTemp.setText("Temperaturen: " + round(temperaturSum, 2) + " Grad");
        vorhersageRegen.setText("Niederschlag: " + round(niederschlagSum, 4) + " mm");

    }

    public double round(final double value, final int frac) {
        return Math.round(Math.pow(10.0, frac) * value) / Math.pow(10.0, frac);
    }
}
