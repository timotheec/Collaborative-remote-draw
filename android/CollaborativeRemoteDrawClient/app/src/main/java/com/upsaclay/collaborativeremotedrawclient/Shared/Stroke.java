package com.upsaclay.collaborativeremotedrawclient.Shared;

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

}
