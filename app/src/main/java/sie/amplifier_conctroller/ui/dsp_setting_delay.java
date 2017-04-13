package sie.amplifier_conctroller.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import common.zhang.customer.VerticalSeekBar;
import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_delay extends Activity {

    final String TAG = dsp_setting_delay.class.getSimpleName();
    private FEShare share = FEShare.getInstance();
    Sie_app_data_share sie_data_share;

    Button [] buttonMinusList;
    Button [] buttonPlusList;
    Button [] buttonsTxtShow;

    VerticalSeekBar [] verticalSeekBardelayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delaysettings);
        sie_data_share = (Sie_app_data_share)getApplication();
        initDelay();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //解除广播接收器
       // unregisterReceiver(receiver);
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume()
    {
        super.onResume();
      //  registerReceiver(receiver, share.getIntent_ui_delay_Filter());
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
            if (share.SIE_UI_ACTION_CHANEL_DELAY.equals(action)) {
                // 连接成功
                msg.what=1;
                msg.arg1= DataStruct.sieProtocol.prtc_delay;
            }

            myHandler.sendMessage(msg);

        }

    };


    public void initDelay()
    {
        verticalSeekBardelayList = new VerticalSeekBar[DataStruct.max_channel];
        buttonMinusList = new Button[DataStruct.max_channel];
        buttonPlusList = new Button[DataStruct.max_channel];
        buttonsTxtShow = new Button[DataStruct.max_channel];

        verticalSeekBardelayList[0] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch0);
        verticalSeekBardelayList[1] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch1);
        verticalSeekBardelayList[2] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch2);
        verticalSeekBardelayList[3] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch3);
        verticalSeekBardelayList[4] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch4);
        verticalSeekBardelayList[5] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch5);
        verticalSeekBardelayList[6] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch6);
        verticalSeekBardelayList[7] = (VerticalSeekBar)findViewById(R.id.id_sb_delay_ch7);

        buttonMinusList[0] = (Button)findViewById(R.id.id_button_delay_sub_ch0);
        buttonMinusList[1] = (Button)findViewById(R.id.id_button_delay_sub_ch1);
        buttonMinusList[2] = (Button)findViewById(R.id.id_button_delay_sub_ch2);
        buttonMinusList[3] = (Button)findViewById(R.id.id_button_delay_sub_ch3);
        buttonMinusList[4] = (Button)findViewById(R.id.id_button_delay_sub_ch4);
        buttonMinusList[5] = (Button)findViewById(R.id.id_button_delay_sub_ch5);
        buttonMinusList[6] = (Button)findViewById(R.id.id_button_delay_sub_ch6);
        buttonMinusList[7] = (Button)findViewById(R.id.id_button_delay_sub_ch7);

        buttonPlusList[0] = (Button)findViewById(R.id.id_button_delay_inc_ch0);
        buttonPlusList[1] = (Button)findViewById(R.id.id_button_delay_inc_ch1);
        buttonPlusList[2] = (Button)findViewById(R.id.id_button_delay_inc_ch2);
        buttonPlusList[3] = (Button)findViewById(R.id.id_button_delay_inc_ch3);
        buttonPlusList[4] = (Button)findViewById(R.id.id_button_delay_inc_ch4);
        buttonPlusList[5] = (Button)findViewById(R.id.id_button_delay_inc_ch5);
        buttonPlusList[6] = (Button)findViewById(R.id.id_button_delay_inc_ch6);
        buttonPlusList[7] = (Button)findViewById(R.id.id_button_delay_inc_ch7);


        buttonsTxtShow[0] = (Button)findViewById(R.id.id_b_delay_show_ch0);
        buttonsTxtShow[1] = (Button)findViewById(R.id.id_b_delay_show_ch1);
        buttonsTxtShow[2] = (Button)findViewById(R.id.id_b_delay_show_ch2);
        buttonsTxtShow[3] = (Button)findViewById(R.id.id_b_delay_show_ch3);
        buttonsTxtShow[4] = (Button)findViewById(R.id.id_b_delay_show_ch4);
        buttonsTxtShow[5] = (Button)findViewById(R.id.id_b_delay_show_ch5);
        buttonsTxtShow[6] = (Button)findViewById(R.id.id_b_delay_show_ch6);
        buttonsTxtShow[7] = (Button)findViewById(R.id.id_b_delay_show_ch7);



        for (int i =0;i<DataStruct.max_channel;i++)
        {
            verticalSeekBardelayList[i].setMax(DataStruct.delayMax);
            verticalSeekBardelayList[i].setProgress(DataStruct.delay[i]);
            verticalSeekBardelayList[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    for (int j = 0 ;j<DataStruct.max_channel;j++)
                    {
                        if (seekBar.getId() == verticalSeekBardelayList[j].getId())
                        {
                            DataStruct.delay[j] = progress;
                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_delay);
                            buttonsTxtShow[j].setText(""+DataStruct.delay[j]+getResources().getText(R.string.uS));
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            buttonPlusList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0 ;j<DataStruct.max_channel;j++)
                    {
                        if (v.getId() == buttonPlusList[j].getId())
                        {
                            DataStruct.delay[j]  = DataStruct.delay[j]+1;
                            verticalSeekBardelayList[j].setProgress(DataStruct.delay[j]);
                            buttonsTxtShow[j].setText(""+DataStruct.delay[j]+getResources().getText(R.string.uS));
                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_delay);
                        }
                    }
                }
            });
            buttonMinusList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0 ;j<DataStruct.max_channel;j++)
                    {
                        if (v.getId() == buttonMinusList[j].getId())
                        {
                            DataStruct.delay[j] = DataStruct.delay[j]-1;
                            verticalSeekBardelayList[j].setProgress(DataStruct.delay[j]);
                            buttonsTxtShow[j].setText(""+DataStruct.delay[j]+getResources().getText(R.string.uS));
                            sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_delay);
                        }
                    }
                }
            });

            buttonsTxtShow[i].setText(""+DataStruct.delay[i]+getResources().getText(R.string.uS));
        }
    }

}
