package com.example.dreamapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    private Bitmap btmBackground, btmView, image;
    private Paint mPaint = new Paint();
    private Path mPath =  new Path();
    private int colorBackground, sizeBrush, sizeEraser;
    private float mX, mY;
    private Canvas mCanvas;
    private final int DEFERENCE_SPACE = 4;
    private ArrayList<Bitmap> listAction = new ArrayList<>();
    private int leftImage = 50, topImage= 50;



    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        sizeEraser = sizeBrush = 12;
        colorBackground = Color.WHITE;

        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(toPx(sizeBrush));

    }

    private float toPx(int sizeBrush) {
        return sizeBrush*(getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        btmBackground = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        btmView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(btmView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(colorBackground);
        canvas.drawBitmap(btmBackground, 0, 0, null);

        if (image != null)
            canvas.drawBitmap(image,leftImage,topImage, null);

        canvas.drawBitmap(btmView, 0, 0, null);
    }

    public void setColorBackground(int  color){
        colorBackground = color;
        invalidate();
    }

    public void setSizeBrush(int s){
        sizeBrush = s;
        mPaint.setStrokeWidth(toPx(sizeBrush));
    }

    public void setBrushColor(int color){
        mPaint.setColor(color);
    }

    public void setSizeEraser(int s){
        sizeEraser = s;
        mPaint.setStrokeWidth(toPx(sizeEraser));
    }

    public  void enableEraser(){

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void desableEraser(){
        mPaint.setXfermode(null);
        mPaint.setShader(null);
        mPaint.setMaskFilter(null);
    }

    public void addLastAction(Bitmap bitmap){
        listAction.add(bitmap);
    }

    public void returnLastAction(){
        if (listAction.size() > 0 ){
            listAction.remove(listAction.size() - 1);
            if (listAction.size() > 0){
                btmView = listAction.get(listAction.size()-1);
            }else {
                btmView =  Bitmap.createBitmap(getWidth(),  getHeight(), Bitmap.Config.ARGB_8888);
            }
            mCanvas = new Canvas(btmView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                addLastAction(getBitmap());
                break;
        }
        return true;
    }

    private void touchUp() {
        mPath.reset();
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x-mX);
        float dy = Math.abs(y-mY);

        if (dx >= DEFERENCE_SPACE || dy >= DEFERENCE_SPACE){
            mPath.quadTo(x,y, (x+mX)/2, (y+mY)/2);

            mY = y;
            mX = x;

            mCanvas.drawPath(mPath, mPaint);
            invalidate();
        }
    }

    private void touchStart(float x, float y) {
        mPath.moveTo(x,y);
        mX=x;
        mY=y;
    }

    public Bitmap getBitmap(){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap =  Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);

        return bitmap;
    }



    public void setImageB(Bitmap bitmap) {
        image = Bitmap.createScaledBitmap(bitmap, getWidth()/2, getHeight()/2, true);
        invalidate();
    }



}
