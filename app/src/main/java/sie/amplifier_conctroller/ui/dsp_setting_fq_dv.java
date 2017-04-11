package sie.amplifier_conctroller.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_fq_dv extends AppCompatActivity {

    final String TAG = dsp_setting_delay.class.getSimpleName();
    private FEShare share = FEShare.getInstance();
    Sie_app_data_share sie_data_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xover);
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
        registerReceiver(receiver, share.getIntent_ui_delay_Filter());
    }

    private Handler myHandler = new Handler()
    {
        // 2.重写消息处理函数
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                {
                    //flashUIFromProtocol(msg.arg1);
                    break;
                }
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
            MyLog.v(TAG,"delay");
            if (share.SIE_UI_ACTION_CHANEL_VOLUME.equals(action)) {
                // 连接成功
                msg.what=1;
                msg.arg1= DataStruct.sieProtocol.prtc_chanleVolume;
            }

            myHandler.sendMessage(msg);

        }

    };
}
