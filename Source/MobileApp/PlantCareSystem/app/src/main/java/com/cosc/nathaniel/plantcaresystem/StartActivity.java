package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSettings, btnView, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnView = (Button) findViewById(R.id.btnView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSettings.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSettings)){
            try {
                Intent in = new Intent(v.getContext(), Settings.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "StartActivity: Failed to launch settings activity.");
            }
        }
        else if (v.equals(btnAdd)){
            try {
                Intent in = new Intent(v.getContext(), AddPlant.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "StartActivity: Failed to launch add activity.");
            }
        }
        else if (v.equals(btnView)){
            try {
                //TODO: check whether connection successful
                Intent in = new Intent(v.getContext(), MainActivity.class);
                startActivity(in);
            } catch (Exception e) {
                Log.e("DEBUG", "StartActivity: Failed to launch add activity.");
            }
        }
    }
}
