package shared;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Stroke {

	private List<Point> points = new ArrayList<>();

	public void add(Point point) {
		points.add(point);
	}

	public int getLength() {
		return points.size();
	}

	public Point getPoint(int i) {
		return points.get(i);
	}

	public void paint(Graphics2D g, Zoom zoom) {
		if (points.size() < 2)
			return;

		for (int i = 0; i < points.size() - 1; i++) {
			Point firstPt = points.get(i);
			Point secondPt = points.get(i + 1);
			g.drawLine((int) (firstPt.x * zoom.scale + zoom.xOffset), (int) (firstPt.y * zoom.scale + zoom.yOffset),
					(int) (secondPt.x * zoom.scale + zoom.xOffset), (int) (secondPt.y * zoom.scale + zoom.yOffset));
		}
	}

}
