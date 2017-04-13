package sie.amplifier_conctroller.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_fq_dv extends AppCompatActivity {

    final String TAG = dsp_setting_delay.class.getSimpleName();
    private FEShare share = FEShare.getInstance();
    Sie_app_data_share sie_data_share;

    Button [] buttonsFreLowList;
    Button [] buttonsFreHightList;

    Dialog dialogFrq;
    View dialogView;
    Button buttonSure;
    Button buttonSub;
    Button buttonInc;
    SeekBar seekBarInDialog;
    int curSeekbarProgress = 0;
    TextView tvFreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xover);
        initfq();
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
        registerReceiver(receiver, share.getIntent_ui_fq_dv_Filter());
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
            if (share.SIE_UI_ACTION_CHANLE_FRE.equals(action)) {
                // 连接成功
                msg.what=1;
                msg.arg1= DataStruct.sieProtocol.prtc_chanleFreFilter;
            }

            myHandler.sendMessage(msg);

        }

    };

    private void initfq()
    {
        buttonsFreHightList = new Button[DataStruct.max_channel];
        buttonsFreHightList[0] = (Button)findViewById(R.id.id_b_hp_freq_ch0);
        buttonsFreHightList[1] = (Button)findViewById(R.id.id_b_hp_freq_ch1);
        buttonsFreHightList[2] = (Button)findViewById(R.id.id_b_hp_freq_ch2);
        buttonsFreHightList[3] = (Button)findViewById(R.id.id_b_hp_freq_ch3);
        buttonsFreHightList[4] = (Button)findViewById(R.id.id_b_hp_freq_ch4);
        buttonsFreHightList[5] = (Button)findViewById(R.id.id_b_hp_freq_ch5);
        buttonsFreHightList[6] = (Button)findViewById(R.id.id_b_hp_freq_ch6);
        buttonsFreHightList[7] = (Button)findViewById(R.id.id_b_hp_freq_ch7);

        buttonsFreLowList = new Button[DataStruct.max_channel];
        buttonsFreLowList[0] = (Button)findViewById(R.id.id_b_lp_freq_ch0);
        buttonsFreLowList[1] = (Button)findViewById(R.id.id_b_lp_freq_ch1);
        buttonsFreLowList[2] = (Button)findViewById(R.id.id_b_lp_freq_ch2);
        buttonsFreLowList[3] = (Button)findViewById(R.id.id_b_lp_freq_ch3);
        buttonsFreLowList[4] = (Button)findViewById(R.id.id_b_lp_freq_ch4);
        buttonsFreLowList[5] = (Button)findViewById(R.id.id_b_lp_freq_ch5);
        buttonsFreLowList[6] = (Button)findViewById(R.id.id_b_lp_freq_ch6);
        buttonsFreLowList[7] = (Button)findViewById(R.id.id_b_lp_freq_ch7);

        for (int i = 0;i<DataStruct.max_channel;i++)
        {
            buttonsFreHightList[i].setText(""+DataStruct.freDivHight[i]+"Hz");
            buttonsFreLowList[i].setText(""+DataStruct.freDivLow[i]+"Hz");

            buttonsFreHightList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int j = 0;j<DataStruct.max_channel;j++)
                    {
                        if (v.getId()==buttonsFreHightList[j].getId())
                        {
                            MyLog.v(TAG,"show?");
                            curSeekbarProgress = DataStruct.freDivHight[j];
                            dialogFrq.show();
                        }
                    }
                }
            });
            buttonsFreLowList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0;j<DataStruct.max_channel;j++)
                    {
                        if (v.getId()==buttonsFreLowList[j].getId())
                        {
                            MyLog.v(TAG,"show?");
                            dialogFrq.show();
                        }
                    }
                }
            });
        }

        dialogFrq = new Dialog(this);
        dialogView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_user_data_dialog, (ViewGroup)findViewById(R.id.LinearLayout_userdata_dialog));
        dialogFrq.setContentView(dialogView);
        dialogFrq.setCanceledOnTouchOutside(false);
        dialogFrq.setTitle(getResources().getText(R.string.Frequency));
        tvFreq = (TextView)dialogView.findViewById(R.id.tv_progress) ;
        buttonInc = (Button)dialogView.findViewById(R.id.id_b_fre_inc);
        buttonSub = (Button)dialogView.findViewById(R.id.id_b_fre_sub);
        buttonSure = (Button)dialogView.findViewById(R.id.id_b_userdata_ok);
        seekBarInDialog = (SeekBar)dialogView.findViewById(R.id.progress_in_dialog);
        seekBarInDialog.setMax(20000);

        seekBarInDialog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curSeekbarProgress = progress;
                tvFreq.setText(""+curSeekbarProgress+"Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curSeekbarProgress++;
                updateDialog();
            }
        });

        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curSeekbarProgress++;
                updateDialog();
            }
        });

        buttonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFrq.cancel();

            }
        });


    }

    void updateDialog()
    {
        seekBarInDialog.setProgress(curSeekbarProgress);
        tvFreq.setText(""+curSeekbarProgress+"Hz");
    }
}
