package buiernst.eis.planto;

import java.util.Vector;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;

public class ForecastAdaptor extends ArrayAdapter<WeatherData>
    {
        Context context;
        int layoutResourceId;

        Vector<WeatherData> data;
        /** Called when the activity is first created. */
        public ForecastAdaptor(ForecastActivity context, int layoutResourceId, Vector<WeatherData> data)
        {
            super(context,layoutResourceId,data);
            this.layoutResourceId = layoutResourceId;
            this.context=context;
            this.data = data;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            MyStringReaderHolder holder;

            if(row==null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent,false);

                holder= new MyStringReaderHolder();

                holder.tvDate =(TextView)row.findViewById(R.id.list_item_time);
                holder.tvMinTemp =(TextView)row.findViewById(R.id.list_item_minTemp);
                holder.tvMaxTemp  =(TextView)row.findViewById(R.id.list_item_maxTemp);
                holder.tvNiederschlag  =(TextView)row.findViewById(R.id.list_item_niederschlag);
                holder.icon=(ImageView) row.findViewById(R.id.list_item_icon);

                row.setTag(holder);
            }
            else
            {
                holder=(MyStringReaderHolder) row.getTag();
            }

            WeatherData mrb = data.elementAt(position);

            holder.tvDate.setText(mrb.date);
            holder.tvMinTemp.setText(mrb.tempMin.toString()+" Grad");
            holder.tvMaxTemp.setText(mrb.tempMax.toString()+" Grad");
            holder.tvNiederschlag.setText(mrb.niederschlag.toString()+" mm");

            switch(mrb.icon){
                case "01d": holder.icon.setImageResource(R.drawable.hell_tag);
                    break;
                case "01n": holder.icon.setImageResource(R.drawable.hell_nacht);
                    break;
                case "02d": holder.icon.setImageResource(R.drawable.wolke_tag);
                    break;
                case "02n": holder.icon.setImageResource(R.drawable.wolke_nacht);
                    break;
                case "03d": holder.icon.setImageResource(R.drawable.bedeckt_tag);
                    break;
                case "03n": holder.icon.setImageResource(R.drawable.bedeckt_nacht);
                    break;
                case "04d": holder.icon.setImageResource(R.drawable.dunkel_tag);
                    break;
                case "04n": holder.icon.setImageResource(R.drawable.dunkel_nacht);
                    break;
                case "09d": holder.icon.setImageResource(R.drawable.regen_bedeckt_tag);
                    break;
                case "09n": holder.icon.setImageResource(R.drawable.regen_bedeckt_nacht);
                    break;
                case "10d": holder.icon.setImageResource(R.drawable.regen_tag);
                    break;
                case "10n": holder.icon.setImageResource(R.drawable.regen_nacht);
                    break;
                case "11d": holder.icon.setImageResource(R.drawable.blitz_tag);
                    break;
                case "11n": holder.icon.setImageResource(R.drawable.bedeckt_nacht);
                    break;
                case "13d": holder.icon.setImageResource(R.drawable.schnee_tag);
                    break;
                case "13n": holder.icon.setImageResource(R.drawable.schnee_nacht);
                    break;
                case "50d": holder.icon.setImageResource(R.drawable.nebel_tag);
                    break;
                case "50n": holder.icon.setImageResource(R.drawable.nebel_nacht);
                    break;
            }
            return row;
        }
        static class MyStringReaderHolder
        {
            TextView tvDate,tvMinTemp,tvMaxTemp,tvNiederschlag;
            ImageView icon;
        }

    }

