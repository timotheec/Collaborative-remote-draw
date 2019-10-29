package network;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.google.gson.Gson;

import shared.Stroke;

public class NetworkHelper {

	// Helper to get the ip of the host running the app
	public static String getHostAdress() {
		String host = "127.0.0.1";
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002); // No need to be reachable
			host = socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return host;
	}

	// Send an image trough network
	public static void sendImage(BufferedImage image, final Socket socket) {
		try {
			byte[] imgBytes = ((DataBufferByte) image.getData().getDataBuffer()).getData();
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeInt(image.getWidth());
			out.writeInt(image.getHeight());
			out.write(imgBytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	// Send string trough network
	public static void sendData(final String message, final Socket socket) {		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					writer.write(message);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}
			}
		}).start();
	}
	
	// Send string trough network
	public static void sendStroke(final Stroke stroke, final Socket socket) {
		sendData(new Gson().toJson(stroke), socket);
	}

}
