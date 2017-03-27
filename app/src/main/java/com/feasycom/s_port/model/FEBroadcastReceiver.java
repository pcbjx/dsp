package com.feasycom.s_port.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yumingyue on 2017/2/16.
 */

public class FEBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "FEBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        MyLog.i(TAG, msg);
    }
}
