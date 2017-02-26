package com.cosc.nathaniel.plantcaresystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PrevRead extends AppCompatActivity {

    String whichSensor = "ERROR";
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_read);

        whichSensor = getIntent().getStringExtra("SENSOR");
        Log.e(whichSensor, whichSensor);

        title = (TextView) findViewById(R.id.textTitle);
        String titleStr = "Previous " + whichSensor + " Readings";
        title.setText(titleStr.toCharArray(), 0, titleStr.length());
    }
}
