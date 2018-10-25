package com.ahqlab.xvic.domain;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BluetoothItem {
    private int level;
    private boolean state;
    private String name;
    private BluetoothDevice device;
    private String uuid;
}
