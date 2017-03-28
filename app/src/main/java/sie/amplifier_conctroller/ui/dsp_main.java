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
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.TabActivity;
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

    private boolean isconnect = false;
    private MyToolBar myToolBar;// 自定义toolbar

    TextView connect_state;
    TextView tv_bass_vol;
    Button bt_vol_setting;
    RotateButtom main_volue_r_bt;
    SeekBar bass_seekbar;
    Button bt_titile_right;


    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    private Handler mhandler = new Handler();
    private Handler myHandler = new Handler()
    {
        // 2.重写消息处理函数
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 判断发送的消息
                case 1:
                {
                    // 更新View
                    MyLog.i(TAG, "得到音量:"+DataStruct.main_vol);
                    //main_volue_r_bt.setpos(DataStruct.main_vol);
                    main_volue_r_bt.setBarinit(0,60,DataStruct.main_vol);
                    tv_bass_vol.setText(""+DataStruct.main_vol);
                    //isconnect = true;
                    break;
                }
                case 0:
                {
                    // 更新View
                    MyLog.i("连接", "失败");
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

        Sie_app_data_share sie_data_share = (Sie_app_data_share)getApplication();

        init();
        sie_data_share.startrev();

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
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.bt_turned_on),
                    Toast.LENGTH_SHORT).show();
        }

        main_volue_r_bt = (RotateButtom)findViewById(R.id.main_volume);
        main_volue_r_bt.set_titile("Main Volume");
        main_volue_r_bt.setBarinit(0,60,10);
        main_volue_r_bt.setOnTempChangeListener(new RotateButtom.OnTempChangeListener()
        {
            @Override
            public void change(int pos) {
                Toast.makeText(dsp_main.this, pos + "°", Toast.LENGTH_SHORT).show();
                byte [] b_mainvolume ;
                b_mainvolume = new byte[2];
                b_mainvolume[0]= (byte)0x08;
                b_mainvolume[1] = (byte) pos;
                share.sie_write(b_mainvolume,2);
            }
        });



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

        /*
		 * toolbar的点击事件处理
		 */
        myToolBar.setOnMyToolBarClickListener(new MyToolBarClickListener() {

            @Override
            public void rightBtnClick() {// 右边按钮点击事件
                if(!isconnect)
                {
                    Toast.makeText(dsp_main.this, "菜单", Toast.LENGTH_SHORT).show();
                    final Intent deviceIntent = new Intent(dsp_main.this, TabActivity.class);
                    startActivity(deviceIntent);
                }else
                {

                }

            }

            @Override
            public void leftBtnClick() {// 左边按钮点击事件
                Toast.makeText(dsp_main.this, "返回", Toast.LENGTH_SHORT).show();

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
                    } else if (share.SIE_UI_ACTION_INPUTCHANNEL.equals(action)) {
                    // 断开连接
                        msg.what=0;
                    } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                            MyLog.i("蓝牙", "关闭");
                            msg.what=0;
                    }
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
