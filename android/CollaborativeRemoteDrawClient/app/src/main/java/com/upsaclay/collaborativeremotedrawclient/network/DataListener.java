package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public interface DataListener {
    void onReceiveImage(Bitmap image);
    void onRecieveStroke(Stroke stroke);
}
