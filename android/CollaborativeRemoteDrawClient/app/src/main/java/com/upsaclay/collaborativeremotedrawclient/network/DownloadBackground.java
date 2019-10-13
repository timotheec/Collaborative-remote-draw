package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class DownloadBackground extends AsyncTask<Void, Void, Bitmap> {

    private String host;
    private int port;
    private DataListener dataListener;

    private static final String command = "BACKGROUND";

    public DownloadBackground(String serverIp, int serverPort, DataListener dataListener) {
        this.host = serverIp;
        this.port = serverPort;
        this.dataListener = dataListener;
    }

    @Override
    protected Bitmap doInBackground(Void... arg0) {
        Bitmap image = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        try (Socket socket = new Socket(host, port)) {
            // Ask the background to the server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.write(command);
            writer.flush();

            // Waiting for the server answer
            image = NetworkHelper.receiveImage(socket);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        if (dataListener != null)
            dataListener.onReceiveImage(image);
    }
}
