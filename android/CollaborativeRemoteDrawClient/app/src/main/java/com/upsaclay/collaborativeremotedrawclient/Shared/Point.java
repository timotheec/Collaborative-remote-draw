package com.upsaclay.collaborativeremotedrawclient.Shared;

public class Point {

    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //calculates the distance between this point and the other
    public float distance(Point p){
        float dist = (float) Math.sqrt((this.x - p.x)*(this.x - p.x) + (this.y - p.y)*(this.y - p.y));
        return dist;
    }
}
