package com.cosc.nathaniel.plantcaresystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

//this class handles sending notifications to the user
public class NotificationManager extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        //from http://stackoverflow.com/questions/34516160/android-service-control-notifications-in-time-doesnt-work/34540681
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        createNotification(context);

        wl.release();
    }

    static public void createNotification(Context context){
        // get from server whether temp and/or humidity need to be changed
        String tempFlag = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS), "tempFlag");
        String humidFlag = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS), "humidFlag");

        if(tempFlag.equals("1") || humidFlag.equals("1")) {
            //assume humidity was checked flag
            String message = "humidity";
            if(tempFlag.equals("1")){
                //if temp was checked flag, change message to temp
                //(temp and humid should never both be checked)
                message = "temperature";
            }

            //send notification to user
            //this is inside the if so we don't bother the user if nothing needs to change
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