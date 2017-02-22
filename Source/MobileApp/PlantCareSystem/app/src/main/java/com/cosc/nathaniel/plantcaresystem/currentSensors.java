package com.cosc.nathaniel.plantcaresystem;

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
            Log.e("Clicked: ", "WATER");
        }
        else if (v.equals(itemLight)){
            Log.e("Clicked: ", "LIGHT");
        }
        else if (v.equals(itemTemp)){
            Log.e("Clicked: ", "TEMP");
        }
        else if (v.equals(itemHumidity)){
            Log.e("Clicked: ", "HUMIDITY");
        }

    }
}
