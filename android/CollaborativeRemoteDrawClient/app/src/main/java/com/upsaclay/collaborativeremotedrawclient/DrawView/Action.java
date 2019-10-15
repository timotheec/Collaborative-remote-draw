package com.upsaclay.collaborativeremotedrawclient.DrawView;

import java.util.ArrayList;

public class Action {
    private ArrayList<Float> xCoords, yCoords;
    int length;

    Action(){
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();
        length = 0;
    }

    public int getLength() {
        return length;
    }

    public float getX(int i) {
        return xCoords.get(i);
    }

    public float getY(int i) {
        return yCoords.get(i);
    }

    public void add(float x, float y) {
        xCoords.add(x);
        yCoords.add(y);
        length++;
    }
}
