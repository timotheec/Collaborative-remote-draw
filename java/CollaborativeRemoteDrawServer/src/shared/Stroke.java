package shared;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared class between the client and the server to advantage of the both side using java
 * Represent a stroke in 2D space
 */
public class Stroke {

    /**
     * list of points representing the stroke
     */
    private List<Point> points = new ArrayList<>();

    /**
     * add a point to the stroke
     * @param point point to add
     */
    public void add(Point point) {
        points.add(point);
    }

    /**
     * length of the stroke
     * @return number of points
     */
    public int getLength() {
        return points.size();
    }

    /**
     * get a specific point from its index
     * @param i index of the point
     * @return point
     */
    public Point getPoint(int i){
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
