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
import android.view.View;
import android.widget.Button;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.TabActivity;
import com.feasycom.s_port.model.MyLog;

import common.zhang.customer.RotateButtom;
import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_chanel extends Activity {

    final String TAG = dsp_setting_chanel.class.getSimpleName();
    Button [] chanelBtMuteList;
    Button [] polarBtList;
    RotateButtom [] chanel_r_bt_list;

    private FEShare share = FEShare.getInstance();
    Sie_app_data_share sie_data_share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_output);
        sie_data_share = (Sie_app_data_share)getApplication();
        init();
    }

    public void init()
    {

        chanel_r_bt_list = new RotateButtom[DataStruct.max_channel];

        chanel_r_bt_list[0] =(RotateButtom)findViewById(R.id.id_sb_output_ch0);
        chanel_r_bt_list[1] =(RotateButtom)findViewById(R.id.id_sb_output_ch1);
        chanel_r_bt_list[2] =(RotateButtom)findViewById(R.id.id_sb_output_ch2);
        chanel_r_bt_list[3] =(RotateButtom)findViewById(R.id.id_sb_output_ch3);
        chanel_r_bt_list[4] =(RotateButtom)findViewById(R.id.id_sb_output_ch4);
        chanel_r_bt_list[5] =(RotateButtom)findViewById(R.id.id_sb_output_ch5);
        chanel_r_bt_list[6] =(RotateButtom)findViewById(R.id.id_sb_output_ch6);
        chanel_r_bt_list[7] =(RotateButtom)findViewById(R.id.id_sb_output_ch7);

        for (int i = 0;i<DataStruct.max_channel;i++)
        {
            chanel_r_bt_list[i].set_titile(getResources().getText(R.string.title_setting_chanel).toString()+i);
            chanel_r_bt_list[i].setBarinit(0,60,DataStruct.chanelLastVolume[i]);
            chanel_r_bt_list[i].setOnTempChangeListener(new RotateButtom.OnTempChangeListener()
            {
                @Override
                public void change(View v, int temp) {
                    //Toast.makeText(dsp_setting_chanel.this, temp + ""+"V id "+v.getId(), Toast.LENGTH_SHORT).show();
                    int id = v.getId();
                    for (int j = 0;j<DataStruct.max_channel;j++)
                    {
                        if (id==chanel_r_bt_list[j].getId())
                        {
                            DataStruct.chanelLastVolume[j] = (byte) temp;
                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_chanleVolume);
                        }
                    }
                }
            });
        }

        chanelBtMuteList = new Button[DataStruct.max_channel];
        chanelBtMuteList[0] = (Button)findViewById(R.id.id_b_output_mute_ch0);
        chanelBtMuteList[1] = (Button)findViewById(R.id.id_b_output_mute_ch1);
        chanelBtMuteList[2] = (Button)findViewById(R.id.id_b_output_mute_ch2);
        chanelBtMuteList[3] = (Button)findViewById(R.id.id_b_output_mute_ch3);
        chanelBtMuteList[4] = (Button)findViewById(R.id.id_b_output_mute_ch4);
        chanelBtMuteList[5] = (Button)findViewById(R.id.id_b_output_mute_ch5);
        chanelBtMuteList[6] = (Button)findViewById(R.id.id_b_output_mute_ch6);
        chanelBtMuteList[7] = (Button)findViewById(R.id.id_b_output_mute_ch7);

        for (int i = 0;i<DataStruct.max_channel;i++)
        {
            chanelBtMuteList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    for (int j =0;j<DataStruct.max_channel;j++)
                    {
                        if (id == chanelBtMuteList[j].getId())
                        {
                           DataStruct.chanelMute[j] = !(DataStruct.chanelMute[j]);
                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_chanleVolume);
                            if (DataStruct.chanelMute[j])
                            {
                                chanelBtMuteList[j].setBackgroundResource(R.drawable.output_mute_off);
                            }else
                            {
                                chanelBtMuteList[j].setBackgroundResource(R.drawable.output_mute_on);
                            }
                        }
                    }
                }
            });
        }

        polarBtList = new Button[DataStruct.max_channel];
        polarBtList[0] = (Button)findViewById(R.id.id_b_output_polar_ch0);
        polarBtList[1] = (Button)findViewById(R.id.id_b_output_polar_ch1);
        polarBtList[2] = (Button)findViewById(R.id.id_b_output_polar_ch2);
        polarBtList[3] = (Button)findViewById(R.id.id_b_output_polar_ch3);
        polarBtList[4] = (Button)findViewById(R.id.id_b_output_polar_ch4);
        polarBtList[5] = (Button)findViewById(R.id.id_b_output_polar_ch5);
        polarBtList[6] = (Button)findViewById(R.id.id_b_output_polar_ch6);
        polarBtList[7] = (Button)findViewById(R.id.id_b_output_polar_ch7);


        for (int i = 0;i<DataStruct.max_channel;i++)
        {
            polarBtList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    for (int j =0;j<DataStruct.max_channel;j++)
                    {
                        if (id == polarBtList[j].getId())
                        {
                            if (DataStruct.polar[j]==0)
                            {
                                DataStruct.polar[j]=1;
                                //polarBtList[j].setBackgroundResource(R.drawable.output_polar_n);
                                polarBtList[j].setText(R.string.Polar_N);
                            }else
                            {
                                DataStruct.polar[j]=0;
                                //polarBtList[j].setBackgroundResource(R.drawable.output_polar_p);
                                polarBtList[j].setText(R.string.Polar_P);
                            }

                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_chanleVolume);
                        }
                    }
                }
            });
        }


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
        registerReceiver(receiver, share.getIntent_ui_chanel_Filter());
    }
    private void flashUIFromProtocol(int sieProtocolValue)
    {

        if (sieProtocolValue == DataStruct.sieProtocol.prtc_chanleVolume)
        {
            for (int i =0;i<8;i++)
            {
                chanel_r_bt_list[i].setBarinit(0,60,DataStruct.chanelLastVolume[i]);
                if (DataStruct.polar[i]==0)
                {
                    polarBtList[i].setText(R.string.Polar_P);
                }else
                {
                    polarBtList[i].setText(R.string.Polar_N);
                }

            }
        }

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
                    flashUIFromProtocol(msg.arg1);
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
            MyLog.v(TAG,"chanel");
            if (share.SIE_UI_ACTION_CHANEL_VOLUME.equals(action)) {
                // 连接成功
                msg.what=1;
                msg.arg1=DataStruct.sieProtocol.prtc_chanleVolume;
            }

            myHandler.sendMessage(msg);

        }

    };


}
