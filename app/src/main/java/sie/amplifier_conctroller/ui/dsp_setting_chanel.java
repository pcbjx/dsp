package sie.amplifier_conctroller.ui;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import com.feasycom.s_port.R;

import common.zhang.customer.RotateButtom;

public class dsp_setting_chanel extends Activity {

    RotateButtom chanel1_r_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_output);
        init();
    }

    public void init()
    {
        chanel1_r_bt = (RotateButtom)findViewById(R.id.id_sb_output_ch0);
        chanel1_r_bt.set_titile("通道1");
        chanel1_r_bt.setBarinit(0,60,10);
        chanel1_r_bt.setOnTempChangeListener(new RotateButtom.OnTempChangeListener()
        {
            @Override
            public void change(int temp) {
                Toast.makeText(dsp_setting_chanel.this, temp + "°", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
