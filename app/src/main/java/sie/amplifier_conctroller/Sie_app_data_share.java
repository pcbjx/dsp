package sie.amplifier_conctroller;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import java.io.IOException;
import java.util.zip.CRC32;

import common.zhang.customer.MyToolBar;
import common.zhang.customer.RotateButtom;
import sie.amplifier_conctroller.DataStruct.*;


public class Sie_app_data_share extends Application {
    final  String TAG = "Sie_app_data_share";

    private static Sie_app_data_share instance = null;
    public DataStruct m_dateStruct;

    private CRC32 seCRC;
    private CRC32 reCRC;

    public int U0DataCnt = 0;
    public int U0HeadCnt = 0;
    public int U0HeadFlg = 0;
    public int U0RcvFrameFlg = 0;
    public int U0SendFrameFlg = 0;
    public boolean U0SynDataSucessFlg = false;

    public DataStruct RcvDeviceData = new DataStruct();

    private FEShare share = FEShare.getInstance();

    boolean isRunning = true;

    private Runnable sRunnable =new Runnable() {
        @Override
        public void run() {

        }
    };
    private Thread sThread = null;
    private Runnable tRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    private Thread tThread = null;
    private Runnable rRunnable = new Runnable() {
        @Override
        public void run() {

            if (share.isSPP) {
                MyLog.i("连接", "1");
                if (share.connect(share.device)) {
                    MyLog.i("连接", "2");
                    //连接成功
                    readData();
                } else {
                    MyLog.i("连接", "3");
                    //连接失败
                    //goBackToDo();
                }
                //停止搜索
                share.stopSearch();
            }

        }
    };
    private Thread rThread = null;

        private void readData(){

            if (share.isSPP) {
                new Thread(new Runnable() {
                    //private Scanner r = new Scanner(mmInStream);
                    @Override
                    public void run() {
                        byte[] arrayOfByte = new byte['Ơ'];
                        int i = (int)Thread.currentThread().getId();
                        System.out.println("rRunnable thread:" + i);
                        Thread.currentThread().setName("rThread");
                        for (;;)
                        {
                            if ((!isRunning) || (share.socket == null)) {
                                continue;
                            }
                            try
                            {
                                int j = share.inputStream.read(arrayOfByte);
                                reCRC.update(arrayOfByte, 0, j);
                                String strBuf = new String(arrayOfByte, 0, j);
                                MyLog.v(TAG,"read data:"+String.format("%s",strBuf));
                                if (j <= 0) {
                                    continue;
                                }
                                i = 0;
                                while (i < j)
                                {
                                    ReceiveDataFromDevice(arrayOfByte[i] & 0xFF, 1);
                                    i += 1;
                                }
                            }
                            catch (Exception localException)
                            {
                               // recvMessageClient = ("接收异常:" + localException.getMessage());
                                Message localMessage = Message.obtain();
                                localMessage.what = 1;
                                //mHandler.sendMessage(localMessage);
                            }
                        }
                    }
                }).start();
            }
        }

    public void ReceiveDataFromDevice(int paramInt1, int paramInt2)
    {
        if (this.U0HeadFlg == 0) {
            if ((paramInt1 == DataStruct.HEAD_DATA) && (this.U0HeadCnt == 0))
            {
                this.U0HeadCnt += 1;
                this.U0DataCnt = 0;
            }
        }
        do
        {
            do
            {
                do
                {
                    if ((paramInt1 == DataStruct.HEAD_DATA) && (this.U0HeadCnt == 1))
                    {
                        this.U0HeadCnt += 1;
                        break;
                    }
                    if ((paramInt1 == DataStruct.HEAD_DATA) && (this.U0HeadCnt == 2))
                    {
                        this.U0HeadCnt += 1;
                        break;
                    }
                    if ((paramInt1 == 238) && (this.U0HeadCnt == 3))
                    {
                        this.U0HeadFlg = 1;
                        this.U0HeadCnt = 0;
                        break;
                    }
                    this.U0HeadCnt = 0;
                    break;
                } while (this.U0HeadFlg != 1);
                this.U0HeadCnt = 0;
                this.RcvDeviceData.DataBuf[this.U0DataCnt] = paramInt1;
                this.U0DataCnt += 1;
            } while (this.U0DataCnt < this.RcvDeviceData.DataBuf[8] + this.RcvDeviceData.DataBuf[9] * 256 + 16 - 4);
            this.RcvDeviceData.FrameType = this.RcvDeviceData.DataBuf[0];
            this.RcvDeviceData.DeviceID = this.RcvDeviceData.DataBuf[1];
            this.RcvDeviceData.UserID = this.RcvDeviceData.DataBuf[2];
            this.RcvDeviceData.DataType = this.RcvDeviceData.DataBuf[3];
            this.RcvDeviceData.ChannelID = this.RcvDeviceData.DataBuf[4];
            this.RcvDeviceData.DataID = this.RcvDeviceData.DataBuf[5];
            this.RcvDeviceData.PCFadeInFadeOutFlg = this.RcvDeviceData.DataBuf[6];
            this.RcvDeviceData.PcCustom = this.RcvDeviceData.DataBuf[7];
            this.RcvDeviceData.DataLen = (this.RcvDeviceData.DataBuf[8] + this.RcvDeviceData.DataBuf[9] * 256);
            this.RcvDeviceData.CheckSum = this.RcvDeviceData.DataBuf[(this.RcvDeviceData.DataLen + 16 - 6)];
            this.RcvDeviceData.FrameEnd = this.RcvDeviceData.DataBuf[(this.RcvDeviceData.DataLen + 16 - 5)];
            this.U0HeadFlg = 0;
            this.U0DataCnt = 0;
        } while (this.RcvDeviceData.FrameEnd != 170);
        int i = 0;
        paramInt1 = 0;
        for (;;)
        {
            if (paramInt1 >= this.RcvDeviceData.DataLen + 16 - 6)
            {
                if (i != this.RcvDeviceData.CheckSum) {
                    break;
                }
                //this.PcConnectFlg = 1;
               // this.PcConnectCnt = 0;
                //this.ComType = paramInt2;
                //ProcessRcvData();
                return;
            }
            i ^= this.RcvDeviceData.DataBuf[paramInt1];
            paramInt1 += 1;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        m_dateStruct =  new DataStruct();


        reCRC = new CRC32();
        reCRC.reset();

    }

    public void startrev()
    {
        this.rThread = new Thread(this.rRunnable);
        this.rThread.start();
        this.sThread = new Thread(this.sRunnable);
        this.sThread.start();
        this.tThread = new Thread(this.tRunnable);
        this.tThread.start();
    }

    //监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (share.tabId != R.d.communication) return;
            final String action = intent.getAction();
//            MyLog.i("main蓝牙回调", String.valueOf(intent));
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //找到设备

            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //断开连接

            }

        }
    };

    public static Sie_app_data_share getInstance()
    {
        return instance;
    }

}
