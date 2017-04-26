package com.xiaoxuan.map;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.view.View;

public class astarView extends View{
	private int x,y;
	private Point src;
	private Point dst;
	private ArrayList<Point> path;
	
	//获取屏幕的像素点密度
	DisplayMetrics metric = new DisplayMetrics();
	private float density = metric.density;
	
	public astarView(Context context) {
		super(context);
	}
	
	public astarView(Context context,Barrier barrier,Point src,Point dst) {
		super(context);
		this.src = src;
		this.dst = dst;
		GraphForAstar test = new GraphForAstar(500, 250, barrier, src, dst);
		test.calculatePath();
		this.path = test.getFinalPath();
	}
	
	private void drawDpPoint(Canvas canvas,Point point,Paint paint){
		int u = 3;
		x=(point.x+20)*u;
		y=(point.y+30)*u;
		canvas.drawPoint(x,y,paint);
	}
	
	
	private void drawDpPoint(Canvas canvas,ArrayList<Point> points,Paint paint){
		int u = 3;
		for(Point p:points){
			x=(p.x+20)*u;
			y=(p.y+30)*u;
			canvas.drawPoint(x,y,paint);
		}
	}
	
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(8); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		Paint paint_special = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(15); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.BLUE); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		//用较粗的点画出起点和终点
		drawDpPoint(canvas, src, paint_special);
		drawDpPoint(canvas, dst, paint_special);
		//用较细的点画出astar算法给出的轨迹点
		drawDpPoint(canvas, path, paint_Point);
		
	}
	
}
