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
    protected enum ACTION {
        NONE, DRAG, ZOOM
    }

    /**
     * Direction of the aciton: UP, DOWN, LEFT, and RIGHT
     */
    protected enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private List<MoodBoardComponentBitmap> bitmapList;
    private Context curContext;
    private MoodBoardComponentBitmap curMoodBoardComponentBitmap;
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

                if (curMoodBoardComponentBitmap == null && bitmapList.size() > 0) {
                    curMoodBoardComponentBitmap = bitmapList.get(bitmapList.size() - 1);
                }

                for (MoodBoardComponentBitmap moodBoardComponentBitmap : bitmapList) {
                    float[] values = new float[9];
                    moodBoardComponentBitmap.matrix.getValues(values);
                    float globalX = values[Matrix.MTRANS_X];
                    float globalY = values[Matrix.MTRANS_Y];
                    float width = moodBoardComponentBitmap.widthAfterScale;
                    float height = moodBoardComponentBitmap.heightAfterScale;
                    float midX = (globalX + width) / 2;
                    float midY = (globalY + height) / 2;
                    moodBoardComponentBitmap.midPoint = new PointF(midX, midY);
                }

                for (MoodBoardComponentBitmap moodBoardComponentBitmap : bitmapList) {
                    float[] values = new float[9];
                    moodBoardComponentBitmap.matrix.getValues(values);
                    float globalX = values[Matrix.MTRANS_X];
                    float globalY = values[Matrix.MTRANS_Y];
                    float width = moodBoardComponentBitmap.widthAfterScale;
                    float height = moodBoardComponentBitmap.heightAfterScale;

                    Rect rect = new Rect((int) globalX, (int) globalY,
                            (int) (globalX + width), (int) (globalY + height));

                    if (motionEvent.getX() > rect.left && motionEvent.getX() < rect.right &&
                            motionEvent.getY() > rect.top && motionEvent.getY() < rect.bottom) {
                        curMoodBoardComponentBitmap = moodBoardComponentBitmap;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() < rect.left &&
                            motionEvent.getX() > (rect.left - width) &&
                            motionEvent.getY() < rect.top &&
                            motionEvent.getY() > (rect.top - height)) {
                        curMoodBoardComponentBitmap = moodBoardComponentBitmap;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() > (rect.left - height) &&
                            motionEvent.getX() < rect.left &&
                            motionEvent.getY() > rect.top &&
                            motionEvent.getY() < (rect.top + width)) {
                        curMoodBoardComponentBitmap = moodBoardComponentBitmap;
                        change(true, motionEvent);
                        break;
                    } else if (motionEvent.getX() > rect.left &&
                            motionEvent.getX() < (rect.left + height) &&
                            motionEvent.getY() > (rect.top - width) &&
                            motionEvent.getY() < rect.top) {
                        curMoodBoardComponentBitmap = moodBoardComponentBitmap;
                        change(true, motionEvent);
                        break;
                    }
                }

                change(false, motionEvent);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                action = ACTION.ZOOM;
                oldDist = calculateSpacing(motionEvent);
                curMoodBoardComponentBitmap.oldRotation = calculateRotation(motionEvent);
                curMoodBoardComponentBitmap.startDist = calculateDistance(motionEvent);

                if (curMoodBoardComponentBitmap.startDist > 10f) {
                    curMoodBoardComponentBitmap.midPoint = calculateMidPoint(motionEvent);
                    curMatrix.set(curMoodBoardComponentBitmap.matrix);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (action == ACTION.DRAG) {
                    float dX = motionEvent.getX() - curMoodBoardComponentBitmap.startPoint.x;
                    float dY = motionEvent.getY() - curMoodBoardComponentBitmap.startPoint.y;
                    curMoodBoardComponentBitmap.matrix.set(curMatrix);
                    curMoodBoardComponentBitmap.matrix.postTranslate(dX, dY);
                } else if (action == ACTION.ZOOM) {
                    float endDist = calculateDistance(motionEvent);
                    curMoodBoardComponentBitmap.newRotation =
                            calculateRotation(motionEvent) - curMoodBoardComponentBitmap.oldRotation;
                    if (endDist > 10f) {
                        curScaleParam = endDist / curMoodBoardComponentBitmap.startDist;
                        curMoodBoardComponentBitmap.matrix.set(curMatrix);
                        curMoodBoardComponentBitmap.matrix.postScale(curScaleParam, curScaleParam,
                                curMoodBoardComponentBitmap.midPoint.x,
                                curMoodBoardComponentBitmap.midPoint.y);
                        curMoodBoardComponentBitmap.matrix.postRotate(curMoodBoardComponentBitmap.newRotation,
                                curMoodBoardComponentBitmap.midPoint.x, curMoodBoardComponentBitmap.midPoint.y);
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

        for (MoodBoardComponentBitmap moodBoardComponentBitmap : bitmapList) {
            if (moodBoardComponentBitmap != null) {
                canvas.drawBitmap(moodBoardComponentBitmap.getBitmap(), moodBoardComponentBitmap.matrix, paint);
            }
        }

//        Paint anotherPaint = new Paint();
//        anotherPaint.setColor(Color.BLACK);
//        anotherPaint.setTextSize(20);
//        canvas.drawText("Some Text", 10, 25, anotherPaint);
    }

    public void setOffset(DIRECTION direction) {
        if (direction == DIRECTION.UP) {
            curMoodBoardComponentBitmap.matrix.postTranslate(0, -2);
        } else if (direction == DIRECTION.DOWN) {
            curMoodBoardComponentBitmap.matrix.postTranslate(0, 2);
        } else if (direction == DIRECTION.LEFT) {
            curMoodBoardComponentBitmap.matrix.postTranslate(-2, 0);
        } else if (direction == DIRECTION.RIGHT) {
            curMoodBoardComponentBitmap.matrix.postTranslate(2, 0);
        }

        invalidate();
    }

    public void scaleSelected(float scaleParam) {
        curMoodBoardComponentBitmap.matrix.preScale(scaleParam, scaleParam,
                curMoodBoardComponentBitmap.midPoint.x, curMoodBoardComponentBitmap.midPoint.y);

        curMoodBoardComponentBitmap.widthAfterScale = ((int) (curMoodBoardComponentBitmap.widthAfterScale * scaleParam));
        curMoodBoardComponentBitmap.heightAfterScale = ((int) (curMoodBoardComponentBitmap.heightAfterScale * scaleParam));

        float[] values = new float[9];
        curMoodBoardComponentBitmap.matrix.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float width= curMoodBoardComponentBitmap.widthAfterScale;
        float height = curMoodBoardComponentBitmap.heightAfterScale;

        Rect rect = new Rect((int) globalX,
                (int) globalY,
                (int) (globalX + width),
                (int) (globalY + height));

        curMoodBoardComponentBitmap.x = rect.left;
        curMoodBoardComponentBitmap.y = rect.top;
        curMoodBoardComponentBitmap.scaleParam = scaleParam;

        invalidate();
    }

    public void rotateSelected(int rotationDegree) {
        curMoodBoardComponentBitmap.matrix.preRotate(rotationDegree,
                curMoodBoardComponentBitmap.midPoint.x, curMoodBoardComponentBitmap.midPoint.y);

        float[] values = new float[9];
        curMoodBoardComponentBitmap.matrix.getValues(values);
        float globalX = values[Matrix.MTRANS_X];
        float globalY = values[Matrix.MTRANS_Y];
        float width= curMoodBoardComponentBitmap.widthAfterScale;
        float height = curMoodBoardComponentBitmap.heightAfterScale;

        Rect rect = new Rect((int) globalX,
                (int) globalY,
                (int) (globalX + width),
                (int) (globalY + height));

        curMoodBoardComponentBitmap.x = rect.left;
        curMoodBoardComponentBitmap.y = rect.top;

        invalidate();
    }

    public void addBitmap(MoodBoardComponentBitmap moodBoardComponentBitmap) {
        bitmapList.add(moodBoardComponentBitmap);
    }

//    public List<MoodBoardComponentBitmap> getViews() {
//        return bitmapList;
//    }

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
        for (MoodBoardComponentBitmap moodBoardComponentBitmap : bitmapList) {
            moodBoardComponentBitmap.matrix.postScale(scaleParam, scaleParam,
                    moodBoardComponentBitmap.midPoint.x, moodBoardComponentBitmap.midPoint.y);
        }
    }

    private void setWeightAndHeight(float scaleParam) {
        for (MoodBoardComponentBitmap moodBoardComponentBitmap : bitmapList) {
            moodBoardComponentBitmap.widthAfterScale = ((int) (moodBoardComponentBitmap.widthAfterScale * scaleParam));
            moodBoardComponentBitmap.heightAfterScale = ((int) (moodBoardComponentBitmap.heightAfterScale * scaleParam));

            float[] values = new float[9];
            moodBoardComponentBitmap.matrix.getValues(values);
            float globalX = values[Matrix.MTRANS_X];
            float globalY = values[Matrix.MTRANS_Y];
            float width= moodBoardComponentBitmap.widthAfterScale;
            float height = moodBoardComponentBitmap.heightAfterScale;

            Rect rect = new Rect((int) globalX,
                    (int) globalY,
                    (int) (globalX + width),
                    (int) (globalY + height));

            moodBoardComponentBitmap.x = rect.left;
            moodBoardComponentBitmap.y = rect.top;
            moodBoardComponentBitmap.scaleParam = scaleParam;
        }
    }

    private void change(boolean isChanged, MotionEvent motionEvent) {
        if (isChanged) {
            bitmapList.remove(curMoodBoardComponentBitmap);
            bitmapList.add(curMoodBoardComponentBitmap);
        }

        curMatrix.set(curMoodBoardComponentBitmap.matrix);
        curMoodBoardComponentBitmap.matrix.set(curMatrix);
        curMoodBoardComponentBitmap.startPoint.set(motionEvent.getX(), motionEvent.getY());
        postInvalidate();
    }
}