package com.tristan.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;

import com.tristan.mapview.PointView;

public class anchorsView extends PointView {
	private ArrayList<Point> anchorPoints;

	public anchorsView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public anchorsView(Context context, ArrayList<Point> anchorPoints){
		super(context);
		this.anchorPoints = (ArrayList<Point>) anchorPoints;
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
		paint_Point.setStrokeWidth(30); 
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); 
		paint_Point.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		//���������ű�ڵ�
		drawDpPoint(canvas, anchorPoints, paint_special);
	}
}
