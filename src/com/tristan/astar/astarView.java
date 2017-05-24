package com.tristan.astar;

import java.util.ArrayList;

import com.tristan.mapview.PointView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 生成astar算法得到的路径，后面要改为PointView的子类
 * @author TristanHuang
 * 2017-5-23 上午11:51:04
 */
public class astarView extends PointView{
	private Point src;
	private Point dst;
	private ArrayList<Point> path;
	
	
	public astarView(Context context) {
		super(context);
	}
	
	
	/**
	 * 主要做了两件事：
	 * 1. 传递参数给A*算法模块，开始运算
	 * 2. 画出了障碍物（后面分离出来）
	 * 3. 将运算结果发送给path保存下来
	 * @param context
	 * @param barrier
	 * @param src  起点
	 * @param dst  终点
	 */
	public astarView(Context context,Barrier barrier,Point src,Point dst) {
		super(context);
		this.src = src;
		this.dst = dst;
		GraphForAstar test = new GraphForAstar(500, 250, barrier, src, dst);
		test.calculatePath();
		this.path = test.getFinalPath();
	}
	
	
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(8); 
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); 
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		Paint paint_special = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(15); 
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.BLUE); 
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		//用较粗的点画出起点和终点
		drawDpPoint(canvas, src, paint_special);
		drawDpPoint(canvas, dst, paint_special);
		//用较细的点画出astar算法给出的轨迹点
		drawDpPoint(canvas, path, paint_Point);
		
	}
	
}
