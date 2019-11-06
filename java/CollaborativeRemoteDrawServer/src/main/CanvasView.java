package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import shared.Stroke;
import shared.Zoom;

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
		Zoom zoom = calculateFinalZoom();

		if (image != null)
			g.drawImage(image, zoom.xOffset, zoom.yOffset, (int) (image.getWidth() * zoom.scale),
					(int) (image.getHeight() * zoom.scale), null);
	}

	private void paintStrokes(Graphics2D g) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));

		Zoom zoom = calculateFinalZoom();

		for (final Stroke stroke : controller.getStrokes())
			stroke.paint(g, zoom);
	}

	public Dimension getPreferredSize() {
		if (image != null)
			return new Dimension(image.getWidth(), image.getHeight());
		return new Dimension(WIDTH, HEIGHT);
	}

	private Zoom calculateFinalZoom() {
		if (image == null)
			return new Zoom(1.0f, 0, 0);

		Zoom screenZoom = calculateScreenZoom();
		Zoom imageZoom = controller.getZoom();

		Zoom zoom = new Zoom(imageZoom.scale * screenZoom.scale,
				(int) (imageZoom.xOffset * screenZoom.scale + screenZoom.xOffset),
				(int) (imageZoom.yOffset * screenZoom.scale + screenZoom.yOffset));

		return zoom;
	}

	// adjusts the scale and position of the image to center it on the screen
	private Zoom calculateScreenZoom() {
		int vw = controller.getSize().width;
		int vh = controller.getSize().height;

		Zoom screenZoom = new Zoom(1.0f, 0, 0);

		double imageRatio = image.getWidth() / (double) image.getHeight();
		double screenRatio = vw / (double) vh;
		if (imageRatio >= screenRatio) {
			// the image is wider than the screen, it should take the full width and be
			// centered vertically
			screenZoom.scale = vw / (float) image.getWidth();
			screenZoom.xOffset = 0;
			screenZoom.yOffset = (int) ((vh / 2.0) - (image.getHeight() * screenZoom.scale / 2));
		} else {
			// the image is taller than the screen, it should take the full height and be
			// centered horizontally
			screenZoom.scale = vh / (float) image.getHeight();
			screenZoom.xOffset = (int) ((vw / 2.0) - (image.getWidth() * screenZoom.scale / 2));
			screenZoom.yOffset = 0;
		}

		return screenZoom;
	}

}
