package com.tristan.astar;


/**
 * 如果一个点加入了open，那么久记下那个点的唯一id
 * 方便后面直接寻找，避免了遍历所有的点
 * @author TristanHuang
 * 
 * 2017-5-23 上午11:50:12
 */
public class CountHelper {
	private int index;
	private int fatherIndex;
	
	public CountHelper(int index,int fatherIndex) {
		this.index = index;
		this.fatherIndex = fatherIndex;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public int getFatherIndex(){
		return this.fatherIndex;
	}
	
	public void resetFatherIndex(int fatherIndex){
		this.fatherIndex = fatherIndex;
	}
}
