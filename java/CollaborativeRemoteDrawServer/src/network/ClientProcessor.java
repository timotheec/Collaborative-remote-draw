package network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;

import main.AppConfig;
import shared.Stroke;

// Source : https://openclassrooms.com/fr/courses/2654601-java-et-la-programmation-reseau/2668874-les-sockets-cote-serveur
// Note : code is inspire from an openclassrooms solutions but completly adpated to our needs.
public class ClientProcessor implements Runnable {

	private Socket sock;
	private BufferedInputStream reader = null;
	private DataListener dataListener;
	private Gson gson = new Gson();

	public ClientProcessor(Socket pSock, DataListener dataListener) {
		sock = pSock;
		this.dataListener = dataListener;
	}

	@Override
	public void run() {
		boolean closeConnexion = false;

		// While the connection is open we treat the demand
		while (!sock.isClosed()) {
			try {
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
//				System.out.println("\n" + debug);

				// We treat the demand correctly
				String toSend = "";

				switch (response.toUpperCase()) {
				case "BACKGROUND":
					NetworkHelper.sendImage(AppConfig.getInstance().getImage(), sock);
					System.out.println("image sent");
					closeConnexion = true;
					break;
				case "CLOSE":
					toSend = "Cummunication closed";
					closeConnexion = true;
					break;
				default:
					publishStroke(gson.fromJson(response, Stroke.class));
					toSend = "Unknwon command";
					break;
				}

				if (closeConnexion) {
					System.err.println("CONNETION CLOSED DETECTED");
					reader = null;
					sock.close();
					break;
				}
			} catch (SocketException e) {
				e.printStackTrace(System.err);
				System.err.println("CONNECTION INTERRUPTED");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void publishStroke(Stroke stroke) {
		dataListener.onRecieveStroke(stroke);
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
