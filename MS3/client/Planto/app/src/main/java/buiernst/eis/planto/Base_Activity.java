package buiernst.eis.planto;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Base_Activity extends AppCompatActivity {

    Integer id;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        id= getIntent().getExtras().getInt("UserID");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_anweisung:
                Intent intentTodo = new Intent(Base_Activity.this, PlantlistActivity.class);
                intentTodo.putExtra("UserID", id);
                startActivity(intentTodo);
                return true;

            case R.id.menu_plantlist:
                Intent intentPlant = new Intent(Base_Activity.this, PlantlistActivity.class);
                intentPlant.putExtra("UserID", id);
                startActivity(intentPlant);
                return true;

            case R.id.menu_weather:
                Intent intentWeather = new Intent(Base_Activity.this, ForecastActivity.class);
                intentWeather.putExtra("UserID", id);
                startActivity(intentWeather);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
