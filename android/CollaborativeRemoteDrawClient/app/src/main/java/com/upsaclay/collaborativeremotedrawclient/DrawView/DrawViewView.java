package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public class DrawViewView {

    /**
     * controller of the interface
     */
    private DrawView controller;

    /**
     * used to paint the strokes on the screen
     */
    private Paint paint;

    /**
     * initialize the view and its variables
     */
    public DrawViewView(DrawView controller){
        this.controller = controller;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * draw the image and strokes
     * @param canvas canvas to draw on
     */
    public void draw(Canvas canvas) {
        DrawViewModel model = controller.getModel();
        //draw image
        if (model.imageIsSet()){
            Bitmap image = model.getImage();
            Matrix m = new Matrix();
            m.setScale(model.getScale(), model.getScale());
            m.postTranslate(model.getOx(),model.getOy());
            canvas.drawBitmap(image, m, paint);
        }
        paint.setStrokeWidth((model.getWidth() + model.getHeight())/400);
        float startX = 0;
        float startY = 0;
        float endX;
        float endY;

        //draw previous strokes
        paint.setColor(Color.BLACK);
        for (Stroke s : model.getStrokeList()) {
            for(int i = 0; i < s.getLength(); i++){
                if(i % 2 == 0){
                    startX = model.imageToScreenX(s.getPoint(i).x);
                    startY = model.imageToScreenY(s.getPoint(i).y);
                } else {
                    endX = model.imageToScreenX(s.getPoint(i).x);
                    endY = model.imageToScreenY(s.getPoint(i).y);
                    canvas.drawLine(startX, startY, endX, endY, paint);
                }
            }
        }

        //draw the current stroke
        paint.setColor(Color.BLUE);
        if(model.strokeIsInProgress()) {
            Stroke s = model.getCurStroke();
            for(int i = 0; i < s.getLength(); i++){
                if(i % 2 == 0){
                    startX = model.imageToScreenX(s.getPoint(i).x);
                    startY = model.imageToScreenY(s.getPoint(i).y);
                } else {
                    endX = model.imageToScreenX(s.getPoint(i).x);
                    endY = model.imageToScreenY(s.getPoint(i).y);
                    canvas.drawLine(startX, startY, endX, endY, paint);
                }
            }
        }
    }
}
