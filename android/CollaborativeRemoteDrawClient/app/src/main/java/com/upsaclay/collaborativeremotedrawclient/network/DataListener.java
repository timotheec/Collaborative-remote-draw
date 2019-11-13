package com.upsaclay.collaborativeremotedrawclient.network;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;

// Used to notify the model and server when a stroke arrived or need to be sent.
public interface DataListener {
    void onRecieveStroke(Stroke stroke);
    void onSendZoom(Zoom zoom);
}
