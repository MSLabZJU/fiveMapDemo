package com.tristan.test;

import java.util.HashMap;

import android.media.AudioManager;
import android.media.SoundPool;

import com.tristan.fivemapdemo.R;

public class PlaySound implements Runnable {
	
	//��SoundPool��������	
	private SoundPool sp = new SoundPool(7,AudioManager.STREAM_MUSIC,0);//ͬʱ���ŵ������Ƶ��Ϊ7
	private HashMap<Integer, Integer> spMap = new HashMap<Integer,Integer>();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
