package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // button variables
    Button btnSettings, btnSensors, btnDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate buttons
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnDatabase = (Button) findViewById(R.id.btnDatabase);
        btnSettings.setOnClickListener(this);
        btnSensors.setOnClickListener(this);
        btnDatabase.setOnClickListener(this);
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
        else if (v.equals(btnDatabase)){
            try {
                Intent in = new Intent(v.getContext(), Database.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to launch new activity.");
            }
        }
    }
}
