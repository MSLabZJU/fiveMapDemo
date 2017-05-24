package com.tristan.astar;

import java.util.ArrayList;

import android.graphics.Point;

/**
 * �ϰ���
 * @author TristanHuang
 * 2017-5-23 ����11:50:42
 */
public class Barrier {
	private ArrayList<Point> barrier=new ArrayList<Point>();
    
	/**
	 * ���һ���ϰ���ķ���
	 * @param p
	 */
	public void addBarrierPoint(Point p){
		this.barrier.add(p);
	}
	
	/**
	 * ���һϵ���ϰ���ķ���
	 * @param ps ������ArrayList<Point>
	 */
	public void addBarrierPoint(ArrayList<Point> ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	/**
	 * ���һϵ���ϰ���ķ���
	 * @param ps ������Point[]
	 */
	public void addBarrierPoint(Point[] ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	/**
	 * @return ����ʵ���� barrier��������ArrayList<Point>
	 */
	public ArrayList<Point> getBarrierPoint(){
		return this.barrier;
	}
}
