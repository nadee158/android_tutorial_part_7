package lk.uok.mit.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import lk.uok.mit.helloworld.R;
import lk.uok.mit.listener.OnCustomButtonClickListener;

public class BluetoothDeviceAdadpter extends ArrayAdapter<BluetoothDevice> {

    //a variable to refer to the passed data set through constructor
    private List<BluetoothDevice> bluetoothDevices;
    //a variable to refer to the passed context through constructor
    private Context context;
    //an inflater to inflate layout
    private LayoutInflater mInflater;
    //a click listener to handle click event of pair/unpair button
    private OnCustomButtonClickListener pairButonListener;
    //a click listener to handle click event of send data button
    private OnCustomButtonClickListener sendDataButonListener;

    //the view holder pattern is used to prevent
    // calling findViewById() frequently during the scrolling of ListView
    //findViewById() is an expensive method in terms of resources
    static class ViewHolder {
        TextView textViewDeviceName;
        TextView textViewDeviceAddress;
        Button buttonPairDevice;
        Button buttonSendData;
    }

    //add a constructor to accept context and data set
    public BluetoothDeviceAdadpter(@NonNull Context context, List<BluetoothDevice> bluetoothDevices) {
        //call super clase's constructor by passing the context, layout and the data set
        super(context, R.layout.bluetooth_device_lsit_row_item, bluetoothDevices);
        this.context = context;
        this.bluetoothDevices = bluetoothDevices;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //declare a variable f type ViewHolder
        ViewHolder holder;
        //check if the convert view is null;
        if (convertView == null) {
            //if the convert is null, it means view is not initilaized yet
            //initialize the view by inflating the row item layout
            convertView = mInflater.inflate(R.layout.bluetooth_device_lsit_row_item, null);
            //initialize the view holder
            holder = new ViewHolder();
            //set the text views and buttons of the view hoder by getting views from layout
            holder.textViewDeviceName =
                    (TextView) convertView.findViewById(R.id.textViewDeviceName);
            holder.textViewDeviceAddress =
                    (TextView) convertView.findViewById(R.id.textViewDeviceAddress);
            holder.buttonPairDevice =
                    (Button) convertView.findViewById(R.id.buttonPairDevice);
            holder.buttonSendData =
                    (Button) convertView.findViewById(R.id.buttonSendData);
            //Sets the tag associated with this view
            convertView.setTag(holder);
        } else {
            //if its not null, means we have already initialized the view holder and set tag once
            //just get the initialized view holder by tag
            holder = (ViewHolder) convertView.getTag();
        }
        //access the bluetooth device in given position from list
        BluetoothDevice device = bluetoothDevices.get(position);
        //set the device name in the text box
        holder.textViewDeviceName.setText(device.getName());
        //set the device address in the text box
        holder.textViewDeviceAddress.setText(device.getAddress());
        //set the button's text based on paired status of the device
        //if the device is already paired, make the button text to "Unpair"
        //if the device is NOT paired, make the button text to "Pair"
        holder.buttonPairDevice.setText((device.getBondState()
                == BluetoothDevice.BOND_BONDED) ? "Unpair" : "Pair");
        //set the onclick listner of the pair button  as shown below;
        holder.buttonPairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pairButonListener != null) {
                    pairButonListener.onCustomButtonClick(position);
                }
            }
        });
        //set the onclick listner of the send data button as shown below;
        holder.buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendDataButonListener != null) {
                    sendDataButonListener.onCustomButtonClick(position);
                }
            }
        });
        return convertView;
    }

    //public setter method to set the click listener of pair button
    public void setPairButonListener(OnCustomButtonClickListener pairButonListener) {
        this.pairButonListener = pairButonListener;
    }

    //public setter method to set the click listener of send data  button
    public void setSendDataButonListener(OnCustomButtonClickListener sendDataButonListener) {
        this.sendDataButonListener = sendDataButonListener;
    }
}

