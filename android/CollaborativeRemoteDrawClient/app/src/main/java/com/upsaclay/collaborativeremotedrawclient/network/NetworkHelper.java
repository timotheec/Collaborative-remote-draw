package com.upsaclay.collaborativeremotedrawclient.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {

    public static Bitmap receiveImage(Socket socket) {
        Bitmap image = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888); // create empty bitmap

        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            int w = in.readInt();
            int h = in.readInt();
            byte[] imgBytes = new byte[w * h * 4]; // 4 byte ABGR
            Log.i("INFO", "Image size (before): " + w + " : " + h);
            in.readFully(imgBytes);
            Log.i("INFO", "Image size (after): " + w + " : " + h);
            image = convertImage(imgBytes, w, h);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return image;
    }

    // Read answer from the server
    public static String readMessage(DataInputStream reader) throws IOException {
        String response;
        int size = reader.readInt();
        byte[] b = new byte[size];
        reader.readFully(b);
        response = new String(b, 0, size);
        return response;
    }

    public static List<Stroke> receiveStrokes(Socket socket) {
        List<Stroke> strokes = new ArrayList<>();
        Gson gson = new Gson();
        try {
            Type strokeListType = new TypeToken<ArrayList<Stroke>>(){}.getType();
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            strokes = gson.fromJson(readMessage(reader), strokeListType);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return strokes;
    }

    public static void sendMessage(final String message, final DataOutputStream out) {
        try {
            out.writeInt(message.length());
            out.writeBytes(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    // Convert a BufferedImage from java to an android Bitmap
    // Source : https://stackoverflow.com/questions/29453417/send-java-bufferedimage-to-bitmap-android/29476525#29476525
    private static Bitmap convertImage(byte[] buffuredImageBytes, int width, int heigth) {
        // Convert 4 byte interleaved ABGR to int packed ARGB
        int[] pixels = new int[width * heigth];
        for (int i = 0; i < pixels.length; i++) {
            int byteIndex = i * 4;
            pixels[i] =
                    ((buffuredImageBytes[byteIndex] & 0xFF) << 24)
                            | ((buffuredImageBytes[byteIndex + 3] & 0xFF) << 16)
                            | ((buffuredImageBytes[byteIndex + 2] & 0xFF) << 8)
                            | (buffuredImageBytes[byteIndex + 1] & 0xFF);
        }

        // Finally, create bitmap from packed int ARGB, using ARGB_8888
        return Bitmap.createBitmap(pixels, width, heigth, Bitmap.Config.ARGB_8888);
    }

}
