package com.upsaclay.collaborativeremotedrawclient.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class SocketClient extends AsyncTask<Void, Void, Void> {
    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String host;
    private int port;

    private String[] listCommands = {"BACKGROUND", "CLOSE"};

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
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

        while (true) {
            if (socket == null || isCancelled() || socket.isClosed())
                break;

            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedInputStream(socket.getInputStream());

                // Send the command to the server
                String commande = getCommand();
                writer.write(commande);
                writer.flush();

                Log.i("INFO", "Command " + commande + " sent to the server.");

                // Wait for the server answer
                String response = read();
                Log.i("INFO", "\t * Server answer : " + response);
            } catch (Exception e) {
                Log.i("INFO", "Connection closed");
                break;
            }
        }

        closeSocket();
        return null;
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
