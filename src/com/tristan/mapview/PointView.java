package com.tristan.mapview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * the superclass of classes aimed to draw point from the output of localization
 * @author TristanHuang
 * 
 * 2017-5-23 ����11:46:00
 */
public class PointView extends View {

	public PointView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	//��ȡ��Ļ�����ص��ܶ�
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
		
	
	/**
	 * ������Ļ��DPI���·�װdrawPoint()����������drawPoint()���������õ����굥λ��px����Ҫת��Ϊdip/dp��
	 * ����Ҫ�������ĵ���ʱ��������points�Ȼ�����������
	 * �����������ֻ��������������绪Ϊ��mate2Ϊ2,LG������nexus5Ϊ3�����Ժ�����С��λ�Ŀ��ܻ�������
	 * �����imageview��ԭ����ƽ��һ��(20,30)
	 * @param canvas
	 * @param points
	 * @param paint
	 */
	protected void drawDpPoint(Canvas canvas,List<Point> points,Paint paint){
		int u = 3;
		for(Point p:points){
			int x=(p.x+20)*u;
			int y=(p.y+30)*u;
			canvas.drawPoint(x,y,paint);
		}
	}
	
	protected void drawDpPoint(Canvas canvas,Point point,Paint paint){
		int u = 3;
		int x=(point.x+20)*u;
		int y=(point.y+30)*u;
		canvas.drawPoint(x,y,paint);
	}
	
	private void drawDpPoint(Canvas canvas,ArrayList<Point> points,Paint paint){
		int u = 3;
		for(Point p:points){
			int x=(p.x+20)*u;
			int y=(p.y+30)*u;
			canvas.drawPoint(x,y,paint);
		}
	}

}
