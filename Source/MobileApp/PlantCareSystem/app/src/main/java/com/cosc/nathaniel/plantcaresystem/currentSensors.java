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
        String level = getLevel("water");
        readWater.setText(level.toCharArray(), 0, level.length());
        readLight = (TextView) findViewById(R.id.readLight);
        level = getLevel("light");
        readLight.setText(level.toCharArray(), 0, level.length());
        readTemp = (TextView) findViewById(R.id.readTemp);
        level = getLevel("temp");
        readTemp.setText(level.toCharArray(), 0, level.length());
        readHumidity = (TextView) findViewById(R.id.readHumidity);
        level = getLevel("humid");
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
                Log.e("DEBUG", "Failed to launch new activity.");
            }

            Log.e("DEBUG", "WATER");
        }
        else if (v.equals(itemLight)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Light"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "Failed to launch new activity.");
            }

            Log.e("DEBUG", "LIGHT");
        }
        else if (v.equals(itemTemp)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Temperature"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "Failed to launch new activity.");
            }

            Log.e("DEBUG", "TEMP");
        }
        else if (v.equals(itemHumidity)){
            // start PrevRead activity
            try {
                Intent in = new Intent(v.getContext(), PrevRead.class);
                in.putExtra("SENSOR", "Humidity"); //pass which sensor to get readings from
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "Failed to launch new activity.");
            }

            Log.e("DEBUG", "HUMIDITY");
        }

    }

    private String getLevel(String key){
        return ConnectionMethods.parsePlant(ConnectionMethods.queryServer(ConnectionMethods.Q_CURRENT), key);
    }
}
