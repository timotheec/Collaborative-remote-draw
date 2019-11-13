package com.upsaclay.collaborativeremotedrawclient.Shared;

import java.util.ArrayList;
import java.util.List;

// Shared class between the client and the server to advantage of the both side using java
// Represent a stroke in 2D space
public class Stroke {

    private List<Point> points = new ArrayList<>();

    public void add(Point point) {
        points.add(point);
    }

    public int getLength() {
        return points.size();
    }

    public Point getPoint(int i){
        return points.get(i);
    }

}
