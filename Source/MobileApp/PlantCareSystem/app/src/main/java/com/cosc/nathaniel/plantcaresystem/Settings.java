package com.cosc.nathaniel.plantcaresystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    EditText etIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn = (Button) findViewById(R.id.btnSubmit);
        btn.setOnClickListener(this);
        etIP = (EditText) findViewById(R.id.etIP);

        //TODO notification number
    }

    @Override
    public void onClick(View v) {
        String newIP = etIP.getText().toString();
        ConnectionMethods.setIP(newIP);
        //TODO automatically return to main screen
    }
}
