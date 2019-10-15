package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class DrawViewView {

    private DrawView controller;

    private Paint paint;

    public DrawViewView(DrawView controller){
        this.controller = controller;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void draw(Canvas canvas) {
        DrawViewModel model = controller.getModel();
        if (model.imageIsSet()) canvas.drawBitmap(model.getImage(), 0, 0, paint);
        paint.setStrokeWidth(10);
        float startX = 0;
        float startY = 0;

        //draw previous actions
        paint.setColor(Color.BLACK);
        for (Action a : model.getActionList()) {
            for(int i = 0; i < a.length; i++){
                if(i % 2 == 0){
                    startX = a.getX(i);
                    startY = a.getY(i);
                } else {
                    canvas.drawLine(startX, startY, a.getX(i), a.getY(i), paint);
                }
            }
        }

        //draw the current action
        paint.setColor(Color.BLUE);
        if(model.isPerformingAction()) {
            Action a = model.getCurAction();
            for(int i = 0; i < a.length; i++){
                if(i % 2 == 0){
                    startX = a.getX(i);
                    startY = a.getY(i);
                } else {
                    canvas.drawLine(startX, startY, a.getX(i), a.getY(i), paint);
                }
            }
        }

    }
}
