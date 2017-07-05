package sie.amplifier_conctroller.ui;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.TabActivity;
import com.feasycom.s_port.about.AboutActivity;
import com.feasycom.s_port.model.MyLog;

import common.zhang.customer.MyToolBar;
import common.zhang.customer.MyToolBar.MyToolBarClickListener;
import common.zhang.customer.RotateButtom;
import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;


import android.widget.SeekBar.OnSeekBarChangeListener;


import java.util.ArrayList;

public class dsp_main extends Activity implements View.OnClickListener,  OnSeekBarChangeListener {

    final String TAG = dsp_main.class.getSimpleName();

    private FEShare share = FEShare.getInstance();

    int UserGroup = 1;
    int InputSRC = 1;
    int Outout_mode= 1;

    Sie_app_data_share sie_data_share;

    private boolean isconnect = false;
    private MyToolBar myToolBar;// 自定义toolbar

    TextView connect_state;
    TextView tv_bass_vol;

    private ImageView[] B_UGbg = new ImageView[6];
    private Button[] B_UserGroup = new Button[6];
    private Button[] B_InputSrcChanel = new Button[6];
    private Button[] B_Output_mode = new Button[2];

    private Button bt_Hw_mute;
    Button bt_vol_setting;
    RotateButtom main_volue_r_bt;
    SeekBar bass_seekbar;
    Button bt_titile_right;

    Button bt_getData;


    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    private Handler mhandler = new Handler();

