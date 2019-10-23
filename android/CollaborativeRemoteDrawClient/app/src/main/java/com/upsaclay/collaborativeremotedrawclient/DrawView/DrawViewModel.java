package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.util.Log;

import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DataSender;

import java.util.ArrayList;

public class DrawViewModel {
    //size of the view
    private int vw, vh;

    //DataSender used to send inputs to the server
    private DataSender dataSender;

    //used to store image data
    private Bitmap image;
    private boolean imageSet;
    //image offset (distance from top left corner of the image)
    private int oxI, oyI;
    //image scale
    private float scale;
    //screen offset (distance from top left corner of the display)
    private int oxS, oyS;
    //screen scale
    private float scaleS;

    //used to store stroke data
    private ArrayList<Stroke> strokeList;
    private Stroke curStroke;
    private boolean performingAction;
    private Point start, end;

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

    public ArrayList<Stroke> getStrokeList() {
        return strokeList;
    }

    public Stroke getCurStroke(){
        return curStroke;
    }

    public boolean isPerformingAction() {
        return performingAction;
    }

    public void  setViewSize(int vw, int vh){
        this.vw = vw;
        this.vh = vh;

        //resets image position for new display
        if(imageIsSet()) centerImage();
    }

    public void setImage(Bitmap image){
        Log.i("INFO", "Image size : " + image.getWidth() + " : " + image.getHeight());

        this.image = image;

        centerImage();

        imageSet = true;
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

    public void beginAction(float x, float y){
        start = new Point(screenToImageX(x), screenToImageY(y));
    }

    public void continueAction(float x, float y){
        if(!performingAction){
            curStroke = new Stroke();
            performingAction = true;
        }
        end = new Point(screenToImageX(x), screenToImageY(y));
        curStroke.add(start);
        curStroke.add(end);
        start = end;
    }

    public void endAction(){
        strokeList.add(curStroke);
        dataSender.send(curStroke);

        performingAction = false;
    }

    public void zoom(){
        scale = 2;
        oxI = -image.getWidth()/2;
        oyI = -image.getHeight()/2;
    }


    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
