package com.ivan.particleFilter;

import java.util.Random;

public class ParticleFilter {

    Particle[] particles;   //绮掑瓙缇�?
    int numParticles = 0;   //绮掑瓙鏁伴噺
    Random gen = new Random();

    public ParticleFilter(int numParticles, Point position) {
        this.numParticles = numParticles;
        particles = new Particle[numParticles];
        for (int i = 0; i < numParticles; i ++){
            particles[i] = new Particle(position);
        }
    }

    /**
     *
     * @param Fnoise锛氬墠杩涜宸�
     * @param Tnoise锛氭柟鍚戣宸�
     * @param Snoise锛氫紶鎰熷櫒璇�?
     */
    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        for (int i = 0; i < numParticles; i++) {
            particles[i].setNoise(Fnoise, Tnoise, Snoise);
        }
    }

    /**
     * 绮掑瓙缇よ繘琛岃繍鍔�?
     * @param turn锛氭柟鍚�?
     * @param forward锛氬墠杩涜窛绂�
     * @throws Exception
     */
    public void move(float turn, float forward) {
        for (int i = 0; i < numParticles; i++) {
            particles[i].move(turn, forward);
        }
    }

    /**
     *resample
     * @param measurement
     * @throws Exception
     */
    public void resample(Point measurement) {
        Particle[] new_particles = new Particle[numParticles];
        double wsum = 0;    //绮掑瓙鏉冨�肩殑鍜屻��
        for (int i = 0; i < numParticles; i++) {    //璁＄畻鍚勪釜绮掑瓙鐨勬潈閲�
            particles[i].measurementProb(measurement);	//锟斤拷锟斤拷每锟斤拷particle锟斤拷probability锟斤拷锟斤拷锟斤拷锟斤拷值锟斤拷probability锟斤拷锟斤拷锟剿变化锟斤�?
            wsum += particles[i].probability;
        }
        //绮掑瓙鏉冨�煎綊涓�鍖�
        for (int i = 0; i < numParticles; i ++) {
            particles[i].probability = particles[i].probability/wsum;
        }
        float B = 0f;
        Particle best = getBestParticle();	//锟斤拷取probability锟斤拷锟斤拷锟斤拷锟接ｏ拷锟斤拷锟斤拷为best锟斤拷锛岃幏鍙栨潈閲嶆渶澶х殑绮掑�?
        int index = (int) gen.nextFloat() * numParticles;	//锟斤拷锟斤拷锟�0~numParticles锟斤拷取�?锟斤拷index锟斤�?
        //锟斤拷锟截诧拷锟斤拷锟斤拷锟斤拷锟斤拷matlab锟斤拷PF2锟叫诧拷锟矫的凤拷锟斤拷锟斤拷同锟斤�?
        for (int i = 0; i < numParticles; i++) {
            B += gen.nextFloat() * 2f * best.probability;
            while (B > particles[index].probability) {
                B -= particles[index].probability;
                index = circle(index + 1, numParticles);
            }
            new_particles[i] = new Particle();
            new_particles[i].set(particles[index].x, particles[index].y, particles[index].orientation, particles[index].probability);
            new_particles[i].setNoise(particles[index].forwardNoise, particles[index].turnNoise, particles[index].senseNoise);
        }

        particles = new_particles;
    }

    private int circle(int num, int length) {
        while (num > length - 1) {
            num -= length;
        }
        while (num < 0) {
            num += length;
        }
        return num;
    }
    
    public Particle getBestParticle() {
        Particle particle = particles[0];
        for (int i = 0; i < numParticles; i++) {
            if (particles[i].probability > particle.probability) {
                particle = particles[i];
            }
        }
        return particle;
    }
    
    public Particle getAverageParticle() {
        Particle p = new Particle(new Point(0,0));
        float x = 0, y = 0, orient = 0, prob = 0;
        for(int i=0;i<numParticles;i++) {
            x += particles[i].x;
            y += particles[i].y;
            orient += particles[i].orientation;
            prob += particles[i].probability;
        }
        x /= numParticles;
        y /= numParticles;
        orient /= numParticles;
        prob /= numParticles;
        p.set(x, y, orient, prob);
        p.setNoise(particles[0].forwardNoise, particles[0].turnNoise, particles[0].senseNoise);
        return p;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < numParticles; i++) {
            res += particles[i].toString() + "\n";
        }
        return res;
    }
   
}