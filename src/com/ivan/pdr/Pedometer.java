package com.ivan.pdr;

import android.util.Log;
/*
 * 计步类，计步并估计步长
 */
public class Pedometer {

    private static final String TAG = "SimpleStepDetector";

    private int nowStatus = -1;
    private int formerStatus = -1;
    private int footNumber = 0;
    private long timeOfNow = 0;
    private long timeOfFormer = 0;
    private long intervalOfTime = 0;


    private float thresholdHigh = 0.5f;
    private float thresholdLow = -0.5f;
    private float maxAcc = 0;
    private float minAcc = 0;
    private float distance1 = 0;


    public void DetectorNewStep(float values) {
        if (values > thresholdHigh) {
            nowStatus = 2;
            if (values >= maxAcc) {
                maxAcc = values;
            }
        } else if (values < thresholdLow) {
            nowStatus = 0;
            if (values <= minAcc) {
                minAcc = values;
            }
        } else if (values <= thresholdHigh && values >= thresholdLow) {	//改9.75为-0.5
            nowStatus = 1;
        }
        if ((nowStatus == 1) && (formerStatus == 0)) {
            timeOfNow = System.currentTimeMillis();
            intervalOfTime = timeOfNow - timeOfFormer;
            if ((intervalOfTime >= 200) ) {	//时间域条件判断 && (intervalOfTime <= 2000);
                footNumber ++;				//删除最大时长限制，最大时长限制存在时会影响，第一步的判别。
                Log.d(TAG, "maxAcc: " + maxAcc);
                Log.d(TAG, "minAcc: " + minAcc);
                distance1 = stepLengthEstimate(maxAcc, minAcc) + distance1;
                {minAcc = 0;
                    maxAcc = 0;}
            }
            timeOfFormer = timeOfNow;
        }
        formerStatus = nowStatus;
    }

    public float stepLengthEstimate(float maxAcc, float minAcc) {
        float steplength = (float) (1.31 * Math.pow((maxAcc - minAcc), 0.25)
                - 0.961);
        return steplength;
    }

    public int getFootNumber() {
        return footNumber;
    }

    public float getDistance1() {
        return distance1;
    }
}
