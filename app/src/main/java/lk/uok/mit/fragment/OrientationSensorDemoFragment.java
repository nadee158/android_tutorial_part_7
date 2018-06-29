package lk.uok.mit.fragment;


import android.content.Context;
import android.content.pm.ActivityInfo;
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

import lk.uok.mit.view.CompassView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrientationSensorDemoFragment extends Fragment implements SensorEventListener {

    //to hold the reference to sensor manager
    private SensorManager sensorService;
    //to hold the reference to instance of the CompassView
    private CompassView compassView;


    public OrientationSensorDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Orientation Sensor Demonstration");
        //create an instance of the CompassView and set it as the view
        this.compassView = new CompassView(getActivity());
        return compassView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the sensor manager
        this.sensorService = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //check if the sensor type is TYPE_ORIENTATION
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            //if yes, write a method to get its data and display
            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            float azimuth = event.values[0];
            compassView.updatePaintPosition(azimuth);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //lock to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // register this class as a listener for the orientation and
        // accelerometer sensors
        boolean isAvaialable = sensorService.registerListener(this,
                sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        //if the sensor is not available, disply the notification to the user
        if (!(isAvaialable)) {
            Toast.makeText(getContext(),
                    "The Orientation sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }
    }


    //unsubscribe from listening to sensor events if the fragment execution is paused
    @Override
    public void onPause() {
        super.onPause();
        //unlock orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        //unregister as a sensor listener
        sensorService.unregisterListener(this);
    }


}
