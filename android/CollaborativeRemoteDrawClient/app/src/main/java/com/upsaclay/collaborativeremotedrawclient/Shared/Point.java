package com.upsaclay.collaborativeremotedrawclient.Shared;

/**
 * Shared class between the client and the server to advantage of the both side using java
 * Represents a point in 2D space
 */
public class Point {

    /**
     * coordinates of the point
     */
    public float x, y;

    /**
     * initialize the point with given coordinates
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //calculates the distance between this point and the other

    /**
     * calculates the distance between this point and another
     * @param p point to measure distance with
     * @return distance between the two points
     */
    public float distance(Point p){
        return (float) Math.sqrt((this.x - p.x)*(this.x - p.x) + (this.y - p.y)*(this.y - p.y));
    }
}
