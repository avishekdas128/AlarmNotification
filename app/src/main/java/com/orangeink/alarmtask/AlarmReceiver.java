package com.orangeink.alarmtask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("CANCEL") != null) {
            context.stopService(new Intent(context, NotificationService.class));
        } else {
            Intent i = new Intent(context, NotificationService.class);
            i.putExtra("TEXT", intent.getStringExtra("TEXT"));
            context.startService(i);
        }
    }

}
