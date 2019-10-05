package network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

// Source : https://openclassrooms.com/fr/courses/2654601-java-et-la-programmation-reseau/2668874-les-sockets-cote-serveur
public class ClientProcessor implements Runnable {

	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	public ClientProcessor(Socket pSock) {
		sock = pSock;
	}

	@Override
	public void run() {
		System.err.println("Lancement du traitement de la connexion cliente");

		boolean closeConnexion = false;

		// While the connection is open we treat the demand
		while (!sock.isClosed()) {
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());

				// We waiting for the client demand
				String response = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();

				// We print some information for debugging purpose
				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Asking for host adress : " + remote.getAddress().getHostAddress() + ".";
				debug += " on port : " + remote.getPort() + ".\n";
				debug += "\t -> Command received : " + response + "\n";
				System.out.println("\n" + debug);

				// We treat the demand correctly
				String toSend = "";

				switch (response.toUpperCase()) {
				case "BACKGROUND":
					toSend = "image.jpg";
					break;
				case "CLOSE":
					toSend = "Cummunication closed";
					closeConnexion = true;
					break;
				default:
					toSend = "Unknwon command";
					break;
				}

				writer.write(toSend);
				writer.flush(); // Absolutly required for the client to receive the response

				if (closeConnexion) {
					System.err.println("CONNETION CLOSED DETECTED");
					writer = null;
					reader = null;
					sock.close();
					break;
				}
			} catch (SocketException e) {
				System.err.println("CONNECTION INTERRUPTED");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Read the response from the client
	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

}