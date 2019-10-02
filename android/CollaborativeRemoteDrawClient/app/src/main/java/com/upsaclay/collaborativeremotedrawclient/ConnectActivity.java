package com.upsaclay.collaborativeremotedrawclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    public void connect(View view){
        //Create the intent
        Intent intent = new Intent(this, MainActivity.class);
        //Get the values
        EditText ipAddrInput = (EditText) findViewById(R.id.ipAddrInput);
        EditText portNumInput = (EditText) findViewById(R.id.portNumInput);
        //Add the value to the intent
        intent.putExtra("ip_addr", ipAddrInput.getText().toString());
        intent.putExtra("port_num", portNumInput.getText().toString());
        //Open the intent
        startActivity(intent);
    }
}
