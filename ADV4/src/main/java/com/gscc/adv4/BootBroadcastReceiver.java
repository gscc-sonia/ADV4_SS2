package com.gscc.adv4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sonia on 2013/11/11.
 */
public class BootBroadcastReceiver extends BroadcastReceiver{
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context,Intent intent){

        if(intent.getAction().equals(ACTION)){
            Intent ADV4intent = new Intent(context,MainActivity.class);
            ADV4intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(ADV4intent);
        }
    }
}
