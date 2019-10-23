package main;

public class Main {

	public static void main(String[] args) {
		// Launch the server
		final String HOST = network.NetworkHelper.getHostAdress();
		final int PORT = 5001;
		new network.Server(HOST, PORT).open();
		System.out.println("Server running on " + HOST + ":" + PORT);

		// Display main window
		DisplayWindow displayWindow = new DisplayWindow(" ip : " + HOST + " | port : " + PORT);
		displayWindow.setVisible(true);
	}

}
