package lk.uok.mit.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import lk.uok.mit.helloworld.R;
import lk.uok.mit.util.DemoUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothDemoFragment extends Fragment {

    //code to request bluetooth
    public static int BLUETOOTH_REQUEST_CODE = 111;
    //code to request permissions
    public static int PERMISSION_REQUEST_CODE = 112;
    //a class variable to refer to the buttonSwitchBluetooth of the layout,
    //upon its click we will enable/disable Bluetooth of the device;
    private Button buttonSwitchBluetooth;
    //a reference to the bluetooth adapter
    BluetoothAdapter mBluetoothAdapter;
    //permission array required execute the code here
    private String[] requiredPermissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    //to store bluetooth switch status
    private boolean isBluetoothSwitchedOn = false;
    //to refer to context
    private Context context;

    //code to request bluetooth visibility
    public static int BLUETOOTH_VISIBILITY_REQUEST_CODE = 113;
    //a class variable to refer to the buttonSwitchBluetooth of the layout,
    //upon its click we will enable/disable Bluetooth of the device;
    private Button buttonSwitchBluetoothVisibility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Bluetooth Demo");
        //initilaize context
        this.context = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the button reference
        this.buttonSwitchBluetooth = view.findViewById(R.id.buttonSwitchBluetooth);
        //set the onclick listener of the button
        buttonSwitchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the switchBluetooth method upon click of the button
                switchBluetooth();
            }
        });

        //get the button reference for set visibility button
        this.buttonSwitchBluetoothVisibility = view.findViewById(R.id.buttonSwitchBluetoothVisibility);
        //set the onclick listener of set visibility button
        buttonSwitchBluetoothVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the switchBluetooth method upon click of the button
                switchBluetoothVisibility();
            }
        });

    }

    //to set visibility of device to other Bluetooth devices
    private void switchBluetoothVisibility() {
        //check if user has given the required permissions
        boolean hasUserPermissions = DemoUtil.hasUserPermissions(context, requiredPermissions);
        if (!(hasUserPermissions)) {
            //if the requested permissions are not already granted, request permission
            requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE);
        } else {
            //check if the bluetooth is available in this device
            boolean bluetoothAvailable = checkIfBluetoothAvailable();
            if (bluetoothAvailable) {
                //if bluetooth is available, check if it is enabled
                if (this.mBluetoothAdapter.isEnabled()) {
                    //check if the device is already visible over bluetooth
                    if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                        //if not try to enable Bluetooth visibility by sending user request
                        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        //make the device visible for 30 seconds, if the user allows
                        getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
                        //set the code to request, so we can track the result
                        startActivityForResult(getVisible, BLUETOOTH_VISIBILITY_REQUEST_CODE);
                    } else {
                        Toast.makeText(context,
                                "Device is visible over Bluetooth now!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //notify the user that bluetooth of device is turned off
                    Toast.makeText(context,
                            "Bluetooth is turned off!", Toast.LENGTH_SHORT).show();
                }
            } else {
                //else notify the user
                Toast.makeText(context,
                        "Bluetooth is not Available in this device!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //to switch the Bluetooth as On/Off
    private void switchBluetooth() {
        //check if user has given the required permissions
        boolean hasUserPermissions = DemoUtil.hasUserPermissions(context, requiredPermissions);
        if (!(hasUserPermissions)) {
            //if read contact permission is not already granted, request permission
            requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE);
        } else {
            //check if the bluetooth is available in this device
            boolean bluetoothAvailable = checkIfBluetoothAvailable();
            if (bluetoothAvailable) {
                //if bluetooth is available, check if it is enabled
                if (!this.mBluetoothAdapter.isEnabled()) {
                    //if bluetooth is not enabled, first set the status
                    isBluetoothSwitchedOn = false;
                    //try to enable Bluetooth by sending user request
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, BLUETOOTH_REQUEST_CODE);
                } else {
                    //if bluetooth is enabled, first set the status
                    isBluetoothSwitchedOn = true;
                    //disable bluetooth
                    this.mBluetoothAdapter.disable();
                    //change text and background colour of the button
                    buttonSwitchBluetooth.setText("Turn Bluetooth On");
                    buttonSwitchBluetooth.setBackgroundColor(Color.GREEN);
                    //notify the user
                    Toast.makeText(context,
                            "Bluetooth is turned off!", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                //else notify the user
                Toast.makeText(context,
                        "Bluetooth is not Available in this device!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if the result is requesting permissions
        if (requestCode == PERMISSION_REQUEST_CODE) {
            //declare a variable of type boolean to hold if the user has granted the permission or not
            boolean hasGranted = true;
            //first check if the request code matched with our request
            //check if any results are granted
            int length = grantResults.length;
            if (length > 0) {
                //when permissions are available, iterate through them to check if granted
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        hasGranted = false;
                    }
                }
            }
            //if granted, show a notification accordingly
            if (hasGranted) {
                //call switch bluetooth method again to execute the logic
                switchBluetooth();
            } else {
                //if not granted, show a notification accordingly
                Toast.makeText(context, "User denied permission for Bluetooth", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Activity result method will be called after user allowing/denied to enable bluetooth
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is activating bluetooth
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //notify user
                Toast.makeText(context,
                        "Bluetooth is turned on!", Toast.LENGTH_SHORT)
                        .show();
                // changing the button text
                buttonSwitchBluetooth.setText("Turn Bluetooth Off");
                //change the background color
                buttonSwitchBluetooth.setBackgroundColor(Color.RED);
                //changing the status we maintain
                isBluetoothSwitchedOn = true;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(context,
                        "User denied enabling Bluetooth", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(context,
                        "Sorry! Failed to enable Bluetooth", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private boolean checkIfBluetoothAvailable() {
        boolean isAvailable = true;
        //get the bluetooth adapter
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if the bluetooth adapter is null or not
        if (this.mBluetoothAdapter == null) {
            // Phone does not support Bluetooth so let the user know and exit.
            isAvailable = false;
            //create an alert dialog
            AlertDialog alert = new AlertDialog.Builder(context)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            //show the alert
            alert.show();
        }
        return isAvailable;
    }

    public BluetoothDemoFragment() {
        // Required empty public constructor
    }
}
