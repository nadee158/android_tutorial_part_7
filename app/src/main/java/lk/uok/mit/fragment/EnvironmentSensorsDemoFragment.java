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
import android.widget.TextView;
import android.widget.Toast;

import lk.uok.mit.helloworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnvironmentSensorsDemoFragment extends Fragment implements SensorEventListener{

    //text view to display the Ambient Temperature
    private TextView textViewAmbientTemperature;
    //text view to display the Illuminance
    private TextView textViewIlluminance;
    //text view to display the Ambient Air Pressure
    private TextView textViewAmbientAirPressure;
    //text view to display the Ambient Relative Humidity
    private TextView textViewAmbientRelativeHumidity;
    //text view to display the Device temperature
    private TextView textViewDeviceTemperature;

    //to hold the reference to sensor manager
    private SensorManager sensorManager;

    //a reference to the Ambient Temperature sensor
    Sensor temperatureSensor = null;
    //a reference to the Illuminance sensor
    Sensor illuminanceSensor = null;
    //a reference to the Ambient Air Pressure sensor
    Sensor airPressureSensor = null;
    //a reference to the Ambient Relative Humidity sensor
    Sensor relativeHumiditySensor = null;
    //a reference to the Device temperature sensor
    Sensor deviceTemperatureSensor = null;


    public EnvironmentSensorsDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Environment Sensors Demo");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_environment_sensors_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize the sensor manager
        this.sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        //initialize the text views
        this.textViewAmbientTemperature=view.findViewById(R.id.textViewAmbientTemperature);
        this.textViewIlluminance=view.findViewById(R.id.textViewIlluminance);
        this.textViewAmbientAirPressure=view.findViewById(R.id.textViewAmbientAirPressure);
        this.textViewAmbientRelativeHumidity=view.findViewById(R.id.textViewAmbientRelativeHumidity);
        this.textViewDeviceTemperature=view.findViewById(R.id.textViewDeviceTemperature);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //check if the sensor type is TYPE_AMBIENT_TEMPERATURE
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            //if yes, write a method to get its data and display
            displayEnvironmentSensorData(event.values[0],
                    this.textViewAmbientTemperature,
                    "Ambient Temperature",
                    "°C");
        }
        //check if the sensor type is TYPE_LIGHT
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            //if yes, write a method to get its data and display
            displayEnvironmentSensorData(event.values[0],
                    this.textViewIlluminance,
                    "Illuminance",
                    "lx");
        }
        //check if the sensor type is TYPE_PRESSURE
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            //if yes, write a method to get its data and display
            displayEnvironmentSensorData(event.values[0],
                    this.textViewAmbientAirPressure,
                    "Ambient Air Pressure",
                    "hPa");
        }
        //check if the sensor type is TYPE_RELATIVE_HUMIDITY
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            //if yes, write a method to get its data and display
            displayEnvironmentSensorData(event.values[0],
                    this.textViewAmbientRelativeHumidity,
                    "Ambient Relative Humidity",
                    "%");
        }
        //check if the sensor type is TYPE_TEMPERATURE
        if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
            //if yes, write a method to get its data and display
            displayEnvironmentSensorData(event.values[0],
                    this.textViewDeviceTemperature,
                    "Device Temperature",
                    "°C");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void displayEnvironmentSensorData(float value,
                                              TextView textView,
                                              String text,
                                              String measureUnit) {
        textView.setText(text + ":- " + value + measureUnit);
    }

    //subscribe to listening to sensor events if the fragment execution resumed/started
    @Override
    public void onResume() {
        super.onResume();
        //lock to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // register this class as a listener for the sensors
        //initialize the sensors
        this.temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(this.temperatureSensor==null){
            //if the sensor not available, notify the user
            Toast.makeText(getContext(),
                    "TYPE_AMBIENT_TEMPERATURE sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }else{
            //if the sensor not available, register it
            this.sensorManager.registerListener(this, this.temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        this.illuminanceSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(this.illuminanceSensor==null){
            //if the sensor not available, notify the user
            Toast.makeText(getContext(),
                    "TYPE_LIGHT sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }else{
            //if the sensor not available, register it
            this.sensorManager.registerListener(this, this.illuminanceSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        this.airPressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(this.airPressureSensor==null){
            //if the sensor not available, notify the user
            Toast.makeText(getContext(),
                    "TYPE_PRESSURE sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }else{
            //if the sensor not available, register it
            this.sensorManager.registerListener(this, this.airPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        this.relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(this.relativeHumiditySensor==null){
            //if the sensor not available, notify the user
            Toast.makeText(getContext(),
                    "TYPE_RELATIVE_HUMIDITY sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }else{
            //if the sensor not available, register it
            this.sensorManager.registerListener(this, this.relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        this.deviceTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        if(this.deviceTemperatureSensor==null){
            //if the sensor not available, notify the user
            Toast.makeText(getContext(),
                    "TYPE_TEMPERATURE sensor is not available in this device!",
                    Toast.LENGTH_LONG).show();
        }else{
            //if the sensor not available, register it
            this.sensorManager.registerListener(this, this.deviceTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
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







}
