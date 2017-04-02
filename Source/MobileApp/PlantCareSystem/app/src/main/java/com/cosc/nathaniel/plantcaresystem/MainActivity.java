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
    Button btnSettings, btnSensors, btnAdd, btnRight, btnLeft;
    TextView txtRating, txtPlant;
    int currentID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate textview
        txtRating = (TextView) findViewById(R.id.textRating); //TODO make rating clickable to set health
        txtPlant = (TextView) findViewById(R.id.txtPlant);
        // instantiate buttons
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnSettings.setOnClickListener(this);
        btnSensors.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        //TODO delete plant button

        setCurrentPlant(0);
    }

    @Override
    public void onClick(View v) {
        //check which button clicked
        if (v.equals(btnSensors)){
            try {
                Intent in = new Intent(v.getContext(), currentSensors.class);
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
        else if (v.equals(btnAdd)){
            try {
                Intent in = new Intent(v.getContext(), AddPlant.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to launch new activity.");
            }
        }
        else if (v.equals(btnRight)){
            Log.e("DEBUG", "Right clicked");
            setCurrentPlant(1);
        }
        else if (v.equals(btnLeft)){
            Log.e("DEBUG", "Left clicked");
            setCurrentPlant(-1);
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

    private int getRating(int id){
        //get health rating of current plant from server
        String rating = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_PLANT + id), "health");
        return Integer.parseInt(rating);
    }

    private void setRatingText(int id){
        //set rating text
        int ratingNum = getRating(id);
        txtRating.setText(Integer.toString(ratingNum) + "%");
        //set color of text to indicate rating
        txtRating.setTextColor(
                ratingNum > 75 ? Color.GREEN :
                        ratingNum > 50 ? Color.YELLOW : Color.RED
        );
    }

    private void setCurrentPlant(int direction){
        //increment or decrement according to direction (direction of 0 does not increment or decrement)
        if ( (direction > 0) && (currentID < ConnectionMethods.getNumOfPlants()) ){
            Log.e("DEBUG", "Increment");
            currentID++;
        }
        else if ( (direction < 0) && (currentID > 1) ){
            Log.e("DEBUG", "Decrement");
            currentID--;
        }

        //disable buttons if at edge of range
        if (currentID <= 1){
            Log.e("DEBUG", "Disable left");
            btnLeft.setEnabled(false);
        }
        else{
            btnLeft.setEnabled(true);
        }

        if (currentID >= ConnectionMethods.getNumOfPlants()){
            Log.e("DEBUG", "Disable right");
            btnRight.setEnabled(false);
        }
        else{
            btnRight.setEnabled(true);
        }

        //set textview for plant id
        txtPlant.setText("Plant " + currentID);
        setRatingText(currentID);
    }

}
