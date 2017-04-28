package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddPlant extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button btnAdd, btnSetTimer;
    EditText etName;
    ImageButton btn1, btn2, btn3, btn4, btn5, btn6;
    int colorID = 0;
    long touchTime = 0;
    long duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnSetTimer = (Button) findViewById(R.id.btnSetWater);
        btnSetTimer.setOnTouchListener(this);
        btn1 = (ImageButton) findViewById(R.id.color1);
        btn1.setOnClickListener(this);
        btn2 = (ImageButton) findViewById(R.id.color2);
        btn2.setOnClickListener(this);
        btn3 = (ImageButton) findViewById(R.id.color3);
        btn3.setOnClickListener(this);
        btn4 = (ImageButton) findViewById(R.id.color4);
        btn4.setOnClickListener(this);
        btn5 = (ImageButton) findViewById(R.id.color5);
        btn5.setOnClickListener(this);
        btn6 = (ImageButton) findViewById(R.id.color6);
        btn6.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);

        btnSetTimer.setEnabled(false);

        //tell arduino to be ready
        setArduinoAlertFlag();
        int i = 0;
        //wait for arduino response
        while(!arduinoIsReady() && i < 100){
            //wait 100ms
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {/*Do nothing*/}
            }, 100);
            //then increment
            i++;
        }

        if(i>=100){
            //arduino took too long to respond
            Toast.makeText(this, "Error connecting to Arduino", Toast.LENGTH_SHORT).show();
        }
        else {
            btnSetTimer.setEnabled(true);
        }
    }

    @Override
    //listener for submit button and color selection buttons
    public void onClick(View v) {
        if (v.equals(btnAdd)) {
            //get name from EditText
            String name = etName.getText().toString();
            //if string is empty, set placeholder
            if (name.isEmpty()){name = "Name";}
            //send add plant query to server
            ConnectionMethods.queryServer(ConnectionMethods.Q_ADD_PLANT +
                    name.replaceAll("\\s", "") + " " + //name (with white space removed)
                    10 + " " + //light
                    10 + " " + //water
                    10 + " " + //humidity
                    10 + " " + //temp
                    100 + " " + //health
                    duration + " " + //water timer
                    colorID //foliage color
            );
            try {
                Intent in = new Intent(v.getContext(), StartActivity.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "Failed to launch new activity.");
            }
        }

        else if (v.equals(btn1)){
            colorID = 0;
        }
        else if (v.equals(btn2)){
            colorID = 1;
        }
        else if (v.equals(btn3)){
            colorID = 2;
        }
        else if (v.equals(btn4)){
            colorID = 3;
        }
        else if (v.equals(btn5)){
            colorID = 4;
        }
        else if (v.equals(btn6)){
            colorID = 5;
        }
    }

    @Override
    //listener for water timing button
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //send water on signl to arduino
            setWaterFlag(1);
            //set time that touch occured
            touchTime = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //send water off signal to arduino
            setWaterFlag(0);
            //get time that release occured and calculate difference
            duration = System.currentTimeMillis() - touchTime;
        }
        return true;
    }

    private void setArduinoAlertFlag() {
        String settings = ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS);
        String[] keys = {"lightFlag", "tempFlag", "humidFlag"};
        String query = "0 ";
        for(int i = 0; i < 3; i++){
            query = query + ConnectionMethods.parsePlant(settings, keys[i]) + " ";
        }
        query = query + "1";
        ConnectionMethods.queryServer(ConnectionMethods.Q_SET_SETTINGS + query);
    }

    private void setWaterFlag(int state) {
        String settings = ConnectionMethods.queryServer(ConnectionMethods.Q_GET_SETTINGS);
        String[] keys = {"lightFlag", "tempFlag", "humidFlag", "waitFlag"};
        String query = state + " ";
        for(int i = 0; i < 4; i++){
            query = query + ConnectionMethods.parsePlant(settings, keys[i]) + " ";
        }
        ConnectionMethods.queryServer(ConnectionMethods.Q_SET_SETTINGS + query);
    }

    private boolean arduinoIsReady() {
        return ConnectionMethods.queryServer(ConnectionMethods.Q_GET_WAITING).equals("1");
    }

}
