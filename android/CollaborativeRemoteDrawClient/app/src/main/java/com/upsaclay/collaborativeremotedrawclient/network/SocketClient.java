package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketClient extends AsyncTask<Void, String, Void> implements DataListener {
    private Socket socket = null;
    private DataOutputStream writer = null;
    private DataInputStream reader = null;
    private String host;
    private int port;
    private DataListener dataListener;
    private Gson gson = new Gson();

    public SocketClient(String host, int port, DataListener dataListener) {
        this.host = host;
        this.port = port;
        this.dataListener = dataListener;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            socket = new Socket(host, port);
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            if (socket == null || isCancelled() || socket.isClosed())
                break;
            try {
                String response = NetworkHelper.readMessage(reader);
                publishProgress(response);
            } catch (Exception e) {
                Log.i("INFO", "Connection closed");
                break;
            }
        }

        closeSocket();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... messages) {
        for (String message : messages)
            dataListener.onRecieveStroke(gson.fromJson(message, Stroke.class));
    }

    private void closeSocket() {
        if (socket != null && !socket.isClosed())
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("ERROR", "Socket could not close.");
            }
    }

    @Override
    public void onReceiveImage(Bitmap image) {
    }

    @Override
    public void onRecieveStroke(Stroke stroke) {
        NetworkHelper.sendMessage(gson.toJson(stroke), writer);
    }
}
