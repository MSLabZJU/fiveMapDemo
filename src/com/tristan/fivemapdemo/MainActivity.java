package com.tristan.fivemapdemo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//测试一下github是否关联成功
public class MainActivity extends Activity {
	//测试按钮
	private ToggleButton btn1;
	//显示蒙版的状态切换按钮
	private ToggleButton btn2;
	//用于代码中画图的按钮
	private ToggleButton btn3;
	//初始化按钮
	private Button btn4;
	//屏幕相关信息展示
	private TextView screenInfo;
	//用来画测试图的view
	private MapView draw_point;
	//用来画蒙板的view
	private BoardView draw_board;
	//用来画地图的view
	private BackgroundView draw_map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		// 获取帧布局对象，并设置其背景图
		final FrameLayout fl = (FrameLayout) findViewById(R.id.outside);


	   //获取屏幕的分辨率
	   DisplayMetrics metric = new DisplayMetrics();
       getWindowManager().getDefaultDisplay().getMetrics(metric);
       
       //华为mate2宽为720px，高度为1208px
       //nexus5宽为1080px,高度为1776px
       int width = metric.widthPixels;  // 屏幕宽度（像素）
       int height = metric.heightPixels;  // 屏幕高度（像素）
       //华为的mate2测试密度为2.0
       //nexus5测试密度为3.0
       float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5/ 2.0）
       int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
       screenInfo = (TextView) findViewById(R.id.screenInfo);
       screenInfo.setText("宽度："+width+"  高度："+height+"  密度："+density+"  DPI："+densityDpi);
		
       
       //将assets中的外部db文件拷贝到data/data/databases中
		DatabaseUtil.packDataBase(this);
       
       
		btn1 = (ToggleButton) findViewById(R.id.btn_test);
		btn2 = (ToggleButton) findViewById(R.id.btn_board);
		btn3 = (ToggleButton) findViewById(R.id.btn_map);
		btn4 = (Button) findViewById(R.id.btn_reset);
		
		
		PointsData locPoint = new PointsData(this);
		List<Point> points = locPoint.getPointList();
		draw_point=new MapView(MainActivity.this,"test",points);
		System.out.println(points);
		
		
		//btn1的按键监听
		btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		            fl.addView(draw_point);
		        } else {
		            fl.removeView(draw_point);
		        }
		    }
		});
		
		//btn2的按键状态监听，打开则显示蒙版
		btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		    		draw_board=new  BoardView(MainActivity.this);
		            fl.addView(draw_board);
		        } else {
		            fl.removeView(draw_board);
		        }
		    }
		});
		

		//btn3的按键监听,打开则显示已画出来的地图
		btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	if (isChecked) {
		    		draw_map = new BackgroundView(MainActivity.this);
		    		fl.addView(draw_map);
		        } else {
		        	fl.removeView(draw_map);
		        }
		    }
		});
		
		
		//btn4的按键监听
		btn4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//重启主活动
				Intent intent=getIntent();
				finish();
				startActivity(intent);
				
				//现在直接用动态去除view的方法，而不是重启主活动了
//				ll.removeView(draw_board);
//				ll.removeView(draw_point);
			}
		});
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
	//实现从dip单位到px单位的转换
	public  int dip2px(Context context, float dipValue){ 
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int)(dipValue * scale + 0.5f); 
		} 
    
	
	//封装，从dip->px
	public  int transf(float dipValue){
		return dip2px(MainActivity.this,dipValue);
	}
}



