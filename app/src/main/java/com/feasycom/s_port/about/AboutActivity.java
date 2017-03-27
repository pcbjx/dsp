package com.feasycom.s_port.about;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.feasycom.s_port.R;
import com.feasycom.s_port.model.MyLog;


/**
 * Created by yumingyue on 2016/11/17.
 */

public class AboutActivity extends AppCompatActivity {
    private Context aboutActivity = this;
    private TextView tv_tel, tv_mail, tv_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //TextView
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_tel.setOnClickListener(new MyClickListener());
        tv_web = (TextView) findViewById(R.id.tv_web);
        tv_web.setOnClickListener(new MyClickListener());
        tv_mail = (TextView) findViewById(R.id.tv_mail);
        tv_mail.setOnClickListener(new MyClickListener());

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.tv_tel: {
                    MyLog.i("点击了", "打电话");
                    //用intent启动拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel.getText()));
                    if (ActivityCompat.checkSelfPermission(aboutActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                    break;
                }
                case R.id.tv_web:{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.feasycom.com/" + getResources().getText(R.string.web_language)));
                    startActivity(intent);
                    break;
                }
                case R.id.tv_mail:{
                    String[] reciver = new String[] {tv_mail.getText().toString()};
//                    String[] mySbuject = new String[] { "test" };
//                    String myCc = "cc";
//                    String mybody = "测试Email Intent";
                    Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                    myIntent.setType("plain/text");
                    myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
//                    myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
                    myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getText(R.string.inquiry));
//                    myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
                    startActivity(Intent.createChooser(myIntent, "mail"));
                    break;
                }
            }
        }
    }
}
