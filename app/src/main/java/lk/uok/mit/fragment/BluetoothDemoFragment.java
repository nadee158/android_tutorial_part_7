package lk.uok.mit.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import lk.uok.mit.adapter.BluetoothDeviceAdadpter;
import lk.uok.mit.helloworld.R;
import lk.uok.mit.listener.OnPairButtonClickListener;
import lk.uok.mit.thread.ManageConnectThread;
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

    //a class variable to refer to the buttonListBluetoothDevices of the layout,
    //upon its click we will get and display the list of bluetooth devices
    private Button buttonListBluetoothDevices;
    //a variable to refer to a list view
    private ListView listViewBluetoothDevices;
    //The adapter used to render list view
    private BluetoothDeviceAdadpter bluetoothDeviceAdadpter;
    //a variable to hold device list
    private ArrayList<BluetoothDevice> bluetoothDevices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Bluetooth Demo");
        //initilaize context
        context = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the button reference
        buttonSwitchBluetooth = view.findViewById(R.id.buttonSwitchBluetooth);
        //set the onclick listener of the button
        buttonSwitchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the switchBluetooth method upon click of the button
                switchBluetooth();
            }
        });

        //get the button reference for set visibility button
        buttonSwitchBluetoothVisibility = view.findViewById(R.id.buttonSwitchBluetoothVisibility);
        //set the onclick listener of set visibility button
        buttonSwitchBluetoothVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the switchBluetooth method upon click of the button
                switchBluetoothVisibility();
            }
        });

        //initialize the bluetooth device list
        bluetoothDevices = new ArrayList<BluetoothDevice>();
        //initialize the ListView
        listViewBluetoothDevices = view.findViewById(R.id.listViewBluetoothDevices);
        //initialize the adapter
        bluetoothDeviceAdadpter = new BluetoothDeviceAdadpter(context, bluetoothDevices);
        //set the listener to handle onclick events of each button
        bluetoothDeviceAdadpter.setPairButonListener(new OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = bluetoothDevices.get(position);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);
                } else {
                    Toast.makeText(context,
                            "Pairing device..", Toast.LENGTH_SHORT).show();
                    pairDevice(device);
                }
            }
        });

        //set the listener to handle send data events of each button
        bluetoothDeviceAdadpter.setSendDataButonListener(new OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = bluetoothDevices.get(position);
                //check if the device is paired
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.e("SENDING DATA", device.getName());
                    senDataToDevice(device);
                } else {
                    Toast.makeText(context,
                            "The device is not paired!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listViewBluetoothDevices.setAdapter(bluetoothDeviceAdadpter);

        //get the button reference for list devices button
        buttonListBluetoothDevices = view.findViewById(R.id.buttonListBluetoothDevices);
        //set the onclick listener of list devices button
        buttonListBluetoothDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the switchBluetooth method upon click of the button
                listBluetoothDevices();
            }
        });
    }


    private void senDataToDevice(BluetoothDevice device) {
        try {

            ManageConnectThread manageConnectThread = new ManageConnectThread(device);
            manageConnectThread.start();

        } catch (Exception ex) {
            Log.e("senDataToDevice", ex.getMessage());
        }

    }

    //to list the Bluetooth devices
    private void listBluetoothDevices() {
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
                if (mBluetoothAdapter.isEnabled()) {
                    //start scanning bluetooth devices
                    mBluetoothAdapter.startDiscovery();
                    //clear the existing items from list
                    bluetoothDeviceAdadpter.clear();
                    //list the currently paired devices
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    //iterate over set and add devices to the lsit associated with adapter
                    for (BluetoothDevice device : pairedDevices) {
                        bluetoothDevices.add(device);
                    }
                    //call “notifyDataSetChanged” method of the adapter to reflect the
                    // latest data set available in the “bluetoothDevices”
                    bluetoothDeviceAdadpter.notifyDataSetChanged();
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

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
            device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(context,
                            "Paired!", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(context,
                            "Unpaired!", Toast.LENGTH_SHORT).show();
                }

                bluetoothDeviceAdadpter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                Toast.makeText(context,
                        "Scanning of Bluetooth Devices started!", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                Toast.makeText(context,
                        "Scanning of Bluetooth Devices finished!", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                boolean isDeviceExist = checkIfDeviceExist(device);
                if (!(isDeviceExist)) {
                    bluetoothDevices.add(device);
                    bluetoothDeviceAdadpter.notifyDataSetChanged();
                }
                Toast.makeText(context,
                        "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean checkIfDeviceExist(BluetoothDevice device) {
        for (BluetoothDevice deviceItem : bluetoothDevices) {
            if (deviceItem.getAddress().equals(device.getAddress())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
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
                if (mBluetoothAdapter.isEnabled()) {
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
                if (!mBluetoothAdapter.isEnabled()) {
                    //if bluetooth is not enabled, first set the status
                    isBluetoothSwitchedOn = false;
                    //try to enable Bluetooth by sending user request
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, BLUETOOTH_REQUEST_CODE);
                } else {
                    //if bluetooth is enabled, first set the status
                    isBluetoothSwitchedOn = true;
                    //disable bluetooth
                    mBluetoothAdapter.disable();
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
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if the bluetooth adapter is null or not
        if (mBluetoothAdapter == null) {
            // Phone does not support Bluetooth so let the user know and exit.
            isAvailable = false;
            //create an alert dialog
            AlertDialog alert = new AlertDialog.Builder(context)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
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
