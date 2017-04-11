package sie.amplifier_conctroller.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.feasycom.s_port.R;

import com.feasycom.s_port.ShareFile.FEShare;
import com.feasycom.s_port.ShareFile.FileClass;
import com.feasycom.s_port.model.MyLog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import common.zhang.customer.VerticalSeekBar;
import sie.amplifier_conctroller.DataStruct.DataStruct;
import sie.amplifier_conctroller.Sie_app_data_share;

public class dsp_setting_main extends Activity  implements OnChartGestureListener ,OnChartValueSelectedListener{

    FileClass sieFile;

    private LineChart mChart;
    ArrayList<Entry> m_values = new ArrayList<Entry>();

    final String TAG = dsp_setting_main.class.getSimpleName();
    Sie_app_data_share sie_data_share;
    private FEShare share = FEShare.getInstance();

    VerticalSeekBar [] verticalSeekBarsList;
    Button[] btLvGainEqualizerList;
    Button[] btid_tv_freq_equalizerList;
    SeekBar seekBarQvalue;
    Button buttonrest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_eq_page);

        sie_data_share = (Sie_app_data_share)getApplication();

        init_eq();
        init_Chart();
    }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

            // un-highlight values after the gesture is finished and no single-tap
            if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }

         @Override
        public void onValueSelected(Entry e, Highlight h) {

        }

        @Override
        public void onNothingSelected() {

        }


    private void init_Chart()
    {
        mChart = (LineChart) findViewById(R.id.id_eq_eqfilter_page);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(14f);
        leftAxis.setAxisMinimum(-14f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // setData(31, 5);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);

        for (int i = 0; i < DataStruct.eqMax; i++) {

            int val = DataStruct.cur_eq[i]-12;
            m_values.add(new Entry(i, val));
        }
        setsieData(0,DataStruct.cur_eq[0]);

    }

    private void setsieData(int index,int valaue)
    {

        Entry tmp;
        tmp =  m_values.get(index);
        tmp.setY(valaue-12);
        m_values.set(index,tmp);

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(m_values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(m_values, "EQ");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }

        mChart.invalidate();
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "EQ");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    private void init_eq()
    {
        seekBarQvalue = (SeekBar)findViewById(R.id.id_sb_equalizer_bw);
        seekBarQvalue.setMax(DataStruct.eqMax-DataStruct.QMin);
        seekBarQvalue.setProgress(DataStruct.curQValue);

        seekBarQvalue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DataStruct.curQValue = progress + DataStruct.QMin;
                sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_eqBandWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        verticalSeekBarsList = new VerticalSeekBar[DataStruct.eqMax];

        verticalSeekBarsList[0]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_1);
        verticalSeekBarsList[1]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_2);
        verticalSeekBarsList[2]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_3);
        verticalSeekBarsList[3]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_4);
        verticalSeekBarsList[4]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_5);
        verticalSeekBarsList[5]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_6);
        verticalSeekBarsList[6]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_7);
        verticalSeekBarsList[7]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_8);
        verticalSeekBarsList[8]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_9);
        verticalSeekBarsList[9]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_10);
        verticalSeekBarsList[10]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_11);
        verticalSeekBarsList[11]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_12);
        verticalSeekBarsList[12]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_13);
        verticalSeekBarsList[13]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_14);
        verticalSeekBarsList[14]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_15);
        verticalSeekBarsList[15]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_16);
        verticalSeekBarsList[16]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_17);
        verticalSeekBarsList[17]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_18);
        verticalSeekBarsList[18]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_19);
        verticalSeekBarsList[19]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_20);
        verticalSeekBarsList[20]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_21);
        verticalSeekBarsList[21]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_22);
        verticalSeekBarsList[22]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_23);
        verticalSeekBarsList[23]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_24);
        verticalSeekBarsList[24]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_25);
        verticalSeekBarsList[25]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_26);
        verticalSeekBarsList[26]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_27);
        verticalSeekBarsList[27]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_28);
        verticalSeekBarsList[28]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_29);
        verticalSeekBarsList[29]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_30);
        verticalSeekBarsList[30]= (VerticalSeekBar) findViewById(R.id.id_mvs_equalizer_one_31);

        btLvGainEqualizerList = new Button[DataStruct.eqMax];
        btLvGainEqualizerList[0] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_1);
        btLvGainEqualizerList[1] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_2);
        btLvGainEqualizerList[2] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_3);
        btLvGainEqualizerList[3] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_4);
        btLvGainEqualizerList[4] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_5);
        btLvGainEqualizerList[5] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_6);
        btLvGainEqualizerList[6] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_7);
        btLvGainEqualizerList[7] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_8);
        btLvGainEqualizerList[8] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_9);
        btLvGainEqualizerList[9] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_10);
        btLvGainEqualizerList[10] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_11);
        btLvGainEqualizerList[11] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_12);
        btLvGainEqualizerList[12] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_13);
        btLvGainEqualizerList[13] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_14);
        btLvGainEqualizerList[14] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_15);
        btLvGainEqualizerList[15] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_16);
        btLvGainEqualizerList[16] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_17);
        btLvGainEqualizerList[17] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_18);
        btLvGainEqualizerList[18] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_19);
        btLvGainEqualizerList[19] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_20);
        btLvGainEqualizerList[20] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_21);
        btLvGainEqualizerList[21] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_22);
        btLvGainEqualizerList[22] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_23);
        btLvGainEqualizerList[23] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_24);
        btLvGainEqualizerList[24] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_25);
        btLvGainEqualizerList[25] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_26);
        btLvGainEqualizerList[26] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_27);
        btLvGainEqualizerList[27] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_28);
        btLvGainEqualizerList[28] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_29);
        btLvGainEqualizerList[29] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_30);
        btLvGainEqualizerList[30] = (Button)findViewById(R.id.id_tv_gain_equalizer_one_31);

        btid_tv_freq_equalizerList = new Button[DataStruct.eqMax];
        btid_tv_freq_equalizerList[0] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_1);
        btid_tv_freq_equalizerList[1] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_2);
        btid_tv_freq_equalizerList[2] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_3);
        btid_tv_freq_equalizerList[3] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_4);
        btid_tv_freq_equalizerList[4] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_5);
        btid_tv_freq_equalizerList[5] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_6);
        btid_tv_freq_equalizerList[6] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_7);
        btid_tv_freq_equalizerList[7] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_8);
        btid_tv_freq_equalizerList[8] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_9);
        btid_tv_freq_equalizerList[9] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_10);
        btid_tv_freq_equalizerList[10] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_11);
        btid_tv_freq_equalizerList[11] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_12);
        btid_tv_freq_equalizerList[12] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_13);
        btid_tv_freq_equalizerList[13] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_14);
        btid_tv_freq_equalizerList[14] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_15);
        btid_tv_freq_equalizerList[15] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_16);
        btid_tv_freq_equalizerList[16] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_17);
        btid_tv_freq_equalizerList[17] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_18);
        btid_tv_freq_equalizerList[18] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_19);
        btid_tv_freq_equalizerList[19] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_20);
        btid_tv_freq_equalizerList[20] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_21);
        btid_tv_freq_equalizerList[21] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_22);
        btid_tv_freq_equalizerList[22] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_23);
        btid_tv_freq_equalizerList[23] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_24);
        btid_tv_freq_equalizerList[24] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_25);
        btid_tv_freq_equalizerList[25] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_26);
        btid_tv_freq_equalizerList[26] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_27);
        btid_tv_freq_equalizerList[27] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_28);
        btid_tv_freq_equalizerList[28] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_29);
        btid_tv_freq_equalizerList[29] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_30);
        btid_tv_freq_equalizerList[30] = (Button)findViewById(R.id.id_tv_freq_equalizer_one_31);


        for (int i = 0;i<DataStruct.eqMax;i++)
        {
            verticalSeekBarsList[i].setMax(24);
            verticalSeekBarsList[i].setProgress(DataStruct.cur_eq[i]);
            btid_tv_freq_equalizerList[i].setText(""+DataStruct.EQ_FREQ[i]+"hz");

            updateEqGain(i);

            verticalSeekBarsList[i].setOnSeekBarChangeListener(onSeekBarChangeListenerEq);
            btid_tv_freq_equalizerList[i].setOnClickListener(onClickListenertTv_freq_equalizer);
        }

        buttonrest = (Button)findViewById(R.id.id_b_eq_reset);
        buttonrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetQq();
            }
        });


    }

    public void resetQq()
    {
        for (int i = 0 ;i<DataStruct.eqMax;i++)
        {
            DataStruct.cur_eq[i] = 12;
            updateEqSeekBar(i);
        }

        sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_eq32);
    }

    View.OnClickListener onClickListenertGainEqualizer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    View.OnClickListener onClickListenertTv_freq_equalizer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    SeekBar.OnSeekBarChangeListener onSeekBarChangeListenerEq = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            for (int i = 0;i<DataStruct.eqMax;i++)
            {
                if (seekBar.getId() == verticalSeekBarsList[i].getId())
                {
                    DataStruct.cur_eq[i] = (byte) progress;
                    updateEqSeekBar(i);
                    sie_data_share.sendSieData(DataStruct.sieProtocol.prtc_eq32);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //解除广播接收器
        unregisterReceiver(receiver);
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, share.getIntent_ui_setting_Filter());
    }


    private void updateEqGain(int i) {
        int gain = DataStruct.cur_eq[i]-12;
        btLvGainEqualizerList[i].setText("" + gain + "db");
    }
    private void updateEqSeekBar(int i) {
        updateEqGain(i);
        verticalSeekBarsList[i].setProgress(DataStruct.cur_eq[i]);
        setsieData(i,DataStruct.cur_eq[i]);
    }
    private void flashUIFromProtocol(int sieProtocolValue)
    {

        if (sieProtocolValue == DataStruct.sieProtocol.prtc_eq32)
        {
            for (int i =0;i<DataStruct.eqMax;i++)
            {
                verticalSeekBarsList[i].setProgress(DataStruct.cur_eq[i]);
                updateEqGain(i);
            }
        }

    }

    private Handler myHandler = new Handler()
    {
        // 2.重写消息处理函数

        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                {
                    flashUIFromProtocol(msg.arg1);
                    break;
                }
            }

            super.handleMessage(msg);
        }

    };


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Message msg = new Message();
            MyLog.v(TAG,"recver eq31");
            if (share.SIE_UI_ACTION_EQ_31.equals(action)) {
                // 连接成功
                MyLog.v(TAG,"recver eq31");
                msg.what=1;
                msg.arg1=DataStruct.sieProtocol.prtc_eq32;
            }

            myHandler.sendMessage(msg);
        }

    };

}
