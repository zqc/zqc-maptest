package com.example.maptest;

public class Point {

	private double pointAcc;
	private long pointTime;
	private int flag;//1 波峰 -1 波谷 
	
	public double getPointAcc() {
		return pointAcc;
	}

	public void setPointAcc(double pointAcc) {
		this.pointAcc = pointAcc;
	}

	public long getPointTime() {
		return pointTime;
	}

	public void setPointTime(long pointTime) {
		this.pointTime = pointTime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
