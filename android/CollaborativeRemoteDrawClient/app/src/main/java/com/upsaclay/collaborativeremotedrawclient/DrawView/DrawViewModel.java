package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.util.Log;

import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DataSender;

import java.util.ArrayList;
import java.util.List;

public class DrawViewModel {
    //size of the view
    private int vw, vh;

    //DataSender used to send inputs to the server
    private DataSender dataSender;

    //used to store image data
    private Bitmap image;
    private boolean imageSet;

    //display properties
    //image offset (distance from top left corner of the image)
    private int oxI, oyI;
    //image scale
    private float scale;
    //screen offset (distance from top left corner of the display)
    private int oxS, oyS;
    //screen scale
    private float scaleS;

    //touchscreen mode (0 = zoom, 1 = draw)
    private int touchMode;

    //used to handle zooming
    //previous position of first finger
    private Point previousPos1;
    //previous position of second finger
    private Point previousPos2;
    //true if more than one finger is on the screen
    private boolean multitouch;

    //used to store stroke data
    private List<Stroke> strokeList;
    private Stroke curStroke;
    private boolean performingAction;
    private Point start, end;

    //used to handle zooming

    public DrawViewModel(){
        vw = 1;
        vh = 1;


        imageSet = false;

        oxI = 0;
        oyI = 0;
        scale = 1;

        oxS = 0;
        oyS = 0;
        scaleS = 1;

        touchMode = 0;

        previousPos1 = new Point(0, 0);
        previousPos2 = new Point(0, 0);

        strokeList = new ArrayList<>();
        curStroke = new Stroke();
        performingAction = false;
        start = new Point(0, 0);
    }

    public boolean imageIsSet() {
        return imageSet;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getOx() { return (int)(oxS + scaleS * oxI); }

    public int getOy() {
        return (int)(oyS + scaleS * oyI);
    }

    public float getScale() {
        return scaleS * scale;
    }

    public List<Stroke> getStrokeList() {
        return strokeList;
    }

    public Stroke getCurStroke(){
        return curStroke;
    }

    public boolean isPerformingAction() {
        return performingAction;
    }

    public void setStrokeList(List<Stroke> strokeList) {
        this.strokeList = strokeList;
    }

    public void setImage(Bitmap image){
        Log.i("INFO", "Image size : " + image.getWidth() + " : " + image.getHeight());

        this.image = image;

        centerImage();

        imageSet = true;
    }

    public void  setViewSize(int vw, int vh){
        this.vw = vw;
        this.vh = vh;

        //resets image position for new display
        if(imageIsSet()) centerImage();
    }

    //adjusts the scale and position of the image to center it on the screen
    public void centerImage(){
        double imageRatio = image.getWidth() / (double)image.getHeight();
        double screenRatio = vw / (double)vh;
        if(imageRatio >= screenRatio){
            //the image is wider than the screen, it should take the full width and be centered vertically
            scaleS = vw / (float)image.getWidth();
            oxS = 0;
            oyS = (int)((vh/2.0) - (image.getHeight()*scaleS/2));
        } else {
            //the image is taller than the screen, it should take the full height and be centered horizontally
            scaleS = vh / (float)image.getHeight();
            oxS = (int)((vw/2.0) - (image.getWidth()*scaleS/2));
            oyS = 0;
        }

        //reset zoom
        oxI = 0;
        oyI = 0;
        scale = 1;
    }

    public float screenToImageX(float x){
        return (x - getOx()) / getScale();
    }

    public float screenToImageY(float y){
        return (y - getOy()) / getScale();
    }

    public float imageToScreenX(float x){
        return x*getScale() + getOx();
    }

    public float imageToScreenY(float y){
        return y*getScale() + getOy();
    }

    public void setTouchMode(int touchMode) {
        this.touchMode = touchMode;
    }

    public void sendDisplay(){
        Log.i("DIDPLAY","scale: " + scale + " offset: " + oxI + " " + oyI);
    }

    public void beginAction(float x, float y){
        switch (this.touchMode){
            case 0:
                this.previousPos1 = new Point(x, y);
                break;
            case 1:
                start = new Point(screenToImageX(x), screenToImageY(y));
                break;
        }
    }

    public void continueAction(float x, float y){
        switch(this.touchMode) {
            case 0:
                oxI += (x - previousPos1.x)/scaleS;
                oyI += (y - previousPos1.y)/scaleS;
                Point newPos = new Point(x, y);
                if(multitouch){
                    zoom(newPos.distance(previousPos2)/previousPos1.distance(previousPos2));
                }
                previousPos1 = newPos;
                break;
            case 1:
                if (!performingAction) {
                    curStroke = new Stroke();
                    performingAction = true;
                }
                end = new Point(screenToImageX(x), screenToImageY(y));
                curStroke.add(start);
                curStroke.add(end);
                start = end;
                break;
        }
    }

    public void endAction(){
        if(touchMode == 1) {
            dataSender.send(curStroke);
            performingAction = false;
        }
        multitouch = false;
    }

    public void addSecondPointer(float x, float y){
        if(touchMode == 0) {
            previousPos2 = new Point(x, y);
            multitouch = true;
        }
    }

    public void moveSecondPointer(float x, float y){
        if(touchMode == 0) {
            Point newPos = new Point(x, y);
            zoom(previousPos1.distance(newPos)/previousPos1.distance(previousPos2));
            previousPos2 = newPos;
        }
    }

    public void removeSecondPointer(){
        this.multitouch = false;
    }

    public void zoom(float ratio){
        float newScale = scale * ratio;
        scale = newScale;

        /*
        scale = 2;
        oxI = -image.getWidth()/2;
        oyI = -image.getHeight()/2;
        */
    }

    public void addStroke(Stroke stroke){
        strokeList.add(stroke);
    }


    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
