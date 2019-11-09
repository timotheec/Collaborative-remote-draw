package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shared.Stroke;
import shared.Zoom;

public class CanvasModel {

	private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	private List<Stroke> strokes = new ArrayList<>();
	private Zoom imageZoom = new Zoom(1.0f, 0, 0);

	public Zoom getZoom() {
		return imageZoom;
	}

	public void setZoom(Zoom zoom) {
		this.imageZoom = zoom;
		fireChangeListeners();
	}

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	private void fireChangeListeners() {
		for (ChangeListener listener : changeListeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}

	public synchronized void add(Stroke stroke) {
		strokes.add(stroke);
		fireChangeListeners();
	}

	public List<Stroke> getStrokes() {
		return Collections.unmodifiableList(strokes);
	}

}
