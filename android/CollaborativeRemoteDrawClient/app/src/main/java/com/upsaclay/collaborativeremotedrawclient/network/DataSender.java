package com.upsaclay.collaborativeremotedrawclient.network;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public class DataSender implements Runnable {

    private Stroke stroke;
    private DataListener dataListener;

    public DataSender(Stroke stroke, DataListener dataListener) {
        this.stroke = stroke;
        this.dataListener =dataListener;
    }
    @Override
    public void run() {
        dataListener.onRecieveStroke(stroke);
    }

}
