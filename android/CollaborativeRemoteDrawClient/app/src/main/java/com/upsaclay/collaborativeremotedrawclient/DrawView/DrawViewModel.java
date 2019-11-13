package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;

import com.upsaclay.collaborativeremotedrawclient.Shared.Point;
import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.Shared.Zoom;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;
import com.upsaclay.collaborativeremotedrawclient.network.DataSender;

import java.util.ArrayList;
import java.util.List;

public class DrawViewModel {
    /**
     * size of the view
     */
    private int width, height;

    /**
     * DataSender used to send inputs to the server
     */
    private DataSender dataSender;

    /**
     * used to store image data
     */
    private Bitmap image;
    /**
     * whether or not the image is set
     */
    private boolean imageSet;





    //display properties
    /**
     * Zoom set manually by the user
     * offset is from top left corner of the image's original position
     */
    private Zoom imageZoom;
    /**
     * Automatic zoom to fit screen
     * offset is from top left corner of view
     */
    private Zoom screenZoom;






    /**
     * touch screen mode
     * 0 = zoom, 1 = draw
     */
    private int touchMode;

    //used to handle zooming
    /**
     * previous position of the first pointer
     */
    private Point previousPos1;
    /**
     * previous position of second pointer
     */
    private Point previousPos2;
    /**
     * true if more than one finger is on the screen
     */
    private boolean multitouch;




    //used to store stroke data
    /**
     * strokes from server
     */
    private List<Stroke> strokeList;
    /**
     * stroke currently drawn by the user
     */
    private Stroke curStroke;
    /**
     * the current stroke is in progress
     */
    private boolean strokeInProgress;
    /**
     * start & end of the current linear portion of the current stroke
     */
    private Point start, end;





    /**
     * initialize the model and its variables
     * sets default/placeholder values while waiting for updates
     */
    public DrawViewModel() {
        width = 1;
        height = 1;

        imageSet = false;

        imageZoom = new Zoom(1, 0, 0);

        screenZoom = new Zoom(1, 0, 0);

        touchMode = 0;

        previousPos1 = new Point(0, 0);
        previousPos2 = new Point(0, 0);

        strokeList = new ArrayList<>();
        curStroke = new Stroke();
        strokeInProgress = false;
        start = new Point(0, 0);
    }

    /**
     * getter for width
     * @return width of the view
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter for height
     * @return height of the view
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter for imageSet
     * @return whether or not the image is set
     */
    public boolean imageIsSet() {
        return imageSet;
    }

    /**
     * getter for image
     * @return image data
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * combines the horizontal offset values of the different zooms
     * @return offset from left side of the view
     */
    public int getOx() {
        return (int) (screenZoom.xOffset + imageZoom.xOffset*screenZoom.scale);
    }

    /**
     * combines the vertical offset values of the different zooms
     * @return offset from top of the view
     */
    public int getOy() {
        return (int) (screenZoom.yOffset + imageZoom.yOffset*screenZoom.scale);
    }

    /**
     * combines the scale values of the different zooms
     * @return total scale
     */
    public float getScale() {
        return screenZoom.scale * imageZoom.scale;
    }

    /**
     * getter for strokeList
     * @return strokes from server
     */
    public List<Stroke> getStrokeList() {
        return strokeList;
    }

    /**
     * getter for curStroke
     * @return stroke currently drawn by the user
     */
    public Stroke getCurStroke() {
        return curStroke;
    }

    /**
     * getter for strokeInProgress
     * @return the current stroke is in progress
     */
    public boolean strokeIsInProgress() {
        return strokeInProgress;
    }

    /**
     * set the initial list of strokes
     * @param strokeList initial stroke list
     */
    public void setStrokeList(List<Stroke> strokeList) {
        this.strokeList = strokeList;
    }

    /**
     * set the image and make it fit on the screen
     * @param image image
     */
    public void setImage(Bitmap image) {
        this.image = image;
        imageSet = true;

        resetZoom();
        fitToScreen();
    }

    /**
     * set the size of the view and make the image fit in the new view area
     * @param vw new width
     * @param vh new height
     */
    public void setViewSize(int vw, int vh) {
        this.width = vw;
        this.height = vh;

        fitToScreen();
    }

    /**
     * adjust the screen zoom so that the image fits in the middle of the screen with the default imageZoom
     */
    public void fitToScreen() {
        if (imageIsSet()){
            double imageRatio = image.getWidth() / (double) image.getHeight();
            double screenRatio = width / (double) height;
            if (imageRatio >= screenRatio) {
                //the image is wider than the screen, it should take the full width and be centered vertically
                screenZoom.scale = width / (float) image.getWidth();
                screenZoom.xOffset = 0;
                screenZoom.yOffset = (int) ((height / 2.0) - (image.getHeight() * screenZoom.scale / 2));
            } else {
                //the image is taller than the screen, it should take the full height and be centered horizontally
                screenZoom.scale = height / (float) image.getHeight();
                screenZoom.xOffset = (int) ((width / 2.0) - (image.getWidth() * screenZoom.scale / 2));
                screenZoom.yOffset = 0;
            }
        }
    }

