package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.util.Log;

import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;
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
    //Zoom set by the user
    private Zoom imageZoom;
    //Automatic zoom to fit screen
    private Zoom screenZoom;

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

    public DrawViewModel() {
        vw = 1;
        vh = 1;

        imageSet = false;

        imageZoom = new Zoom(1, 0, 0);

        screenZoom = new Zoom(1, 0, 0);

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

    public int getOx() {
        return (int) (screenZoom.xOffset + imageZoom.xOffset*screenZoom.scale);
    }

    public int getOy() {
        return (int) (screenZoom.yOffset + imageZoom.yOffset*screenZoom.scale);
    }

    public float getScale() {
        return screenZoom.scale * imageZoom.scale;
    }

    public List<Stroke> getStrokeList() {
        return strokeList;
    }

    public Stroke getCurStroke() {
        return curStroke;
    }

    public boolean isPerformingAction() {
        return performingAction;
    }

    public void setStrokeList(List<Stroke> strokeList) {
        this.strokeList = strokeList;
    }

    public void setImage(Bitmap image) {
        Log.i("INFO", "Image size : " + image.getWidth() + " : " + image.getHeight());

        this.image = image;

        resetZoom();

        imageSet = true;
    }

    public void setViewSize(int vw, int vh) {
        this.vw = vw;
        this.vh = vh;

        //resets image position for new display
        fitToScreen();
    }

    //adjusts the scale and position of the image to center it on the screen
    public void fitToScreen() {
        if (imageIsSet()){
            double imageRatio = image.getWidth() / (double) image.getHeight();
            double screenRatio = vw / (double) vh;
            if (imageRatio >= screenRatio) {
                //the image is wider than the screen, it should take the full width and be centered vertically
                screenZoom.scale = vw / (float) image.getWidth();
                screenZoom.xOffset = 0;
                screenZoom.yOffset = (int) ((vh / 2.0) - (image.getHeight() * screenZoom.scale / 2));
            } else {
                //the image is taller than the screen, it should take the full height and be centered horizontally
                screenZoom.scale = vh / (float) image.getHeight();
                screenZoom.xOffset = (int) ((vw / 2.0) - (image.getWidth() * screenZoom.scale / 2));
                screenZoom.yOffset = 0;
            }
        }
    }

    //reset zoom
    public void resetZoom() {
        imageZoom = new Zoom(1, 0, 0);
    }

    public float screenToImageX(float x) {
        return (x - getOx()) / getScale();
    }

    public float screenToImageY(float y) {
        return (y - getOy()) / getScale();
    }

    public float imageToScreenX(float x) {
        return x * getScale() + getOx();
    }

    public float imageToScreenY(float y) {
        return y * getScale() + getOy();
    }

    public void setTouchMode(int touchMode) {
        this.touchMode = touchMode;
    }

    public void sendDisplay() {
        dataSender.send(imageZoom);
    }

    public void beginAction(float x, float y) {
        switch (this.touchMode) {
            case 0:
                this.previousPos1 = new Point(x, y);
                break;
            case 1:
                start = new Point(screenToImageX(x), screenToImageY(y));
                break;
        }
    }

    public void continueAction(float x, float y) {
        switch (this.touchMode) {
            case 0:
                Point newPos = new Point(x, y);

                //move the image to follow the pointer
                imageZoom.xOffset += (x - previousPos1.x) / screenZoom.scale;
                imageZoom.yOffset += (y - previousPos1.y) / screenZoom.scale;

                //adjust zoom if two fingers are on the screen
                if (multitouch) {
                    zoom(newPos.distance(previousPos2) / previousPos1.distance(previousPos2));
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

    public void endAction() {
        switch (this.touchMode) {
            case 0:
                multitouch = false;
                if(imageSet) {
                    //if image is too zoomed out, reset the zoom
                    if (imageZoom.scale < 1)
                        resetZoom();
                    //if image is moved out of bounds, move it back
                    if (imageZoom.xOffset > 0)
                        imageZoom.xOffset = 0;
                    if (imageZoom.xOffset < image.getWidth() - image.getWidth() * imageZoom.scale)
                        imageZoom.xOffset = (int) (image.getWidth() - image.getWidth() * imageZoom.scale);
                    if (imageZoom.yOffset > 0)
                        imageZoom.yOffset = 0;
                    if (imageZoom.yOffset < image.getHeight() - image.getHeight() * imageZoom.scale)
                        imageZoom.yOffset = (int) (image.getHeight() - image.getHeight() * imageZoom.scale);
                }
                break;
            case 1:
                performingAction = false;
                dataSender.send(curStroke);
                break;
        }
    }

    public void addSecondPointer(float x, float y) {
        if (touchMode == 0) {
            previousPos2 = new Point(x, y);
            multitouch = true;
        }
    }

    public void moveSecondPointer(float x, float y) {
        if (touchMode == 0) {
            Point newPos = new Point(x, y);
            zoom(previousPos1.distance(newPos) / previousPos1.distance(previousPos2));
            previousPos2 = newPos;
        }
    }

    public void removeSecondPointer() {
        this.multitouch = false;
    }

    public void zoom(float ratio) {
        if (imageIsSet()) {
            //if image is too zoomed in, prevent zooming beyond the limit (wiew is at least 10 pixels wide & 10 pixels high
            float zoomLimit;
            if (image.getWidth() >= image.getHeight()) {
                zoomLimit = image.getHeight() / 10;
            } else {
                zoomLimit = image.getWidth() / 10;
            }
            if (imageZoom.scale * ratio > zoomLimit) {
                ratio = zoomLimit / imageZoom.scale;
            }
        }

        //apply the resulting scale
        imageZoom.scale *= ratio;

        //scale the offset based on the main pointer position
        imageZoom.xOffset = (int) ((imageZoom.xOffset - (previousPos1.x-screenZoom.xOffset)/screenZoom.scale) * ratio + (previousPos1.x-screenZoom.xOffset)/screenZoom.scale);
        imageZoom.yOffset = (int) ((imageZoom.yOffset - (previousPos1.y-screenZoom.yOffset)/screenZoom.scale) * ratio + (previousPos1.y-screenZoom.yOffset)/screenZoom.scale);
    }

    public void addStroke(Stroke stroke) {
        strokeList.add(stroke);
    }


    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
