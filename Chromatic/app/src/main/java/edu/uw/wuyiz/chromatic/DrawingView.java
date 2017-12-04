package edu.uw.wuyiz.chromatic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyiz on 11/28/17.
 */

public class DrawingView extends View {

    private static final String TAG = "Drawing View";

    /**
     * Action done on the image: NONE, DRAG, and ZOOM
     */
    public enum ACTION {
        NONE, DRAG, ZOOM
    }

    /**
     * Direction of the aciton: UP, DOWN, LEFT, and RIGHT
     */
    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private List<CustomBitmapForMoodBoard> bitmapList;
    private Context curContext;
    private CustomBitmapForMoodBoard curCustomBitmapForMoodBoard;
    private Matrix curMatrix;
    private ACTION action;

    private float oldDist;
    private float newDist;
    private float curScaleParam;

    public DrawingView(Context context) {
        super(context);
        this.curContext = context;
        this.bitmapList = new ArrayList<>();
        this.curMatrix = new Matrix();
        // set default action to none;
        this.action = ACTION.NONE;
    }

    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.curContext = context;
        this.bitmapList = new ArrayList<>();
        this.curMatrix = new Matrix();
        // set default action to none;
        this.action = ACTION.NONE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                action = ACTION.DRAG;

                if (curCustomBitmapForMoodBoard == null && bitmapList.size() > 0) {
                    curCustomBitmapForMoodBoard = bitmapList.get(bitmapList.size() - 1);
                }

                for (CustomBitmapForMoodBoard customBitmapForMoodBoard : bitmapList) {
                    float[] values = new float[9];
                    customBitmapForMoodBoard.matrix.getValues(values);
                    float globalX = values[Matrix.MTRANS_X];
                    float globalY = values[Matrix.MTRANS_Y];
                    float width = customBitmapForMoodBoard.widthAfterScale;
                    float height = customBitmapForMoodBoard.heightAfterScale;
                    float midX = (globalX + width) / 2;
                    float midY = (globalY + height) / 2;
                    PointF pointF = new PointF(midX, midY);
                    customBitmapForMoodBoard.midPoint = pointF;
                }

                for (CustomBitmapForMoodBoard customBitmapForMoodBoard : bitmapList) {
                    float[] values = new float[9];
                    customBitmapForMoodBoard.matrix.getValues(values);
                    float globalX = values[Matrix.MTRANS_X];
                    float globalY = values[Matrix.MTRANS_Y];
                    float width = customBitmapForMoodBoard.widthAfterScale;
                    float height = customBitmapForMoodBoard.heightAfterScale;

                    Rect rect = new Rect((int) globalX, (int) globalY,
                            (int) (globalX + width), (int) (globalY + height));

                    if (motionEvent.getX() > rect.left && motionEvent.getX() < rect.right &&
                            motionEvent.getY() > rect.top && motionEvent.getY() < rect.bottom) {
                        curCustomBitmapForMoodBoard = customBitmapForMoodBoard;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() < rect.left &&
                            motionEvent.getX() > (rect.left - width) &&
                            motionEvent.getY() < rect.top &&
                            motionEvent.getY() > (rect.top - height)) {
                        curCustomBitmapForMoodBoard = customBitmapForMoodBoard;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() > (rect.left - height) &&
                            motionEvent.getX() < rect.left &&
                            motionEvent.getY() > rect.top &&
                            motionEvent.getY() < (rect.top + width)) {
                        curCustomBitmapForMoodBoard = customBitmapForMoodBoard;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() > rect.left &&
                            motionEvent.getX() < (rect.left + height) &&
                            motionEvent.getY() > (rect.top - width) &&
                            motionEvent.getY() < rect.top) {
                        curCustomBitmapForMoodBoard = customBitmapForMoodBoard;
                        change(true, motionEvent);
                        break;
                    }
                }

                change(false, motionEvent);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                action = ACTION.ZOOM;
                oldDist = calculateSpacing(motionEvent);
                curCustomBitmapForMoodBoard.oldRotation = calculateRotation(motionEvent);
                curCustomBitmapForMoodBoard.startDist = calculateDistance(motionEvent);

