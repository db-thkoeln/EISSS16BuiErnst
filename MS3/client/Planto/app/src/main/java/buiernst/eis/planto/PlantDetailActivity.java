package buiernst.eis.planto;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



public class PlantDetailActivity extends Base_Activity{


    private ArrayList<String> data = new ArrayList<>();

    OkHttp callPlants = new OkHttp();
    String myplantUrl;
    String plantUrl;
    String respPlant;
    String respMyPlant;
    JSONObject jsonObjectPlant;
    JSONObject jsonObjectMyPlant;
    JSONArray jsonArrayMyPlant;
    String ip;
    Integer id;
    String plantName;
    String plantID;
    String measuredPlantID;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantdetail);

        id = getIntent().getExtras().getInt("UserID");
        ip = getIntent().getExtras().getString("IP");

        plantName = getIntent().getExtras().getString("Plantname");
        plantID = getIntent().getExtras().getString("PlantID");
        measuredPlantID = getIntent().getExtras().getString("MeasuredPlant");


        System.out.println(plantName);
        System.out.println(plantID);
        System.out.println(measuredPlantID);

        myplantUrl = "http://"+ip+":8888/user/"+id+"/measuredPlant/";

        //new CallTask().execute();

    }



}