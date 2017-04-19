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
    Button btnRate, btnDelete, btnSetCurrent, btnRight, btnLeft;
    TextView txtRating, txtPlant; //TODO: Add plant type var
    int currentID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate textview
        txtRating = (TextView) findViewById(R.id.textRating);
        txtPlant = (TextView) findViewById(R.id.txtPlant);
        Log.e("DEBUG", "---------------1");
        // instantiate buttons
        btnRate = (Button) findViewById(R.id.btnRate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSetCurrent = (Button) findViewById(R.id.btnSetCurrent);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        Log.e("DEBUG", "---------------2");
        btnRate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSetCurrent.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        Log.e("DEBUG", "---------------3");

        setCurrentPlant(0);
        Log.e("DEBUG", "---------------4");
    }

    @Override
    public void onClick(View v) {
        //check which button clicked
        if (v.equals(btnRate)){
            try {
                Intent in = new Intent(v.getContext(), Rate.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
            }
        }
        else if (v.equals(btnDelete)){
            try {
                //TODO: delete plant
            } catch (Exception e) {
                Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
            }
        }
        else if (v.equals(btnSetCurrent)){
            try {
                //TODO: set the current plant
            } catch (Exception e) {
                Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
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
            btnLeft.setVisibility(View.INVISIBLE);
        }
        else{
            btnLeft.setEnabled(true);
            btnLeft.setVisibility(View.VISIBLE);
        }
        Log.e("DEBUG", "--------------sub 1");
        if (currentID >= ConnectionMethods.getNumOfPlants()){
            Log.e("DEBUG", "Disable right");
            btnRight.setEnabled(false);
            btnRight.setVisibility(View.INVISIBLE);
        }
        else{
            btnRight.setEnabled(true);
            btnRight.setVisibility(View.VISIBLE);
        }
        Log.e("DEBUG", "--------------sub 2");
        //set textview for plant id
        txtPlant.setText("Plant " + currentID);
        Log.e("DEBUG", "--------------sub 3");
        setRatingText(currentID);
    }

}
