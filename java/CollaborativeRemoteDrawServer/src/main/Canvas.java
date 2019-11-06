package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import network.DataListener;
import shared.Stroke;
import shared.Zoom;

public class Canvas extends JPanel {

	private File file;
	private CanvasView view;
	private CanvasModel model;
	
	private DataListener dataListener;

	public Canvas() {
		view = new CanvasView(this);
		setModel(new CanvasModel());
	}
	
	private void setModel(CanvasModel model) {
		this.model = model;
		model.addChangeListener(event -> repaint());
	}

	public void setImage(String imagePath) throws IOException {
		file = new File(imagePath);
		view.setImage(ImageIO.read(file));
		repaint();
	}
	
	public void addStroke(Stroke stroke) {
		model.add(stroke);
		dataListener.onSendingStroke(stroke);
	}
	
	public List<Stroke> getStrokes() {
		return model.getStrokes();
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

	public void setDatalistener(DataListener dataListener) {
		this.dataListener = dataListener;
	}

	public void setZoom(Zoom zoom) {
		model.setZoom(zoom);
	}

	public Zoom getZoom() {
		return model.getZoom();
	}

}
