package lk.uok.mit.fragment;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import lk.uok.mit.helloworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProximitySensorDemoFragment extends Fragment implements SensorEventListener{

    //to hold the reference to sensor manager
    private SensorManager sensorManager;
    //a reference to the proximity sensor
    Sensor proximitySensor = null;


    public ProximitySensorDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Proximity Sensor Demo");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proximity_sensor_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);

        //initialize the sensor manager
        this.sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        //initialize the proximity sensor
        this.proximitySensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //check if the sensor type is PROXIMITY
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //if yes, write a method to get its data and display
            displayProximitySensorData(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //subscribe to listening to sensor events if the fragment execution resumed/started
    @Override
    public void onResume() {
        super.onResume();
        //lock to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // register this class as a listener for the proximity sensor
        boolean isAvaialable=sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        if(!(isAvaialable)){
            Toast.makeText(getContext(),
                    "Proximity sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }
    }



    //unsubscribe from listening to sensor events if the fragment execution is paused
    @Override
    public void onPause() {
        super.onPause();
        //unlock orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        sensorManager.unregisterListener(this);
    }

    private void displayProximitySensorData(SensorEvent sensorEvent){
        if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
            // Detected something nearby
            getActivity().getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else {
            // Nothing is nearby
            getActivity().getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        }
    }


}
