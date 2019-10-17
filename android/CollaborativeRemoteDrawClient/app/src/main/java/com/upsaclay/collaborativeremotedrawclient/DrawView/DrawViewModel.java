package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.util.Log;

import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DataSender;

import java.util.ArrayList;

public class DrawViewModel {

    private ArrayList<Action> actionList;
    private Action curAction;
    private boolean performingAction;
    private float startX, startY, endX, endY;

    private Bitmap image;
    private boolean imageSet;
    private DataSender dataSender;

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

        // send data to the server
        dataSender.send(s);

        performingAction = false;
    }


    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
