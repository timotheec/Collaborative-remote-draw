package com.upsaclay.collaborativeremotedrawclient.network;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public class DataSender implements Runnable {

    private DataListener dataListener;
    private Stroke stroke;

    public DataSender(DataListener dataListener) {
        super();
        this.dataListener = dataListener;
    }

    public void send(Stroke stroke) {
        this.stroke = stroke;
        new Thread(this).start();
    }

    @Override
    public void run() {
        dataListener.onRecieveStroke(stroke);
    }

}
