package buiernst.eis.planto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ForecastActivity extends Base_Activity{


    Vector<WeatherData> data = new Vector<>();
    ForecastAdaptor listAdapter;
    ListView lv;

    OkHttp callPlants = new OkHttp();
    String weatherUrl;
    String respWeather;
    JSONObject jsonObjectWeather;
    JSONArray jsonArrayWeather;

    Integer id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        id = getIntent().getExtras().getInt("UserID");

        weatherUrl = "http://api.openweathermap.org/data/2.5/forecast?q=hennef&appid=ef953dab59421b03e9f978bb389bde56&units=metric";

        lv = (ListView) findViewById(R.id.listview);
        listAdapter = new ForecastAdaptor(this, R.layout.list_item_forecast, data);

        new CallTask().execute();



    }

    private void populate() {
        lv.setAdapter(listAdapter);
    }

    class CallTask extends AsyncTask<Void, String, JSONObject>{
        String respWeather;
        JSONObject jsonObjectWeather;

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                // HTTP Get on Openweathermap Ressource
                Call get = callPlants.doGetRequest(weatherUrl , new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        respWeather = response.body().string();
                        System.out.println("respWeather: "+ respWeather);
                        try {
                            jsonObjectWeather = new JSONObject(respWeather);
                            System.out.println("jsonObjectWeather: "+jsonObjectWeather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonObjectWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                //converting to an Object
                //Iterate through the JSON-response to get the Data
                JSONArray jsonArrayWeather = jsonObjectWeather.getJSONArray("list");
                for (int i = 0; i < 8; i++) {

                    JSONObject obj = jsonArrayWeather.getJSONObject(i);
                    String date = obj.getString("dt_txt");
                    System.out.println("Date: "+date);

                    JSONObject rain = obj.getJSONObject("rain");
                    Double niederschlag;
                    if (rain.has("3h")) {
                        niederschlag = rain.getDouble("3h");
                    }
                    else {
                        niederschlag = 0.00;
                    }
                    System.out.println("Niederschlag: "+niederschlag);

                    JSONArray weatherArray = obj.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    String icon = weather.getString("icon");
                    System.out.println("Icon: "+icon);

                    JSONObject temperatur = obj.getJSONObject("main");
                    Double tempMax = temperatur.getDouble("temp_max");
                    Double tempMin = temperatur.getDouble("temp_min");
                    System.out.println("tempMax: "+tempMax+" tempMin: "+tempMin);

                    data.add(new WeatherData(icon, date, tempMax, tempMin, niederschlag));

                }

                //listAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            populate();

        }
    }


}