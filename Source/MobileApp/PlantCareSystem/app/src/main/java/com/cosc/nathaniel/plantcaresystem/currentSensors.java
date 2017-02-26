package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class currentSensors extends AppCompatActivity implements View.OnClickListener{

    LinearLayout itemWater, itemLight, itemTemp, itemHumidity;
    TextView readWater, readLight, readTemp, readHumidity;

    int waterLevel = 1, lightLevel = 2, tempLevel = 3, humLevel = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_sensors);

        itemWater = (LinearLayout) findViewById(R.id.itemWater);
        itemLight = (LinearLayout) findViewById(R.id.itemLight);
        itemTemp = (LinearLayout) findViewById(R.id.itemTemp);
        itemHumidity = (LinearLayout) findViewById(R.id.itemHumidity);
        itemWater.setOnClickListener(this);
        itemLight.setOnClickListener(this);
        itemTemp.setOnClickListener(this);
        itemHumidity.setOnClickListener(this);

        readWater = (TextView) findViewById(R.id.readWater);
        String level = Integer.toString(waterLevel);
        readWater.setText(level.toCharArray(), 0, level.length());
        readLight = (TextView) findViewById(R.id.readLight);
        level = Integer.toString(lightLevel);
        readLight.setText(level.toCharArray(), 0, level.length());
        readTemp = (TextView) findViewById(R.id.readTemp);
        level = Integer.toString(tempLevel);
        readTemp.setText(level.toCharArray(), 0, level.length());
        readHumidity = (TextView) findViewById(R.id.readHumidity);
        level = Integer.toString(humLevel);
        readHumidity.setText(level.toCharArray(), 0, level.length());
    }

    @Override
    public void onClick(View v) {
        if (v.equals(itemWater)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Water"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("currentSensors", "Failed to launch new activity.");
            }

            Log.e("Clicked: ", "WATER");
        }
        else if (v.equals(itemLight)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Light"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("currentSensors", "Failed to launch new activity.");
            }

            Log.e("Clicked: ", "LIGHT");
        }
        else if (v.equals(itemTemp)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Temperature"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("currentSensors", "Failed to launch new activity.");
            }

            Log.e("Clicked: ", "TEMP");
        }
        else if (v.equals(itemHumidity)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Humidity"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("currentSensors", "Failed to launch new activity.");
            }

            Log.e("Clicked: ", "HUMIDITY");
        }

    }
}
