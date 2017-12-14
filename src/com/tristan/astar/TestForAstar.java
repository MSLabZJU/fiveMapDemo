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
 * ��A*�㷨����һ������
 * @author TristanHuang
 * 2017-5-24  ����10:56:36
 */
public class TestForAstar extends PointView{
	private Point src;
	private Point dst;
	private ArrayList<Point> path;
	private Barrier barrier;
	private ArrayList<Point> barrierPath;
	
	
	/**
	 * Ĭ�ϲ��ԣ����д����(75,350),�յ�(200,50)
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
	 * �ɱ�����յ㹹����
	 * @param context
	 * @param barrier �ϰ��д�������������
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
	 * ��Ӻͻ����ϰ���㼯
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

		// ����ͼ��ı���ɫ
		canvas.drawColor(Color.TRANSPARENT);
		// ��ӻ���
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(20); // ���û��ʿ��
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // ���ʵ���ɫ
		paint_Point.setStrokeCap(Cap.SQUARE);//Բͷ�Ļ���ͷ
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true); // �����
		paint_Line.setStrokeWidth(8); // ���û��ʿ��
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setColor(Color.RED); // ���ʵ���ɫ
		paint_Line.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		Paint paintBarrier = new Paint();
		paintBarrier.setAntiAlias(true); // �����
		paintBarrier.setStrokeWidth(10); // ���û��ʿ��
		paintBarrier.setStyle(Style.STROKE);
		paintBarrier.setColor(Color.BLACK); // ���ʵ���ɫ
		paintBarrier.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		//�ýϴֵĵ㻭�������յ�
		drawDpPoint(canvas, paint_Point, src, dst);
		drawDpPoint(canvas, paintBarrier, this.barrierPath);
		//�ý�ϸ�ĵ㻭��astar�㷨�����Ĺ켣��
		drawDpPoint(canvas, paint_Line, path);
	}
	
}
