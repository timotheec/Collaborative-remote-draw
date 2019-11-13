package network;

import shared.Stroke;

/**
 *  Used to notify the model and server when a stroke arrived or need to be sent.
 *
 */
public interface DataListener {
	
    void onRecieveStroke(Stroke stroke);

	void onSendingStroke(Stroke stroke);
	
}
