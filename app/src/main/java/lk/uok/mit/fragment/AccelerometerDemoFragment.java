package lk.uok.mit.fragment;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lk.uok.mit.helloworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccelerometerDemoFragment extends Fragment implements SensorEventListener {

    //to hold the reference to sensor manager
    private SensorManager sensorManager;
    //to refer to the main LinearLayout layout of the fragment
    private View mainView;
    //to refer to the text view to display measured acceleration in X axis
    private TextView textViewX;
    //to refer to the text view to display measured acceleration in Y axis
    private TextView textViewY;
    //to refer to the text view to display measured acceleration in Z axis
    private TextView textViewZ;

    //to keep if the background color of the parent layout changed, to switch colors
    private boolean color = false;
    //to keep the time in milliseconds on which the UI was updated last time
    private long lastUpdate;
    //the threshold to determine oif the movement of the device is a shake or not
    private static final int SHAKE_THRESHOLD = 600;
    //the last position retrieved position of the device along X,Y, and Z axis from accelerometer
    private float last_x, last_y, last_z;


    public AccelerometerDemoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Accelerometer Sensor Demonstration");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accelerometer_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the parent layout
        mainView = view.findViewById(R.id.fragment_background);
        //set green as the background color at startup
        mainView.setBackgroundColor(Color.GREEN);

        //initialize the sensor manager
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        //set the current time as the last update time
        lastUpdate = System.currentTimeMillis();
        //initialize the three text views
        this.textViewX = view.findViewById(R.id.textViewX);
        this.textViewY = view.findViewById(R.id.textViewY);
        this.textViewZ = view.findViewById(R.id.textViewZ);
    }

    //subscribe to listening to sensor events if the fragment execution resumed/started
    @Override
    public void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    //unsubscribe from listening to sensor events if the fragment execution is paused
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //check if the sensor type is ACCELEROMETER
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //if yes, write a method to get its data and display
            displayAccelerometerData(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void displayAccelerometerData(SensorEvent event) {
        //from the android.hardware.SensorEvent, get the x,y and z axis values like below
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //get the current time n milliseconds
        long curTime = System.currentTimeMillis();

        //check if the difference between last update and this update
        // is greater than 100 ms
        //this is because the sensors will be continuously producing data,
        // but we periodically display it
        if ((curTime - lastUpdate) > 100) {
            //get the difference between last update and this update
            long diffTime = (curTime - lastUpdate);
            //set the last update to current update, as we are upsdaing now
            lastUpdate = curTime;

            //set the text of three text boxes in ui to the relieved values from sensor
            this.textViewX.setText("measured acceleration in X axis:- " + x);
            this.textViewY.setText("measured acceleration in Y axis:- " + y);
            this.textViewZ.setText("measured acceleration in Z axis:- " + z);

            //use below formula to calculate the speed of movement of device
            // from last axis data and current axis data
            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
            //if the speed is greater than the threshold, consider as a shake
            if (speed > SHAKE_THRESHOLD) {
                //switch the color between red and green for each shake
                if (color) {
                    mainView.setBackgroundColor(Color.GREEN);
                } else {
                    mainView.setBackgroundColor(Color.RED);
                }
                color = !color;
                //vibrate the device on a shake
                vibrateDevice();
            }
            //se the last position to current position
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    private void vibrateDevice() {
        //get the vibrator service from system services
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }


}
