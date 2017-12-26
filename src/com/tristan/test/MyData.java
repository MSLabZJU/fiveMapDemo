package com.tristan.test;

public class MyData {

	private int size;   //数据长度标记
	
	private long timestampSound;
	
	private double tdoaValue1;
	private double tdoaValue2;
	private double tdoaValue3;
	private long timestamp1;
	private long timestamp2;
	private long timestamp3;
	
	private double x;
	private double y;
	private long timestampX;
	private long timestampY;
	
	private long timestampPDR;
	private int stepNumber;
	private float stepLength;
	private float orientation;
	
	public MyData(int size, long timestamp1, double tdoaValue1, long timestamp2, double tdoaValue2, long timestamp3, double tdoaValue3){
		this.size = size;
		this.tdoaValue1 = tdoaValue1;
		this.tdoaValue2 = tdoaValue2;
		this.tdoaValue3 = tdoaValue3;
		this.timestamp1 = timestamp1;
		this.timestamp2 = timestamp2;
		this.timestamp3 = timestamp3;
	}
	
	public MyData(int size, long timestampX, double x, long timestampY, double y){
		this.size = size;
		this.x = x;
		this.y = y;
		this.timestampX = timestampX;
		this.timestampY = timestampY;
	}
	
	public MyData(int size, long timestampPDR, int stepNumber, float stepLength, float orientation){
		this.size = size;
		this.timestampPDR = timestampPDR;
		this.stepNumber = stepNumber;
		this.stepLength = stepLength;
		this.orientation = orientation;
	}
	
	public MyData(int size, long timestampSound){
		this.size = size;
		this.timestampSound = timestampSound;
	}
	
	public int getSize() {
		return size;
	}
	
	public double getTdoaValue1() {
		return tdoaValue1;
	}
	
	public double getTdoaValue2() {
		return tdoaValue2;
	}

	public long getTimestampPDR() {
		return timestampPDR;
	}

	public int getStepNumber() {
		return stepNumber;
	}


	public float getStepLength() {
		return stepLength;
	}

	public float getOrientation() {
		return orientation;
	}

	public double getTdoaValue3() {
		return tdoaValue3;
	}

	public long getTimestamp1() {
		return timestamp1;
	}

	public long getTimestamp2() {
		return timestamp2;
	}

	public long getTimestamp3() {
		return timestamp3;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public long getTimestampX() {
		return timestampX;
	}

	public long getTimestampY() {
		return timestampY;
	}

	public long getTimestampSound() {
		return timestampSound;
	}


}
