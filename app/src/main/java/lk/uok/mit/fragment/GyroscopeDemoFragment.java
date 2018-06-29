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
import android.widget.Toast;

import lk.uok.mit.helloworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GyroscopeDemoFragment extends Fragment implements SensorEventListener {

    //to hold the reference to sensor manager
    private SensorManager sensorManager;
    //a reference to the gyroscope
    Sensor gyroscope = null;


    public GyroscopeDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Gyroscope Demo");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gyroscope_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the sensor manager
        this.sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        //initialize the proximity sensor
        this.gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //lock to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (this.gyroscope == null) {
            Toast.makeText(getContext(),
                    "TYPE_GYROSCOPE sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        } else {
            // Register the listener
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //check if the sensor type is TYPE_GYROSCOPE
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //if yes, write a method to get its data and display
            if (sensorEvent.values[2] > 0.5f) { // anticlockwise
                //turn background to blue
                getActivity().getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            } else if (sensorEvent.values[2] < -0.5f) { // clockwise
                //turn background to yellow
                getActivity().getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
