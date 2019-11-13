package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// Worker class to download an image from the server without blocking UI thread
public class DownloadBackground extends AsyncTask<Void, Void, Bitmap> {

    private String host;
    private int port;
    private static final String command = "BACKGROUND";

    public DownloadBackground(String serverIp, int serverPort) {
        this.host = serverIp;
        this.port = serverPort;
    }

    @Override
    protected Bitmap doInBackground(Void... arg0) {
        Bitmap image = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        try (Socket socket = new Socket(host, port)) {
            // Ask the background to the server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            NetworkHelper.sendMessage(command, out);

            // Waiting for the server answer
            image = NetworkHelper.receiveImage(socket);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return image;
    }
}
