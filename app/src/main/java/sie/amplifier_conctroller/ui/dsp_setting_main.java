package sie.amplifier_conctroller.ui;

import android.os.Bundle;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feasycom.s_port.R;

import com.github.mikephil.charting.charts.LineChart;

public class dsp_setting_main extends Activity {

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_eq_page);
    }

}
