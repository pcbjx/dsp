package sie.amplifier_conctroller;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import java.util.zip.CRC32;

import sie.amplifier_conctroller.DataStruct.*;

import static sie.amplifier_conctroller.DataStruct.DataStruct.EQList;


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
                readData();
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
                                MyLog.v(TAG,"read data:");

                                if (j <= 0) {
                                    continue;
                                }
                                i = 0;
                                while (i < j)
                                {
                                    MyLog.v(TAG, String.format("%02x ", arrayOfByte[i]));
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

    /*思翼协议格式
        * head (2 BYTE)：DE 57
        * data length (2 BYTE):data[2]*0xff+data3
        * mode(1byte):0x04
        * data:
        * crc = 0xFF-sum(data)
        * */
    public void ReceiveDataFromDevice(int paramInt1, int paramInt2)
    {
        MyLog.v(TAG,"ReceiveDataFromDevice");
        if (U0HeadFlg == 0) {
            if ((paramInt1 == DataStruct.HEAD_DATA) && (U0HeadCnt == 0))
            {
                U0HeadCnt += 1;//sign find first head
                U0DataCnt = 0;
                return;
            }
            if ((paramInt1 == 0x57) && (U0HeadCnt == 1))
            {
                U0HeadFlg = 1;//sign find full head
                U0HeadCnt = 0;
            }
            U0HeadCnt = 0;
            //丢弃协议头以前的数据
            return;
        }

        if (U0HeadFlg==1) {
            RcvDeviceData.DataBuf[U0DataCnt] = paramInt1;
            U0DataCnt += 1;//U0DateCnt 表示找到头以后的有效数据、包括长度，mode+data+check sum的计算
            if (U0DataCnt == 2)//记录长度
            {
                RcvDeviceData.DataLen = (RcvDeviceData.DataBuf[0]*0xff + RcvDeviceData.DataBuf[1]);
            }else if (U0DataCnt >2 ){
                if (U0DataCnt >= RcvDeviceData.DataLen+4) {
                    //理论接完了，清楚标记
                    U0HeadFlg = 0;

                    RcvDeviceData.CheckSum = RcvDeviceData.DataBuf[(U0DataCnt-1)];
                    RcvDeviceData.DataType = RcvDeviceData.DataBuf[2];//mode
                    RcvDeviceData.DataID = RcvDeviceData.DataBuf[3];//para0
                    U0DataCnt = 0;

                    byte sum = 0;
                    int i = 0;
                    for (;;)
                    {
                        if (i >= RcvDeviceData.DataLen)
                        {
                            int tmp = 255 -sum;
                            if (tmp != RcvDeviceData.CheckSum) {
                                //非法数据
                                MyLog.e(TAG,"error command");

                            }else
                            {
                                MyLog.v(TAG,"find command");
                                ProcessRcvData();

                            }

                            return;
                        }
                        sum = (byte) (sum + RcvDeviceData.DataBuf[3+i]);//数据区
                        MyLog.v(TAG, String.format("%02x ", RcvDeviceData.DataBuf[3 + i]));
                        i += 1;
                    }
                }
            }

        }

    }

    private void ProcessRcvData()
    {
        String action = share.SIE_UI_ACTION_non;
        switch (RcvDeviceData.DataID)
        {
            case 0x01:
                MyLog.v(TAG,"Input channel");
                action = share.SIE_UI_ACTION_INPUTCHANNEL;
                m_dateStruct.input_source = RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1];
                break;
            case 0x02:
                MyLog.v(TAG,"Output channel");
                action = share.SIE_UI_ACTION_OUTPUTCHANNEL;
                break;
            case 0x03:
                MyLog.v(TAG,"EQ PRE");
                action = share.SIE_UI_ACTION_EQ_PREPARE;
                break;
            case 0x04:
                MyLog.v(TAG,"8 EQ");
                action = share.SIE_UI_ACTION_EQ_8;
                break;
            case 0x05:
                MyLog.v(TAG,"31 EQ");
                action = share.SIE_UI_ACTION_EQ_31;
                for (int i = 0;i<31;i++)
                {
                    DataStruct.EQList.InEQ[i].level = RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1+i];
                }
                break;
            case 0x06:
                MyLog.v(TAG,"EQ Bandwidth");
                action = share.SIE_UI_ACTION_EQ_Bandwidth;
                DataStruct.EQList.InQ_value = RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1]*255;
                DataStruct.EQList.InQ_value += RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+2];
                break;
            case 0x07:
                MyLog.v(TAG,"SIE_UI_ACTION_CHANEL_DELAY");
                action = share.SIE_UI_ACTION_CHANEL_DELAY;
                //DataStruct.DataStructBuf.IN_CH[0].delay =
                break;
            case 0x08:
                MyLog.v(TAG,"SIE_UI_ACTION_MAINVOLUME");
                action = share.SIE_UI_ACTION_MAINVOLUME;
                m_dateStruct.main_vol = RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1];
                break;
            case 0x09:
                MyLog.v(TAG,"SIE_UI_ACTION_CHANEL_VOLUME");
                action = share.SIE_UI_ACTION_CHANEL_VOLUME;
                for (int i = 0;i<DataStruct.max_channel;i++)
                {
                    m_dateStruct.chanelLastVolume[i] = (byte) ((RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1+i])&0x7f);//拿掉最高位
                    m_dateStruct.polar[i] = (byte)  ((RcvDeviceData.DataBuf[DataStruct.DATA_START_POS+1+i])>>7);//取最高位
                }

                break;
            case 0x0a:
                MyLog.v(TAG,"SIE_UI_ACTION_CHANLE_FRE");
                action = share.SIE_UI_ACTION_CHANLE_FRE;
                break;
            case 0x0b:
                MyLog.v(TAG,"SIE_UI_ACTION_DEEP_BASS");
                action = share.SIE_UI_ACTION_DEEP_BASS;
                break;
            case 0x0c:
                MyLog.v(TAG,"SIE_UI_ACTION_WIDESOUND");
                action = share.SIE_UI_ACTION_WIDESOUND;
                break;
            case 0x40:
                MyLog.v(TAG,"EQ Bandwidth");
                action = share.SIE_UI_ACTION_INPUTCHANNEL;
                break;
            default:
                break;

        }

        Intent intent = new Intent(action);
        intent.putExtra("msg", "hello receiver.");
        sendBroadcast(intent);
    }

    public int sendSieData(byte protocol)
    {
        byte []sendbuf;
        switch (protocol)
        {
            case 0x01 :
                sendbuf = new byte[]{DataStruct.sieProtocol.prtc_inputChanel,(byte)0x00};
                sendbuf[1] = (byte) DataStruct.input_source;
                share.sie_write(sendbuf,sendbuf.length);
                break;
            case 0x02 :
                break;
            case 0x03 :
                break;
            case 0x04 :
                break;
            case 0x05 :
                break;
            case 0x06 :
                break;
            case 0x07 :
                break;
            case 0x08 :
                sendbuf = new byte[]{DataStruct.sieProtocol.prtc_mainVolume,(byte)0x00};
                sendbuf[1] = (byte) DataStruct.main_vol;
                share.sie_write(sendbuf,sendbuf.length);
                break;
            case 0x09 :
                sendbuf = new byte[DataStruct.max_channel+1];
                sendbuf[0] =  DataStruct.sieProtocol.prtc_chanleVolume;
                for (int i=0;i<DataStruct.max_channel;i++)
                {
                    if(!(DataStruct.chanelMute[i]))
                    {
                        byte tmp = (byte)(DataStruct.polar[i]<<7);
                        sendbuf[i+1] =(byte) (DataStruct.chanelLastVolume[i]+tmp);
                    }
                    else
                    {
                        sendbuf[i+1] = 0;
                    }
                }

                share.sie_write(sendbuf,sendbuf.length);
                break;
            case 0x0a :
                break;
            case 0x0b :
                break;
            case 0x0c :
                break;
            case 0x40 :
                sendbuf = new byte[]{(byte)0x40,(byte)0x00};
                sendbuf[1] = DataStruct.otherCMD;
                share.sie_write(sendbuf,sendbuf.length);
                break;
            default:
                break;
        }


        return 0;

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
