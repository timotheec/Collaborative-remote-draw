package network;

import shared.Stroke;

public interface DataListener {
    void onRecieveStroke(Stroke stroke);
}