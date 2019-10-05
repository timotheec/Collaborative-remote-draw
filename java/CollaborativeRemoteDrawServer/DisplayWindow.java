import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DisplayWindow extends JFrame {
	
	private Canvas canvas;
	private JFileChooser fileChooser;

	public DisplayWindow() {
		super("Collaborative Remote Draw");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupUi();
	}

	private void setupUi() {
		setupFileChooser();
		setupMenu();
		setupCanvas();

		pack();
	}

	private void setupFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (png, gif, jpg, bmp)", "png", "gif", "jpg",
				"jpeg", "bmp");
		fileChooser.addChoosableFileFilter(filter);
	}

	private void setupCanvas() {
		canvas = new Canvas();
		getContentPane().add(canvas, BorderLayout.CENTER);
	}

	private void setupMenu() {
		// Menu bar
		JMenuBar menuBar = new JMenuBar();

		// File menu
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem importPhoto = new JMenuItem("Import");
		importPhoto.addActionListener(e -> {
			int chooserStatus = fileChooser.showOpenDialog(this);
			if (chooserStatus == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isFile())
				try {
					canvas.setImage(fileChooser.getSelectedFile().getPath());
					pack();
				} catch (IOException exception) {
					// TODO : Show up the error on a dialog window
					System.err.println(exception.getMessage());
				}
		});
		fileMenu.add(importPhoto);

		// Network menu
		JMenu networkMenu = new JMenu("Network");
		menuBar.add(networkMenu);

		getContentPane().add(menuBar, BorderLayout.NORTH);
	}
	
}
