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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // view element variables
    Button btnHome, btnDelete, btnSetCurrent, btnRight, btnLeft;
    TextView txtRating, txtPlant, txtName;

    // vars for keeping track of plant IDs
    int displayedPlantID = 0;
    String currentPlantID = "";
    List<String> idList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate textview
        txtRating = (TextView) findViewById(R.id.textRating);
        txtPlant = (TextView) findViewById(R.id.txtPlant);
        txtName = (TextView) findViewById(R.id.txtName);
        // instantiate buttons
        btnHome = (Button) findViewById(R.id.btnHome);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSetCurrent = (Button) findViewById(R.id.btnSetCurrent);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnHome.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSetCurrent.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        //get list of all active plant IDs
        idList = ConnectionMethods.getIDList();
        //get current plant id
        currentPlantID = ConnectionMethods.getCurrentPlantID();
        //call switchPlant to set dynamic fields
        switchPlant(0);
    }

    @Override
    public void onClick(View v) {
        //check which button clicked
        if (v.equals(btnHome)){
            try {
                Intent in = new Intent(v.getContext(), StartActivity.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
            }
        }
        else if (v.equals(btnDelete)){
            try {
                //send remove instruction to server
                ConnectionMethods.queryServer(ConnectionMethods.Q_REMOVE + idList.get(displayedPlantID));
                //update plant list
                idList = ConnectionMethods.getIDList();
                //switch view to next plant, or if last plant was deleted, previous plant
                switchPlant( (displayedPlantID >= idList.size()-1) ? -1 : 0 );
            } catch (Exception e) {
                Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
            }
        }
        else if (v.equals(btnSetCurrent)){
            if (isCurrentPlant()) {
                try {
                    Intent in = new Intent(v.getContext(), Rate.class);
                    startActivity(in);
                } catch (Exception e) {
                    Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
                }
            }
            else{
                try {
                    String plantData = ConnectionMethods.queryServer(ConnectionMethods.Q_PLANT + idList.get(displayedPlantID));
                    ConnectionMethods.queryServer(ConnectionMethods.Q_UPDATE_CURRENT +
                            idList.get(displayedPlantID) + " " +
                            ConnectionMethods.parsePlant(plantData, "lightSens") + " " +
                            ConnectionMethods.parsePlant(plantData, "waterSens") + " " +
                            ConnectionMethods.parsePlant(plantData, "humidSens") + " " +
                            ConnectionMethods.parsePlant(plantData, "tempSens") + " " +
                            ConnectionMethods.parsePlant(plantData, "health") + " " +
                            ConnectionMethods.parsePlant(plantData, "inTraining")
                    );
                    //update current plant id
                    currentPlantID = ConnectionMethods.getCurrentPlantID();
                    setBtnRate();
                } catch (Exception e) {
                    Log.e("DEBUG", "MainActivity: Failed to launch new activity.");
                }
            }
        }
        else if (v.equals(btnRight)){
            switchPlant(1);
        }
        else if (v.equals(btnLeft)){
            switchPlant(-1);
        }
    }

    //TODO: update water timer

    private String createXML(int id, String name, int light, int water, int humid, int temp){
        return "<plant id=\"" + id + "\" name=\"" + name + "\" light=\"" + light + "\" water=\""
                + water + "\" humid=\"" + humid + "\" temp=\"" + temp + "\"/>";
    }

    private int getRating(String id){
        //get health rating of current plant from server
        String rating = ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_PLANT + id), "health");
        return Integer.parseInt(rating);
    }

    private void setRatingText(String id){
        //set rating text
        int ratingNum = getRating(id);
        txtRating.setText(Integer.toString(ratingNum) + "%");
        //set color of text to indicate rating
        txtRating.setTextColor(
                ratingNum > 75 ? Color.GREEN :
                        ratingNum > 50 ? Color.YELLOW : Color.RED
        );
    }

    private void switchPlant(int direction){
        //increment or decrement according to direction (direction of 0 does not increment or decrement)
        if ( (direction > 0) && (displayedPlantID < idList.size()) ){
            displayedPlantID++;
        }
        else if ( (direction < 0) && (displayedPlantID > 0) ){
            displayedPlantID--;
        }

        //disable buttons if at edge of range
        if (displayedPlantID <= 0){
            btnLeft.setEnabled(false);
            btnLeft.setVisibility(View.INVISIBLE);
        }
        else{
            btnLeft.setEnabled(true);
            btnLeft.setVisibility(View.VISIBLE);
        }
        if (displayedPlantID >= idList.size()-2){
            btnRight.setEnabled(false);
            btnRight.setVisibility(View.INVISIBLE);
        }
        else {
            btnRight.setEnabled(true);
            btnRight.setVisibility(View.VISIBLE);
        }
        //set textview for plant id
        txtPlant.setText("Plant " + idList.get(displayedPlantID));
        txtName.setText(ConnectionMethods.getPlantName(idList.get(displayedPlantID)));
        setRatingText(idList.get(displayedPlantID));
        setBtnRate();
    }

    private void setBtnRate(){
        char[] text = "Set as current plant".toCharArray();
        if(isCurrentPlant()){
            text = "Rate health".toCharArray();
        }
        btnSetCurrent.setText(text,0,text.length);
    }

    private boolean isCurrentPlant(){
        return currentPlantID.equals(idList.get(displayedPlantID));
    }

}
