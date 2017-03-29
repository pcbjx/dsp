package com.feasycom.s_port.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.BluetoothDeviceDetail;
import com.feasycom.s_port.model.DropDownRefresh.RefreshableView;
import com.feasycom.s_port.model.MyListAdapter;
import com.feasycom.s_port.model.MyLog;

import sie.amplifier_conctroller.CommunicationChat;
import sie.amplifier_conctroller.ui.dsp_main;


public class MainActivity extends AppCompatActivity {
    private TextView tv_state, tv_search_model;
    private RefreshableView refreshableView;
    private ListView listView_devices;
    private Button btn_search;
    //
    private MyListAdapter adapter;

    //BLE
    private int theSameCount = 0;

    private FEShare share = FEShare.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_search_model = (TextView) findViewById(R.id.tv_search_model);

        //RefreshableView
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);

        //ListView
        listView_devices = (ListView) findViewById(R.id.listView_devices);

        //Button
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new MyClickListener());


        if (!share.bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.on)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.bt_turned_on),
                    Toast.LENGTH_SHORT).show();
        }
        adapter = new MyListAdapter
                (share.devices,this,getLayoutInflater());

        listView_devices.setAdapter(adapter);
        //点击列表
        listView_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("点击列表", ".");
//                System.out.println(Integer.toString(position));

                // 获取蓝牙设备的连接状态
//                connetedAddr = deviceAddrs.get(position);
                BluetoothDeviceDetail deviceDetail = share.devices.get(position);
                if (!share.bluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.turn_on_bt)
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                share.device = share.bluetoothAdapter.getRemoteDevice(deviceDetail.address);
                share.intent.setClass(MainActivity.this, dsp_main.class);
                MyLog.i("创建", "intent3");
                startActivity(share.intent);
                MyLog.i("创建", "intent4");
            }
        });

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                MyLog.i("下拉刷新", "回调");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        MyLog.i("setOnRefreshListener", "---------");
                        refreshableView.finishRefreshing();
                        btn_search.performClick();

                    }
                });

            }
        }, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, share.getIntentFilter());
        /**
         * 设置搜索模式
         * share.model_default() 获取一般搜索模式字符串, 只有SPP或BLE
         */
        tv_search_model.setText(share.model_default());
        btn_search.performClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        share.stopSearch();
        unregisterReceiver(receiver);
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_search: {
                    share.search();
                    break;
                }
                default:
                    break;
            }
        }
    }

    //监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (share.tabId != R.d.communication) return;
            final String action = intent.getAction();
//            MyLog.i("main蓝牙回调", String.valueOf(intent));
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //找到设备
                final BluetoothDevice deviceGet = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MyLog.i("找到设备", deviceGet.getAddress());
                //信号强度。
                final int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                deviceFound(deviceGet, rssi);

//                System.out.println(strShow);
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                tv_state.setText(getResources().getText(R.string.searched));
            }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                tv_state.setText(getResources().getText(R.string.searching));
            }
        }
    };

    private void deviceFound(final BluetoothDevice device, final int rssi) {

                if (share.devices.size() > 0) {
                    if (share.devices_addrs.contains(device.getAddress())) {
//                        MyLog.i("5555", "1");
                        theSameCount++;
//                        MyLog.i("5555", "1....." + theSameCount);
                        if (theSameCount > 30) {
//                            MyLog.i("5555", "2--" + theSameCount);
                            theSameCount = 0;
                            final int index = share.devices_addrs.indexOf(device.getAddress());
//                            MyLog.i("5555", "2");
                            //MyLog.i(device.getAddress(), "更新信号");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    MyLog.i("5555", "3");
                                    share.devices.get(index).setDetail(device.getName(), device.getAddress(), rssi);
//                                    MyLog.i("5555", "4");
                                    adapter.notifyDataSetChanged();
//                                    MyLog.i("5555", "5");
                                }
                            });

                        }
                        return;
                    }
                }else
                {

                }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                MyLog.i("6666", "1"+share.devices.size());
                share.addDevice(new BluetoothDeviceDetail(device.getName(), device.getAddress(), rssi));
//                MyLog.i("6666", "2");
                adapter.notifyDataSetChanged();
//                MyLog.i("6666", "3");

            }
        });
    }
//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (share.BA.isDiscovering()){
//            share.BA.cancelDiscovery();
//        }
//    }
public Resources getSResources() {
    // TODO Auto-generated method stub
    return getResources();
}
}

