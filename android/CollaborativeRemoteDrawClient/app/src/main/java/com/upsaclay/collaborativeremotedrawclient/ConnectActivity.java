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

    /**
     * verifies the content of the fields, then launches main activity
     */
    public void connect(View view){
        //Get the values
        String ipAddr = ((EditText)findViewById(R.id.ipAddrInput)).getText().toString();
        String port = ((EditText)findViewById(R.id.portNumInput)).getText().toString();
        if(!ipAddr.isEmpty() && !port.isEmpty()) {
            //Create the intent
            Intent intent = new Intent(this, MainActivity.class);
            //Add the value to the intent
            AppConfig.getInstance().setServerIp(ipAddr);
            AppConfig.getInstance().setServerPort(Integer.valueOf(port));
            //Open the intent
            startActivity(intent);
        }
    }
}
