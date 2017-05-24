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
 * 对A*算法做的一个测试
 * @author TristanHuang
 * 2017-5-24  上午10:56:36
 */
public class TestForAstar extends PointView{
	private Point src;
	private Point dst;
	private ArrayList<Point> path;
	private Barrier barrier;
	private ArrayList<Point> barrierPath;
	
	
	/**
	 * 默认测试，起点写死的(75,350),终点(200,50)
	 * @param context
	 */
	public TestForAstar(Context context) {
		super(context);
		initialBarrier();
		this.barrierPath = this.barrier.getBarrierPoint();
		this.src = new Point(75,350);
		this.dst = new Point(200,50);
		GraphForAstar test = new GraphForAstar(500, 250, barrier, src, dst);
		test.calculatePath();
		this.path = test.getFinalPath();
	}
	
	
	/**
	 * 可变起点终点构造器
	 * @param context
	 * @param barrier 障碍物，写在这里类里面的
	 * @param src (75,350)
	 * @param dst (200,50)
	 */
	public TestForAstar(Context context,Barrier barrier,Point src,Point dst) {
		super(context);
		this.src = src;
		this.dst = dst;
		GraphForAstar test = new GraphForAstar(500, 250, barrier, src, dst);
		test.calculatePath();
		this.path = test.getFinalPath();
	}
	
	
	
	/**
	 * 添加和绘制障碍物点集
	 */
	private void initialBarrier(){
		this.barrier = new Barrier();
		for(int i = 100; i<=300 ; i++){
			this.barrier.addBarrierPoint(new Point(50,i));
		}
		for(int i = 300; i<=400 ; i++){
			this.barrier.addBarrierPoint(new Point(100,i));
		}
		for(int i = 100; i<=400 ; i++){
			this.barrier.addBarrierPoint(new Point(150,i));
		}
		for(int i = 50; i<=150 ; i++){
			this.barrier.addBarrierPoint(new Point(i,100));
		}
		for(int i = 50; i<=100 ; i++){
			this.barrier.addBarrierPoint(new Point(i,300));
		}
		for(int i = 100; i<=150 ; i++){
			this.barrier.addBarrierPoint(new Point(i,400));
		}
	}
	

	
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(20); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.SQUARE);//圆头的画笔头
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true); // 抗锯齿
		paint_Line.setStrokeWidth(8); // 设置画笔宽度
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setColor(Color.RED); // 画笔的颜色
		paint_Line.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		Paint paintBarrier = new Paint();
		paintBarrier.setAntiAlias(true); // 抗锯齿
		paintBarrier.setStrokeWidth(10); // 设置画笔宽度
		paintBarrier.setStyle(Style.STROKE);
		paintBarrier.setColor(Color.BLACK); // 画笔的颜色
		paintBarrier.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		//用较粗的点画出起点和终点
		drawDpPoint(canvas, paint_Point, src, dst);
		drawDpPoint(canvas, paintBarrier, this.barrierPath);
		//用较细的点画出astar算法给出的轨迹点
		drawDpPoint(canvas, paint_Line, path);
	}
	
}
