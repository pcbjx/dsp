package com.feasycom.s_port.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;


/**
 * Created by yumingyue on 2016/11/13.
 */

public class SettingActivity extends AppCompatActivity {
    private Button btn_communication;
    private FEShare share = FEShare.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //Button
        btn_communication = (Button) findViewById(R.id.btn_communication);
        btn_communication.setOnClickListener(new MyClickListener());
    }
    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Class targetClass = null;
            switch (v.getId()) {
                case R.id.btn_communication: {

                    break;
                }
            }
            share.intent.setClass(SettingActivity.this, targetClass);
            startActivity(share.intent);
        }
    }

}
