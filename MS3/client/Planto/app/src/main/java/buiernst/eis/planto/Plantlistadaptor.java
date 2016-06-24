package buiernst.eis.planto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class Plantlistadaptor extends ArrayAdapter<PlantData> {
    Context context;
    int layoutResourceId;
    Vector<PlantData> data;

    /**
     * Called when the activity is first created.
     */
    public Plantlistadaptor(PlantlistActivity context, int layoutResourceId, Vector<PlantData> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyStringReaderHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MyStringReaderHolder();

            holder.tvName = (TextView) row.findViewById(R.id.list_item_text);
            holder.tvID = (TextView) row.findViewById(R.id.plantid);
            holder.tvMID = (TextView) row.findViewById(R.id.measuredplantid);
            holder.icon = (ImageButton) row.findViewById(R.id.list_item_button);

            row.setTag(holder);
        } else {
            holder = (MyStringReaderHolder) row.getTag();
        }

        PlantData mrb = data.elementAt(position);

        holder.tvName.setText(mrb.plantname);
        holder.tvID.setText(mrb.plantid);
        holder.tvMID.setText(mrb.measuredplantid);
        holder.icon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);

        return row;

    }
        static class MyStringReaderHolder {
            TextView tvName, tvID, tvMID;
            ImageButton icon;
        }

    }
