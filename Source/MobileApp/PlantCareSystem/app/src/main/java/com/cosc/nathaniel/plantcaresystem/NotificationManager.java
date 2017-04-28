package com.cosc.nathaniel.plantcaresystem;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationManager extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        createNotification(context);

        wl.release();
    }

    public void createNotification(Context context){
        int fieldToChange = Integer.parseInt(ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SCORE));

        if(fieldToChange > 2) {
            String message = "temperature";
            if(fieldToChange > 3){
                message = "humidity";
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_light)
                            .setContentTitle("Update on your plant")
                            .setContentText("The " + message + " is outside of acceptable parameters, please adjust.");
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());
        }
    }

}