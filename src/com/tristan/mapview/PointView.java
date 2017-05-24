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
 * 2017-5-23 上午11:46:00
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

	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
		
	
	/**
	 * 根据屏幕的DPI重新封装drawPoint()方法，这里drawPoint()方法里所用的坐标单位是px，需要转化为dip/dp。
	 * 把需要画出来的点暂时放在数组points里，然后遍历画出来
	 * 现在碰到的手机都还是整数，如华为的mate2为2,LG代工的nexus5为3，但以后碰到小数位的可能会有隐患
	 * 相对于imageview的原点再平移一个(20,30)
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
