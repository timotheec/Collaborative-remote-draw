package main;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import shared.Stroke;

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
		paintStrokes(g);
	}
	
	private void paintImage(Graphics2D g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
	}
	
	private void paintStrokes(Graphics2D g) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		g.setStroke(new BasicStroke(2));
		
		for(final Stroke stroke : controller.getStrokes())
			stroke.paint(g);
	}
	
	public Dimension getPreferredSize() {
		if (image != null)
			return new Dimension(image.getWidth(), image.getHeight());
		return new Dimension(WIDTH, HEIGHT);
	}
	

}
