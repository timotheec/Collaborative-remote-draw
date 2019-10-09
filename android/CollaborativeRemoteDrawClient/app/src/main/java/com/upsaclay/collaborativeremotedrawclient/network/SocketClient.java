package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
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

    private String[] listCommands = {"BACKGROUND"};

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
                String commande = getCommand();
                writer.write(commande);
                writer.flush();

                Log.i("INFO", "Command " + commande + " sent to the server.");

                // Wait for the server answer
                if (commande == "BACKGROUND") {
                    Bitmap image = readImage();
                    publishProgress(image);
                } else if (commande == "CLOSE") {
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

    private Bitmap readImage() throws Exception {

        // Source : https://stackoverflow.com/questions/29453417/send-java-bufferedimage-to-bitmap-android/29476525#29476525
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int w = in.readInt();
        int h = in.readInt();
        byte[] imgBytes = new byte[w * h * 4]; // 4 byte ABGR
        in.readFully(imgBytes);

        // Convert 4 byte interleaved ABGR to int packed ARGB
        int[] pixels = new int[w * h];
        for (int i = 0; i < pixels.length; i++) {
            int byteIndex = i * 4;
            pixels[i] =
                    ((imgBytes[byteIndex] & 0xFF) << 24)
                            | ((imgBytes[byteIndex + 3] & 0xFF) << 16)
                            | ((imgBytes[byteIndex + 2] & 0xFF) << 8)
                            | (imgBytes[byteIndex + 1] & 0xFF);
        }

        // Finally, create bitmap from packed int ARGB, using ARGB_8888
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888);
    }
}
