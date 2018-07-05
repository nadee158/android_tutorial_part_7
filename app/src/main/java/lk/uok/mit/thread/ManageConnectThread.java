package lk.uok.mit.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ManageConnectThread extends Thread {

    private BluetoothDevice device;

    public ManageConnectThread(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public void run() {
        try {
            UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service
            SERIAL_UUID = device.getUuids()[0].getUuid();
            if (SERIAL_UUID == null) {
                SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            }
            // if you don't know the UUID of the bluetooth device service, you can get it like this from android cache

            BluetoothSocket clientSocket = null;

            try {
                clientSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
            } catch (Exception e) {
                Log.e("", "Error creating socket");
            }

            try {
                clientSocket.connect();
                Log.e("", "Connected");
            } catch (IOException e) {
                Log.e("", e.getMessage());
                try {
                    Log.e("", "trying fallback...");

                    clientSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                    clientSocket.connect();

                    Log.e("", "Connected");
                } catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }

            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
            Log.e("SENDING DATA", "Text Message");
            ByteArrayOutputStream output = new ByteArrayOutputStream(4);
            output.write(1);
            os.write(output.toByteArray());
            os.flush();
            //os.close();
        } catch (Exception e1) {
            Log.e("SENDING DATA", e1.getMessage());
            e1.printStackTrace();
            return;
        }
    }


}
