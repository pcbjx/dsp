package com.feasycom.s_port.ShareFile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import com.feasycom.s_port.R;
import com.feasycom.s_port.model.BluetoothDeviceDetail;
import com.feasycom.s_port.model.MyLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by yumingyue on 2017/1/5.
 */

public class FEShare implements Serializable {
    private final static String TAG = FEShare.class.getSimpleName();

    ////////////////////////////////////////////////////
    /**
     * 连接成功
     */
    public static final int CONNECT_SUCCESSFUL = 0;
    /**
     * 已经连接了设备，无需再连
     */
    public static final int ALREADY_CONNECTED = 1;
    /**
     * 连接失败
     */
    public static final int CONNECT_FAILURE = 2;
    /**
     * 上一个设备连接中，请稍候！
     */
   // public static final int CONNECTING = 3;

    ////////////////////////////////////////////////////
    /**
     * SPP搜索模式
     */
    public static final int MODEL_SPP = 0;
    /**
     * BLE搜索模式
     */
    public static final int MODEL_BLE = 1;

    ////////////////////////////////////////////////////
    //tab
    public int tabId = R.id.communication;
    public int searchModel = MODEL_BLE;

    public Intent intent = new Intent();
    private boolean waitingForReturn = false;
    public boolean isBleSendFinish = true;
    public boolean isSPP = true;
    public Calendar c_start = Calendar.getInstance();
    public Calendar c_end;
    public Context context;
    public boolean isFinishRefrashList = true;

    public ArrayList<BluetoothDeviceDetail> devices = new ArrayList<BluetoothDeviceDetail>();
    public ArrayList<String> devices_addrs = new ArrayList<String>();

    //蓝牙
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public int connect_state = BluetoothProfile.STATE_DISCONNECTED;
    public BluetoothSocket socket;
    public BluetoothDevice device;
    public InputStream inputStream;
    public OutputStream outputStream;

    public boolean autoConect = false;

//    public DataStruct RcvDeviceData = new DataStruct();

    public ScanCallback scanCallback;
    private Handler mHandler = new Handler() {
    };
    private Runnable stopSPPScanRunnable;
    private List<ScanFilter> filters;


    public static int  CONNECTING = 0;
    public static int  CONNECTED = 1;
    public static int  DIS_CONNECT = 2;



    // 广播信息过滤
    private IntentFilter intentFilter;
    /**********************************************************/
    private IntentFilter intentFilter_ui_main;
    private IntentFilter intentFilter_ui_chanle;
    private IntentFilter intentFilter_ui_setting;
    private IntentFilter intentFilter_ui_delay;
    private IntentFilter intentFilter_ui_fre;

    public static final String SIE_UI_ACTION_INPUTCHANNEL =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_INPUTCHANNEL";
    public static final String SIE_UI_ACTION_OUTPUTCHANNEL =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_OUTPUTCHANNEL";
    public static final String SIE_UI_ACTION_EQ_PREPARE =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_EQ_PREPARE";
    public static final String SIE_UI_ACTION_EQ_8 =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_EQ_8";
    public static final String SIE_UI_ACTION_EQ_31 =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_EQ_31";
    public static final String SIE_UI_ACTION_EQ_Bandwidth =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_EQ_Bandwidth";
    public static final String SIE_UI_ACTION_CHANEL_DELAY =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_CHANEL_DELAY";
    public static final String SIE_UI_ACTION_MAINVOLUME =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_MAINVOLUME";
    public static final String SIE_UI_ACTION_CHANEL_VOLUME =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_CHANEL_VOLUME";
    public static final String SIE_UI_ACTION_CHANLE_FRE =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_CHANLE_FRE";
    public static final String SIE_UI_ACTION_DEEP_BASS =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_DEEP_BASS";
    public static final String SIE_UI_ACTION_WIDESOUND =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_WIDESOUND";

