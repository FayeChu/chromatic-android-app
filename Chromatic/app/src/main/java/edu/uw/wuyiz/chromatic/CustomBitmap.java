package edu.uw.wuyiz.chromatic;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

import java.io.File;

/**
 * Custom Bitmap class for manipulation in creating Mood Boards
 *
 * Created by wuyiz on 11/28/17.
 */

public class CustomBitmap {

    private int id;

    public int x;
    public int y;
    public int widthAfterScale;
    public int heightAfterScale;
    public int imageId;
    public float startDist;
    public float scaleParam;
    public float oldRotation;
    public float newRotation;

    public File file;
    public String url;
    public String name;
    public Matrix matrix;
    public PointF startPoint;
    public PointF midPoint;
    public Bitmap bitmap;

    public CustomBitmap(Bitmap bitmap) {
        this.oldRotation = 0;
        this.newRotation = 0;
        this.startPoint = new PointF();
        this.matrix = new Matrix();
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidthAfterScale() {
        return widthAfterScale;
    }

    public void setWidthAfterScale(int widthAfterScale) {
        this.widthAfterScale = widthAfterScale;
    }

    public int getHeightAfterScale() {
        return heightAfterScale;
    }

    public void setHeightAfterScale(int heightAfterScale) {
        this.heightAfterScale = heightAfterScale;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public float getStartDist() {
        return startDist;
    }

    public void setStartDist(float startDist) {
        this.startDist = startDist;
    }

    public float getScaleParam() {
        return scaleParam;
    }

    public void setScaleParam(float scaleParam) {
        this.scaleParam = scaleParam;
    }

    public float getOldRotation() {
        return oldRotation;
    }

    public void setOldRotation(float oldRotation) {
        this.oldRotation = oldRotation;
    }

    public float getNewRotation() {
        return newRotation;
    }

    public void setNewRotation(float newRotation) {
        this.newRotation = newRotation;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public PointF getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(PointF startPoint) {
        this.startPoint = startPoint;
    }

    public PointF getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(PointF midPoint) {
        this.midPoint = midPoint;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}