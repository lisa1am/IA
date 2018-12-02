package imageProcessing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;



public class Point {

	
	private int x,y;
	
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
	
	public Point(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public int getX(){return x;}
	
	public int getY(){return y;}
	
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
	
	public double crossProduct(Point p) {
		return ((this.x * p.y) - ( this.y * p.x));
	}
	
	public String toString(){
		NumberFormat nf = new DecimalFormat("#.00");
		return "(" + x + "," + y+")"; // + nf.format(Math.atan2(y, x))+")";
	}
}