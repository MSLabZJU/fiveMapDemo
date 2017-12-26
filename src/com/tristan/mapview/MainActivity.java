package com.tristan.mapview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.impl.conn.Wire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ivan.particleFilter.ParticleFilter;
import com.ivan.pdr.GyroscopeOrientation;
import com.ivan.pdr.Orientation;
import com.ivan.pdr.PositionEstimate;
import com.ivan.pdr.StepDetector;
import com.tristan.astar.TestForAstar;
import com.tristan.astar.astarView;
import com.tristan.fivemapdemo.R;
import com.tristan.sqlhelper.DatabaseUtil;
import com.tristan.sqlhelper.PointsData;
import com.tristan.test.AnchorsView;
import com.tristan.test.MyData;
import com.tristan.test.Toolbox;

public class MainActivity extends Activity {
	private static int numParticles = 200;	
	private int mStepNumber = 0;	//濮濄儲鏆�
	private int mFormerStepNumber = 0;		
    private float[] vOrientation = new float[3];	//閺傜懓鎮�
    private float[] mPosition = new float[2];
    private float[] mIndoorPosition = new float[2];
    protected Runnable mRunable;
    protected Handler mHandler;
    
    private Orientation mOrientation;	//閺傜懓鎮�
    private StepDetector mStepDetector;		//鐠佲剝顒�
    private PositionEstimate mPositionEstimate;		//娴ｅ秶鐤嗘导鎷岊吀
    private ParticleFilter mParticleFilter;

	private Button btn_connect; // 用于与服务器建立连接
	private ToggleButton btn_sound;
	private ToggleButton btn1; // 测试按钮
	private ToggleButton btn2; // 显示蒙版的状态切换按钮
	private ToggleButton btn3; // 用于代码中画图的按钮
	private Button btn4; // 初始化按钮
	private Button btn5; // 用于测试A星算法的按钮
	private astarView pathView; // 用于画出astar算法给出的路径
	private TextView screenInfo; // 屏幕相关信息展示
	private MapView draw_point; // 用来画测试图的view
	private BoardView draw_board; // 用来画蒙板的view
	private BackgroundView draw_map; // 用来画地图的view

	private ImageView map_bg; // 地图
	private Spinner mapSet;
	private TestForAstar astarTest; // 测试

	private boolean soundFlag;

	private ArrayList<Point> anchorPoints; // 锚节点坐标
	private AnchorsView anchorsView; // 锚节点视图
	
	private String dataFileNameString; //记录文件名
	OutputStreamWriter writer;

	// 用SoundPool播放声音
	private SoundPool sp;
	private HashMap<Integer, Integer> spMap;

	public void playSounds(int sound, int number) {
		// AudioManger对象通过getSystemService(Service.AUDIO_SERVICE)获取
		AudioManager am = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
		// 获得手机播放最大音乐音量
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// float audioCurrentVolumn =
		// am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = audioMaxVolumn;
		sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
	}

