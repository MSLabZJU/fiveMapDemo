package com.tristan.astar;


//���һ���������open����ô�ü����Ǹ����Ψһid��
//�������ֱ��Ѱ�ң������˱������еĵ�

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
