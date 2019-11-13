package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.upsaclay.collaborativeremotedrawclient.AppConfig;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DownloadBackground;
import com.upsaclay.collaborativeremotedrawclient.network.DownloadStrokes;

import java.util.concurrent.ExecutionException;

public class DrawView extends View implements DataListener {

    /**
     * model of the interface
     */
    private DrawViewModel model;

    /**
     * view of the interface
     */
    private DrawViewView view;

    /**
     * used to track the main pointer in a touch event
     */
    private int mainPointerID;

    /**
     * used to track the second pointer in a touch event
     */
    private int secondPointerID;

    /**
     * initialize the DrawView and its variables.
     * starts connection with server
     */
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

    /**
     * get reference to the model
     * @return model
     */
    public DrawViewModel getModel() {
        return model;
    }

    /**
     * add the dataListeer to the model
     * @param dataListener dataListener to add to the model
     */
    public void setDataListener(DataListener dataListener) {
        model.setDataListener(dataListener);
    }

    /**
     * handle touch event by tracking the main pointer and a secondary pointer, then sending their actions to the model
     * @param e event to handle
     */
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
                this.invalidate();
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

    /**
     * handle view size changes, update the model and refresh the view
     * @param w new width
     * @param h new height
     * @param oldw old width
     * @param oldh old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        model.setViewSize(w, h);
        this.invalidate();
    }

    /**
     * handles drawing by calling the view
     * @param canvas the canvas on which to draw
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        view.draw(canvas);
    }

    /**
     * receive a stroke from the server, add it to the model and refresh the view
     * @param stroke the received stroke
     */
    @Override
    public void onRecieveStroke(Stroke stroke) {
        model.addStroke(stroke);
        this.invalidate();
    }

    /**
     * useless here but required to implement interface
     */
    @Override
    public void onSendZoom(Zoom zoom) { }

    /**
     * zoom mode selected
     */
    public void zoom(){
        model.setTouchMode(0);
    }

    /**
     * draw mode selected
     */
    public void draw(){
        model.setTouchMode(1);
    }

    /**
     * center the image to fit on the screen
     */
    public void centerImage(){
        model.resetZoom();
        this.invalidate();
    }

    /**
     * send the current display to the server
     */
    public void sendDisplay(){
        model.sendDisplay();
    }
}
