package com.orangeink.alarmtask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {

    Ringtone ringtone;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification(intent);
        startRingtone();
        return Service.START_STICKY;
    }

    private void sendNotification(Intent intent) {
        String channelId = getString(R.string.channel_id);
        Intent deleteIntent = new Intent(this, AlarmReceiver.class);
        deleteIntent.putExtra("CANCEL", "CANCEL");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ALARM")
                .setDeleteIntent(PendingIntent.getBroadcast(this, (int) SystemClock.uptimeMillis(), deleteIntent, 0))
                .setContentText(intent.getStringExtra("TEXT"));
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    " Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int) SystemClock.uptimeMillis(), notificationBuilder.build());
    }

    private void startRingtone() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
        ringtone.play();
        Thread th = new Thread(() -> {
            try {
                Thread.sleep(25000);  //30000 is for 30 seconds, 1 sec =1000
                if (ringtone.isPlaying())
                    ringtone.stop();   // for stopping the ringtone
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        th.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
        super.onDestroy();
    }
}
