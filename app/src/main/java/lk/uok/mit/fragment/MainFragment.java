package lk.uok.mit.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import lk.uok.mit.adapter.SensorAadapter;
import lk.uok.mit.helloworld.R;

public class MainFragment extends Fragment {

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //change the title bar textto meaningful one
        getActivity().setTitle("Sensor List In This Device");
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.
    // E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Get a reference to the ListView
        ListView listView = (ListView) view.findViewById(R.id.listViewSensors);
        //Get the list of sensors in device
        SensorManager mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //initialize the adapter using the retrieved sensor list and context
        SensorAadapter adapter = new SensorAadapter(getContext(), sensorList);
        //set the initialized adapter to the list view
        listView.setAdapter(adapter);
        //call “notifyDataSetChanged” method of the adapter to reflect the
        // latest data set available in the “messages”
        adapter.notifyDataSetChanged();
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("|%-" + length + "s|", string);
    }
}
