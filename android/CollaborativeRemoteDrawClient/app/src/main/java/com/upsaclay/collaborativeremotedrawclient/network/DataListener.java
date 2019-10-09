package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;

public interface DataListener {
    void onReceiveImage(Bitmap image);
}
