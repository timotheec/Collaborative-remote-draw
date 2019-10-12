package main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CanvasView {
	
	private final static int WIDTH = 500;
	private final static int HEIGHT = 500;
	
	private Canvas controller;
	private BufferedImage image;
	
	public CanvasView(Canvas canvas) {
		this.controller = canvas;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void paint(Graphics2D g) {
		paintImage(g);
	}
	
	private void paintImage(Graphics2D g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
	}
	
	public Dimension getPreferredSize() {
		if (image != null)
			return new Dimension(image.getWidth(), image.getHeight());
		return new Dimension(WIDTH, HEIGHT);
	}
	

}
