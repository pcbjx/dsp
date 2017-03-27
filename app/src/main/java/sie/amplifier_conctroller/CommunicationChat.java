package sie.amplifier_conctroller;
/**
 ┏┓　　  ┏┓
 ┏┛┻━━--━┛┻┓
 ┃　　　　　 ┃
 ┃　　 ━　　 ┃
 ┃　┳┛　┗┳  ┃
 ┃　　　　　 ┃
 ┃    ┻     ┃
 ┃　　　　　 ┃
 ┗━┓　　　┏━┛
 ┃　　　┃   神兽保佑
 ┃　　　┃   代码无BUG！
 ┃　　　┗━━━┓
 ┃　　　　 　┣┓
 ┃　　　    ┏┛
 ┗┓┓┏-━┳┓┏┛
 ┃┫┫  ┃┫┫
 ┗┻┛  ┗┻┛
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.model.MyLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CRC32;

import static com.feasycom.s_port.ShareFile.FileClass.byteToHex;
import static com.feasycom.s_port.ShareFile.FileClass.byteToString;
import static com.feasycom.s_port.ShareFile.FileClass.getFileAbsolutePath;
import static com.feasycom.s_port.ShareFile.FileClass.hexToByte;
import static com.feasycom.s_port.ShareFile.FileClass.readFile;
import static com.feasycom.s_port.ShareFile.FileClass.readSDFile;
import static com.feasycom.s_port.ShareFile.FileClass.stringToByte;
import static com.feasycom.s_port.ShareFile.FileClass.stringToHex;
import static com.feasycom.s_port.ShareFile.FileClass.writeSDFile;


/**
 * Created by yumingyue on 2016/11/7.
 */

public class CommunicationChat extends Activity {
    private final static String TAG = CommunicationChat.class.getSimpleName();

    private TextView re_byteTV, re_packageTV, se_byteTV, se_packageTV, se_CRC, re_CRC, tv_connectState;
    private EditText re_tv, se_tv, et_interval;
    private Button btn_goBack, btn_clear, btn_send, btn_sendFile;
    private Button sw_hexRe, sw_hexSe, sw_intervalSend, sw_response;
    private StringBuffer re_string;
    private int re_bytes;
    private int re_package;
    private int se_bytes;
    private int se_package;
    private long fileLength;
    private long fileSendedLength;
    private boolean isChangeData;
    private boolean isRunning;
    private byte[] byte_send;
    private byte[] fileByte;
    private boolean isRefashingReTV;
    private boolean isSendingFile;
    private boolean isChangeHex;
    private CRC32 seCRC;
    private CRC32 reCRC;

    private BluetoothGattCallback mGattCallback;

    private FEShare share = FEShare.getInstance();


    // Code to manage Service lifecycle.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_communication);
        byte_send = new byte[0];
        seCRC = new CRC32();
        seCRC.reset();
        reCRC = new CRC32();
        reCRC.reset();
        //EditText
        re_tv = (EditText) findViewById(R.id.re_tv);
        re_tv.setOnClickListener(new MyClickListener());
        se_tv = (EditText) findViewById(R.id.se_tv);
        se_tv.addTextChangedListener(myTextWatcher);
        et_interval = (EditText) findViewById(R.id.et_interval);

        //TextView
        re_byteTV = (TextView) findViewById(R.id.re_byteTV);
        re_packageTV = (TextView) findViewById(R.id.re_packageTV);
        se_byteTV = (TextView) findViewById(R.id.se_byteTV);
        se_packageTV = (TextView) findViewById(R.id.se_packageTV);
        se_CRC = (TextView) findViewById(R.id.se_CRC);
        re_CRC = (TextView) findViewById(R.id.re_CRC);
        tv_connectState = (TextView) findViewById(R.id.tv_connectState);

