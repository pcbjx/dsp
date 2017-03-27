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
        if (U0HeadFlg == 0) {
            if ((paramInt1 == DataStruct.HEAD_DATA) && (U0HeadCnt == 0))
            {
                U0HeadCnt += 1;
                U0DataCnt = 0;
            }
        }
        do
        {
            do
            {
                do
                {
                    if ((paramInt1 == DataStruct.HEAD_DATA) && (U0HeadCnt == 1))
                    {
                        U0HeadCnt += 1;
                        break;
                    }
                    if ((paramInt1 == DataStruct.HEAD_DATA) && (U0HeadCnt == 2))
                    {
                        U0HeadCnt += 1;
                        break;
                    }
                    if ((paramInt1 == 0x57) && (U0HeadCnt == 3))
                    {
                        U0HeadFlg = 1;
                        U0HeadCnt = 0;
                        break;
                    }
                    U0HeadCnt = 0;
                    break;
                } while (U0HeadFlg != 1);
                U0HeadCnt = 0;
                RcvDeviceData.DataBuf[U0DataCnt] = paramInt1;
                U0DataCnt += 1;
            } while (U0DataCnt < RcvDeviceData.DataBuf[8] + RcvDeviceData.DataBuf[9] * 256 + 16 - 4);
            RcvDeviceData.FrameType = RcvDeviceData.DataBuf[0];
            RcvDeviceData.DeviceID = RcvDeviceData.DataBuf[1];
            RcvDeviceData.UserID = RcvDeviceData.DataBuf[2];
            RcvDeviceData.DataType = RcvDeviceData.DataBuf[3];
            RcvDeviceData.ChannelID = RcvDeviceData.DataBuf[4];
            RcvDeviceData.DataID = RcvDeviceData.DataBuf[5];
            RcvDeviceData.PCFadeInFadeOutFlg = RcvDeviceData.DataBuf[6];
            RcvDeviceData.PcCustom = RcvDeviceData.DataBuf[7];
            RcvDeviceData.DataLen = (RcvDeviceData.DataBuf[8] + RcvDeviceData.DataBuf[9] * 256);
            RcvDeviceData.CheckSum = RcvDeviceData.DataBuf[(RcvDeviceData.DataLen + 16 - 6)];
            RcvDeviceData.FrameEnd = RcvDeviceData.DataBuf[(RcvDeviceData.DataLen + 16 - 5)];
            U0HeadFlg = 0;
            U0DataCnt = 0;
        } while (RcvDeviceData.FrameEnd != 170);
        int i = 0;
        paramInt1 = 0;
        for (;;)
        {
            if (paramInt1 >= RcvDeviceData.DataLen + 16 - 6)
            {
                if (i != RcvDeviceData.CheckSum) {
                    break;
                }

                //PcConnectFlg = 1;
                //PcConnectCnt = 0;
                //ComType = paramInt2;
                //ProcessRcvData();

                return;
            }
            i ^= RcvDeviceData.DataBuf[paramInt1];
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
        rThread = new Thread(rRunnable);
        rThread.start();
        sThread = new Thread(sRunnable);
        sThread.start();
        tThread = new Thread(tRunnable);
        tThread.start();
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
