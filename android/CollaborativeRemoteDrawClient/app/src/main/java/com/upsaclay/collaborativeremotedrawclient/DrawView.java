package com.upsaclay.collaborativeremotedrawclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {

    private Paint paint;

    private ArrayList<Point> pointList;

    private Point startPos, endPos;

    public DrawView(Context context, AttributeSet attrSet){
        super(context, attrSet);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointList = new ArrayList<>();
        startPos = new Point(0, 0);
        endPos = new Point(0, 0);
    }

    public boolean onTouchEvent(MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.d("touch","DOWN " + e.getX() + " " + e.getY());
                startPos = new Point(Math.round(e.getX()), Math.round(e.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("touch","MOVE " + e.getX() + " " + e.getY());
                endPos = new Point(Math.round(e.getX()), Math.round(e.getY()));
                pointList.add(startPos);
                pointList.add(endPos);
                startPos = endPos;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("touch","UP");
                this.invalidate();
                break;
        }
        return true;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        Point startPos = new Point(0, 0);
        boolean even = true;
        for(Point p : pointList) {
            if(even){
                startPos = p;
            }else {
                canvas.drawLine(startPos.x, startPos.y, p.x, p.y, paint);
            }
            even = !even;
        }
    }
}
