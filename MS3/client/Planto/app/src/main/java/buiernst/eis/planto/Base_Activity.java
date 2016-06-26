package buiernst.eis.planto;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Base_Activity extends AppCompatActivity {

    Integer id;
    String ip;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        id= getIntent().getExtras().getInt("UserID");
        ip= getIntent().getExtras().getString("IP");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_anweisung:
                Intent intentTodo = new Intent(Base_Activity.this, InstructionActivity.class);
                intentTodo.putExtra("UserID", id);
                intentTodo.putExtra("IP", ip);
                startActivity(intentTodo);
                return true;

            case R.id.menu_plantlist:
                Intent intentPlant = new Intent(Base_Activity.this, PlantlistActivity.class);
                intentPlant.putExtra("UserID", id);
                intentPlant.putExtra("IP", ip);
                startActivity(intentPlant);
                return true;

            case R.id.menu_weather:
                Intent intentWeather = new Intent(Base_Activity.this, ForecastActivity.class);
                intentWeather.putExtra("UserID", id);
                intentWeather.putExtra("IP", ip);
                startActivity(intentWeather);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
