package buiernst.eis.planto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Vector;

public class InstructionAdaptor extends ArrayAdapter<InstructionData> {
    Context context;
    int layoutResourceId;
    Vector<InstructionData> data;

    /**
     * Called when the activity is first created.
     */
    public InstructionAdaptor(InstructionActivity context, int layoutResourceId, Vector<InstructionData> data) {
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

            holder.tvAnweisung = (TextView) row.findViewById(R.id.anweisung);
            holder.tvPlant = (TextView) row.findViewById(R.id.plant);
            holder.tvValue = (TextView) row.findViewById(R.id.value);

            row.setTag(holder);
        } else {
            holder = (MyStringReaderHolder) row.getTag();
        }

        InstructionData mrb = data.elementAt(position);

        holder.tvAnweisung.setText(mrb.anweisung);
        holder.tvPlant.setText(mrb.plantname);
        holder.tvValue.setText(mrb.values);

        return row;

    }
    static class MyStringReaderHolder {
        TextView tvAnweisung, tvPlant, tvValue;
    }

}
