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

    Integer id;
    String ip;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        id = getIntent().getExtras().getInt("UserID");
        ip = getIntent().getExtras().getString("IP");

        weatherUrl = "http://api.openweathermap.org/data/2.5/forecast?q=hennef&appid=ef953dab59421b03e9f978bb389bde56&units=metric";

        lv = (ListView) findViewById(R.id.listview);
        listAdapter = new ForecastAdaptor(this, R.layout.list_item_forecast, data);

        new CallTask().execute();

    }


    class CallTask extends AsyncTask<Void, String, String>{
        String respWeather;
        boolean arrived = false;

        @Override
        protected String doInBackground(Void... params) {
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
                        arrived = true;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(!arrived){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return respWeather;
        }

        @Override
        protected void onPostExecute(String resp) {
            try {
                //converting to an Object
                //Iterate through the JSON-response to get the Data
                JSONObject jsonObjectWeather = new JSONObject(resp);
                JSONArray jsonArrayWeather = jsonObjectWeather.getJSONArray("list");
                for (int i = 0; i < 8; i++) {

                    JSONObject obj = jsonArrayWeather.getJSONObject(i);
                    String date = obj.getString("dt_txt");

                    JSONObject rain;
                    Double niederschlag;
                    if(obj.has("rain")) {
                        rain = obj.getJSONObject("rain");
                        if (rain.has("3h")) {
                            niederschlag = rain.getDouble("3h");
                        }
                        else {
                            niederschlag = 0.00;
                        }
                    }
                    else{
                        niederschlag = 0.00;
                    }

                    JSONArray weatherArray = obj.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    String icon = weather.getString("icon");

                    JSONObject temperatur = obj.getJSONObject("main");
                    Double tempMax = temperatur.getDouble("temp_max");
                    Double tempMin = temperatur.getDouble("temp_min");

                    data.add(new WeatherData(icon, date, tempMax, tempMin, niederschlag));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            lv.setAdapter(listAdapter);
        }
    }


}