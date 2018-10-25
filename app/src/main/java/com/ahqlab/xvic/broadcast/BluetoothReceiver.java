package com.ahqlab.xvic.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {
    private final String TAG = BluetoothReceiver.class.getSimpleName();
    private OnStateListener mListener;
    public interface OnStateListener {
        void onFoundListener( BluetoothDevice device );
        void onScanStateListener( boolean state );
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: BLUETOOTH WOW!! " + action);
        // When discovery finds a device
        if ( BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action) ) {
            if ( mListener != null )
                mListener.onScanStateListener(true);
        }
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if ( mListener != null )
                mListener.onFoundListener(device);
            // If it's already paired, skip it, because it's been listed already
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

            }
            // When discovery is finished, change the Activity title
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            if ( mListener != null )
                mListener.onScanStateListener(false);
        }
    }
    public void setOnFoundListener ( OnStateListener listener ) {
        mListener = listener;
    }
}
