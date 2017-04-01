package sie.amplifier_conctroller.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feasycom.s_port.R;

import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;
import com.github.mikephil.charting.charts.LineChart;

import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_main extends Activity {

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    final String TAG = dsp_main.class.getSimpleName();
    Sie_app_data_share sie_data_share;
    private FEShare share = FEShare.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_eq_page);

        sie_data_share = (Sie_app_data_share)getApplication();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //解除广播接收器
        unregisterReceiver(receiver);
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, share.getIntent_ui_setting_Filter());
    }


    private Handler myHandler = new Handler()
    {
        // 2.重写消息处理函数
        public void handleMessage(Message msg)
        {
            switch (msg.what) {

            }
            super.handleMessage(msg);
        }

    };


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            Message msg = new Message();
            Bundle bundle = new Bundle();
            if (share.SIE_UI_ACTION_MAINVOLUME.equals(action)) {
                // 连接成功
                msg.what=1;
            } else if (share.SIE_UI_ACTION_INPUTCHANNEL.equals(action)) {
                msg.what=2;
            }else if (share.SIE_UI_ACTION_BT_CHANGE.equals(action)) {
                // 断开连接
                MyLog.i(TAG, "蓝牙状态");

                msg.what=0;
                msg.arg1 =intent.getExtras().getInt("bt_status");

            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                    MyLog.i("蓝牙", "关闭");
                    msg.what=0;
                }
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                MyLog.v(TAG,"蓝牙连接");
                sie_data_share.startrev();
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                MyLog.v(TAG,"蓝牙断开连接");
            }

            myHandler.sendMessage(msg);

        }

    };

}
