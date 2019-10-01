import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class DisplayWindow extends JFrame{

	public DisplayWindow() {
		super("Collaborative Remote Draw");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupui();
	}
	
	private void setupui(){
		//menu bar
		JMenuBar menuBar = new JMenuBar();
		
		//File menu
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		//Network menu
		JMenu networkMenu = new JMenu("Network");
		menuBar.add(networkMenu);
		
		this.getContentPane().add(menuBar, BorderLayout.NORTH);
		this.setPreferredSize(new Dimension(500, 500));
		this.pack();
	}
}
