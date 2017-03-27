package com.feasycom.s_port;

import android.Manifest;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.about.AboutActivity;
import com.feasycom.s_port.communication.MainActivity;
import com.feasycom.s_port.model.MyLog;
import com.feasycom.s_port.setting.SettingActivity;

public class TabActivity extends ActivityGroup {
    private RadioGroup radioGroup;
    private RadioButton settingButton;
    private RadioButton communicationButton;
    private RadioButton aboutButton;

    private LinearLayout container;

    private FEShare share = FEShare.getInstance();

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tab);
        initView();
        share.context = this;
        share.init();
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        radioGroup.check(share.tabId);
        communicationButton.setText(getResources().getText(R.string.communication));
        settingButton.setText(getResources().getText(R.string.setting));
        aboutButton.setText(getResources().getText(R.string.about));
    }
    //    @Override
//    public void onBackPressed(){
//        new AlertDialog.Builder(this)
//                .setTitle("")
//                .setMessage(getResources().getString(R.string.exit_hint))
//                .setPositiveButton(
//                        getResources().getString(android.R.string.yes),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                // TODO Auto-generated method stub
//                                finish();
//                            }
//                        })
//                .setNegativeButton(
//                        getResources().getString(android.R.string.cancel),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                // TODO Auto-generated method stub
//                                dialog.dismiss();
//                            }
//                        }).show();
//    }
    private void initView(){
        MyLog.i("Tab", "initView");
        radioGroup = (RadioGroup) findViewById(R.id.bottom);
        radioGroup.setOnCheckedChangeListener(new myOnCheckedChangeListener());

        communicationButton = (RadioButton) findViewById(R.id.communication);
        settingButton = (RadioButton) findViewById(R.id.setting);
        aboutButton = (RadioButton) findViewById(R.id.about);

        container = (LinearLayout) findViewById(R.id.container);

    }
    class myOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            Drawable top;
            share.tabId = checkedId;
            switch(checkedId){
                case R.id.communication:
                    showActivity(MainActivity.class);
                    communicationButton.setTextColor(getResources().getColor(R.color.text_color_blue));
                    settingButton.setTextColor(getResources().getColor(R.color.white));
                    aboutButton.setTextColor(getResources().getColor(R.color.white));
                    top = getResources().getDrawable(R.drawable.communication);
                    communicationButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.setting2);
                    settingButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.about2);
                    aboutButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    break;
                case R.id.setting:
                    showActivity(SettingActivity.class);
                    communicationButton.setTextColor(getResources().getColor(R.color.white));
                    settingButton.setTextColor(getResources().getColor(R.color.text_color_blue));
                    aboutButton.setTextColor(getResources().getColor(R.color.white));
                    top = getResources().getDrawable(R.drawable.communication2);
                    communicationButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.setting);
                    settingButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.about2);
                    aboutButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    break;
                case R.id.about:
                    showActivity(AboutActivity.class);
                    communicationButton.setTextColor(getResources().getColor(R.color.white));
                    settingButton.setTextColor(getResources().getColor(R.color.white));
                    aboutButton.setTextColor(getResources().getColor(R.color.text_color_blue));
                    top = getResources().getDrawable(R.drawable.communication2);
                    communicationButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.setting2);
                    settingButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    top = getResources().getDrawable(R.drawable.about);
                    aboutButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    break;

            }
        }

    }
    private void showActivity(Class activityClass) {
        MyLog.i("Tab", "showActivity");
        //切换通讯以外的Tab时，停止搜索
        if (share.tabId != R.id.communication){
            share.stopSearch();
        }
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Window activity = getLocalActivityManager().startActivity(activityClass.getSimpleName(), intent);
        activity.getDecorView().requestFocus();

        container.removeAllViews();
        container.addView(activity.getDecorView());
//		setContentView(activity.getDecorView());

    }
}
