package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import main.Canvas;
import shared.Stroke;

// Source : https://openclassrooms.com/fr/courses/2654601-java-et-la-programmation-reseau/2668874-les-sockets-cote-serveur
public class Server implements DataListener {
	
	private final static int MAX_CLIENT_CONNECTIONS = 100;

	// Declare default values
	private int port = 2345;
	private String host = "127.0.0.1";
	private ServerSocket server = null;
	private boolean isRunning = true;
	private Canvas canvas;

	public Server(String pHost, int pPort, Canvas canvas) {
		host = pHost;
		port = pPort;
		try {
			server = new ServerSocket(port, MAX_CLIENT_CONNECTIONS, InetAddress.getByName(host));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		this.canvas = canvas;
	}

	public void open() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning == true) {
					try {
						// Waiting for a client connection
						Socket client = server.accept();

						// Process the requests in a separate thread
						System.out.println("Connexion cliente reçue.");
						Thread t = new Thread(new ClientProcessor(client, Server.this));
						t.start();

					} catch (IOException e) {
						e.printStackTrace(System.err);
					}
				}
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace(System.err);
					server = null;
				}
			}
		});

		t.start();
	}

	public void close() {
		isRunning = false;
	}

	@Override
	public void onRecieveStroke(Stroke stroke) {
		canvas.addStroke(stroke);		
	}
}
