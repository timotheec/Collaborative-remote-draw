package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;

public class DrawViewView {

    private DrawView controller;

    private Paint paint;

    public DrawViewView(DrawView controller){
        this.controller = controller;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void draw(Canvas canvas) {
        DrawViewModel model = controller.getModel();
        //draw image
        if (model.imageIsSet()){
            Bitmap image = model.getImage();
            Bitmap scaledImage = Bitmap.createScaledBitmap(image, (int)(image.getWidth()*model.getScale()), (int)(image.getHeight()*model.getScale()), true);
            canvas.drawBitmap(scaledImage, model.getOx(), model.getOy(), paint);
        }
        paint.setStrokeWidth(10);
        float startX = 0;
        float startY = 0;
        float endX = 0;
        float endY = 0;

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
        if(model.isPerformingAction()) {
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
