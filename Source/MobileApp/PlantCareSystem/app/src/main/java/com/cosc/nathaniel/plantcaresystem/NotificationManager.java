package com.cosc.nathaniel.plantcaresystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

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
        String tempFlag = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS), "tempFlag");
        String humidFlag = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS), "humidFlag");

        if(tempFlag.equals("1") || humidFlag.equals("1")) {
            String message = "humidity";
            if(tempFlag.equals("1")){
                message = "temperature";
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