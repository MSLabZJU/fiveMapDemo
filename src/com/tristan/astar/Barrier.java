package com.tristan.astar;

import java.util.ArrayList;

import android.graphics.Point;

/**
 * 障碍物
 * @author TristanHuang
 * 
 * 2017-5-23 上午11:50:42
 */
public class Barrier {
	private ArrayList<Point> barrier=new ArrayList<Point>();
    
	//添加一个障碍点的方法
	public void addBarrierPoint(Point p){
		this.barrier.add(p);
	}
	
	//添加一系列障碍点的方法
	public void addBarrierPoint(ArrayList<Point> ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	public void addBarrierPoint(Point[] ps){
		for (Point point : ps) {
			this.barrier.add(point);
		}
	}
	
	//返回实例域的方法
	public ArrayList<Point> getBarrierPoint(){
		return this.barrier;
	}
}
