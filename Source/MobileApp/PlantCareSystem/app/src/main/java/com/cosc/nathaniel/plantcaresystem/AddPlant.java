package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddPlant extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd;
    //EditText etLight, etWater, etHumid, etTemp, etHealth;
    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        /*etLight = (EditText) findViewById(R.id.editLight);
        etWater = (EditText) findViewById(R.id.editWater);
        etHumid = (EditText) findViewById(R.id.editHumid);
        etTemp = (EditText) findViewById(R.id.editTemp);
        etHealth = (EditText) findViewById(R.id.editHealth);*/

        spType = (Spinner) findViewById(R.id.spinnerType);
        List<String> spinnerArray =  ConnectionMethods.getTypeList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String ptype = Integer.toString(spType.getSelectedItemPosition() + 1);
        /*String light = etLight.getText().toString();
        String water = etWater.getText().toString();
        String humid = etHumid.getText().toString();
        String temp = etTemp.getText().toString();
        String health = etHealth.getText().toString();*/

        ConnectionMethods.queryServer(ConnectionMethods.Q_ADD_PLANT +
            ptype + " " +
            10 + " " +
            10 + " " +
            10 + " " +
            10 + " " +
            100
        );

        ConnectionMethods.setNumOfPlants(1);

        try {
            Intent in = new Intent(v.getContext(), MainActivity.class);
            startActivity(in);
        } catch (Exception e) {
            Log.e("DEBUG", "Failed to launch new activity.");
        }
    }
}
