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
import android.util.Log;

import com.tristan.mapview.PointView;

public class AnchorsView extends PointView {
	private ArrayList<Point> anchorPoints;

	public AnchorsView(Context context) {
		super(context);
	}
	
	public AnchorsView(Context context, ArrayList<Point> anchorPoints){
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
		
		Paint paint_anchor = new Paint();
		paint_anchor.setAntiAlias(true); // �����
		paint_anchor.setStrokeWidth(30); 
		paint_anchor.setStyle(Style.STROKE);
		paint_anchor.setColor(Color.RED); 
		paint_anchor.setStrokeCap(Cap.ROUND);//Բͷ�Ļ���ͷ
		
		//���������ű�ڵ�
		drawDpPoint(canvas, paint_anchor, anchorPoints);
		Log.i("1212", "��AnchorsView���У�"+anchorPoints.get(3));
	}
}
