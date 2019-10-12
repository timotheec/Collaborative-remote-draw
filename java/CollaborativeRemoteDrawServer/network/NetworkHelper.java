package network;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkHelper {

	public static String getHostAdress() {
		String host = "127.0.0.1";
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			host = socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return host;
	}

}
