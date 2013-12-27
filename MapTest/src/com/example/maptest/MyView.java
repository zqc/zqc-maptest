package com.example.maptest;

/**
 * 
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class MyView extends View {
	Bitmap mBitmap;
	private int recLength = 40;
	private int width;
	private int heigh;
	// 屏幕宽度几等分 width/recLength
	private int withNo = 0;
	// 屏幕高度几等分 heigh/recLength
	private int heighNo = 0;
	private Canvas mCanvas = new Canvas();
	private Paint paint;
	private Paint paint2;

	public MyView(Context context) {
		super(context);
		mCanvas = new Canvas();
		paint = new Paint();
		paint2 = new Paint();
	}

	public int getMyWidth() {
		return this.width;
	}

	public int getMyHeigh() {
		return this.heigh;
	}

	public MyView(Context context, AttributeSet set) {
		super(context, set);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		mCanvas.setBitmap(mBitmap);
		mCanvas.drawColor(0xFFFFFFFF);
		width = w;
		heigh = h;
		withNo = width / recLength;
		heighNo = heigh / recLength;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	// 绘图
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(mBitmap, 0, 0, null);
		 
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		 
		paint.setStyle(Paint.Style.STROKE);
	 
		paint.setStrokeWidth(1);

		for (int i = 0; i <= heighNo; i++) {
			canvas.drawLine(0, recLength * i, withNo * recLength, recLength * i, paint);
		}

		for (int j = 0; j <= withNo; j++) {
			canvas.drawLine(recLength * j, 0, recLength * j, heighNo * recLength, paint);
		}

	}

	public void setPointOnMap(float[] pointStart, float[] pointEnd) {

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		/*
		 * float startX = width / 2 + pointStart[0] * 5; float startY = heigh /
		 * 2 - pointStart[1] * 5; float stopX = width / 2 + pointEnd[0] * 5;
		 * float stopY = heigh / 2 - pointEnd[1] * 5;
		 */

		float startX = 120 + pointStart[0];
		float startY = 240 - pointStart[1];
		float stopX = 120 + pointEnd[0];
		float stopY = 240 - pointEnd[1];
		paint.setColor(Color.BLACK);
		//mCanvas.drawLine(startX, startY, stopX, stopY, paint);
		//invalidate();
		this.markRectangle(stopX, stopY);
	}

	/**
	 * 根据点坐标标注所在矩形
	 * 
	 * @param m
	 */
	public void markRectangle(float x, float y) {

		Paint p = new Paint();
		
		int a = (int) (x / recLength);
		int b = (int) (y / recLength);

		// 清屏
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		mCanvas.drawPaint(p);

		mCanvas.drawColor(Color.WHITE);
		mCanvas.drawBitmap(mBitmap, 0, 0, null);
		// 去锯齿
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		// 画笔风格， 空心
		paint.setStyle(Paint.Style.STROKE);
		// 画笔大小
		paint.setStrokeWidth(1);

		for (int i = 0; i <= heighNo; i++) {
			mCanvas.drawLine(0, recLength * i, withNo * recLength, recLength * i, paint);
		}

		for (int j = 0; j <= withNo; j++) {
			mCanvas.drawLine(recLength * j, 0, recLength * j, heighNo * recLength, paint);
		}

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.YELLOW);
		mCanvas.drawRect(new Rect((a + 1) * recLength, (b - 1) * recLength, a * recLength, b * recLength), paint);

		invalidate();

	}

}
