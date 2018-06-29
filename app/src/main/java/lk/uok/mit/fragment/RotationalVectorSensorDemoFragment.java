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
public class RotationalVectorSensorDemoFragment extends Fragment  implements SensorEventListener {

    //to hold the reference to sensor manager
    private SensorManager sensorManager;
    //to refer to the main RelativeLayout layout of the fragment
    private View mainView;
    //to refer to the text view to display measured acceleration in X axis
    private TextView textViewRotation;

    //to keep the time in milliseconds on which the UI was updated last time
    private long lastUpdate;

    public RotationalVectorSensorDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Rotational Vector Sensor Demo");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotational_vector_sensor_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the parent layout
        mainView = view.findViewById(R.id.main_content_rotation);
        //set green as the background color at startup
        mainView.setBackgroundColor(Color.GREEN);

        //initialize the sensor manager
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        //initialize the the text view to disply the rotation data
        this.textViewRotation = view.findViewById(R.id.textViewRotation);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //check if the sensor type is ACCELEROMETER
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            //if yes, write a method to get its data and display
            displayRotationVectorSensorData(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void displayRotationVectorSensorData(SensorEvent sensorEvent){
        //convert the quaternion into a rotation matrix, a 4x4 matrix,
        // by using the getRotationMatrixFromVector() method of the SensorManager class
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

        //remap the coordinate system of the rotation matrix.
        // rotate the rotation matrix such that the Z-axis of the new coordinate system
        // coincides with the Y-axis of the original coordinate system.
        // Remap coordinate system
        float[] remappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);


        //convert the rotation matrix into an array of orientations,
        // specifying the rotation of the device along the Z, X, and Y axes.
        // To do so, use the getOrientation() method of the SensorManager class.
        // Convert to orientations
        float[] orientations = new float[3];
        SensorManager.getOrientation(remappedRotationMatrix, orientations);

        //By default, the orientations array contains angles
        // in radians instead of degrees.
        //use the following code to convert all its angles to degrees:
        for(int i = 0; i < orientations.length; i++) {
            orientations[i] = (float)(Math.toDegrees(orientations[i]));
        }

        //change the background color of the activity
        // //based on the third element of the orientations array
        float rotation=orientations[2];
        if(rotation > 35) {
            this.textViewRotation.setTextColor(Color.BLACK);
            mainView.setBackgroundColor(Color.YELLOW);
        } else if(rotation < -35) {
            this.textViewRotation.setTextColor(Color.WHITE);
            mainView.setBackgroundColor(Color.BLUE);
        } else if((Math.floor(rotation) < 3) && (Math.floor(rotation)) > -1) {
            this.textViewRotation.setTextColor(Color.BLACK);
            mainView.setBackgroundColor(Color.WHITE);
        } else{
            this.textViewRotation.setTextColor(Color.WHITE);
            mainView.setBackgroundColor(Color.GREEN);
        }

        //get the current time n milliseconds
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 500) {
            //set the last update to current update, as we are upsdaing now
            lastUpdate = curTime;

            this.textViewRotation.setPadding(10, 100, 10, 100);
            this.textViewRotation.setText("Rotation:- " + Math.floor(rotation));
        }
    }

    //subscribe to listening to sensor events if the fragment execution resumed/started
    @Override
    public void onResume() {
        super.onResume();
        //lock to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // register this class as a listener for the orientation and
        // accelerometer sensors
        boolean isAvaialable=sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        if(!(isAvaialable)){
            Toast.makeText(getContext(),
                    "TYPE ROTATION VECTOR sensor is not available in this device!",
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

}