        //Button
        btn_goBack = (Button) findViewById(R.id.btn_goBack);
        btn_goBack.setOnClickListener(new MyClickListener());
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new MyClickListener());
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new MyClickListener());
        btn_sendFile = (Button) findViewById(R.id.btn_sendFile);
        btn_sendFile.setOnClickListener(new MyClickListener());

        //Switch
        sw_hexRe = (Button) findViewById(R.id.sw_hexRe);
        sw_hexRe.setOnClickListener(new MyClickListener());
        sw_hexSe = (Button) findViewById(R.id.sw_hexSe);
        sw_hexSe.setOnClickListener(new MyClickListener());
        sw_intervalSend = (Button) findViewById(R.id.sw_intervalSend);
        sw_intervalSend.setOnClickListener(new MyClickListener());
        sw_response = (Button) findViewById(R.id.sw_response);
        sw_response.setOnClickListener(new MyClickListener());

        re_string = new StringBuffer();
        isChangeData = false;
        isRunning = true;
        isRefashingReTV = false;
        isChangeHex = false;
        registerReceiver(receiver, share.getIntentFilter());
        reflashViewThread();
        setUpBLECallBack();
        new Thread(new Runnable() {
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
                        goBackToDo();
                    }
                    //停止搜索
                    share.stopSearch();
                }else {
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            if (task != null) {
                task.cancel();
                task = null;
            }
            share.disConnect();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_goBack: {
                    goBackToDo();
                    break;
                }
                case R.id.btn_clear: {
                    re_bytes = 0;
                    re_package = 0;
                    se_bytes = 0;
                    se_package = 0;
                    re_string = re_string.delete(0,re_string.length());
                    reCRC.reset();
                    seCRC.reset();
                    if (!isChangeData) isChangeData = true;
                    try {
                        saveSetting();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.btn_send: {
                    if (share.socket == null && share.isSPP) return;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && share.isSPP) {
                        if (!share.socket.isConnected()) return;
                    }
                    if (se_tv.getText().toString().length() < 1) return;
                    int packets = share.write(byte_send);
                    if (packets>0) {
                        seCRC.update(byte_send);
                        se_bytes += byte_send.length;
                        se_package += packets;
                        se_packageTV.setText(String.valueOf(se_package));
                        se_byteTV.setText(String.valueOf(se_bytes));
                        if (!isChangeData) isChangeData = true;
                    }else {
                    }
                    try {
                        saveSetting();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.btn_sendFile: {
                    if (share.socket == null && share.isSPP) return;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && share.isSPP) {
                        if (!share.socket.isConnected()) return;
                    }
                    if (!isSendingFile) {
                        fileLength = 0;
                        fileSendedLength = 0;
                        openSystemFile();
                    } else {
                        isSendingFile = false;
                        btn_sendFile.setText(getResources().getText(R.string.btnSendFile));
                    }
                    break;
                }
                case R.id.re_tv: {
                    //缩回键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {//isOpen若返回true，则表示输入法打开
                        //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        imm.hideSoftInputFromWindow(se_tv.getWindowToken(), 0);
                    }
                    break;
                }
                case R.id.sw_hexRe: {
                    sw_hexRe.setSelected(!sw_hexRe.isSelected());

                    if (sw_hexRe.isSelected()) {
                        sw_hexRe.setBackgroundResource(R.drawable.btn_hightlight);
                        sw_hexRe.setPadding(2,2,2,2);
                        re_tv.setText(stringToHex(re_string.toString()));
                    } else {
                        sw_hexRe.setBackgroundResource(R.drawable.btn_default);
                        sw_hexRe.setPadding(2,2,2,2);
                        re_tv.setText(re_string.toString());
                    }
                    break;
                }
                case R.id.sw_hexSe: {
                    sw_hexSe.setSelected(!sw_hexSe.isSelected());
                    //if (byte_send.length <= 0) return;
                    isChangeHex = true;
                    if (sw_hexSe.isSelected()) {
                        sw_hexSe.setBackgroundResource(R.drawable.btn_hightlight);
                        sw_hexSe.setPadding(2,2,2,2);
                        se_tv.setText(byteToHex(byte_send));
                    } else {
                        sw_hexSe.setBackgroundResource(R.drawable.btn_default);
                        sw_hexSe.setPadding(2,2,2,2);
                        se_tv.setText(byteToString(byte_send));
                    }
                    se_tv.setSelection(se_tv.getText().length());
                    break;
                }
                case R.id.sw_intervalSend: {
                    if (share.socket == null && share.isSPP) return;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && share.isSPP) {
                        if (!share.socket.isConnected()) return;
                    }
                    sw_intervalSend.setSelected(!sw_intervalSend.isSelected());
                    if (sw_intervalSend.isSelected()) {
                        sw_intervalSend.setBackgroundResource(R.drawable.btn_hightlight);
                        sw_intervalSend.setPadding(2,2,2,2);
                        if (et_interval.getText().length() < 1) {
                            et_interval.setText("100");
                        }
                        //定时
                        task = new myTimerTask();
                        timer.schedule(task, 50, Integer.parseInt(et_interval.getText().toString()));
                    } else {
                        sw_intervalSend.setBackgroundResource(R.drawable.btn_default);
                        sw_intervalSend.setPadding(2,2,2,2);
                        if (task != null) {
                            task.cancel();
                            task = null;
                        }
                    }
                    break;
                }
                case R.id.sw_response: {
                    sw_response.setSelected(!sw_response.isSelected());
                    if (sw_response.isSelected()) {
                        sw_response.setBackgroundResource(R.drawable.btn_hightlight);
                        sw_response.setPadding(2,2,2,2);
                    } else {
                        sw_response.setBackgroundResource(R.drawable.btn_default);
                        sw_response.setPadding(2,2,2,2);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void readData(){
        if (share.isSPP) {
            new Thread(new Runnable() {
                //private Scanner r = new Scanner(mmInStream);
                @Override
                public void run() {
                    byte[] buffer = new byte[1000];// 缓冲数据流
                    int bytes = 0;// 返回读取到的数据
                    while (isRunning) {
                        try {
                            bytes = share.inputStream.read(buffer);
                            // 此处处理数据……
                            receveData(buffer, bytes);
                        } catch (IOException e) {
//                            e.printStackTrace();
                            isRunning = false;
//                            goBackToDo();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("value", "0");
                            msg.setData(bundle);
                            disConnectHandler.sendMessage(msg);
                        }

                    }
                }
            }).start();
        }
    }

    private void receveData(final byte[] buffer, final int bytes){
        reCRC.update(buffer, 0, bytes);
        String strBuf = new String(buffer, 0, bytes);
        //String strBuf = mmInStream.toString();
        re_bytes += bytes;
        re_package++;
//                        re_string = re_string.length() > 2000 ? strBuf : re_string + strBuf;
        re_string = re_string.append(strBuf);


        if (sw_hexRe.isSelected()) {
            if (re_string.length() > 300) {
                re_string = re_string.delete(0, re_string.length() - 100);
            }
        } else {
            if (re_string.length() > 1000) {
                re_string = re_string.delete(0, re_string.length() - 300);
            }
        }
        if (!isChangeData) isChangeData = true;
    }

    //自定义广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MyLog.i("comm蓝牙回调", String.valueOf(intent));
            final String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                // 连接成功
                tv_connectState.setText("");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                // 断开连接
                tv_connectState.setText(getResources().getText(R.string.disConnect));
                tv_connectState.setTextColor(getResources().getColor(R.color.red));
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                    MyLog.i("蓝牙", "关闭");
                }
            }
        }
    };

    TextWatcher myTextWatcher = new TextWatcher() {
        String stringTemp = "";
        int startIndex = 0;//光标位置
        int inputCount = 0;//插入多少
        int deleCount = 0;//删除多少
        Boolean isRight = true;

        @Override
        //s：改变前字符串，star改变前光标位置，count//准备删除的数量，after//准备添加的数量
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //MyLog.i("改变前++++++",s.toString() + " " + start + " " + count + " " + after);
            startIndex = start;
        }

        @Override
        //s：改变后的字符串，star改变前光标位置，before//删除的数量，count//添加的数量
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //MyLog.i("改变中：", s + " " + start + " " + before + " " + count);
            stringTemp = s.toString();
            inputCount = count;
            deleCount = before;
            if (sw_hexSe.isSelected()) {
                //过滤无效字符
                char[] chArray = stringTemp.toCharArray();
                for (char ch : chArray) {
                    if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || ch == ' ') {
                    } else {
                        String strCh = String.valueOf(ch);
                        stringTemp = stringTemp.replace(strCh, "");
                        inputCount--;
                        if (isRight) {
                            isRight = false;
                        }
                    }
                }
            }
            //MyLog.i("修改值",stringTemp + isRight.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //MyLog.i("改变后-------",s.toString());
            if (!isRight) {
                isRight = true;
                se_tv.setText(stringTemp);
                se_tv.setSelection(startIndex + inputCount);
            } else {
                if (!isChangeHex) {
                    if (sw_hexSe.isSelected()) {
                        byte_send = hexToByte(stringTemp);
                    } else {
                        byte_send = stringToByte(stringTemp);
                    }
                } else {
                    isChangeHex = false;
                }
            }
            //se_tv.setSelection(startIndex+inputCount-deleCount);
        }
    };

    private void getLastExitInfo() throws IOException {
        String stringRead = readSDFile("setting.ini");
        if (stringRead.length() < 1) return;
        String str = cutFileString(stringRead, "sw_hexRe");
        Boolean bool = Boolean.parseBoolean(str);
        sw_hexRe.setSelected(Boolean.parseBoolean(cutFileString(stringRead, "sw_hexRe")));
        sw_hexRe.setBackgroundResource(sw_hexRe.isSelected() ? R.drawable.btn_hightlight : R.drawable.btn_default);
        sw_hexSe.setSelected(Boolean.parseBoolean(cutFileString(stringRead, "sw_hexSe")));
        sw_hexSe.setBackgroundResource(sw_hexSe.isSelected() ? R.drawable.btn_hightlight : R.drawable.btn_default);
        sw_response.setSelected(Boolean.parseBoolean(cutFileString(stringRead, "sw_response")));
        sw_response.setBackgroundResource(sw_response.isSelected() ? R.drawable.btn_hightlight : R.drawable.btn_default);
        sw_hexRe.setPadding(2,2,2,2);
        sw_hexSe.setPadding(2,2,2,2);
        sw_response.setPadding(2,2,2,2);
        se_tv.setText(cutFileString(stringRead, "se_tv"));
        String intervalString = cutFileString(stringRead, "et_interval");
        et_interval.setText(intervalString.length() > 0 ? intervalString : "100");
    }

    private String cutFileString(final String stringBefore, final String stringName) {
        if (stringBefore == "" || stringBefore == null) return "";
        String string = stringBefore;
        string = string.substring(string.indexOf(stringName) + stringName.length() + 1);//因为有冒号所以+1
        string = string.substring(0, string.indexOf("//") - 2);
        return string;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Communication Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
    final Handler disConnectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String stringRe = msg.getData().getString("value");
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.disConnect)
                    , Toast.LENGTH_SHORT).show();
            tv_connectState.setText(getResources().getText(R.string.disConnect));
            tv_connectState.setTextColor(getResources().getColor(R.color.red));
        }
    };
    final Handler connectingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String stringRe = msg.getData().getString("value");
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.connecting)
                    , Toast.LENGTH_SHORT).show();
        }
    };
    final Handler connectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String stringRe = msg.getData().getString("value");
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.connected)
                    , Toast.LENGTH_SHORT).show();
            tv_connectState.setText("");
        }
    };

    //定时器
    Timer timer = new Timer();
    myTimerTask task = null;

    class myTimerTask extends TimerTask {
        @Override
        public void run() {
            if (byte_send.length == 0) {
                return;
            }
            int packets = share.write(byte_send);
            if (packets > 0) {
                seCRC.update(byte_send);
                se_bytes += byte_send.length;
                se_package += packets;
                if (!isChangeData) isChangeData = true;
            }else {

            }
        }
    }

    /**
     * 文件操作
     */
    public void openSystemFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            MyLog.i("uri*******", data.toString());
            Uri uri = data.getData();//得到uri。
            String filePath = getFileAbsolutePath(this.getApplicationContext(), uri);
