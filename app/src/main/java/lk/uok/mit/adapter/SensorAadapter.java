package lk.uok.mit.adapter;

import android.content.Context;
import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lk.uok.mit.helloworld.R;

public class SensorAadapter extends ArrayAdapter<Sensor> {

    //a variable to refer to the passed data set through constructor
    private List<Sensor> sensors;
    //a variable to refer to the passed context through constructor
    private Context context;

    //four variables to refer to four text boxes
    private TextView rowItemSensorName;
    private TextView rowItemVendor;
    private TextView rowItemSensorVersion;

    //add a constructor to accept context and data set
    public SensorAadapter(Context context, List<Sensor> sensors) {
        //call super clase's constructor by passing the context, layout and the data set
        super(context, R.layout.sensor_list_row_item, sensors);
        this.context = context;
        this.sensors = sensors;
    }

    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        //Using the “position” parameter get the data item for current
        // position from sensors list
        Sensor currentSensor = this.sensors.get(position);
        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            //a view to be reused is not available, therefore inflate new views
            //to inflate views initialize a android.view.LayoutInflater object
            LayoutInflater inflater = LayoutInflater.from(this.context);
            //inflate the R.layout.sensor_list_row_item, and assign the return
            // value to the convertView
            convertView = inflater.inflate(R.layout.sensor_list_row_item,
                    parent, false);
        }

        //using the inflated (initialized) convertView,
        // get its child text views by id, if they are null
        this.rowItemSensorName = convertView.findViewById(R.id.textViewSensorName);
        this.rowItemVendor = convertView.findViewById(R.id.textViewSensorVendor);
        this.rowItemSensorVersion = convertView.findViewById(R.id.textViewSensorVersion);

        //now set the data to be displayed in each text view
        this.rowItemSensorName.setText(currentSensor.getName());
        this.rowItemVendor.setText(currentSensor.getVendor());
        this.rowItemSensorVersion.setText(Integer.toString(currentSensor.getVersion()));

        return convertView;
    }
}
