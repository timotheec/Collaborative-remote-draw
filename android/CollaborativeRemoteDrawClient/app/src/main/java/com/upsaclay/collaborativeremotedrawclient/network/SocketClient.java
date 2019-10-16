package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class SocketClient extends AsyncTask<Void, Bitmap, Void> {
    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String host;
    private int port;
    private DataListener dataListener;

    private String[] listCommands = {"CLOSE"};

    public SocketClient(String host, int port, DataListener dataListener) {
        this.host = host;
        this.port = port;
        this.dataListener = dataListener;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        while (true) {
//            if (socket == null || isCancelled() || socket.isClosed())
//                break;

            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedInputStream(socket.getInputStream());

                // Send the command to the server
//                String commande = getCommand();
                Gson gson = new Gson();
                String commande = gson.toJson(new Stroke(new Point(1.0F, 1.2F)));

                writer.write(commande);
                writer.flush();

                Log.i("INFO", "Command " + commande + " sent to the server.");

                // Wait for the server answer
                if (commande == "CLOSE") {
                    String response = read();
                    Log.i("INFO", "\t * Server answer : " + response);
                }

            } catch (Exception e) {
                Log.i("INFO", "Connection closed");
//                break;
            }
//        }

        closeSocket();
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... progress) {
        dataListener.onReceiveImage(progress[0]);
    }

    private void closeSocket() {
        if (socket != null && !socket.isClosed())
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("ERROR", "Socket could not close.");
            }
    }

    // Temporary method to get a command for the server
    private String getCommand() {
        Random rand = new Random();
        return listCommands[rand.nextInt(listCommands.length)];
    }

    // Read answer from the server
    private String read() throws IOException {
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

}
