package network;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

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

}
