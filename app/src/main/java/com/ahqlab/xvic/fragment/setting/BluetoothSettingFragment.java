package com.ahqlab.xvic.fragment.setting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.adapter.XvicBluetoothAdapter;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.broadcast.BluetoothReceiver;
import com.ahqlab.xvic.databinding.FragmentBluetoothSettingBinding;
import com.ahqlab.xvic.domain.BluetoothItem;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.util.XvicUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothSettingFragment extends BaseFragment<BluetoothSettingFragment> {
    private FragmentBluetoothSettingBinding binding;
    private BluetoothAdapter btAdapter;
    private BluetoothReceiver mReceiver;
    private List<BluetoothItem> mItems;
    private List<BluetoothItem> mPairedList;
    private XvicBluetoothAdapter mAdapter;
    private String connectBtName;

    private final String AUTO_CONNECT_UUID_STR = "00:11:67:11:1B:DA";

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth_setting, container, false);
        binding.setFragment(this);
        List<CircleProgress> steps = new ArrayList<>();
        CircleProgress step = CircleProgress.builder().imageResorce(R.drawable.bluetooth_icon).startColor(Color.parseColor("#9d9d9d")).endColor(Color.parseColor("#777777")).startImgColor(Color.parseColor("#aaaaaa")).endImgColor(Color.parseColor("#aaaaaa")).type(CircleProgress.NORMAL_TYPE).size(CircleProgress.SMALL_SIZE).padding(XvicUtil.dpToPx(100)).build();
        steps.add(step);
        //startColor()
        binding.circleProgressView.setStep(steps);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        mPairedList = new ArrayList<>();


        checkConnected();

        if ( btAdapter == null ) {
            Toast.makeText(getContext(), "블루투스를 지원하지 않는 기기 입니다.", Toast.LENGTH_SHORT).show();
            binding.circleProgressView.setAlpha(20);
        } else {
            for ( BluetoothDevice device : btAdapter.getBondedDevices() ) {
                BluetoothItem item = BluetoothItem.builder().name(device.getName()).device(device).uuid(device.getAddress()).build();
                Log.e(TAG, String.format("state : %d", device.getBondState()));
                mPairedList.add(item);
            }
        }

        mReceiver = new BluetoothReceiver();
        mReceiver.setOnFoundListener(new BluetoothReceiver.OnStateListener() {
            @Override
            public void onFoundListener(BluetoothDevice device) {
                newData(device);
                if ( device.getAddress().equals(AUTO_CONNECT_UUID_STR) ) {
                    try {
                        createBond(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onScanStateListener(boolean state) {
                if ( state ) {
                    binding.scanProgress.setVisibility(View.VISIBLE);
                } else {
                    binding.scanProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        getActivity().registerReceiver(mReceiver, filter);
        btAdapter.startDiscovery();

        List<String> groupList = new ArrayList<>();
        groupList.add("등록된 디바이스");
        groupList.add("연결 가능한 디바이스");

        List<List<BluetoothItem>> childList = new ArrayList<>();
        childList.add(mPairedList);

        mItems = new ArrayList<>();
        childList.add(mItems);

        mAdapter = new XvicBluetoothAdapter(getActivity(), R.layout.bluetooth_list_item, groupList, childList);
        binding.bluetoothList.setAdapter(mAdapter);
        int groupCount = (int) mAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            binding.bluetoothList.expandGroup(i);
         }
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
    public void newData( BluetoothDevice device ) {
        BluetoothItem item = null;
        String name = "";
        name = device.getName() != null ? device.getName() : device.getAddress();
        if ( (item = listCheck(name) ) != null ) {
            item.setUuid(device.getAddress());
            mItems.add(item);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void onClick( View v ) {
        switch (v.getId()) {
            case R.id.search_btn :
                mPairedList.clear();
                for ( BluetoothDevice device : btAdapter.getBondedDevices() ) {
                    BluetoothItem item = BluetoothItem.builder().name(device.getName()).state(device.getName().equals(connectBtName)).device(device).build();
                    mPairedList.add(item);
                }
                mItems.clear();
                btAdapter.startDiscovery();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
    private BluetoothItem listCheck ( String name ) {
        for ( int i = 0; i < mItems.size(); i++ ) {
            if ( mItems.get(i).getName().equals(name) )
                return null;
        }
        return BluetoothItem.builder().name(name).build();
    }

    public void checkConnected()
    {
        // true == headset connected && connected headset is support hands free
        int state = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(BluetoothProfile.HEADSET);
        if (state != BluetoothProfile.STATE_CONNECTED)
            return;

        try
        {
            BluetoothAdapter.getDefaultAdapter().getProfileProxy(getContext(), serviceListener, BluetoothProfile.HEADSET);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private BluetoothProfile.ServiceListener serviceListener = new BluetoothProfile.ServiceListener()
    {
        @Override
        public void onServiceDisconnected(int profile)
        {

        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy)
        {
            for (BluetoothDevice device : proxy.getConnectedDevices()) {
                connectBtName = device.getName();
                Log.i("onServiceConnected", "|" + device.getName() + " | " + device.getAddress() + " | " + proxy.getConnectionState(device) + "(connected = "
                        + BluetoothProfile.STATE_CONNECTED + ")");
            }
            for ( int i = 0; i < mPairedList.size(); i++ ) {
                if ( mPairedList.get(i).getName().equals(connectBtName) ) {
                    if ( connectBtName != null && !connectBtName.equals("") )
                        binding.circleProgressView.changeColor(Color.parseColor("#bdc0f8"),Color.parseColor("#64d9fc"));
                    mPairedList.get(i).setState(true);
                    mAdapter.notifyDataSetChanged();
                }

            }
            BluetoothAdapter.getDefaultAdapter().closeProfileProxy(profile, proxy);
        }
    };
    public boolean createBond(BluetoothDevice btDevice) throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        for ( int i = 0; i < mItems.size(); i++ ) {
            if ( mItems.get(i).getUuid().equals(btDevice.getAddress()) )
                mItems.remove(i);
        }

        BluetoothItem item = BluetoothItem.builder().name(btDevice.getName() != null || !btDevice.getName().equals("") ? btDevice.getName() : btDevice.getAddress()).device(btDevice).uuid(btDevice.getAddress()).build();
        mPairedList.add(item);
        mAdapter.notifyDataSetChanged();
//        requestConnect(btDevice, UUID.nameUUIDFromBytes(btDevice.getAddress().getBytes()));
        return returnValue.booleanValue();
    }
    private BluetoothSocket mConnectSocket;
    private boolean mIsConnected = false;

    public boolean requestConnect(BluetoothDevice device, UUID uuid) {
        try {
            mConnectSocket = getBluetoothSocket(device, uuid);
            if (mConnectSocket != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mConnectSocket.connect();
                            mIsConnected = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            mIsConnected = false;
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mIsConnected = false;
        }
        return mIsConnected;
    }
    public BluetoothSocket getBluetoothSocket(BluetoothDevice device, UUID uuid) throws IOException {
        return device.createInsecureRfcommSocketToServiceRecord(uuid);
    }


}
