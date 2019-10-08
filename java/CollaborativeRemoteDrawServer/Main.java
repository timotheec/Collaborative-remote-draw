
public class Main {

	public static void main(String[] args) {
		// Launch the server
		final String HOST = "10.55.227.3";
		final int PORT = 5001;
		new network.Server(HOST, PORT).open();
		System.out.println("Server running on " + HOST + ":" + PORT);
		
		// Display main window
		DisplayWindow displayWindow = new DisplayWindow();
		displayWindow.setVisible(true);
	}

}
