package com.cosc.nathaniel.plantcaresystem;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // button variables
    Button btnSettings, btnSensors, btnDatabase;
    TextView txtRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate textview
        txtRating = (TextView) findViewById(R.id.textRating);
        // instantiate buttons
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnDatabase = (Button) findViewById(R.id.btnDatabase);
        btnSettings.setOnClickListener(this);
        btnSensors.setOnClickListener(this);
        btnDatabase.setOnClickListener(this);

        //set rating text
        int ratingNum = getRating();
        txtRating.setText(Integer.toString(ratingNum) + "%");
        //set color of text to indicate rating
        txtRating.setTextColor(
                ratingNum > 75 ? Color.GREEN :
                        ratingNum > 50 ? Color.YELLOW : Color.RED
        );
    }

    @Override
    public void onClick(View v) {
        //check which button clicked
        if (v.equals(btnSensors)){
            Log.e("DEBUG", "-------------debug point 1");
            try {
                Log.e("DEBUG", "-------------debug point 2");
                Intent in = new Intent(v.getContext(), currentSensors.class);
                Log.e("DEBUG", "-------------debug point 3");
                startActivity(in);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to launch new activity.");
            }
        }
        else if (v.equals(btnSettings)){
            try {
                Intent in = new Intent(v.getContext(), Settings.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to launch new activity.");
            }
        }
        else if (v.equals(btnDatabase)){
            try {
                Intent in = new Intent(v.getContext(), Database.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to launch new activity.");
            }
        }
    }

    private String createXML(int id, String name, int light, int water, int humid, int temp){
        return "<plant id=\"" + id + "\" name=\"" + name + "\" light=\"" + light + "\" water=\""
                + water + "\" humid=\"" + humid + "\" temp=\"" + temp + "\"/>";
    }

    private void createNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_light)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
                        //.setContentIntent(pendingIntent); //Required on Gingerbread and below
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }

    private int getRating(){
        //get health rating of current plant from server
        String rating = ConnectionMethods.parseData(ConnectionMethods.queryServer(ConnectionMethods.Q_CURRENT), "health");
        return Integer.parseInt(rating);
    }


}
