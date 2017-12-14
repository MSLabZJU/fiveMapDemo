package com.tristan.mapview;

import java.io.BufferedReader;
import java.io.File;
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

import com.tristan.astar.TestForAstar;
import com.tristan.astar.astarView;
import com.tristan.fivemapdemo.R;
import com.tristan.sqlhelper.DatabaseUtil;
import com.tristan.sqlhelper.PointsData;
import com.tristan.test.AnchorsView;
import com.tristan.test.Toolbox;

public class MainActivity extends Activity {

	private Button btn_connect; // �������������������
	private ToggleButton btn_sound;
	private ToggleButton btn1; // ���԰�ť
	private ToggleButton btn2; // ��ʾ�ɰ��״̬�л���ť
	private ToggleButton btn3; // ���ڴ����л�ͼ�İ�ť
	private Button btn4; // ��ʼ����ť
	private Button btn5; // ���ڲ���A���㷨�İ�ť
	private astarView pathView; // ���ڻ���astar�㷨������·��
	private TextView screenInfo; // ��Ļ�����Ϣչʾ
	private MapView draw_point; // ����������ͼ��view
	private BoardView draw_board; // �������ɰ��view
	private BackgroundView draw_map; // ��������ͼ��view

	private ImageView map_bg; // ��ͼ
	private Spinner mapSet;
	private TestForAstar astarTest; // ����

	private boolean soundFlag;

	private ArrayList<Point> anchorPoints; // ê�ڵ�����
	private AnchorsView anchorsView; // ê�ڵ���ͼ
	
	private String dataFileNameString; //��¼�ļ���

	// ��SoundPool��������
	private SoundPool sp;
	private HashMap<Integer, Integer> spMap;

	public void playSounds(int sound, int number) {
		// AudioManger����ͨ��getSystemService(Service.AUDIO_SERVICE)��ȡ
		AudioManager am = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
		// ����ֻ����������������
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// float audioCurrentVolumn =
		// am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = audioMaxVolumn;
		sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
	}

	private void mkLogDir() {
		// ������Ŀ¼�µ�logfiles�ļ���
		String dataDirString = Toolbox.getSDPath().toString() + File.separator + "loc_logfiles";
		File dataDirFile = new File(dataDirString);
		dataDirFile.mkdir();
		Log.i("1213", "loc_logfiles�ļ��д����ɹ�");
	}
	
	
	// �����������߳��͹�������Ϣ
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Point this_point = (Point) msg.obj;
			draw_point = new MapView(MainActivity.this, "test2", this_point);
			FrameLayout fl = (FrameLayout) findViewById(R.id.outside);
			fl.addView(draw_point);
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

		// ��ȡ֡���ֶ��󣬲������䱳��ͼ
		final FrameLayout fl = (FrameLayout) findViewById(R.id.outside);

		// ��assets�е��ⲿdb�ļ�������data/data/databases��
		DatabaseUtil.packDataBase(this);
		PointsData locPoint = new PointsData(this);
		final List<Point> points = locPoint.getPointList();
		// draw_point=new MapView(MainActivity.this,"test1",points);

		sp = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
		spMap = new HashMap<Integer, Integer>();
		spMap.put(1, sp.load(this, R.raw.testsound, 1));

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ��ֹ����

		mkLogDir(); // ������־�ļ���

		// TODO ��������ȡ����
		Thread getPoints = new Thread(new Runnable() {
			public void run() {
				Socket socket = new Socket();
				try {
					Log.i("1213", "����getPoints�߳�");
					SocketAddress socAddress = new InetSocketAddress("192.168.1.103", 9999);
					socket.connect(socAddress, 1000);
					Log.i("1213", "�������ӳɹ�");
					//ÿ��һ�������ͻᴴ��һ���µ��ļ����Ե�ǰϵͳʱ������
					dataFileNameString = Toolbox.getFileNameString();
					try {
						Toolbox.writeExperimentInfo(dataFileNameString);
					} catch (IOException e) {
						Log.i("1213", "д��˵���ļ�ʧ��");
						e.printStackTrace();
					}
					//��������д����
					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dataFileNameString, true));
					BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					String line = null;
					while ((line = bw.readLine()) != null) {
						if ("success".equals(line)) {
							line = bw.readLine();
							int size = Integer.parseInt(line);
							if (size == 3) {
								writer.append("TDOA;");
							} else if (size == 2) {
								writer.append("POSI;");
							}
							int[] result = new int[size];
							long[] timestamp = new long[size];
							for (int i = 0; i < size; i++) {
								result[i] = (int) Float.parseFloat(bw.readLine());
								timestamp[i] =  System.currentTimeMillis()/1000;  //��ֵΪ1970.1.1�����ڵ�����
								writer.append(timestamp[i]+";"+result[i]+";");
								Log.i("1213", timestamp[i]+";"+result[i]+";");
							}
							writer.append("\n"); //����
							Point point_get = new Point(result[0], result[1]);
							Log.i("1213", point_get.toString());
							Message msg = new Message();
							msg.obj = point_get;
							handler.sendMessage(msg);
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

		// ��ȡ��Ļ�ķֱ���
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		// ��Ϊmate2��Ϊ720px���߶�Ϊ1208px
		// nexus5��Ϊ1080px,�߶�Ϊ1776px
		int width = metric.widthPixels; // ��Ļ��ȣ����أ�
		int height = metric.heightPixels; // ��Ļ�߶ȣ����أ�
		// ��Ϊ��mate2�����ܶ�Ϊ2.0
		// nexus5�����ܶ�Ϊ3.0
		float density = metric.density; // ��Ļ�ܶȣ�0.75 / 1.0 / 1.5/ 2.0��
		int densityDpi = metric.densityDpi; // ��Ļ�ܶ�DPI��120 / 160 / 240��
		screenInfo = (TextView) findViewById(R.id.screenInfo);
		screenInfo.setText("��ȣ�" + width + "  �߶ȣ�" + height + "  �ܶȣ�" + density + "  DPI��" + densityDpi);

		bindElement();// �󶨽����ϵ�Ԫ��

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

		// btn1�İ�������
		btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					soundFlag = true;
					Thread soundTestThread = new Thread(new Runnable() {

						@Override
						public void run() {
							while (soundFlag) {
								playSounds(1, 1);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					});
					soundTestThread.start();
					
				} else {
					soundFlag = false;
				}
			}
		});

		// btn1�İ�������
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

		// btn2�İ���״̬������������ʾ�ɰ�
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

		// btn3�İ�������,������ʾ�ѻ������ĵ�ͼ
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

		// btn4�İ�������
		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �������
				Intent intent = getIntent();
				finish();
				startActivity(intent);

				// ����ֱ���ö�̬ȥ��view�ķ��������������������
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

		// ����Spinner�ļ����������ڸı䱳����ͼ
		mapSet.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					map_bg.setBackgroundResource(R.drawable.blank_map); // �հ�ͼ
					// ���ê�ڵ����ͼ
					fl.addView(anchorsView);
					break;
				case 1:
					map_bg.setBackgroundResource(R.drawable.control_new_5f); // ������¥5F
					fl.removeView(anchorsView);
					break;
				case 2:
					map_bg.setBackgroundResource(R.drawable.map_9_526); // �̾�526
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

	// ʵ�ִ�dip��λ��px��λ��ת��
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// ��װ����dip->px
	public int transf(float dipValue) {
		return dip2px(MainActivity.this, dipValue);
	}
}
