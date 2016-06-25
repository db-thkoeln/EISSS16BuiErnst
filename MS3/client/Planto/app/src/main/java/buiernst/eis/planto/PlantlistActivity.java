package buiernst.eis.planto;

import android.content.Context;
import android.content.Intent;
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
import java.util.Vector;


public class PlantlistActivity extends Base_Activity{


    Vector<PlantData> data = new Vector<>();
    Plantlistadaptor listAdapter;

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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantlist);

        id = getIntent().getExtras().getInt("UserID");
        ip = getIntent().getExtras().getString("IP");

        myplantUrl = "http://"+ip+":8888/user/"+id+"/measuredPlant/";

        ListView lv = (ListView) findViewById(R.id.listview);
        listAdapter = new Plantlistadaptor(this, R.layout.list_item_plants, data);

        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long vId) {
                String title = (String) ((TextView) view.findViewById(R.id.list_item_text)).getText();
                String plantid = (String)((TextView) view.findViewById(R.id.plantid)).getText();
                String measuredId = (String)((TextView) view.findViewById(R.id.measuredplantid)).getText();

                Intent intentPlantdetail = new Intent(PlantlistActivity.this, PlantDetailActivity.class);
                intentPlantdetail.putExtra("UserID", id);
                intentPlantdetail.putExtra("Plantname", title);
                intentPlantdetail.putExtra("PlantID", plantid);
                intentPlantdetail.putExtra("MeasuredPlant", measuredId);
                intentPlantdetail.putExtra("IP", ip);
                startActivity(intentPlantdetail);
            }
        });


        try {
            // HTTP Get on measuredPlants Ressource IDs
            Call get = callPlants.doGetRequest(myplantUrl , new Callback() {
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
                        jsonArrayMyPlant = jsonObjectMyPlant.getJSONArray("measuredPlant");
                        for (int i = 0; i < jsonArrayMyPlant.length(); i++) {

                            JSONObject obj = jsonArrayMyPlant.getJSONObject(i);
                            String measuredplantid = obj.getString("id");
                            String plantid = obj.getString("plantid");
                            addPlantName(plantid, measuredplantid);
                        }

                        listAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void addPlantName(final String id, final String mid) {

        plantUrl = "http://"+ip+":8888/plant/"+id;

        try {
            // HTTP Get on measuredPlants Ressource IDs
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
                        String plantName = jsonObjectPlant.getString("name");
                        data.add(new PlantData(plantName, id, mid));


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
