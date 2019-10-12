package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Canvas extends JPanel {

	private File file;
	private CanvasView view;

	public Canvas() {
		view = new CanvasView(this);
	}

	public void setImage(String imagePath) throws IOException {
		file = new File(imagePath);
		view.setImage(ImageIO.read(file));
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		view.paint((Graphics2D) g);
	}

	@Override
	public Dimension getPreferredSize() {
		return view.getPreferredSize();
	}

}