    public static final String SIE_UI_ACTION_non =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_non";

    public static final String SIE_UI_ACTION_BT_CHANGE =
            "sie.amplifier_conctroller.ui.action.SIE_UI_ACTION_non";

    private static class FEShareHolder {
        //单例对象实例
        static final FEShare INSTANCE = new FEShare();
    }

    public static FEShare getInstance() {
        return FEShareHolder.INSTANCE;
    }

    //private的构造函数用于避免外界直接使用new来实例化对象
    private FEShare() {
    }

    //readResolve方法应对单例对象被序列化时候
    private Object readResolve() {
        return getInstance();
    }

    /**********************************************************/

    public void init() {
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(receiver, getIntentFilter());
    }

    private void initSocket() throws IOException {
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//        final String SPP_UUID = "00001102-0000-1000-8000-00805F9B34FB";
        //public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";

        UUID uuid = UUID.fromString(SPP_UUID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
        } else {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        }
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }


    public IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            //        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        }
        return intentFilter;
    }

    public IntentFilter getIntent_ui_mian_Filter() {
        if (intentFilter_ui_main == null) {
            intentFilter_ui_main = new IntentFilter();
            intentFilter_ui_main.addAction(SIE_UI_ACTION_MAINVOLUME);
            intentFilter_ui_main.addAction(SIE_UI_ACTION_INPUTCHANNEL);
            intentFilter_ui_main.addAction(SIE_UI_ACTION_BT_CHANGE);
            intentFilter_ui_main.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter_ui_main.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        }
        return intentFilter_ui_main;
    }

    public IntentFilter getIntent_ui_setting_Filter() {
        if (intentFilter_ui_setting == null) {
            intentFilter_ui_setting = new IntentFilter();
            intentFilter_ui_setting.addAction(SIE_UI_ACTION_EQ_8);
            intentFilter_ui_setting.addAction(SIE_UI_ACTION_EQ_31);
            intentFilter_ui_setting.addAction(SIE_UI_ACTION_EQ_Bandwidth);

        }
        return intentFilter_ui_setting;
    }

    public IntentFilter getIntent_ui_delay_Filter() {
        if (intentFilter_ui_delay == null) {
            intentFilter_ui_delay = new IntentFilter();
            intentFilter_ui_delay.addAction(SIE_UI_ACTION_EQ_8);
            intentFilter_ui_delay.addAction(SIE_UI_ACTION_EQ_31);
            intentFilter_ui_delay.addAction(SIE_UI_ACTION_EQ_Bandwidth);

        }
        return intentFilter_ui_delay;
    }

    public IntentFilter getIntent_ui_fq_dv_Filter() {
        if (intentFilter_ui_fre == null) {
            intentFilter_ui_fre = new IntentFilter();
            intentFilter_ui_fre.addAction(SIE_UI_ACTION_EQ_8);
            intentFilter_ui_fre.addAction(SIE_UI_ACTION_EQ_31);
            intentFilter_ui_fre.addAction(SIE_UI_ACTION_EQ_Bandwidth);

        }
        return intentFilter_ui_fre;
    }

    private void setupRunnable() {
        if (stopSPPScanRunnable == null) {
            stopSPPScanRunnable = new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.cancelDiscovery();
                }
            };
        }
    }

    /**
     * 搜索设备
     * 注意：BLE搜索前必须赋值 leScanCallback
     */
    public boolean search() {
        if (bluetoothAdapter == null) return false;
        // 先停止搜索
        stopSearch();
        // 清空数组
        if (devices != null) {
            devices.clear();
            devices_addrs.clear();
        }
        // 设置Runnable
        setupRunnable();

        if (isSPP) {
            mHandler.postDelayed(stopSPPScanRunnable, 30000);// 搜索30s
            // 处理已配对设备
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
//            String strShow = "[已配对] " + device.getAddress() + "  " + device.getName();

                String strShow = context.getResources().getText(R.string.paired) + device.getName();
                addDevice(new BluetoothDeviceDetail(strShow, device.getAddress(), -100));
//                MyLog.i("添加已配对设备", " ");
            }
            return bluetoothAdapter.startDiscovery();
        }
        return false;
    }

    synchronized public boolean stopSearch() {
        if (isSPP) {
            sendBtStatus(DIS_CONNECT);
            return bluetoothAdapter.cancelDiscovery();
        }

        return false;
    }

    private void sendBtStatus(int value)
    {
        Intent intent = new Intent(SIE_UI_ACTION_BT_CHANGE);
        intent.putExtra("bt_status",value);
        context.sendBroadcast(intent);
    }

    /**
     * SPP连接该地址设备
     *
     * @throws IOException
     */
    private boolean SPPConnect() {
//        device = bluetoothAdapter.getRemoteDevice(address);
        try {
            MyLog.i("initSocket", "------0");
            sendBtStatus(CONNECTING);
            initSocket();
            MyLog.i("initSocket", "------2");
//            connectingAddr = address;
            sleep(100);
            try {
                socket.connect();
//                connectedAddr = connectingAddr;
//                connectingAddr = null;
                MyLog.i("连接设备", "成功");
                sendBtStatus(CONNECTED);

                sleep(100);
                return true;
            } catch (IOException e) {
                MyLog.i("连接设备", "失败");
                sendBtStatus(DIS_CONNECT);
                sleep(100);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    synchronized public boolean connect(BluetoothDevice device) {
//        if (connect_state == BluetoothProfile.STATE_CONNECTED) return false; //打开只支持单连接
//        connect_state = BluetoothProfile.STATE_CONNECTING;
        this.device = device;
        stopSearch();
        if (isSPP) {
            return SPPConnect();
        } else {
            return false;
        }
    }

    /**
     * 断开连接
     */
    synchronized public void disConnect() {
//       connect_state = BluetoothProfile.STATE_DISCONNECTING;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isSPP) {
                    if (socket != null) {
                        try {
                            outputStream.close();
                            inputStream.close();
                            socket.close();
                            socket = null;
//                            connectedAddr = null;
//                            connectingAddr = null;
                            c_start = Calendar.getInstance();
                            MyLog.i("断开", "SPP");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }
        }).start();

    }

    /**
     * 发送数据
     *
     * @param b
     * @return 返回包数
     */
    public int write(final byte b[]) {
        if (isSPP) {
            int packets = 0;
            int length_bytes = b.length;
            // 分包
            final int perPacketLength = 2000;
            if (length_bytes > perPacketLength) {
                int startPoint = 0;
                byte[] bytes = new byte[perPacketLength];
                while (length_bytes > perPacketLength) {
                    System.arraycopy(b, startPoint, bytes, 0, perPacketLength);
                    try {
                        outputStream.write(bytes);
                        startPoint += perPacketLength;
                        length_bytes -= perPacketLength;

                        packets++;
                        MyLog.i("统计一包", "1");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return packets;
                    }
                }
                if (length_bytes != perPacketLength) {
                    length_bytes = b.length % perPacketLength;
                }
                if (length_bytes > 0) {
                    byte[] bytes_last = new byte[length_bytes];
                    System.arraycopy(b, startPoint, bytes_last, 0, length_bytes);
                    try {
                        outputStream.write(bytes_last);
                        packets++;
                        MyLog.i("统计一包", "2");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return packets;
                    }
                }
                return packets;
            } else {
                try {
                    outputStream.write(b);
                    return 1;
                } catch (IOException e) {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }


    /**
     * 发送指定范围数据
     *
     * @param b
     * @param off
     * @param len
     * @return
     */
    public int write(byte b[], int off, int len) {
        if (isSPP) {
            if (socket != null && outputStream != null) {
                try {
                    outputStream.write(b, off, len);
                    return 1;
                } catch (IOException e) {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {

            return 0;
        }
    }

    public int sie_write(byte b[], int len) {
        byte[] send_frame = new byte[len + 6];

        if (b == null || len <= 0)
            return 0;

        send_frame[0] = (byte) 0xde;
        send_frame[1] = (byte) 0x57;

        send_frame[2] = (byte) ((len >> 8) & 0xff);
        send_frame[3] = (byte) ((len) & 0xff);
        send_frame[4] = (byte) (0x04);

        System.arraycopy(b, 0, send_frame, 5, len);


        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += b[i];
        }
        send_frame[len + 5] = (byte) (0xff - sum & 0xff);
        MyLog.v(TAG, "send crc:" + send_frame[len + 5]);


        MyLog.v(TAG, "send len:" + send_frame.length);
        return write(send_frame, 0, len + 6);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                connect_state = BluetoothProfile.STATE_CONNECTED;
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                connect_state = BluetoothProfile.STATE_DISCONNECTED;
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            } else if (autoConect && (BluetoothDevice.ACTION_FOUND.equals(action))) {
                final BluetoothDevice deviceGet = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MyLog.i("找到设备,并自动连接", deviceGet.getAddress());
                //信号强度。
                final int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                addDevice(new BluetoothDeviceDetail(deviceGet.getName(), deviceGet.getAddress(), rssi));

                String devName = deviceGet.getName();
                int find_str = devName.indexOf("Feasycom");//找到连接过的Feasycom蓝牙设备尝试连接
                if (find_str == 0) {
                    device = deviceGet;
                    if (connect(device)) {
                        autoConect = false;
                    }
                }

            }
        }
    };


    /**
     * 设置是否等待断开回调
     *
     * @param isWait
     */
    public void setWaitingForReturn(boolean isWait) {
//        waitingForReturn = isWait;
//        if (waitingForReturn){
//            c_start = Calendar.getInstance();
//        }else {
//            c_end = Calendar.getInstance();
//            Toast.makeText(context, "Disconnect: " + String.valueOf (c_end.getTime().getTime() - c_start.getTime().getTime()) + " ms"
//                    , Toast.LENGTH_SHORT).show();
//            c_start = c_end;
//        }
    }

    // 搜索模式字符串

    public String setSearchModel(int model) {
        searchModel = model;
        switch (searchModel) {
            case MODEL_SPP: {
                isSPP = true;
                return "SPP";
            }
            case MODEL_BLE: {
                isSPP = false;
                return "BLE";
            }
        }
        return "";
    }

    // 一般搜索模式
    public String model_default() {
        searchModel = isSPP ? MODEL_SPP : MODEL_BLE;
        return setSearchModel(searchModel);
    }

    public void addDevice(BluetoothDeviceDetail deviceDetail) {
        devices.add(deviceDetail);
        devices_addrs.add(deviceDetail.address);
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public boolean auto_connect_bt() {
        autoConect = true;
        if (bluetoothAdapter == null) return false;
        // 先停止搜索
        stopSearch();
        // 清空数组
        if (devices != null) {
            devices.clear();
            devices_addrs.clear();
        }
        // 设置Runnable
        setupRunnable();

        if (isSPP) {
            mHandler.postDelayed(stopSPPScanRunnable, 30000);// 搜索30s
            // 处理已配对设备
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (autoConect) {
                    String devName = device.getName();
                    int find_str = devName.indexOf("Feasycom");//找到连接过的Feasycom蓝牙设备尝试连接
                    if (find_str == 0) {
                        this.device = device;
                        if (connect(this.device)) {
                            MyLog.v(TAG, "连接成功");
                            autoConect = false;
                            return true;
                        } else {
                            MyLog.v(TAG, "连接失败，尝试别的");
                            continue;
                        }
                    }
                }
            }
        }
        bluetoothAdapter.startDiscovery();
        return false;
    }

}
