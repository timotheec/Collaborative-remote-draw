package com.upsaclay.collaborativeremotedrawclient.network;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;

public interface DataListener {
    void onRecieveStroke(Stroke stroke);
    void onSendZoom(Zoom zoom);
}
