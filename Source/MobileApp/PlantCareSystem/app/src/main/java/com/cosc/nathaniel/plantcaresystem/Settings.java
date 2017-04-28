package com.cosc.nathaniel.plantcaresystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    EditText etIP, etNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn = (Button) findViewById(R.id.btnSubmit);
        btn.setOnClickListener(this);
        etIP = (EditText) findViewById(R.id.etIP);
        etNotif = (EditText) findViewById(R.id.etNotif);
    }

    @Override
    public void onClick(View v) {
        //set fields in ConnectionMethods class
        String newIP = etIP.getText().toString();
        ConnectionMethods.setIP(newIP);
        int notifNum = Integer.parseInt(etNotif.getText().toString());
        ConnectionMethods.setNotifNum(notifNum);
        //return to start screen
        Intent in = new Intent(v.getContext(), StartActivity.class);
        startActivity(in);
        //TODO: store data to internal storage
    }
}
