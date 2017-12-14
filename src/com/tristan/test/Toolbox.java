/**
 * 
 */
package com.tristan.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

/**
 * 工具箱包
 * @author TristanHuang
 * 2017-6-28  下午4:49:57
 */
public class Toolbox {
	
	/**
	 * @return 返回sd卡根目录的路径
	 */
	public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState()
		  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		  if (sdCardExist)
		  {
		  sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		  }
		  return sdDir.toString();
	}
	
	/**
	 * @return 返回将要新建的txt文件名
	 */
	public static String getFileNameString() {
		Calendar cal = Calendar.getInstance();
		String filenameString = Toolbox.getSDPath().toString()+File.separator+"loc_logfiles"
				+File.separator+"logfile_"+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"
				+cal.get(Calendar.DATE)+"_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"
				+cal.get(Calendar.SECOND)+".txt";
        return filenameString;
	}
	
	public static void TDOAData(String file, String dataString) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(dataString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 生成每次实验log文件的头信息，主要记录实验时间
	 * @param fileNameString  log文件的文件名
	 * @throws IOException
	 */
	public static void writeExperimentInfo(String fileNameString) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileNameString, true);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.append("% LogFile created by the 'fiveMapDemo' App for Android.\n");
		osw.append("% Date of creation: "+new Date()+"\n");
		osw.append("% Developed by Tristan in ISMC research group at ZJU, China.\n");
		osw.append("% The 'fiveMapDemo' program stores information of TDOA+PDR experiment.\n");
		osw.append("% \n");
		osw.append("% The model of device:  "+android.os.Build.MODEL+"\n");
		osw.append("% The Manufacturer:     "+android.os.Build.MANUFACTURER+"\n");
		osw.append("% \n");
		osw.append("% LogFile Data format:\n");
		osw.append("% POSI data: 	'POSI;AppTimestamp1(s);TDOAValue1(m);AppTimestamp2(s);TDOAValue2(m);AppTimestamp3(s);TDOAValue3(m);'\n");
		osw.append("% TDOA data: 	'TDOA;AppTimestamp1(s);TDOAValue1(m);AppTimestamp2(s);TDOAValue2(m);AppTimestamp3(s);TDOAValue3(m);'\n");
		osw.append("% \n");
		osw.close();
		fos.close();
	}
}
