package com.upsaclay.collaborativeremotedrawclient.Shared;

import java.util.ArrayList;
import java.util.List;

public class Stroke {

    private List<Point> points = new ArrayList<>();

    public Stroke(Point origin) {
        points.add(origin);
    }

    public void add(Point point) {
        points.add(point);
    }

}
