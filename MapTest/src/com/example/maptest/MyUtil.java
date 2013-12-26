package com.example.maptest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class MyUtil {
	private final static double ALPHA = 0.2;
	private final static double ALPHA_ORIENTATION = 0.05;

	/**
	 * �������νǶȲ�� Ϊ˳ʱ��(��ת) �� Ϊ��ʱ��(��ת)
	 * @param startAngle
	 * @param endAngle
	 * @return rotationAngle double
	 */
	public static double getRotationAngle(double startAngle, double endAngle) {
		double rotationAngle = 0;
		if (endAngle - startAngle > 90) {
			rotationAngle = -(360 - endAngle + startAngle);
			
		} else if (endAngle - startAngle < -90) {
			rotationAngle = 360 - startAngle + endAngle;
			
		} else {
			rotationAngle = endAngle - startAngle;

		}
		return rotationAngle;
	}
	
	/**
	 * ��ͨ�˲���
	 * @param arr
	 * @param n
	 * @return
	 */
	public static double lowPassFilter(double[] arr, int n) {
		double tempValue = 0;
		if (n == 0) {
			tempValue = arr[0];
		} else {
			tempValue = ALPHA * arr[n] + (1 - ALPHA) * arr[n - 1];
		}

		return tempValue;
	}
	/**
	 * ��ͨ�˲���
	 * @param arr
	 * @param n
	 * @return
	 */
	public static double lowPassFilter_Orientation(double[] arr, int n) {
		double tempValue = 0;
		if (n == 0) {
			tempValue = arr[0];
		} else {
			tempValue = ALPHA_ORIENTATION * arr[n] + (1 - ALPHA_ORIENTATION) * arr[n - 1];
		}

		return tempValue;
	}
	
	/**
	 * ����������д�� SD �� a.txt
	 * @param temp
	 * @param txtFileName
	 */
	public static void wirteTxt(String temp,String txtFileName) {
		String fileName = txtFileName + ".txt";
		
		
		File file = new File(Environment.getExternalStorageDirectory(), fileName);
		
		if(file.exists()){
			file.delete();
		}else{
			
		
		FileWriter fileWriter = null; //�����ļ�ͨ��File����

		try {
			fileWriter = new FileWriter(file,true);
			 
				fileWriter.write(temp);
				fileWriter.write("    ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		}
	}
	/**
	 * ������һ�����꣬��һ��ǰ��������㵱ǰ������
	 * @param arr  ��һ������
	 * @param len  ��һ����ǰ������
	 * @param lastDirect ��һ��ǰ������
	 * @return ��ǰ�����꣬temp[0] x temp[1] y
	 */
	public static float[] getStandPoint(float[] arr,float len,float lastDirect) {
		float[] temp = new float[2];
		temp[0] = (float) (arr[0] + len * Math.sin(lastDirect));
		temp[1] = (float) (arr[1] + len * Math.cos(lastDirect));
		return temp;
	}
	
	public static float[] changeCoordinate(int num,float[] lastPoint,float coordinateAngle,float turnAngleSum,float len){
		float[] temp = new float[2];
		float x = 0,y=0,x1=0,y1=0,x0=0,y0=0;
		float t = (float) (coordinateAngle*Math.PI/180);
		//float t = (float) (turnAngleSum*Math.PI/180);
	 
		x0 = lastPoint[0]; 
		y0 = lastPoint[1];
		 
		x1 = (float) (len * Math.sin(turnAngleSum*Math.PI/180));
		y1 = (float) (len * Math.cos(turnAngleSum*Math.PI/180));
	//	x1 = (float) (len * Math.sin(coordinateAngle*Math.PI/180));
	//	y1 = (float) (len * Math.cos(coordinateAngle*Math.PI/180));
		if(num == 1){
			x = 0;
			y = len;
		}else if(num == 2){
			x = x1;
			y = y0+y1;
		}else{//����ϵת��
			// x=x'cost-y'sint+x0;
			x = (float) (x1*Math.cos(t) - y1*Math.sin(t) + x0);
			 //y=x'sint+y'cost+y0. 
			y = (float) (x1*Math.sin(t) + y1*Math.cos(t) + y0);
		}
		temp[0] = x;
		temp[1] = y;
		
		return temp;
	}

}
