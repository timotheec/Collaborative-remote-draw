package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.upsaclay.collaborativeremotedrawclient.AppConfig;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DownloadBackground;
import com.upsaclay.collaborativeremotedrawclient.network.DownloadStrokes;

import java.util.concurrent.ExecutionException;

public class DrawView extends View implements DataListener {

    private DrawViewModel model;

    private DrawViewView view;

    //used to track the main pointer in a touch event
    private int mainPointerID;
    //used to track the second pointer in a touch event
    private int secondPointerID;

    public DrawView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        model = new DrawViewModel();
        view = new DrawViewView(this);

        mainPointerID = -1;
        secondPointerID = -1;

        try {
            String ipAddr = AppConfig.getInstance().getServerIp();
            int portNum = AppConfig.getInstance().getServerPort();
            model.setStrokeList(new DownloadStrokes(ipAddr, portNum).execute().get());
            model.setImage(new DownloadBackground(ipAddr, portNum).execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace(System.err);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    public DrawViewModel getModel() {
        return model;
    }

    public void setDataListener(DataListener dataListener) {
        model.setDataListener(dataListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //The user touches the screen, starts a new action
                model.beginAction(e.getX(), e.getY());
                this.mainPointerID = e.getPointerId(e.getActionIndex());
                break;
            case MotionEvent.ACTION_MOVE:
                //The user moves a finger, continue the action
                if(e.getPointerId(e.getActionIndex()) == this.mainPointerID){
                    model.continueAction(e.getX(), e.getY());
                }
                int secondPointerIndex = e.findPointerIndex(this.secondPointerID);
                if(secondPointerIndex != -1){
                    model.moveSecondPointer(e.getX(secondPointerIndex), e.getY(secondPointerIndex));
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //The user releases the screen, end the action
                model.endAction();
                this.mainPointerID = -1;
                this.secondPointerID = -1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //The user presses a second finger
                if(secondPointerID == -1) {
                    model.addSecondPointer(e.getX(e.getActionIndex()), e.getY(e.getActionIndex()));
                    this.secondPointerID = e.getPointerId(e.getActionIndex());
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //The user releases one of the two fingers
                if(e.getPointerId(e.getActionIndex()) == this.mainPointerID) {
                    model.endAction();
                    model.beginAction(e.getX(), e.getY());
                }
                if(e.getPointerId(e.getActionIndex()) == this.secondPointerID) {
                    model.removeSecondPointer();
                    this.secondPointerID = -1;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        model.setViewSize(w, h);
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        view.draw(canvas);
    }

    // stroke received from the server
    @Override
    public void onRecieveStroke(Stroke stroke) {
        model.addStroke(stroke);
        this.invalidate();
    }

    public void zoom(){
        model.setTouchMode(0);
    }

    public void draw(){
        model.setTouchMode(1);
    }
    public void centerImage(){
        model.centerImage();
        this.invalidate();
    }

    public void sendDisplay(){
        model.sendDisplay();
    }
}
