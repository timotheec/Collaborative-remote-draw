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
    //image offset (distance on screen from top left corner)
    private int ox, oy;
    //image scale
    private float scale;

    //used to store stroke data
    private ArrayList<Stroke> strokeList;
    private Stroke curStroke;
    private boolean performingAction;
    private Point start, end;

    public DrawViewModel(){
        vw = 1;
        vh = 1;

        imageSet = false;
        ox = 0;
        oy = 0;
        scale = 1;

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

    public int getOx() {
        return ox;
    }

    public int getOy() {
        return oy;
    }

    public float getScale() {
        return scale;
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
            scale = vw / (float)image.getWidth();
            ox = 0;
            oy = (int)((vh/2.0) - (image.getHeight()*scale/2));
        } else {
            //the image is taller than the screen, it should take the full height and be centered horizontally
            scale = vh / (float)image.getHeight();
            ox = (int)((vw/2.0) - (image.getWidth()*scale/2));
            oy = 0;
        }
    }

    public float screenToImageX(float x){
        return (x - ox) / scale;
    }

    public float screenToImageY(float y){
        return (y - oy) / scale;
    }

    public float imageToScreenX(float x){
        return x*scale + ox;
    }

    public float imageToScreenY(float y){
        return y*scale + oy;
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
        //TODO: REMOVE
        /*actionList.add(curAction);
        Stroke s = new Stroke();

        for (int i = 0; i < curAction.length; i++){
            s.add(new Point(curAction.getX(i),curAction.getY(i)));
        }

        // send data to the server
        dataSender.send(s);
        */

        strokeList.add(curStroke);
        dataSender.send(curStroke);

        performingAction = false;
    }


    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
