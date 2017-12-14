package com.tristan.mapview;

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
 * 本来是准备通过程序画出地图的，但是后来发现没必要，可以再补充一下
 * @author TristanHuang
 * @deprecated
 * 2017-5-24  上午10:39:01
 */
public class BackgroundView extends PointView{
	private int x,y;

	public BackgroundView(Context context) {
		super(context);
	}
	
	
	//drawDpLine()接收由几个点组成的Point[]数组，然后连线
	private void drawDpLine(Canvas canvas,Point[] points,Paint paint){
		int den = 3;
		int len = points.length;
		for(int count=0; count < (len-1) ;count++){
			x=(points[count].x+20)*den;
			y=(points[count].y+30)*den;
			int x_next = (points[count+1].x+20)*den;
			int y_next = (points[count+1].y+30)*den;
			canvas.drawLine(x, y, x_next, y_next, paint);
		}
	}
	
	
	
	Point[] line_point = new Point[]{
			new Point(0,0),
			new Point(200,0),
			new Point(50,50),
	};
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置图层的背景色
		canvas.drawColor(Color.TRANSPARENT);
		// 添加画笔
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true); // 抗锯齿
		paint_Line.setStrokeWidth(2); // 设置画笔宽度
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setColor(Color.BLACK); // 画笔的颜色

		Paint paint_Point = new Paint();
		paint_Point.setAntiAlias(true); // 抗锯齿
		paint_Point.setStrokeWidth(4); // 设置画笔宽度
		paint_Point.setStyle(Style.STROKE);
		paint_Point.setColor(Color.RED); // 画笔的颜色
		paint_Point.setStrokeCap(Cap.ROUND);//圆头的画笔头
		
		drawDpLine(canvas, line_point, paint_Line);
	}
	
}
