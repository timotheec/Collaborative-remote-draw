package network;

import java.io.DataInputStream;
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
	private DataInputStream reader = null;
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
				reader = new DataInputStream(sock.getInputStream());

				// We waiting for the client demand
				String response = read();

				// We print some information for debugging purpose
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();
				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Asking for host adress : " + remote.getAddress().getHostAddress() + ".";
				debug += " on port : " + remote.getPort() + ".\n";
				debug += "\t -> Command received : " + response + "\n";
//				System.out.println("\n" + debug);

				switch (response.toUpperCase()) {
				case "BACKGROUND":
					NetworkHelper.sendImage(AppConfig.getInstance().getImage(), sock);
					System.out.println("image sent");
					closeConnexion = true;
					break;
				case "CLOSE":
					closeConnexion = true;
					break;
				default:
					publishStroke(gson.fromJson(response, Stroke.class));
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
		int size = reader.readInt(); // TODO : create a bug when a client log out ?
		byte[] b = new byte[size];
		reader.readFully(b);
		return new String(b, 0, size);
	}

}