//            String filePath = fileClass.getRealFilePath(uri);
            MyLog.i("文件路径：", filePath + "---" + requestCode + "---" + resultCode);
            if (filePath == null || filePath.length() < 1) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.openSendFileError)
                        , Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                fileByte = readFile(filePath);
                if (fileByte == null) return;
                isSendingFile = true;
                btn_sendFile.setText(getResources().getText(R.string.stop));
                fileLength = fileByte.length;
                sendFileThread();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.openSendFileError)
                        , Toast.LENGTH_SHORT).show();
                MyLog.i("读取失败", "~~~~~~~~~~~");
            }
            int i = 0;

        }
    }

    public void saveSetting() throws IOException {
        String saveInFileStr = "[软件设置]\r\n";
        saveInFileStr += saveSettingInfo("hex接收", "sw_hexRe", String.valueOf(sw_hexRe.isSelected()));
        saveInFileStr += saveSettingInfo("hex发送", "sw_hexSe", String.valueOf(sw_hexSe.isSelected()));
        saveInFileStr += saveSettingInfo("response", "sw_response", String.valueOf(sw_response.isSelected()));
        saveInFileStr += saveSettingInfo("定时(ms)", "et_interval", et_interval.getText().toString());
        saveInFileStr += saveSettingInfo("输入框", "se_tv", se_tv.getText().toString());
        saveInFileStr += saveSettingInfo("结尾", "end", "~");
        writeSDFile("setting.ini", saveInFileStr);
    }

    private String saveSettingInfo(String infoMark, String infoName, String value) {
        String string = "//" + infoMark + "\r\n" + infoName + ":" + value + "\r\n";
        return string;
    }

    public void sendFileThread() {
            final int perPackageLength = share.isSPP ? 2000 : 20;
            MyLog.i("发送中", "...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int sendBytePlace = 0, sendByteLength = fileByte.length;
                    int packets = 0;
                    while (sendBytePlace < sendByteLength && isSendingFile) {
                        if (!share.isSPP) {
                            while (!share.isBleSendFinish) {
                            }
                            share.isBleSendFinish = false;
                        }
                        if (sendByteLength - sendBytePlace > perPackageLength) {
                            packets = share.write(fileByte, sendBytePlace, perPackageLength);
                            if (packets > 0) {
                                seCRC.update(fileByte, sendBytePlace, perPackageLength);
                                sendBytePlace += perPackageLength;
                                fileSendedLength += perPackageLength;
                                se_bytes += perPackageLength;
                                se_package += packets;
                                if (!isChangeData) isChangeData = true;
                            } /*else {
                                MyLog.i("重发：", "第一遍");
                                packets = share.write(fileByte, sendBytePlace, perPackageLength);
                                if (packets > 0) {
                                    seCRC.update(fileByte, sendBytePlace, perPackageLength);
                                    sendBytePlace += perPackageLength;
                                    fileSendedLength += perPackageLength;
                                    se_bytes += perPackageLength;
                                    se_package += packets;
                                    if (!isChangeData) isChangeData = true;
                                } else {
                                    MyLog.i("重发：", "失败");
                                    break;
                                }
                            }*/
                        } else {
                            int lastLength = sendByteLength - sendBytePlace;
                            packets = share.write(fileByte, sendBytePlace, lastLength);
                            if (packets > 0) {
                                seCRC.update(fileByte, sendBytePlace, lastLength);
                                se_bytes += lastLength;
                                se_package += packets;
                                sendBytePlace += lastLength;
                                fileSendedLength += lastLength;
                                if (!isChangeData) isChangeData = true;
                                isSendingFile = false;
                                MyLog.i("se_bytes=", se_bytes + " " + sendByteLength);
                                break;
                            }
                            /*else {
                                MyLog.i("end重发：", "第一遍");
                                packets = share.write(fileByte, sendBytePlace, lastLength);
                                if (packets > 0) {
                                    seCRC.update(fileByte, sendBytePlace, lastLength);
                                    se_bytes += lastLength;
                                    se_package += packets;
                                    sendBytePlace += lastLength;
                                    fileSendedLength += lastLength;
                                    if (!isChangeData) isChangeData = true;
                                    isSendingFile = false;
                                } else {
                                    MyLog.i("end重发：", "失败");
                                    break;
                                }
                            }*/
                        }
                    }
                }
            }).start();

    }

    private void reflashViewThread() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //String stringRe = msg.getData().getString("value");
