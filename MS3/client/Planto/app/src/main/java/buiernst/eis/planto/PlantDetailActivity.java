package buiernst.eis.planto;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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



public class PlantDetailActivity extends Base_Activity{

    OkHttp callMyPlants = new OkHttp();
    OkHttp callPlants = new OkHttp();
    OkHttp callStation = new OkHttp();
    String myplantUrl;
    String plantUrl;
    String stationUrl;
    String respPlant;
    String respMyPlant;
    String respStation;
    JSONObject jsonObjectPlant;
    JSONObject jsonObjectMyPlant;
    JSONObject jsonObjectStation;

    TextView name;
    TextView tempAktuell;
    TextView tempBedarf;
    TextView feuchtigkeitAktuell;
    TextView feuchtigkeitBedarf;
    TextView lichtstaerkeAktuell;
    TextView lichtstaerkeBedarf;
    TextView tvWachstum;
    TextView tvStation;

    String ip;
    Integer id;
    String plantName;
    String plantID;
    String measuredPlantID;

    Double mLichtstaerke;
    Double mTemp;
    Double mBodenfeuchtigkeit;
    String stationID;

    Double pLichtstaerke;
    Double pTemp;
    Double pBodenfeuchtigkeit;
    String wachstumsphase;

    String station;

    boolean mPlantArrived;
    boolean plantArrived;
    boolean stationArrived;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantdetail);

        //Setting all Textviews to variables
        name = (TextView) findViewById(R.id.plant_detail_name);
        tempAktuell = (TextView) findViewById(R.id.textView1);
        tempBedarf = (TextView) findViewById(R.id.textView2);
        feuchtigkeitAktuell = (TextView) findViewById(R.id.textView3);
        feuchtigkeitBedarf = (TextView) findViewById(R.id.textView4);
        lichtstaerkeAktuell = (TextView) findViewById(R.id.textView5);
        lichtstaerkeBedarf = (TextView) findViewById(R.id.textView6);
        tvWachstum = (TextView) findViewById(R.id.textView7);
        tvStation = (TextView) findViewById(R.id.textView8);

        mPlantArrived = false;
        plantArrived = false;
        stationArrived = false;

        id = getIntent().getExtras().getInt("UserID");
        ip = getIntent().getExtras().getString("IP");

        plantName = getIntent().getExtras().getString("Plantname");
        plantID = getIntent().getExtras().getString("PlantID");
        measuredPlantID = getIntent().getExtras().getString("MeasuredPlant");

        myplantUrl = "http://"+ip+":8888/user/"+id+"/measuredPlant/"+measuredPlantID;
        plantUrl = "http://"+ip+":8888/plant/"+plantID;

        try {
            // HTTP Get on measuredPlants Ressource ID
            Call get = callMyPlants.doGetRequest(myplantUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respMyPlant = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        jsonObjectMyPlant = new JSONObject(respMyPlant);

                        mLichtstaerke = jsonObjectMyPlant.getDouble("lichtstaerke");
                        mTemp = jsonObjectMyPlant.getDouble("temperatur");
                        mBodenfeuchtigkeit = jsonObjectMyPlant.getDouble("bodenfeuchtigkeit");
                        stationID = jsonObjectMyPlant.getString("stationid");

                        mPlantArrived = true;

                        getPlant();
                        getStation(stationID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //waiting for response
        while(!plantArrived&&!stationArrived){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        name.setText(plantName);
        tempAktuell.setText("Temperatur(Aktuell): "+mTemp);
        tempBedarf.setText("Temperatur(Bedarf): "+pTemp);
        feuchtigkeitAktuell.setText("Bodenfeuchtigkeit(Aktuell): "+mBodenfeuchtigkeit);
        feuchtigkeitBedarf.setText("Bodenfeuchtigkeit(Bedarf): "+pBodenfeuchtigkeit);
        lichtstaerkeAktuell.setText("Lichtstärke(Aktuell): "+mLichtstaerke);
        lichtstaerkeBedarf.setText("Lichtstärke(Obergrenze): "+pLichtstaerke);
        tvWachstum.setText("Wachstumsphase: "+wachstumsphase);
        tvStation.setText("Station: "+station);

    }

    public void getPlant(){

        try {
            // HTTP Get on Plants Ressource with PlantID
            Call get = callPlants.doGetRequest(plantUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respPlant = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        jsonObjectPlant = new JSONObject(respPlant);

                        pLichtstaerke = jsonObjectPlant.getDouble("lichtstaerke");
                        pTemp = jsonObjectPlant.getDouble("temperatur");
                        pBodenfeuchtigkeit = jsonObjectPlant.getDouble("bodenfeuchtigkeit");
                        wachstumsphase = jsonObjectPlant.getString("wachstumsphase");

                        plantArrived = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //getting Station features by StationID
    public void getStation(String stationID){

        stationUrl = "http://"+ip+":8888/station/"+stationID;

        try {
            // HTTP Get on Station Ressource with stationID
            Call get = callStation.doGetRequest(stationUrl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respStation = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        jsonObjectStation = new JSONObject(respStation);

                        station = jsonObjectStation.getString("features");

                        stationArrived = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}