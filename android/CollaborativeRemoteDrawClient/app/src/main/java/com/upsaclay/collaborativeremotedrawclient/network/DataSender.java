package com.upsaclay.collaborativeremotedrawclient.network;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;

public class DataSender implements Runnable {

    private DataListener dataListener;
    private Stroke stroke;
    private Zoom zoom;

    public DataSender(DataListener dataListener) {
        super();
        this.dataListener = dataListener;
    }

    public void send(Stroke stroke) {
        this.stroke = stroke;
        new Thread(this).start();
    }

    public void send(Zoom zoom) {
        this.zoom = zoom;
        new Thread(this).start();
    }

    @Override
    public void run() {
        if(stroke != null)
            dataListener.onRecieveStroke(stroke);
        else if(zoom != null)
            dataListener.onSendZoom(zoom);
        clear();
    }

    private void clear() {
        this.stroke = null;
        this.zoom = null;
    }

}
