package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

import java.util.ArrayList;

public class DrawViewModel {

    private ArrayList<Action> actionList;
    private Action curAction;
    private boolean performingAction;
    private float startX, startY, endX, endY;

    private Bitmap image;
    private boolean imageSet;

    public DrawViewModel(){
        actionList = new ArrayList<>();
        curAction = new Action();
        performingAction = false;
        startX = 0;
        startY = 0;
        imageSet = false;
    }

    public boolean imageIsSet() {
        return imageSet;
    }

    public Bitmap getImage() {
        return image;
    }

    public ArrayList<Action> getActionList() {
        return actionList;
    }

    public Action getCurAction() {
        return curAction;
    }

    public boolean isPerformingAction() { return performingAction; }

    public void setImage(Bitmap image){
        Log.i("INFO", "Image size : " + image.getHeight() + " : " + image.getWidth());

        this.image = image;

        imageSet = true;
    }

    public void beginAction(float x, float y){
        startX = x;
        startY = y;
    }

    public void continueAction(float x, float y){
        if(!performingAction){
            curAction = new Action();
            performingAction = true;
        }
        endX = x;
        endY = y;
        curAction.add(startX, startY);
        curAction.add(endX, endY);
        startX = endX;
        startY = endY;
    }

    public void endAction(){
        actionList.add(curAction);

        Stroke s = new Stroke();

        for (int i = 0; i < curAction.length; i++){
            s.add(new Point(curAction.getX(i),curAction.getY(i)));
        }
        Gson gson = new Gson();
        String command = gson.toJson(s);
        Log.i("Command to send", command);

        performingAction = false;
    }


}
