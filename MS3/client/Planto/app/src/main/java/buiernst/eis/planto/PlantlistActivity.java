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



public class PlantlistActivity extends Base_Activity{


    private ArrayList<String> data = new ArrayList<>();
    MyListAdapter listAdapter;

    OkHttp callPlants = new OkHttp();
    String myplantUrl;
    String plantUrl;
    String respPlant;
    String respMyPlant;
    JSONObject jsonObjectPlant;
    JSONObject jsonObjectMyPlant;
    JSONArray jsonArrayMyPlant;
    String ip = "192.168.0.102";

    Integer id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantlist);

        id = getIntent().getExtras().getInt("UserID");

        myplantUrl = "http://"+ip+":8888/user/"+id+"/measuredPlant/";

        ListView lv = (ListView) findViewById(R.id.listview);
        listAdapter = new MyListAdapter(this, R.layout.list_item_plants, data);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long vId) {
                String title = (String) ((TextView) view.findViewById(R.id.list_item_text)).getText();
                System.out.println(title);
                Intent intentPlantdetail = new Intent(PlantlistActivity.this, PlantDetailActivity.class);
                intentPlantdetail.putExtra("UserID", id);
                intentPlantdetail.putExtra("Plantname", title);
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
                            String plantid = obj.getString("plantid");
                            addPlantName(plantid);
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


    private void addPlantName(String id) {

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
                        data.add(plantName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private List<String> mObjects;
        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewholder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.list_item_button);
                viewHolder.title.setText(getItem(position));
                /*viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = (String) ((TextView) v.findViewById(R.id.list_item_text)).getText();
                        System.out.println(title);
                        }
                });*/
                convertView.setTag(viewHolder);
            }
            else{
                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.title.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView title;
        ImageButton button;

    }




}
