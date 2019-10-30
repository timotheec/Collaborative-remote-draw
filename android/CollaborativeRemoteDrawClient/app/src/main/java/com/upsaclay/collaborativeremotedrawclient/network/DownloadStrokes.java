package com.upsaclay.collaborativeremotedrawclient.network;

import android.os.AsyncTask;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DownloadStrokes extends AsyncTask<Void, Void, List<Stroke>> {

    private String host;
    private int port;

    private static final String command = "ALL_STROKES";

    public DownloadStrokes(String serverIp, int serverPort) {
        this.host = serverIp;
        this.port = serverPort;
    }

    @Override
    protected List<Stroke> doInBackground(Void... arg0) {
        List<Stroke> strokes = new ArrayList<>();

        try (Socket socket = new Socket(host, port)) {
            // Ask the background to the server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            NetworkHelper.sendMessage(command, out);

            // Waiting for the server answer
            strokes = NetworkHelper.receiveStrokes(socket);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return strokes;
    }
}