//                String str = re_string.toString();
//                int str_length = re_string.toString().length();
//                if (str_length > 500){
//                    str = str.substring(str_length - 500, 500);
//                }
                if (isRefashingReTV) {
                    if (sw_hexRe.isSelected()) {
//                        if (re_string.length() > 500){
//                            re_tv.setText(stringToHex(re_string.toString().substring(re_string.length()-500,500)));
//                        }else {
//                            re_tv.setText(stringToHex(re_string.toString()));
//                        }
                        re_tv.setText(stringToHex(re_string.toString()));
                    } else {
                        re_tv.setText(re_string.toString());
                    }
                    if (re_tv.getText().length()>100) {
                        re_tv.setSelection(re_tv.getText().length() - 1);
                    }
                    isRefashingReTV = false;
                }
                re_packageTV.setText(String.valueOf(re_package));
                re_byteTV.setText(String.valueOf(re_bytes));
                se_packageTV.setText(String.valueOf(se_package));
                se_byteTV.setText(String.valueOf(se_bytes));
                re_CRC.setText(Long.toHexString(reCRC.getValue()).toUpperCase());//toUpperCase()小写转大写
                se_CRC.setText(Long.toHexString(seCRC.getValue()).toUpperCase());
                if (!isSendingFile){
                    btn_sendFile.setText(getResources().getText(R.string.btnSendFile));
                }else {
                    btn_sendFile.setText(getResources().getText(R.string.stop) + String.valueOf ((fileSendedLength*100) / fileLength) + "%");
                }
                isChangeData = false;
            }
        };
        try {
            getLastExitInfo();
            MyLog.i("读取上次设置", "");
        } catch (IOException e) {
            MyLog.i("读取上次设置", "失败");
            e.printStackTrace();
        }
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    if (isChangeData) {
                        if (!isRefashingReTV) isRefashingReTV = true;
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("value", "0");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void goBackToDo() {
        isRunning = false;
        share.disConnect();
        if (task != null) {
            task.cancel();
            task = null;
        }
        finish();
    }


    private void setUpBLECallBack(){
        //通过BLE API的不同类型的回调方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mGattCallback = new BluetoothGattCallback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {//当连接状态发生改变
                    //把连接中标志改为false
                    if (newState == BluetoothProfile.STATE_CONNECTED) {//当蓝牙设备已经连接
                        MyLog.i("BLE连接", "成功");
                        gatt.discoverServices();
                        gatt.executeReliableWrite();
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//当设备无法连接
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.connectFail)
                                , Toast.LENGTH_SHORT).show();
                    }else {
                        MyLog.i("BLE连接", "... ...");
                    }
                }

                @Override
                // 发现新服务端
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {

                    } else {
                        String str = "onServicesDiscovered received: " + status;
                        MyLog.i("搜索服务状态",str);ArrayList<BluetoothGattCharacteristic> charas =
                                new ArrayList<BluetoothGattCharacteristic>();
                    }
                }

                @Override
                // 读特性
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    MyLog.i("发现特征", "");
                    if (status == BluetoothGatt.GATT_SUCCESS) {

                    }
                }
                @Override
                // 写特性
                public void onCharacteristicWrite(BluetoothGatt gatt,
                                                  BluetoothGattCharacteristic characteristic, int status) {
                    share.isBleSendFinish = true;
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        MyLog.i("发送回调", "成功");
                    }else {
                        MyLog.i("发送回调", "失败");
                    }
                }
            };
        }
    }
}


