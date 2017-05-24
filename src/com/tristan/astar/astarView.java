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
 * ����astar�㷨�õ���·��������Ҫ��ΪPointView������
 * @author TristanHuang
 * 2017-5-23 ����11:51:04
 */
public class astarView extends PointView{
	private Point src;
	private Point dst;
	private ArrayList<Point> path;
	
	
	public astarView(Context context) {
		super(context);
	}
	
	
	/**
	 * ��Ҫ���������£�
	 * 1. ���ݲ�����A*�㷨ģ�飬��ʼ����
	 * 2. �������ϰ��������������
	 * 3. �����������͸�path��������
	 * @param context
	 * @param barrier
	 * @param src  ���
	 * @param dst  �յ�
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

		// ����ͼ��ı���ɫ
		canvas.drawColor(Color.TRANSPARENT);
		// ��ӻ���
		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(8); 
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); 
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		Paint paint_special = new Paint();
		paint_Point.setAntiAlias(true); // �����
		paint_Point.setStrokeWidth(15); 
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.BLUE); 
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		//�ýϴֵĵ㻭�������յ�
		drawDpPoint(canvas, src, paint_special);
		drawDpPoint(canvas, dst, paint_special);
		//�ý�ϸ�ĵ㻭��astar�㷨�����Ĺ켣��
		drawDpPoint(canvas, path, paint_Point);
		
	}
	
}