    /**
     * reset imageZoom to the default value
     */
    public void resetZoom() {
        imageZoom = new Zoom(1, 0, 0);
    }

    /**
     * convert a horizontal position on the screen to the corresponding position on the image
     * @param x position on screen space
     * @return position on image space
     */
    public float screenToImageX(float x) {
        return (x - getOx()) / getScale();
    }

    /**
     * convert a vertical position on the screen to the corresponding position on the image
     * @param y position on screen space
     * @return position on image space
     */
    public float screenToImageY(float y) {
        return (y - getOy()) / getScale();
    }

    /**
     * convert a horizontal position on the image to the corresponding position on the screen
     * @param x position on image space
     * @return position on screen space
     */
    public float imageToScreenX(float x) {
        return x * getScale() + getOx();
    }

    /**
     * convert a vertical position on the image to the corresponding position on the screen
     * @param y position on image space
     * @return position on screen space
     */
    public float imageToScreenY(float y) {
        return y * getScale() + getOy();
    }

    /**
     * switch between the touch screen modes
     * 0 = zoom, 1 = draw
     * @param touchMode chosen mode
     */
    public void setTouchMode(int touchMode) {
        this.touchMode = touchMode;
    }

    /**
     * send the current display to the server
     */
    public void sendDisplay() {
        dataSender.send(imageZoom);
    }

    /**
     * user begins an action, save the coordinate to use later
     * @param x coordinate of action
     * @param y coordinate of action
     */
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

    /**
     * user continues an action, result depends on touch mode
     * @param x coordinate of action
     * @param y coordinate of action
     */
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
                if(imageIsSet()){
                    float xi = screenToImageX(x);
                    float yi = screenToImageY(y);

                    //prevent drawing out of bounds
                    boolean outOfBounds = false;
                    if (xi < 0){
                        xi = 0;
                        outOfBounds = true;
                    }
                    if (yi < 0){
                        yi = 0;
                        outOfBounds = true;
                    }
                    if(xi > image.getWidth()){
                        xi = image.getWidth();
                        outOfBounds = true;
                    }
                    if(yi > image.getHeight()) {
                        yi = image.getHeight();
                        outOfBounds = true;
                    }
                    end = new Point(xi, yi);

                    //start new stroke if necessary
                    if (!strokeInProgress && !outOfBounds) {
                        curStroke = new Stroke();
                        strokeInProgress = true;
                    }

                    //add points to the stroke if necessary
                    if(strokeInProgress){
                        curStroke.add(start);
                        curStroke.add(end);
                        if(outOfBounds) endAction();
                    }
                    start = end;
                }
                break;
        }
    }

    /**
     * user action is over, process the action
     */
    public void endAction() {
        switch (this.touchMode) {
            case 0:
                multitouch = false;
                if(imageIsSet()) {
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
                //if a stroke was in progress, send it to the server
                if(strokeInProgress) {
                    strokeInProgress = false;
                    dataSender.send(curStroke);
                }
                break;
        }
    }

    /**
     * a secondary pointer was added, store its position and start multi touch action
     * @param x coordinate of second pointer
     * @param y coordinate of second pointer
     */
    public void addSecondPointer(float x, float y) {
        if (touchMode == 0) {
            previousPos2 = new Point(x, y);
            multitouch = true;
        }
    }

    /**
     * the secondary pointer has moved, adjust zoom based on its movement
     * @param x coordinate of second pointer
     * @param y coordinate of second pointer
     */
    public void moveSecondPointer(float x, float y) {
        if (touchMode == 0) {
            Point newPos = new Point(x, y);
            zoom(previousPos1.distance(newPos) / previousPos1.distance(previousPos2));
            previousPos2 = newPos;
        }
    }

    /**
     * the secondary pointer was removed, end multi touch action
     */
    public void removeSecondPointer() {
        this.multitouch = false;
    }

    /**
     * adjusts image zoom with the main pointer as origin
     * @param ratio ratio to apply to imageZoom
     */
    public void zoom(float ratio) {
        if (imageIsSet()) {
            //if image is too zoomed in, prevent zooming beyond the limit (wiew is at least 10 pixels wide & 10 pixels high)
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

    /**
     * add a stroke to the list of strokes
     * @param stroke stroke to add
     */
    public void addStroke(Stroke stroke) {
        strokeList.add(stroke);
    }

    /**
     * sets the dataListener
     */
    public void setDataListener(DataListener dataListener) {
        this.dataSender = new DataSender(dataListener);
    }
}
