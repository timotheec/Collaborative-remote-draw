package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DisplayWindow extends JFrame {
	
	private Canvas canvas;
	private JFileChooser fileChooser;
	private JLabel statusLabel;

	public DisplayWindow(String status) {
		super("Collaborative Remote Draw");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupUi();
		statusLabel.setText(status);
	}

	private void setupUi() {
		setupCanvas();
		setupStatusBar();
		setupFileChooser();

		pack();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	private void setupFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("Select an image");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (png, gif, jpg, bmp)", "png", "gif", "jpg",
				"jpeg", "bmp");
		fileChooser.addChoosableFileFilter(filter);
		askImage();
	}

	private void setupCanvas() {
		canvas = new Canvas();
		getContentPane().add(canvas, BorderLayout.CENTER);
	}
	
	private void setupStatusBar() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		final int STATUS_BAR_HEIGHT = 20;
		statusPanel.setPreferredSize(new Dimension(getContentPane().getWidth(), STATUS_BAR_HEIGHT));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		statusLabel = new JLabel("");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
	}
	
	private void askImage() {
		int chooserStatus = fileChooser.showOpenDialog(this);
		if (chooserStatus == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isFile())
			try {
				String imagePath = fileChooser.getSelectedFile().getPath();
				canvas.setImage(imagePath);
				AppConfig.getInstance().setImagePath(imagePath);
				pack();
			} catch (IOException exception) {
				// TODO : Show up the error on a dialog window
				System.err.println(exception.getMessage());
			}
	}
	
}
