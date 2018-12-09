package com.desktop.telephone.telephonedesktop.desktop.recevier;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.bluetooth.client.pbap.BluetoothPbapClient;
import com.desktop.telephone.telephonedesktop.desktop.bluetooth.android.vcard.VCardEntry;

import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (btDevice.getBondState() == BluetoothDevice.BOND_BONDED) {//已经配对成功读取设备内容
                readInfo(btDevice);
            }
        }
    }

    private void readInfo(BluetoothDevice btDevice) {
        StreamReader streamReader = new StreamReader(btDevice);
        streamReader.start();
    }

    public class StreamReader extends Thread {
        BluetoothPbapClient client;

        public StreamReader(BluetoothDevice bluetoothDevice) {
            client = new BluetoothPbapClient(bluetoothDevice, handler);
        }

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_DONE:
                        if (msg.obj != null) {
                            client.disconnect();
                            List<VCardEntry> list = (List<VCardEntry>) msg.obj;
                            for (int i = 0; i < list.size(); i++) {
                                Log.d(list.get(0).toString(),"sss");
                            }
                        }
                        break;
                }
            }
        };

        @Override
        public void run() {
            super.run();

        }
    }
}
