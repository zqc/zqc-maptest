package com.example.maptest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private int n = 0;
	private int fx = 0;
	private int i = 0;
	private List<Point> pointList = new ArrayList<Point>();
	private float limitMaxValue = 3.6f;
	private float limitMinValue = -1f;
	private StringBuffer pointwave = new StringBuffer();
	private double[] dirArr = new double[3];
	private double[] tempx = new double[3];
	private double[] tempy = new double[3];
	private double[] tempz = new double[3];
	private double[] temp2 = new double[3];
	private double[] tempSum = new double[3];
	private int stepCount = 0;
	private SensorManager sensorManager;
	private MyView myView;
	private Button myButton;
	private TextView y;
	private Calendar calendar;
	private long t1;
	private long t2;

	private final float stepLength = 10f;
	private float lastDirect;
	private float lastDirect0;
	private float coordinateAngle = 0;
	private float turnAngle = 0;
	private float turnAngleSum = 0;
	private float directTemp = -1;
    private java.text.DecimalFormat df;
	private float[] point1 = new float[2];
	private float[] point0 = new float[2];
	
	//private MyEventListene myListener = new MyEventListene();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    df = new java.text.DecimalFormat(".00");
		setContentView(R.layout.activity_main);
		 myButton = (Button) findViewById(R.id.myButton);
		y = (TextView) findViewById(R.id.y);
		LinearLayout mLinerLayout = (LinearLayout) findViewById(R.id.myLinerLayout);
		myView = new MyView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		myView.setLayoutParams(params);
		mLinerLayout.addView(myView);
		calendar = Calendar.getInstance();

	}

 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	public void start(View view) {
		
		//myView.test(1,0);
		myView.setPointOnMap(point0, point1);
	 
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog()
				.penaltyDeath().build());

		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);// get
		myButton.setEnabled(false);
	/*	int sensorType = Sensor.TYPE_ORIENTATION;
		int sensorType2 = Sensor.TYPE_LINEAR_ACCELERATION;

		if (ss == 1) {
			sampleRate = SensorManager.SENSOR_DELAY_FASTEST;// 10 100hz
		} else if (ss == 2) {
			sampleRate = SensorManager.SENSOR_DELAY_GAME;// 20 50hz
		} else if (ss == 3) {
			sampleRate = SensorManager.SENSOR_DELAY_UI;// 60 17hz
		} else if (ss == 4) {
			sampleRate = SensorManager.SENSOR_DELAY_NORMAL;// 200 5hz
		}
*/
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor s : sensors) {
			sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
		}

		/*sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(myListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);*/
	}
	
	 @SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		/* if(fx++ < 20){
			 return;
		 }*/
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			
				
				if(fx<2){
					if(fx == 1){
						dirArr[0] = event.values[0];
						directTemp = (float) dirArr[0];
					}else{
						dirArr[1] = event.values[1];
						dirArr[1] = MyUtil.lowPassFilter_Orientation(dirArr, 1);
						directTemp = (float) dirArr[1];
					}
					fx++;
				}else{
					dirArr[0] = dirArr[1];
					dirArr[1] = dirArr[2];
					dirArr[2] = event.values[0];
					
					dirArr[2] = MyUtil.lowPassFilter_Orientation(dirArr, 2);
					directTemp = (float) dirArr[2];
				}
			
			} 
			if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
				 y.setText( stepCount +"||"+df.format(point1[0]) + "||" +df.format(point1[1]) + "||" + df.format(turnAngle) + "||" + df.format(lastDirect0) + "||" + df.format(this.directTemp));
				if (i < 2) {
					if (i == 0) {
						tempx[0] = event.values[0];
						tempy[0] = event.values[1];
						tempz[0] = event.values[2];
						tempSum[0] = tempx[0] + tempy[0] + tempz[0];

					} else {

						tempx[1] = event.values[0];
						tempy[1] = event.values[1];
						tempz[1] = event.values[2];
						tempSum[1] = tempx[1] + tempy[1] + tempz[1];
						tempSum[1] = MyUtil.lowPassFilter(tempSum, 1);

					}
					i++;
				} else {

					tempx[i - 2] = tempx[i - 1];
					tempx[i - 1] = tempx[i];
					tempx[i] = event.values[0];
					// tempx[i] = MyUtil.lowPassFilter(tempx, i);

					tempy[i - 2] = tempy[i - 1];
					tempy[i - 1] = tempy[i];
					tempy[i] = event.values[1];
					// tempy[i] = MyUtil.lowPassFilter(tempy, i);

					tempz[i - 2] = tempz[i - 1];
					tempz[i - 1] = tempz[i];
					tempz[i] = event.values[2];
					// tempz[i] = MyUtil.lowPassFilter(tempz, i);

					tempSum[i - 2] = tempSum[i - 1];
					tempSum[i - 1] = tempSum[i];
					tempSum[i] = tempx[i] + tempy[i] + tempz[i];

					tempSum[i] = MyUtil.lowPassFilter(tempSum, i);
					Point point = new Point();
					point.setPointAcc(tempSum[i - 1]);

					if ((tempSum[i - 1] - tempSum[i - 2]) > 0.0 && (tempSum[i - 1] - tempSum[i]) > 0.0) {
						// 如果给点值大于傍边的两个值，则该点为波峰
						if (tempSum[i - 1] > limitMaxValue) {
							// 忽略波峰太小的值
							point.setFlag(1);
							pointList.add(point);
							pointwave.append(point.getPointAcc());
							pointwave.append(",");

							stepCount++;
							if(stepCount == 1){
								turnAngle = 0;
							}else{
								turnAngle = (float) MyUtil.getRotationAngle(lastDirect0, this.directTemp);
							}
							
							turnAngleSum += turnAngle;
							coordinateAngle = turnAngle;
							point1 = MyUtil.changeCoordinate(stepCount, point1, coordinateAngle, turnAngleSum, stepLength);

							lastDirect0 = this.directTemp;

							myView.setPointOnMap(point0, point1);
							point0 = point1;

							/*
							 * int n = pointList.size(); if (n == 1) {
							 * stepCount++; } else if (pointList.get(n -
							 * 2).getFlag() == 1) {
							 * 
							 * double acc1 = pointList.get(n - 1).getPointAcc();
							 * double acc2 = pointList.get(n - 2).getPointAcc();
							 * if (acc1 > acc2) { pointList.remove(n - 2); }
							 * else { pointList.remove(n - 1); }
							 * 
							 * } else if (pointList.get(n - 2).getFlag() == -1)
							 * { stepCount++;
							 * 
							 * }
							 */

						}
					}

					if ((tempSum[i - 1] - tempSum[i - 2]) < 0.0 && (tempSum[i - 1] - tempSum[i]) < 0.0) {
						// 如果给点值小于傍边的两个值，则该点为波谷
						if (tempSum[i - 1] < limitMinValue) {

							point.setFlag(-1);
							pointList.add(point);
							pointwave.append(point.getPointAcc());
							pointwave.append(",");

						/*	int n = pointList.size();
							if (n == 1) {

							} else if (pointList.get(n - 2).getFlag() == -1) {
								double acc1 = pointList.get(n - 1).getPointAcc();
								double acc2 = pointList.get(n - 2).getPointAcc();
								if (acc1 > acc2) {
									pointList.remove(n - 1);
								} else {
									pointList.remove(n - 2);
								}
								pointwave.append(pointList.get(n - 1).getPointAcc());
								pointwave.append(",");
							}*/
						}
					}

					/*if (pointList.size() > 1) {// 如果相邻两个点同为波峰或者波谷保留绝对值大的
						int n = pointList.size();
						if ((pointList.get(n - 1).getFlag() * pointList.get(n - 2).getFlag()) > 0.0) {
							double acc1 = Math.abs(pointList.get(n - 1).getPointAcc() - 10);
							double acc2 = Math.abs(pointList.get(n - 2).getPointAcc() - 10);
							if (acc1 > acc2) {
								pointList.remove(n - 2);
							} else {
								pointList.remove(n - 1);
							}

						}
					}*/
				}

		}
	}

	// @Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
