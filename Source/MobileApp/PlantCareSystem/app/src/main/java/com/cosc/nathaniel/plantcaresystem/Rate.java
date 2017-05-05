package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

//the rate activity takes input from the user and sends it to the server
public class Rate extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    RadioGroup rdoGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        rdoGrp = (RadioGroup) findViewById(R.id.radioGroup);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSubmit)){
            //get selected button
            int selected = rdoGrp.getCheckedRadioButtonId();
            View radioButton = rdoGrp.findViewById(selected);
            selected = rdoGrp.indexOfChild(radioButton);
            //send rating to server
            ConnectionMethods.queryServer(ConnectionMethods.Q_SET_SCORE + Integer.toString(selected));
            //return to view activity
            Intent in = new Intent(v.getContext(), MainActivity.class);
            startActivity(in);
        }
    }
}