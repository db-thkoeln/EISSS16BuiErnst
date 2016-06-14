package buiernst.eis.planto;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class Base_Activity  extends Activity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_anweisung:
                Intent intentTodo = new Intent(Base_Activity.this, PlantlistActivity.class);
                startActivity(intentTodo);
                return true;

            case R.id.menu_plantlist:
                Intent intentPlant = new Intent(Base_Activity.this, PlantlistActivity.class);
                startActivity(intentPlant);
                return true;

            case R.id.menu_weather:
                Intent intentWeather = new Intent(Base_Activity.this, WeatherActivity.class);
                startActivity(intentWeather);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
