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
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //The user touches the screen, starts a new action
                model.beginAction(e.getX(), e.getY());
                this.mainPointerID = e.getPointerId(e.getActionIndex());
                break;
            case MotionEvent.ACTION_MOVE:
                //The user moves a finger, continue the action
                int eventPointerID = e.getPointerId(e.getActionIndex());
                if(eventPointerID == this.mainPointerID){
                    model.continueAction(e.getX(), e.getY());
                }
                if(eventPointerID == this.secondPointerID){
                    model.moveSecondPointer(e.getX(), e.getY());
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //The user releases the screen, end the action

                model.endAction();
                //this.invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //The user presses a second finger
                model.addSecondPointer(e.getX(), e.getY());
                this.secondPointerID = e.getPointerId(e.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //The user releases the second finger
                model.removeSecondPointer(e.getX(), e.getY());
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
        model.zoom();
        this.invalidate();
    }

    public void centerImage(){
        model.centerImage();
        this.invalidate();
    }
}
