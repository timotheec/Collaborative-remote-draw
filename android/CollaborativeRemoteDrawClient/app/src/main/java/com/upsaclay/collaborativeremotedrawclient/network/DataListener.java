package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public interface DataListener {
    void onRecieveStroke(Stroke stroke);
}
