package com.upsaclay.collaborativeremotedrawclient.DrawView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.upsaclay.collaborativeremotedrawclient.Shared.Stroke;
import com.upsaclay.collaborativeremotedrawclient.network.DataListener;

import java.util.ArrayList;

public class DrawView extends View implements DataListener {

    private DrawViewModel model;

    private DrawViewView view;

    public DrawView(Context context, AttributeSet attrSet) {
        super(context, attrSet);

        model = new DrawViewModel();
        view = new DrawViewView(this);
    }

    public DrawViewModel getModel() {
        return model;
    }

    public void setDataListener(DataListener dataListener) {
        model.setDataListener(dataListener);
    }

    @Override
    public void onReceiveImage(Bitmap image) {

        model.setImage(image);
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //The user touches the screen, starts a new action

                model.beginAction(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //The user moves their finger, continue the action

                model.continueAction(e.getX(), e.getY());
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //The user releases the screen, end the action

                model.endAction();
                this.invalidate();
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*if (connected) canvas.drawBitmap(image, 0, 0, paint);
        paint.setStrokeWidth(10);
        Point startPos = new Point(0, 0);
        boolean even = true;
        for (Point p : pointList) {
            if (even) {
                startPos = p;
            } else {
                canvas.drawLine(startPos.x, startPos.y, p.x, p.y, paint);
            }
            even = !even;
        }*/

        view.draw(canvas);
    }

    // Useless
    @Override
    public void onRecieveStroke(Stroke stroke) {
    }
}
