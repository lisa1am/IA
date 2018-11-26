package imageProcessing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;



public class Point {

	
	private double x,y;
	
	private static Random gen = new Random();
	private static int max_X, max_Y;

	public void setMax(int maxX, int maxY) {
		max_X=maxX;
		max_Y=maxY;
	}
	
	// generate a random point
	public Point(){
		x= gen.nextInt(max_X);
		y= gen.nextInt(max_Y);
	}
	
	public Point(double x, double y){
		this.x=x;
		this.y=y;
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public void translate(int vx,int vy){
		x += vx;
		y += vy;
	}
	
	public boolean equals(Object o){
		if (o==null)
			return false;
		else if (o == this)
			return true;
		else if (o instanceof Point)
			return ((Point) o).x== this.x && ((Point) o).y== this.y;
		else
			return false;
	}
	
	public String toString(){
		NumberFormat nf = new DecimalFormat("#.00");
		return "(" + x + "," + y+")"; // + nf.format(Math.atan2(y, x))+")";
	}
}