package com.ivan.particleFilter;


import java.util.Random;

/**
 *
 * @author Michael
 */
public class Particle {

    public float forwardNoise, turnNoise, senseNoise;
    public float x, y, orientation;
    public double probability = 1;
    Random random;

    /**
     *
     * @param position锛氫綅缃俊鎭�
     */
    public Particle(Point position){
        random = new Random();
        x = position.x + 0.1f * (float)random.nextGaussian();  //鏍规嵁楂樻柉鍒嗗竷锛�?敓鎴愮矑瀛�,0.1f寰呮枱閰�?
        y = position.y + 0.1f * (float)random.nextGaussian();
        orientation = 0f;   //鏂瑰�???
        forwardNoise = 0f;
        turnNoise = 0f;
        senseNoise = 0f;
    }

    public Particle(){
        random = new Random();
    }

    public void set(float x, float y, float orientation, double prob) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.probability = prob;
    }
    /**
     * Sets the noise of the particles measurements and movements
     * 
     * @param Fnoise noise of particle in forward movement
     * @param Tnoise noise of particle in turning
     * @param Snoise noise of particle in sensing position
     */
    public void setNoise(float Fnoise, float Tnoise, float Snoise) {
        this.forwardNoise = Fnoise;
        this.turnNoise = Tnoise;
        this.senseNoise = Snoise;
    }
    
    /**
     * Moves the particle's position
     * 
     * @param turn turn value, in degrees
     * @param forward move value, must be >= 0
     */
    public void move(float turn, float forward) {
        orientation = turn + (float)random.nextGaussian() * turnNoise;	//锟斤拷锟诫方锟斤拷牟锟斤拷锟�?/状�?�锟斤拷锟斤拷锟斤拷
        double dist = forward + random.nextGaussian() * forwardNoise;	//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤�?
        x += Math.cos(orientation) * dist;  //x杞寸殑鍧愭爣
        y += Math.sin(orientation) * dist;  //y杞寸殑鍧愭爣
    }

    public double measurementProb(Point measurement){
        double prob = 1.0;
        float dist = (float) MathX.distance(x,y,measurement.x,measurement.y);
        prob = MathX.Gaussian(dist,senseNoise,0);
        probability = prob;
        return prob;
    }
    
    @Override
    public String toString() {
        return "[x=" + x + " y=" + y + " orient=" + Math.toDegrees(orientation) + " prob=" +probability +  "]";
    }
    
}