	private void mkLogDir() {
		// 建立根目录下的logfiles文件夹
		String dataDirString = Toolbox.getSDPath().toString() + File.separator + "loc_logfiles";
		File dataDirFile = new File(dataDirString);
		dataDirFile.mkdir();
		Log.i("1213", "loc_logfiles文件夹创建成功");
	}
	
	
	// 用来处理子线程送过来的消息
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MyData data = (MyData) msg.obj;
			//创建数据写入流
			try {
				switch (data.getSize()){
				case 1:
					writer.append("SOUND;"+data.getTimestampSound()+"\n");
					break;
				case 2:
					Point point = new Point((int)data.getX(), (int)data.getY());
					draw_point = new MapView(MainActivity.this, "test2", point);
					FrameLayout fl = (FrameLayout) findViewById(R.id.outside);
					fl.addView(draw_point);
					writer.append("POSI ;"+data.getTimestampX()+";"+data.getX()+";"+data.getTimestampY()+";"+data.getY()+";\n");
					break;
				case 3:
					writer.append("TDOA ;"+data.getTimestamp1()+";"+data.getTdoaValue1()+";"
							+data.getTimestamp2()+";"+data.getTdoaValue2()+";"
							+data.getTimestamp3()+";"+data.getTdoaValue3()+";\n");
					break;
				case 4:
					Log.d("PDRMessage", "PDRMessage: " + data.getOrientation() + " "
							+ data.getStepLength() + " " + data.getStepNumber() + " "
							+ data.getTimestampPDR());
					break;
				}
			} catch (FileNotFoundException e) {
				Log.i("1213", "log文件尚未创建");
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("1213", "文件写入发生错误");
				e.printStackTrace();
			}
			
		}
	};

	private void bindElement() {
		btn_connect = (Button) findViewById(R.id.btn_connect);
		btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
		btn1 = (ToggleButton) findViewById(R.id.btn_test);
		btn2 = (ToggleButton) findViewById(R.id.btn_board);
		btn3 = (ToggleButton) findViewById(R.id.btn_map);
		btn4 = (Button) findViewById(R.id.btn_reset);
		btn5 = (Button) findViewById(R.id.btn_astar);
		mapSet = (Spinner) findViewById(R.id.spinner_map);
		map_bg = (ImageView) findViewById(R.id.map);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		startSensor();
		
        mOrientation.onResume();
        mStepDetector.onResume();
        
		// 获取帧布局对象，并设置其背景图
		final FrameLayout fl = (FrameLayout) findViewById(R.id.outside);

		// 将assets中的外部db文件拷贝到data/data/databases中
		DatabaseUtil.packDataBase(this);
		PointsData locPoint = new PointsData(this);
		final List<Point> points = locPoint.getPointList();
		// draw_point=new MapView(MainActivity.this,"test1",points);

		sp = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
		spMap = new HashMap<Integer, Integer>();
		spMap.put(1, sp.load(this, R.raw.testsound, 1));

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止休眠

		mkLogDir(); // 创建日志文件夹

		// TODO 把这个类抽取出来
		Thread getPoints = new Thread(new Runnable() {
			public void run() {
				Socket socket = new Socket();
				try {
					Log.i("1213", "进入getPoints线程");
					SocketAddress socAddress = new InetSocketAddress("192.168.1.103", 9999);
					socket.connect(socAddress, 1000);
					Log.i("1213", "建立连接成功");
					//每次一进入程序就会创建一个新的文件，以当前系统时间命名
					dataFileNameString = Toolbox.getFileNameString();
					try {
						Toolbox.writeExperimentInfo(dataFileNameString);
					} catch (IOException e) {
						Log.i("1213", "写入说明文件失败");
						e.printStackTrace();
					}
					writer = new OutputStreamWriter(new FileOutputStream(dataFileNameString, true));
					BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					String line = null;
					while ((line = bw.readLine()) != null) {
						if ("success".equals(line)) {
							line = bw.readLine();
							int size = Integer.parseInt(line);
							double[] result = new double[size];
							long[] timestamp = new long[size];
							for (int i = 0; i < size; i++) {
								result[i] = Double.parseDouble(bw.readLine());
								timestamp[i] =  System.currentTimeMillis();  //数值为1970.1.1到现在的毫秒数
								Log.i("1213", timestamp[i]+";"+result[i]+";");
							}
							if (size == 2){
								MyData data = new MyData(2,timestamp[0],result[0],timestamp[1],result[1]);
								Message msg = new Message();
								msg.obj = data;
								handler.sendMessage(msg);
							} else if (size == 3) {
								MyData data = new MyData(3,timestamp[0],result[0],timestamp[1],result[1],timestamp[2],result[2]);
								Message msg = new Message();
								msg.obj = data;
								handler.sendMessage(msg);
							}
						} else if ("failed".equals(line)) {
							//do nothing
						}
					}
					writer.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		getPoints.start();

		// 获取屏幕的分辨率
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		// 华为mate2宽为720px，高度为1208px
		// nexus5宽为1080px,高度为1776px
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		// 华为的mate2测试密度为2.0
		// nexus5测试密度为3.0
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5/ 2.0）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		screenInfo = (TextView) findViewById(R.id.screenInfo);
		screenInfo.setText("宽度：" + width + "  高度：" + height + "  密度：" + density + "  DPI：" + densityDpi);

		bindElement();// 绑定界面上的元素

		anchorPoints = new ArrayList<Point>();
		anchorPoints.add(new Point(50, 50));
		anchorPoints.add(new Point(200, 50));
		anchorPoints.add(new Point(50, 400));
		anchorPoints.add(new Point(200, 400));
		anchorsView = new AnchorsView(MainActivity.this, anchorPoints);

		Log.i("1212", "anchorPoints[3] = " + anchorPoints.get(3));

		btn_connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getPoints.start();
			}
		});

		// btn1的按键监听
		btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					soundFlag = true;
					Thread soundTestThread = new Thread(new Runnable() {

						@Override
						public void run() {
							while (soundFlag) {
								playSounds(1, 1);
								long timestampSound = System.currentTimeMillis();
								/*MyData data = new MyData(1,timestampSound);
								Message msg = new Message();
								msg.obj = data;
								handler.sendMessage(msg);*/
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					});
					soundTestThread.start();
					
					//启动PDRService
					Thread PDRThread = new Thread(new Runnable() {
					
						@Override
						public void run()
						{
							while (soundFlag){
								
							
							//delay
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//get User's orientation
							vOrientation = mOrientation.getOrientation();
							//get User's stepNumber
							mStepNumber = mStepDetector.getStepNumber();
							//Log.d(TAG, "vOrientation[0]: " + Math.toDegrees(vOrientation[0]));
							//when detect a new step
							//Log.d("mStepNumber", "mStepNumber: " + mStepNumber);
							if (mStepNumber != mFormerStepNumber) {
								//Estimate user's position, PDR-Only
								long timeCurrent = System.currentTimeMillis();
								float mStepLength = 0.6f;
			                    Message msg = new Message();
			                    MyData data = new MyData(4, timeCurrent, mStepNumber, 
			                    		mStepLength, (float)Math.toDegrees(vOrientation[0]));
			                    msg.obj = data;
			                    handler.sendMessage(msg);
			    				mFormerStepNumber = mStepNumber;
							}
						}
						}
					});
					PDRThread.start();	
					
				} else {
					soundFlag = false;
				}
			}
		});

		// btn1的按键监听
		btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					new Thread(new Runnable() {
						public void run() {
							for (Point point : points) {
								Message msg = new Message();
								msg.obj = point;
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.sendMessage(msg);
								// draw_point=new
								// MapView(MainActivity.this,"test2",point);
								// fl.addView(draw_point);
							}
						}
					}).start();

				} else {
					fl.removeView(draw_point);
				}
			}
		});

		// btn2的按键状态监听，打开则显示蒙版
		btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					draw_board = new BoardView(MainActivity.this);
					fl.addView(draw_board);
				} else {
					fl.removeView(draw_board);
				}
			}
		});

		// btn3的按键监听,打开则显示已画出来的地图
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

		// btn4的按键监听
		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重启主活动
				Intent intent = getIntent();
				finish();
				startActivity(intent);

				// 现在直接用动态去除view的方法，而不是重启主活动了
				// ll.removeView(draw_board);
				// ll.removeView(draw_point);
			}
		});

		btn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Barrier barrier = new Barrier();
				// for(int i = 100; i<400; i++){
				// barrier.addBarrierPoint(new Point(100,i));
				// }
				// Point src = new Point(70,150);
				// Point dst = new Point(130,150);
				// pathView = new astarView(MainActivity.this, barrier, src,
				// dst);
				// fl.addView(pathView);
				TestForAstar astarTest = new TestForAstar(MainActivity.this);
				fl.addView(astarTest);
			}
		});

		// 设置Spinner的监听器，用于改变背景地图
		mapSet.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					map_bg.setBackgroundResource(R.drawable.blank_map); // 空白图
					// 添加锚节点的视图
					fl.addView(anchorsView);
					break;
				case 1:
					map_bg.setBackgroundResource(R.drawable.control_new_5f); // 工控新楼5F
					fl.removeView(anchorsView);
					break;
				case 2:
					map_bg.setBackgroundResource(R.drawable.map_9_526); // 教九526
					fl.removeView(anchorsView);
					break;

				default:
					break;
				}
			};

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

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

	// 实现从dip单位到px单位的转换
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// 封装，从dip->px
	public int transf(float dipValue) {
		return dip2px(MainActivity.this, dipValue);
	}
	
    
/*    @Override
    public void onResume() {
        mOrientation.onResume();
        mStepDetector.onResume();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOrientation.onPause();
        mStepDetector.onPause();
    }
    
    public void startSensor() {
    	//Log.d(TAG, "Start Sensors");
    	//mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	mOrientation = new GyroscopeOrientation(this);
    	mStepDetector = new StepDetector(this);
    	mPositionEstimate = new PositionEstimate(mPosition, 0);
    	com.ivan.particleFilter.Point tmpPosition = new com.ivan.particleFilter.Point(mPosition[0],mPosition[1]);
    	mParticleFilter = new ParticleFilter(numParticles, tmpPosition);
    	/*Log.d(TAG, "mPosition " + mStepNumber + ", x: " 
				+ mPosition[0] + "y: " + mPosition[1]);*/
    }
}
