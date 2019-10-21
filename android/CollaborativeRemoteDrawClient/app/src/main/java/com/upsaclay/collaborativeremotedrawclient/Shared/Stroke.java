package com.upsaclay.collaborativeremotedrawclient.Shared;

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

    public Point getPoint(int i){
        return points.get(i);
    }

}