    private void flashUIFromProtocol(int sieProtocolValue)
    {

        if (sieProtocolValue == DataStruct.sieProtocol.prtc_inputChanel)
        {
            InputSRC  = DataStruct.input_source-1;
            FlashInputSrcSel();
        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_outputChanel)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_eq_32_preStyle)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_eq8)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_eq32)
        {

        }

        if (sieProtocolValue == DataStruct.sieProtocol.prtc_eqBandWidth)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_delay)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_mainVolume)
        {
            MyLog.i(TAG, "得到音量:"+DataStruct.main_vol);

            main_volue_r_bt.setBarinit(0,60,DataStruct.main_vol);
            tv_bass_vol.setText(""+DataStruct.main_vol);
        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_chanleVolume)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_chanleFreFilter)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_deepBass)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_wideSoud)
        {

        }
        if (sieProtocolValue == DataStruct.sieProtocol.prtc_other)
        {
            byte otherCmd = DataStruct.otherCMD;

            switch (otherCmd)
            {

            }
        }

    }

    private Handler myHandler = new Handler()
    {
        // 2.重写消息处理函数
        public void handleMessage(Message msg)
        {
            int sieProtocolValueWhat = msg.what;
            int sieProtocolValue = msg.arg1;


            switch (msg.what)
            {
                // 判断发送的消息
                case 0:
                {
                    // 更新View
                    MyLog.i(TAG, "状态："+msg.arg1);
                    isconnect = false;
                    myToolBar.updateRightButton(msg.arg1);

                    if (msg.arg1 == 1)
                    {
                        isconnect = true;
                        DataStruct.otherCMD = 0;
                        sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_other);
                    }

                    break;
                }
                case 1:
                {
                    flashUIFromProtocol(msg.arg1);
                    break;
                }
                case 2:
                {
                    // 更新View
                    //MyLog.i("连接", "失败");
                    isconnect = false;
                    break;
                }

            }

            super.handleMessage(msg);
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsp_main);

        sie_data_share = (Sie_app_data_share)getApplication();

        init();
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
        registerReceiver(receiver, share.getIntent_ui_mian_Filter());
    }


    /**
     * @Title: init
     * @Description: TODO(初始化UI控件)
     * @param
     * @return void
     * @throws
     */
    private void init()
    {
        titile_init();

        tv_bass_vol = (TextView) findViewById(R.id.vol_tips);
        bass_seekbar = (SeekBar) findViewById(R.id.seekBar_bass_volume);
        bass_seekbar.setOnSeekBarChangeListener(this);
        bass_seekbar.setMax(60);
        bass_seekbar.setProgress(10);
        bt_vol_setting = (Button)findViewById(R.id.bt_setting);
        bt_vol_setting.setOnClickListener(this);
        share.context = this;
        share.init();

        if (!share.bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.on)
                    , Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getApplicationContext(), getResources().getText(R.string.bt_turned_on),
                  //  Toast.LENGTH_SHORT).show();
        }

        bt_Hw_mute = (Button)findViewById(R.id.bt_Output_Mute);
        bt_Hw_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataStruct.HW_MUTE)
                {
                    DataStruct.HW_MUTE = false;
                    bt_Hw_mute.setBackgroundResource(R.drawable.btn_normal);
                }else
                {
                    DataStruct.HW_MUTE = true;
                    bt_Hw_mute.setBackgroundResource(R.drawable.btn_press);
                }

                sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_mainVolume);

            }
        });

        main_volue_r_bt = (RotateButtom)findViewById(R.id.main_volume);
        main_volue_r_bt.set_titile(getResources().getText(R.string.MainValume).toString());
        main_volue_r_bt.setBarinit(0,60,DataStruct.main_vol);
        main_volue_r_bt.setOnTempChangeListener(new RotateButtom.OnTempChangeListener()
        {
            @Override
            public void change(View v, int temp) {

                DataStruct.main_vol = temp;
                sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_mainVolume);
            }

        });

        B_UserGroup[0] = ((Button)findViewById(R.id.id_b_1));
        B_UserGroup[1] = ((Button)findViewById(R.id.id_b_2));
        B_UserGroup[2] = ((Button)findViewById(R.id.id_b_3));
        B_UserGroup[3] = ((Button)findViewById(R.id.id_b_4));
        B_UserGroup[4] = ((Button)findViewById(R.id.id_b_5));
        B_UserGroup[5] = ((Button)findViewById(R.id.id_b_6));

        B_InputSrcChanel[0] = ((Button)findViewById(R.id.input_src_channel1));
        B_InputSrcChanel[1] = ((Button)findViewById(R.id.input_src_channel2));
        B_InputSrcChanel[2] = ((Button)findViewById(R.id.input_src_channel3));
        B_InputSrcChanel[3] = ((Button)findViewById(R.id.input_src_channel4));

        B_Output_mode[0] = (Button)findViewById(R.id.bt_Output_channel1);
        B_Output_mode[1] = (Button)findViewById(R.id.bt_Output_channel2);


        for (int i = 0;i<6;i++)
        {
            //B_UserGroup[i].setOnClickListener(groupBtClickistener);
            if(i>2)//前三个为机器固话，不许修改
            {
                B_UserGroup[i].setOnLongClickListener(groupBtonLongClicklistener);
            }
            B_UserGroup[i].setOnClickListener(groupBtClickistener);

        }

        if(DataStruct.otherCMD>=0x11&&DataStruct.otherCMD<0x21)
        {
            UserGroup = DataStruct.otherCMD - 0x11;
        }else
        if(DataStruct.otherCMD>=0x21&&DataStruct.otherCMD<0x30)
        {
            UserGroup = DataStruct.otherCMD - 0x21+3;
        }
        FlashUserGroupSel();

        for (int i = 0;i<4;i++)
        {
            B_InputSrcChanel[i].setOnClickListener(InputSrcBtClickistener);
        }
        InputSRC = DataStruct.input_source - 1;
        FlashInputSrcSel();

        for (int i = 0;i<2;i++)
        {
            B_Output_mode[i].setOnClickListener(Output_mode_BtClickistener);
        }
        Outout_mode = DataStruct.Output_mode - 1;
        FlashOutputSel();

        bt_getData = (Button)findViewById(R.id.bt_get_data_test);
        bt_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStruct.otherCMD = 0;
                sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_other);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                share.auto_connect_bt();
            }
        }).start();

    }


    View.OnClickListener InputSrcBtClickistener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            FlashInputSrcNoSel();
            for (int j = 0;j<4;j++)
            {
                if(id == B_InputSrcChanel[j].getId())
                {
                    InputSRC = j;
                    Log.v(TAG,"Group click:"+InputSRC);
                    DataStruct.input_source = InputSRC +1;
                    sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_inputChanel);
                    FlashInputSrcSel();
                }
            }
        }
    };

    View.OnClickListener Output_mode_BtClickistener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            FlashOutputNoSel();
            for (int j = 0;j<2;j++)
            {
                if(id == B_Output_mode[j].getId())
                {
                    Outout_mode = j;
                    Log.v(TAG,"Outout_mode click:"+Outout_mode);
                    DataStruct.Output_mode = Outout_mode +1;
                    sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_outputChanel);
                    FlashOutputSel();
                }
            }
        }
    };




    View.OnClickListener groupBtClickistener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            FlashUserGroupNoSel();
            for (byte j = 0;j<6;j++)
            {
                if(id == B_UserGroup[j].getId())
                {
                    UserGroup = j;
                    Log.v(TAG,"Group click:"+UserGroup);
                    if (j<3)
                    {
                        DataStruct.otherCMD = (byte)(0x11+j) ;
                    }else
                    {
                        DataStruct.otherCMD = (byte)(0x21+j-3) ;
                    }

                    sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_other);
                    FlashUserGroupSel();
                }
            }
        }
    };

    View.OnLongClickListener groupBtonLongClicklistener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v) {
            int id = v.getId();

            FlashUserGroupNoSel();
            for (int j = 0;j<6;j++)
            {
                if(id == B_UserGroup[j].getId())
                {

                    UserGroup = j;
                    Log.v(TAG,"Group long click:"+UserGroup);

                    if (j>=3)
                    {
                        DataStruct.otherCMD = (byte)(0x31+j-3) ;
                    }

                    sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_other);
                    FlashUserGroupSel();

                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.save_cur_data_tips)
                            , Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }
    };


    void FlashUserGroupNoSel()
    {
        B_UserGroup[0].setBackgroundResource(R.drawable.use_group_1_normal);
        B_UserGroup[1].setBackgroundResource(R.drawable.use_group_2_normal);
        B_UserGroup[2].setBackgroundResource(R.drawable.use_group_3_normal);
        B_UserGroup[3].setBackgroundResource(R.drawable.use_group_4_normal);
        B_UserGroup[4].setBackgroundResource(R.drawable.use_group_5_normal);
        B_UserGroup[5].setBackgroundResource(R.drawable.use_group_6_normal);

    }

    void FlashUserGroupSel()
    {
        FlashUserGroupNoSel();
        switch (UserGroup)
        {
            case 0:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_1_press);
                break;
            case 1:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_2_press);
                break;
            case 2:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_3_press);
                break;
            case 3:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_4_press);
                break;
            case 4:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_5_press);
                break;
            case 5:
                B_UserGroup[UserGroup].setBackgroundResource(R.drawable.use_group_6_press);
                break;

            default:
                break;


        }

    }


    void FlashInputSrcNoSel()
    {
        B_InputSrcChanel[0].setBackgroundResource(R.drawable.btn_normal);
        B_InputSrcChanel[1].setBackgroundResource(R.drawable.btn_normal);
        B_InputSrcChanel[2].setBackgroundResource(R.drawable.btn_normal);
        B_InputSrcChanel[3].setBackgroundResource(R.drawable.btn_normal);

    }

    void FlashInputSrcSel()
    {
        FlashInputSrcNoSel();
        switch (InputSRC)
        {
            case 0:
                B_InputSrcChanel[InputSRC].setBackgroundResource(R.drawable.btn_press);
                break;
            case 1:
                B_InputSrcChanel[InputSRC].setBackgroundResource(R.drawable.btn_press);
                break;
            case 2:
                B_InputSrcChanel[InputSRC].setBackgroundResource(R.drawable.btn_press);
                break;
            case 3:
                B_InputSrcChanel[InputSRC].setBackgroundResource(R.drawable.btn_press);
                break;

            default:
                break;


        }

    }

    void FlashOutputNoSel()
    {
        B_Output_mode[0].setBackgroundResource(R.drawable.btn_normal);
        B_Output_mode[1].setBackgroundResource(R.drawable.btn_normal);
    }

    void FlashOutputSel()
    {
        FlashOutputNoSel();
        switch (Outout_mode)
        {
            case 0:
                B_Output_mode[Outout_mode].setBackgroundResource(R.drawable.btn_press);
                break;
            case 1:
                B_Output_mode[Outout_mode].setBackgroundResource(R.drawable.btn_press);
                break;

            default:
                break;


        }

    }


    /**
     * @Title: titile_init
     * @Description: TODO(初始化UI控件)
     * @param
     * @return void
     * @throws
     *
     */
    public void titile_init()
    {
        myToolBar = (MyToolBar) findViewById(R.id.myToolBar);
        // 设置左边右边的按钮是否显示
        myToolBar.setToolBarBtnVisiable(false, true);
        // 设置是否显示中间标题，默认的是显示
        myToolBar.setToolBarTitleVisible(R.string.title_main,true);

        myToolBar.updateRightButton(share.connect_state);

        /*
		 * toolbar的点击事件处理
		 */
        myToolBar.setOnMyToolBarClickListener(new MyToolBarClickListener() {

            @Override
            public void rightBtnClick() {// 右边按钮点击事件
                if((share.connect_state==2))//未连接跳转
                {
                    final Intent deviceIntent = new Intent(dsp_main.this, TabActivity.class);
                    startActivity(deviceIntent);
                }else if ((share.connect_state==1))
                {
                    final Intent deviceIntent = new Intent(dsp_main.this, AboutActivity.class);
                    startActivity(deviceIntent);
                }

            }

            @Override
            public void leftBtnClick() {// 左边按钮点击事件
                //Toast.makeText(dsp_main.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                final String action = intent.getAction();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                if (share.SIE_UI_ACTION_MAINVOLUME.equals(action)) {
                    // 连接成功
                        msg.what=1;
                        msg.arg1=DataStruct.sieProtocol.prtc_mainVolume;
                    } else if (share.SIE_UI_ACTION_INPUTCHANNEL.equals(action)) {
                        msg.what=1;
                        msg.arg1=DataStruct.sieProtocol.prtc_inputChanel;
                    }
                    else if (share.SIE_UI_ACTION_DEEP_BASS.equals(action)) {
                        msg.what=1;
                        msg.arg1=DataStruct.sieProtocol.prtc_deepBass;
                    }
                    else if (share.SIE_UI_ACTION_OTHER.equals(action)) {
                        msg.what=1;
                        msg.arg1=DataStruct.sieProtocol.prtc_other;
                    }
                    else if (share.SIE_UI_ACTION_BT_CHANGE.equals(action)) {
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.bt_setting:
                final Intent intent = new Intent(dsp_main.this,
                        dsp_setting.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_bass_vol.setText(""+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}
