package com.ivan.particleFilter;

import java.util.Random;

/**
 * Created by hyf on 2017/11/21.
 */
public class Test {
    public static void main(String args[]) throws Exception{
        int particleNum = 100;   //绮掑瓙鏁�?
        int stepNum = 20;
        int positionNum = stepNum + 1;  //浣嶇疆鏁�?
        float stepLength = 1f;  //鐪熷疄姝ラ暱
        float stepLengthBias = 0.1f;    //姝ラ暱鍥哄畾鍋忓�?
        float stepOrientation = 0f; //鐪熷疄鏂瑰悜
        float stepOrientationBias = 0f;     //鏂瑰悜鍥哄畾鍋忓�?
        float Fnoise = 0.1f;    //姝ラ暱璇樊
        float Tnoise = 0.1f;    //鏂瑰悜璇樊
        float Snoise = 0.1f;    //閲忔祴璇樊
        Random random = new Random();

        //鍒濆鍖栫粡绮掑瓙婊ゆ尝鍚庣殑杞ㄨ抗
        Point[] afterPFTrack = new Point[positionNum];
        for (int i = 0; i < positionNum; i ++){
            afterPFTrack[i] = new Point();
        }

        //鍒濆鍖朠DR寰楀埌鐨勬�?块噺娴嬪�煎拰鏂瑰悜閲忔祴鍊�?
        float[] stepLengths = new float[stepNum];   //PDR姝ラ暱閲忔祴鍊�
        float[] stepOrientations = new float[stepNum];  //PDR鏂瑰悜閲忔祴鍊�
        for (int i = 0; i < stepNum; i ++){
            stepLengths[i] = stepLength + stepLengthBias + Fnoise * (float) random.nextGaussian();
            stepOrientations[i] = stepOrientation + stepOrientationBias + Tnoise * (float) random.nextGaussian();
        }

        //鍒濆鍖栫湡瀹炶繍鍔ㄨ建杩瑰拰閲忔祴杩愬姩杞ㄨ抗
        Point startPosition = new Point(0,2);   //琛屼汉鍒濆浣嶇�?
        Point tmpPosition = startPosition;
        Point[] trueTrack = new Point[positionNum];
        Point[] measurementTrack = new Point[positionNum];
        for (int i = 0; i < positionNum; i ++){
            //鐪熷疄杞ㄨ抗璧嬪�硷紝娌縳杞存鏂瑰悜杩愬姩
            trueTrack[i] = new Point(tmpPosition.x,tmpPosition.y);
            //閲忔祴杞ㄨ抗璧嬪��
            measurementTrack[i] = new Point(tmpPosition.x + Snoise * (float) random.nextGaussian(),
                    tmpPosition.y + Snoise * (float) random.nextGaussian());
            tmpPosition.x += stepLength;
        }

        //绮掑瓙缇ゅ垵濮嬪寲锛屽苟璁剧疆鐜鍣�?
        ParticleFilter filter = new ParticleFilter(particleNum,measurementTrack[0]);
        filter.setNoise(Fnoise,Tnoise,Snoise);
        afterPFTrack[0].x = filter.getAverageParticle().x;
        afterPFTrack[0].y = filter.getAverageParticle().y;
        //寮�濮嬭繍鍔�?
        for (int i = 1; i < positionNum; i ++){
            filter.move(stepOrientations[i-1],stepLengths[i-1]);    //绮掑瓙杩涜绉诲�?
            filter.resample(measurementTrack[i]);
            afterPFTrack[i].x = filter.getAverageParticle().x;
            afterPFTrack[i].y = filter.getAverageParticle().y;
        }
        System.out.println("haha");
    }
}
