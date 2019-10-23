package main;

public class Main {

	public static void main(String[] args) {
		final String HOST = network.NetworkHelper.getHostAdress();
		final int PORT = 5001;

		// Display main window
		DisplayWindow displayWindow = new DisplayWindow(" ip : " + HOST + " | port : " + PORT);
		displayWindow.setVisible(true);
		
		// Launch the server
		new network.Server(HOST, PORT, displayWindow.getCanvas()).open();
		System.out.println("Server running on " + HOST + ":" + PORT);
	}

}
