package buiernst.eis.planto;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class InstructionActivity extends Base_Activity{
    Vector<InstructionData> data = new Vector<>();
    InstructionAdaptor listAdapter;

    OkHttp callPlant = new OkHttp();
    OkHttp callMeasuredPlant = new OkHttp();
    OkHttp callStation = new OkHttp();
    OkHttp callWeather = new OkHttp();
    OkHttp callWaterNeed = new OkHttp();
    OkHttp callFertilizeNeed = new OkHttp();

    String myplantUrl;
    String plantUrl;
    String stationUrl;
    String weatherUrl;
    String waterNeedUrl;
    String fertilizeNeedUrl;

    String respPlant;
    String respMyPlant;
    String respStation;
    String respWeather;
    String respWaterNeed;
    String respFertilizeNeed;

    JSONObject jsonObjectMyPlant;
    JSONArray jsonArrayMyPlant;

    Double wBodenfeuchtigkeit = 0.0;
    Double wTempMin = 0.0;
    Double wTempMax = 0.0;

    Double mLichtstaerke;
    Double mTemp;
    Double mBodenfeuchtigkeit;
    Double mBodenPH;

    String stationID;

    Double pLichtstaerke;
    Double pTemp;
    Double pBodenfeuchtigkeit;
    Double pBodenPH;
    String pWachstum;

    Double bKalium;
    Double bStickstoff;
    Double bPhosphat;

    Double bedarfKalium;
    Double bedarfStickstoff;
    Double bedarfPhosphat;

    String stationsAenderung;

    Double bedarfWasser;

    Double duengungsFaktor;

    Boolean wArrived = false;
    Boolean mArrived = false;
    Boolean pArrived = false;
    Boolean sArrived = false;
    Boolean waterneedArrived = false;
    Boolean fertilizeneedArrived = false;

    String ip;
    Integer id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        id = getIntent().getExtras().getInt("UserID");
        ip = getIntent().getExtras().getString("IP");

        myplantUrl = "http://"+ip+":8888/user/"+id+"/measuredPlant/";
        weatherUrl = "http://api.openweathermap.org/data/2.5/forecast?q=hennef&appid=ef953dab59421b03e9f978bb389bde56&units=metric";

        try {
            // HTTP Get on OpenWeatherAPI Ressource IDs
            Call get = callWeather.doGetRequest(weatherUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respWeather = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        JSONObject jsonObjectWeather = new JSONObject(respWeather);
                        JSONArray jsonArrayWeather = jsonObjectWeather.getJSONArray("list");

                        //Get WeatherData for 24 hours
                        for (int i = 0; i < 8; i++) {

                            JSONObject obj = jsonArrayWeather.getJSONObject(i);
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

                            wBodenfeuchtigkeit += niederschlag;
                            JSONObject temperatur = obj.getJSONObject("main");
                            Double tempMax = temperatur.getDouble("temp_max");
                            Double tempMin = temperatur.getDouble("temp_min");

                            wTempMin += tempMin;
                            wTempMax += tempMax;
                        }

                        wTempMin = wTempMin/8;
                        wTempMax = wTempMax/8;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    wArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Waiting for Response
        while(!wArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("wBodenfeuchtigkeit: "+wBodenfeuchtigkeit);

        try {
            // HTTP Get on measuredPlants Ressource IDs
            Call get = callMeasuredPlant.doGetRequest(myplantUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respMyPlant = response.body().string();
                    mArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Waiting for response
        while(!mArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            //converting to an Object
            //Iterate through the measuredPlant Array, getting all values for calculating Situations
            jsonObjectMyPlant = new JSONObject(respMyPlant);
            jsonArrayMyPlant = jsonObjectMyPlant.getJSONArray("measuredPlant");
            for (int i = 0; i < jsonArrayMyPlant.length(); i++) {

                JSONObject obj = jsonArrayMyPlant.getJSONObject(i);

                mLichtstaerke = obj.getDouble("lichtstaerke");
                mTemp = obj.getDouble("temperatur");
                mBodenfeuchtigkeit = obj.getDouble("bodenfeuchtigkeit");
                mBodenPH = obj.getDouble("bodenwert");

                duengungsFaktor = getDuengungsFaktor(mBodenPH);

                stationID = obj.getString("stationid");

                String plantid = obj.getString("plantid");

                String plant = getPlant(plantid);
                String station = getStation(stationID);

                String mPlantID = obj.getString("id");

                String wasserBedarfohneWetter = getWaterNeed(id, mPlantID);
                String fertilizeNeed = getFertilizeNeed(id, mPlantID);

                JSONObject waterNeedJson = new JSONObject(wasserBedarfohneWetter);
                Double waterNeed = waterNeedJson.getDouble("wasserbedarf");

                JSONObject ferzilizeNeedJson = new JSONObject(fertilizeNeed);


                JSONObject jsonplant = new JSONObject(plant);
                JSONObject jsonstation = new JSONObject(station);


                pLichtstaerke = jsonplant.getDouble("lichtstaerke");
                pTemp = jsonplant.getDouble("temperatur");
                pBodenfeuchtigkeit = jsonplant.getDouble("bodenfeuchtigkeit");
                pBodenPH = jsonplant.getDouble("bodenwert");
                pWachstum = jsonplant.getString("wachstumsphase");

                bKalium = ferzilizeNeedJson.getDouble("kaliumbedarf");
                bStickstoff = ferzilizeNeedJson.getDouble("stickstoffbedarf");
                bPhosphat = ferzilizeNeedJson.getDouble("phosphatbedarf");

                String plantname = jsonplant.getString("name");
                String stationOrt = jsonstation.getString("features");

                //Check all Situations
                Boolean needsFertilization = checkFertilization(mLichtstaerke, bKalium, bStickstoff, bPhosphat, duengungsFaktor);
                Boolean needsStationChange = checkStationFeatures(stationOrt, waterNeed, pTemp, wBodenfeuchtigkeit, wTempMin, wTempMax);
                Boolean needsWater = checkWatering(stationOrt,waterNeed,wBodenfeuchtigkeit);


                //If True add to the Listview
                if(needsFertilization) {
                    data.add(new InstructionData("Duengung notwendig", plantname,"Kalium: "+bedarfKalium+"   Stickstoff: "+bedarfStickstoff+"   Phosphat: "+bedarfPhosphat));
                }
                //If True add to the Listview
                if(needsStationChange) {
                    data.add(new InstructionData("Stationsaenderung notwendig", plantname, "Station aendern zu: "+stationsAenderung));
                }
                //If True add to the Listview
                if(needsWater){
                    data.add(new InstructionData("Bewaesserung notwendig", plantname, "Die Pflanze braucht: "+bedarfWasser+" liter/m²"));
                }

                pArrived = false;
                sArrived = false;
                waterneedArrived = false;
                fertilizeneedArrived = false;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Setting Listview
        ListView lv = (ListView) findViewById(R.id.instruction_listview);
        listAdapter = new InstructionAdaptor(this, R.layout.list_item_instructions, data);
        lv.setAdapter(listAdapter);

    }

    //Method to check if the Plant needs Watering
    private Boolean checkWatering(String stationOrt, Double waterNeed, Double wBodenfeuchtigkeit) {
        if(waterNeed>0){
            if(Objects.equals(stationOrt, "indoor")){
                bedarfWasser = waterNeed;
                bedarfWasser = round(bedarfWasser,2);
                return true;
            }
            if(Objects.equals(stationOrt, "outdoor") && waterNeed>wBodenfeuchtigkeit){
                bedarfWasser = waterNeed-wBodenfeuchtigkeit;
                bedarfWasser = round(bedarfWasser,2);
                return true;
            }
            else return false;
        }
        return false;
    }

    //Method to check if the Pland needs a station change
    private Boolean checkStationFeatures(String stationOrt, Double waterNeed, Double pTemp, Double wBodenfeuchtigkeit, Double wTempMin, Double wTempMax) {
        Double middleTemp = (wTempMax+wTempMin)/2;

        if(Objects.equals(stationOrt, "indoor")){
            if(waterNeed>0 && wBodenfeuchtigkeit<=waterNeed && (pTemp+5)>middleTemp && (pTemp-5)<middleTemp){
                stationsAenderung = "outdoor";
                return true;
            }
            else return false;
        }
        if(Objects.equals(stationOrt, "outdoor")){
            if(waterNeed < wBodenfeuchtigkeit || (pTemp+5)<middleTemp || (pTemp-5)>middleTemp){
                stationsAenderung = "indoor";
                return true;
            }
            else return false;
        }
        else return false;
    }

    //Method to check if the Pland needs a station change
    private Boolean checkFertilization(Double mLichtstaerke, Double bKalium,Double bStickstoff, Double bPhosphat, Double duengungsFaktor) {

        if(mLichtstaerke<50000){
            if(bKalium > 0 || bStickstoff > 0 || bPhosphat > 0){
                if(bKalium == 0.0){
                    bedarfKalium = 0.0;
                }
                else {
                    bedarfKalium = bKalium * duengungsFaktor;
                    bedarfKalium = round(bedarfKalium,2);
                }

                if(bStickstoff == 0.0){
                    bedarfStickstoff = 0.0;
                }
                else {
                    bedarfStickstoff = bStickstoff * duengungsFaktor;
                    bedarfStickstoff = round(bedarfStickstoff,2);
                }

                if(bPhosphat == 0.0){
                    bedarfPhosphat = 0.0;
                }
                else {
                    bedarfPhosphat = bPhosphat * duengungsFaktor;
                    bedarfPhosphat = round(bedarfPhosphat,2);
                }

                return true;
            }
            else return false;
        }
        else {
            return false;
        }
    }

    //Method to do a Get-Call on fertilizeneed URI
    private String getFertilizeNeed(Integer id, String mPlantID) {
        fertilizeNeedUrl = "http://"+ip+":8888/fertilizeneed/"+id+"/"+mPlantID;

        try {
            Call get = callFertilizeNeed.doGetRequest(fertilizeNeedUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respFertilizeNeed = response.body().string();

                    fertilizeneedArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!fertilizeneedArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return respFertilizeNeed;

    }

    //Method to do a Get-Call on waterneed URI
    private String getWaterNeed(Integer id, String mPlantID) {
        waterNeedUrl = "http://"+ip+":8888/waterneed/"+id+"/"+mPlantID;
        try {
            Call get = callWaterNeed.doGetRequest(waterNeedUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respWaterNeed = response.body().string();

                    waterneedArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!waterneedArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return respWaterNeed;
    }

    //get the Plant with the ID from the MeasuredPlant to get Information of the Plantneeds
    private String getPlant(String plantid) {

        plantUrl = "http://"+ip+":8888/plant/"+plantid;

        try {
            Call get = callPlant.doGetRequest(plantUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respPlant = response.body().string();

                    pArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!pArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return respPlant;
    }

    //Method to get Station features from StationID in measuredPlant
    private String getStation(String stationid) {

        stationUrl = "http://"+ip+":8888/station/"+stationid;

        try {
            Call get = callStation.doGetRequest(stationUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respStation = response.body().string();

                    sArrived = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Waiting for response
        while(!sArrived){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return respStation;
    }

    //Calculate the receptivity of the ground
    public double getDuengungsFaktor(Double bodenwert){
        double c = 0;

        if(bodenwert<5.5 || bodenwert>8)c=0.25;
        if(bodenwert>5.5 && bodenwert<6 || bodenwert<8 && bodenwert>7.5)c=0.5;
        if(bodenwert>6 && bodenwert<6.5 || bodenwert<7.5 && bodenwert>7)c=0.75;
        if(bodenwert>6.5 || bodenwert<7)c=1;

        return c;
    }

    // round Doubles to 2 frac
    public double round(final double value, final int frac) {
        return Math.round(Math.pow(10.0, frac) * value) / Math.pow(10.0, frac);
    }

}
