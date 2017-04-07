package sie.amplifier_conctroller.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.feasycom.s_port.R;
import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.about.AboutActivity;

import common.zhang.customer.MyToolBar;
import common.zhang.customer.MyToolBar.MyToolBarClickListener;



public class dsp_setting  extends TabActivity {

    private FEShare share = FEShare.getInstance();


    static AnimationTabHost mTabHost;
    private MyToolBar myToolBar;// 自定义toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsp_setting);
        titile_init();
        initTab();
    }

    private void initTab() {

        mTabHost = (AnimationTabHost) getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator(getResources().getText(R.string.title_setting_delay).toString(), getResources().getDrawable(android.R.drawable.ic_menu_add))
                .setContent(new Intent(this, dsp_setting_delay.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator(getResources().getText(R.string.title_setting_main).toString(), getResources().getDrawable(android.R.drawable.ic_menu_add))
                .setContent(new Intent(this, dsp_setting_main.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator(getResources().getText(R.string.title_setting_chanel).toString(), getResources().getDrawable(android.R.drawable.ic_menu_add))
                .setContent(new Intent(this, dsp_setting_chanel.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Tab4").setIndicator(getResources().getText(R.string.title_setting_fred).toString(), getResources().getDrawable(android.R.drawable.ic_menu_add))
                .setContent(new Intent(this, dsp_setting_fq_dv.class)));


        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("Tab1")) {

                    // 设置是否显示中间标题，默认的是显示
                    myToolBar.setToolBarTitleVisible(R.string.title_setting_delay,true);
                }
                if (tabId.equals("Tab2")) {
                    // 设置是否显示中间标题，默认的是显示
                    myToolBar.setToolBarTitleVisible(R.string.title_setting_main,true);
                }
                if (tabId.equals("Tab3")) {
                    // 设置是否显示中间标题，默认的是显示
                    myToolBar.setToolBarTitleVisible(R.string.title_setting_chanel,true);
                }
                if (tabId.equals("Tab3")) {
                    // 设置是否显示中间标题，默认的是显示
                    myToolBar.setToolBarTitleVisible(R.string.title_setting_fred,true);
                }

            }
        });

        mTabHost.setCurrentTab(1);
    }


    public void titile_init()
    {
        myToolBar = (MyToolBar) findViewById(R.id.myToolBar);
        // 设置左边右边的按钮是否显示
        myToolBar.setToolBarBtnVisiable(true, true);
        // 设置是否显示中间标题，默认的是显示
        myToolBar.setToolBarTitleVisible(R.string.title_setting_main,true);

        myToolBar.updateRightButton(share.connect_state);

        /*
		 * toolbar的点击事件处理
		 */
        myToolBar.setOnMyToolBarClickListener(new MyToolBarClickListener() {

            @Override
            public void rightBtnClick() {// 右边按钮点击事件
                if((share.connect_state==2))//未连接跳转
                {
                    final Intent deviceIntent = new Intent(dsp_setting.this, com.feasycom.s_port.TabActivity.class);
                    startActivity(deviceIntent);
                }else if ((share.connect_state==1))
                {
                    final Intent deviceIntent = new Intent(dsp_setting.this, AboutActivity.class);
                    startActivity(deviceIntent);
                }
            }

            @Override
            public void leftBtnClick() {// 左边按钮点击事件
                Toast.makeText(dsp_setting.this, "返回", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(dsp_setting.this,
                        dsp_main.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
