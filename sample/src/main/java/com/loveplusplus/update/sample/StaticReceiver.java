package com.loveplusplus.update.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tianc on 2016/5/4.
 */
public class StaticReceiver extends BroadcastReceiver{
    private static boolean if_receive=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        if_receive=true;
        Log.d("收到广播！","GOOD!");
    }
    public static boolean return_state(){
        return if_receive;
    }

}