                if (curCustomBitmapForMoodBoard.startDist > 10f) {
                    curCustomBitmapForMoodBoard.midPoint = calculateMidPoint(motionEvent);
                    curMatrix.set(curCustomBitmapForMoodBoard.matrix);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (action == ACTION.DRAG) {
                    float dX = motionEvent.getX() - curCustomBitmapForMoodBoard.startPoint.x;
                    float dY = motionEvent.getY() - curCustomBitmapForMoodBoard.startPoint.y;
                    curCustomBitmapForMoodBoard.matrix.set(curMatrix);
                    curCustomBitmapForMoodBoard.matrix.postTranslate(dX, dY);
                } else if (action == ACTION.ZOOM) {
                    float endDist = calculateDistance(motionEvent);
                    curCustomBitmapForMoodBoard.newRotation =
                            calculateRotation(motionEvent) - curCustomBitmapForMoodBoard.oldRotation;
                    if (endDist > 10f) {
                        curScaleParam = endDist / curCustomBitmapForMoodBoard.startDist;
                        curCustomBitmapForMoodBoard.matrix.set(curMatrix);
                        curCustomBitmapForMoodBoard.matrix.postScale(curScaleParam, curScaleParam,
                                curCustomBitmapForMoodBoard.midPoint.x,
                                curCustomBitmapForMoodBoard.midPoint.y);
                        curCustomBitmapForMoodBoard.matrix.postRotate(curCustomBitmapForMoodBoard.newRotation,
                                curCustomBitmapForMoodBoard.midPoint.x, curCustomBitmapForMoodBoard.midPoint.y);
                    }
                    newDist = calculateSpacing(motionEvent);
                    if (newDist > oldDist + 1 || newDist < oldDist - 1) {
                        scaleAll(newDist / oldDist);
                        oldDist = newDist;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = ACTION.NONE;
                setWeightAndHeight(curScaleParam);
                curScaleParam = 1;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        for (CustomBitmapForMoodBoard customBitmapForMoodBoard : bitmapList) {
            if (customBitmapForMoodBoard != null) {
                canvas.drawBitmap(customBitmapForMoodBoard.getBitmap(), customBitmapForMoodBoard.matrix, paint);
            }
        }

//        Paint anotherPaint = new Paint();
//        anotherPaint.setColor(Color.BLACK);
//        anotherPaint.setTextSize(20);
//        canvas.drawText("Some Text", 10, 25, anotherPaint);
    }

    public void setOffset(DIRECTION direction) {
        if (direction == DIRECTION.UP) {
            curCustomBitmapForMoodBoard.matrix.postTranslate(0, -2);
        } else if (direction == DIRECTION.DOWN) {
            curCustomBitmapForMoodBoard.matrix.postTranslate(0, 2);
        } else if (direction == DIRECTION.LEFT) {
            curCustomBitmapForMoodBoard.matrix.postTranslate(-2, 0);
        } else if (direction == DIRECTION.RIGHT) {
            curCustomBitmapForMoodBoard.matrix.postTranslate(2, 0);
        }

        invalidate();
    }

    public void scaleSelected(float scaleParam) {
        curCustomBitmapForMoodBoard.matrix.preScale(scaleParam, scaleParam,
                curCustomBitmapForMoodBoard.midPoint.x, curCustomBitmapForMoodBoard.midPoint.y);

        curCustomBitmapForMoodBoard.widthAfterScale = ((int) (curCustomBitmapForMoodBoard.widthAfterScale * scaleParam));
        curCustomBitmapForMoodBoard.heightAfterScale = ((int) (curCustomBitmapForMoodBoard.heightAfterScale * scaleParam));

        float[] values = new float[9];
        curCustomBitmapForMoodBoard.matrix.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float width= curCustomBitmapForMoodBoard.widthAfterScale;
        float height = curCustomBitmapForMoodBoard.heightAfterScale;

        Rect rect = new Rect((int) globalX,
                (int) globalY,
                (int) (globalX + width),
                (int) (globalY + height));

        curCustomBitmapForMoodBoard.x = rect.left;
        curCustomBitmapForMoodBoard.y = rect.top;
        curCustomBitmapForMoodBoard.scaleParam = scaleParam;

        invalidate();
    }

    public void rotateSelected(int rotationDegree) {
        curCustomBitmapForMoodBoard.matrix.preRotate(rotationDegree,
                curCustomBitmapForMoodBoard.midPoint.x, curCustomBitmapForMoodBoard.midPoint.y);

        float[] values = new float[9];
        curCustomBitmapForMoodBoard.matrix.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float width= curCustomBitmapForMoodBoard.widthAfterScale;
        float height = curCustomBitmapForMoodBoard.heightAfterScale;

        Rect rect = new Rect((int) globalX,
                (int) globalY,
                (int) (globalX + width),
                (int) (globalY + height));

        curCustomBitmapForMoodBoard.x = rect.left;
        curCustomBitmapForMoodBoard.y = rect.top;

        invalidate();
    }

    public void addBitmap(CustomBitmapForMoodBoard customBitmapForMoodBoard) {
        bitmapList.add(customBitmapForMoodBoard);
    }

    public List<CustomBitmapForMoodBoard> getViews() {
        return bitmapList;
    }

    private float calculateRotation(MotionEvent motionEvent) {
        double deltaX = (motionEvent.getX(0) - motionEvent.getX(1));
        double deltaY = (motionEvent.getY(0) - motionEvent.getY(1));
        double radians = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radians);
    }

    private float calculateDistance(MotionEvent motionEvent) {
        float dX = motionEvent.getX(1) - motionEvent.getX(0);
        float dY = motionEvent.getY(1) - motionEvent.getY(0);
        return (float) Math.sqrt(dX * dX + dY * dY);
    }

    private float calculateSpacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private PointF calculateMidPoint(MotionEvent motionEvent) {
        float midX = (motionEvent.getX(1) + motionEvent.getX(0)) / 2;
        float midY = (motionEvent.getY(1) + motionEvent.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private void scaleAll(float scaleParam) {
        for (CustomBitmapForMoodBoard customBitmapForMoodBoard : bitmapList) {
            customBitmapForMoodBoard.matrix.postScale(scaleParam, scaleParam,
                    customBitmapForMoodBoard.midPoint.x, customBitmapForMoodBoard.midPoint.y);
        }
    }

    private void setWeightAndHeight(float scaleParam) {
        for (CustomBitmapForMoodBoard customBitmapForMoodBoard : bitmapList) {
            customBitmapForMoodBoard.widthAfterScale = ((int) (customBitmapForMoodBoard.widthAfterScale * scaleParam));
            customBitmapForMoodBoard.heightAfterScale = ((int) (customBitmapForMoodBoard.heightAfterScale * scaleParam));

            float[] values = new float[9];
            customBitmapForMoodBoard.matrix.getValues(values);
            float globalX = values[Matrix.MTRANS_X];
            float globalY = values[Matrix.MTRANS_Y];
            float width= customBitmapForMoodBoard.widthAfterScale;
            float height = customBitmapForMoodBoard.heightAfterScale;

            Rect rect = new Rect((int) globalX,
                    (int) globalY,
                    (int) (globalX + width),
                    (int) (globalY + height));

            customBitmapForMoodBoard.x = rect.left;
            customBitmapForMoodBoard.y = rect.top;
            customBitmapForMoodBoard.scaleParam = scaleParam;
        }
    }

    private void change(boolean isChanged, MotionEvent motionEvent) {
        if (isChanged) {
            bitmapList.remove(curCustomBitmapForMoodBoard);
            bitmapList.add(curCustomBitmapForMoodBoard);
        }

        curMatrix.set(curCustomBitmapForMoodBoard.matrix);
        curCustomBitmapForMoodBoard.matrix.set(curMatrix);
        curCustomBitmapForMoodBoard.startPoint.set(motionEvent.getX(), motionEvent.getY());
        postInvalidate();
    }
}