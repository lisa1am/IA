package imageProcessing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;



public class ConvexPolygon extends Polygon {

	static final int maxNumPoints=210;
	static Random gen = new Random();
	NumberFormat nf = new DecimalFormat("##.00");
	static int max_X;
	static int max_Y;
	double fitness;
	int area= 0;
	int maxX, maxY, minX, minY;
	List<Point> points = new ArrayList<Point>();


	//generates a random polygon
	public ConvexPolygon(int numPoints){
		super();
		genRandomTriangle();
		//genRandomConvexPolygone(numPoints);
		//System.out.println(points.size());
		int r = gen.nextInt(256);
		int g = gen.nextInt(256);
		int b = gen.nextInt(256);
		this.setFill(Color.rgb(r, g, b));
		this.setOpacity(gen.nextDouble());
	}
	
	
	public ConvexPolygon(Color c, List<Point> p) {
		ConvexPolygon ret= new ConvexPolygon(3);
		ret.setFill(Color.color(c.getRed(),c.getGreen(), c.getBlue()));
		ret.setOpacity(c.getOpacity());
		//ret.points=p;
		for(Point point : p) {
			ret.points.add(point);
		}
	}
	
	public ConvexPolygon (ConvexPolygon poly) {
		this.setFill(poly.getColor());
		this.setOpacity(poly.getOpacity());
		for(Double point : poly.getPoints()) {
			this.getPoints().add(point);
		}
		
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	
	public ConvexPolygon(){
		super();
	}


	public List<Point> pointsList() {
		return points;
	}
	
	public double checkfitness(Color[][] target) {
		double fit=0;
		area=0;
		
		Group image = new Group();
		image.getChildren().add(this);
		
		WritableImage wimg = new WritableImage(max_X,max_Y);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		for (int i=0; i<max_X; i++){
			for (int j=0; j<max_Y; j++){
				
				if(this.contains(i, j)){
					Color c = pr.getColor(i, j);
					area++;
					fit += Math.abs(c.getBlue()-target[i][j].getBlue())
							+Math.abs(c.getRed()-target[i][j].getRed())
							+Math.abs(c.getGreen()-target[i][j].getGreen());
					
				}
				
			}
		}
		this.fitness = fit/(3*area);
		this.fitness= 1 - this.fitness;
		this.fitness = this.fitness*100;
		return(this.fitness);
	}


	public double getFitness() {
		return fitness;
	}
	
	public int getArea() {
		return area;
	}

	public String toString(){
		String res = super.toString();
		res += " " + this.getFill() + " opacity " + this.getOpacity();
		return res;
	}

	public void addPoint(double x, double y) {
		getPoints().add(x);
		getPoints().add(y);
	}
	
	
	public void genRandomTriangle() {
		for(int i=0; i<3; i++) {
			addPoint((double)gen.nextInt(max_X), (double)gen.nextInt(max_Y));
		}
		
	}

	// http://cglab.ca/~sander/misc/ConvexGeneration/convex.html
	public void genRandomConvexPolygone(int n){
		List<Point> pointslist = new LinkedList<Point>();
		List<Integer> abs = new ArrayList<>();
		List<Integer> ord = new ArrayList<>();

		for (int i=0;i<n;i++){
			abs.add(gen.nextInt(max_X));
			ord.add(gen.nextInt(max_Y));
		}
		Collections.sort(abs);
		Collections.sort(ord);
		int minX = abs.get(0);
		int maxX = abs.get(n-1);
		int minY = ord.get(0);
		int maxY = ord.get(n-1);

		List<Integer> xVec = new ArrayList<>();
		List<Integer> yVec = new ArrayList<>();

		int top= minX, bot = minX;
		for (int i=1;i<n-1;i++){
			int x = abs.get(i);

			if (gen.nextBoolean()){
				xVec.add(x-top);
				top = x;
			} else{
				xVec.add(bot-x);
				bot = x;
			}
		}
		xVec.add(maxX-top);
		xVec.add(bot-maxX);

		int left= minY, right = minY;
		for (int i=1;i<n-1;i++){
			int y = ord.get(i);

			if (gen.nextBoolean()){
				yVec.add(y-left);
				left = y;
			} else{
				yVec.add(right-y);
				right = y;
			}
		}
		yVec.add(maxY-left);
		yVec.add(right-maxY);

		Collections.shuffle(yVec);

		List<Point> lpAux = new ArrayList<>();
		for (int i=0;i<n;i++)
			lpAux.add(new Point(xVec.get(i), yVec.get(i)));


		// sort in order by angle
		Collections.sort(lpAux, (x,y) ->  Math.atan2(x.getY(), x.getX())  < Math.atan2(y.getY(), y.getX()) ? -1 :
			Math.atan2(x.getY(), x.getX())  == Math.atan2(y.getY(), y.getX()) ? 0 : 1);

		int x=0,y=0;
		int minPolX=0, minPolY=0;

		for (int i=0;i<n;i++){
			pointslist.add(new Point(x,y));
			x += lpAux.get(i).getX();
			y += lpAux.get(i).getY(); 

			if (x < minPolX)
				minPolX=x;
			if (y<minPolY)
				minPolY=y;
		}

		int xshift = gen.nextInt(max_X - (maxX-minX)) ;
		int yshift = gen.nextInt(max_Y - (maxY-minY)) ;
		xshift -= minPolX;
		yshift -= minPolY;
		for (int i=0;i<n;i++){
			Point p = pointslist.get(i);
			p.translate(xshift,yshift);
		}
		for (Point p : pointslist) {
			addPoint(p.getX(), p.getY());
		}
		points=pointslist;			
	}



	/*
	 * 
	 * 
	 */
	
	
	public void mutate() {
	
		//OPACITY
		this.mutateOpacity();
		
		
		
		//CHANGE COLOR
		this.mutateColor();
		
		
		if(gen.nextBoolean()) {
			//TRANSLATE
			this.mutateInverse();
		}else{
			//POINT
			this.mutationPoint(gen.nextInt(20));
		}
		
			
		
	
	}
	
	
	private void mutateTranslate(int distx, int disty) {
		for(int i=0; i<getPoints().size()-1; i++) {
			if(gen.nextBoolean()) {
				getPoints().set(i, giveValuePlus(getPoints().get(i),distx, max_X));
				getPoints().set(i+1, giveValuePlus(getPoints().get(i+1),disty, max_Y));
			}else {
				getPoints().set(i, giveValueMinus(getPoints().get(i),distx));
				getPoints().set(i+1, giveValueMinus(getPoints().get(i+1),disty));
			}
		}
	}
	
	private void mutateInverse() {
		
		double x = getPoints().get(0);
		double y = getPoints().get(1);
		
		for(int i=2; i<getPoints().size()-1; i++) {
			getPoints().set(i-2, getPoints().get(i));
			getPoints().set(i-1, getPoints().get(i+1));
			i++;
		}
		
		getPoints().set(getPoints().size()-2, x);
		getPoints().set(getPoints().size()-1, y);
		
	}
	
	private void mutateChangePolygon() {
		this.mutateColor();
		this.mutationPoint(30);
		this.mutateOpacity();
		
	}

	private void mutateColor() {
		//aléatoirement : prendre changer une nuance de couleur
		Color clr =  this.getColor();
		double red = clr.getRed();
		double blue = clr.getBlue();
		double green = clr.getGreen();


		int choix;
		double mut;
		Random rn = new Random();
		choix = rn.nextInt(5);
		mut = rn.nextInt(30);
		mut=mut/100;
		
		switch(choix) {
		case 0 :
			//changer red
			double red_new = this.mutationColor(red, mut);
			this.setFill(Color.color(red_new,green, blue));
			break;
		case 1 :
			//changer blue
			double blue_new = this.mutationColor(blue, mut);
			this.setFill(Color.color(red,green, blue_new)) ;
			break;
		case 2 :
			//changer green
			double green_new = this.mutationColor(green, mut);
			this.setFill(Color.color(red,green_new, blue));
			break;
		case 3 :
			this.setFill(this.getColor().brighter());
			break;
			
		case 4 :
			this.setFill(this.getColor().darker());
			break;
		}
	}


	private void mutationPoint(int max) {
		Random rn = new Random();
		
		int i = rn.nextInt(getPoints().size()-1);
		int nbtours = rn.nextInt(3);
		for(int j=0; j<nbtours; j++) {
	
				if(rn.nextBoolean()) {
					if(i%2==0) {
						//X
						getPoints().set(i, giveValuePlus(getPoints().get(i),max,max_X));
						getPoints().set(i+1, giveValuePlus(getPoints().get(i+1),max,max_Y));
					}else {
						//Y
						getPoints().set(i-1, giveValuePlus(getPoints().get(i-1),max,max_X));
						getPoints().set(i, giveValuePlus(getPoints().get(i),max,max_Y));
					}
				}else {
					if(i%2==0) {
						//X
						getPoints().set(i, giveValueMinus(getPoints().get(i),max));
						getPoints().set(i+1, giveValueMinus(getPoints().get(i+1),max));
					}else {
						//Y
						getPoints().set(i-1, giveValueMinus(getPoints().get(i-1),max));
						getPoints().set(i, giveValueMinus(getPoints().get(i),max));
					}
				}
		}
		
	}
	
	private double giveValuePlus(double coord,int val, int MAX) {
		if((coord+val)>MAX) {
			return(MAX+10);
		}else {
			return(coord+val);
		}
	}
	
	private double giveValueMinus(double coord, int val) {
		if((coord-val)<0) {
			return(-10);
		}else {
			return(coord-val);
		}
	}
	
	
	private void mutateOpacity() {
		this.setOpacity(this.mutationOpacity());
	}
	/*
	 * 
	 */
	private double mutationOpacity() {
		double ret;
		Random rn = new Random();
		double var = rn.nextInt(100);
		var = var / 100;
		double opacity = this.getOpacity();
		if(rn.nextBoolean()) {
			if((opacity+var) > 1.0) {
				ret = 1.0;
			}
			else {
				ret = opacity+var;
			}
		}
		else {
			if((opacity-var) < 0.4) {
				ret = 0.4;
			}
			else {
				ret = opacity-var;
			}
		}
		return ret;
	}

	/*
	 * 
	 * 
	 */
	private int mutationPositionX(int position, int max) {
		int ret;
		Random rn = new Random();
		int var = rn.nextInt();

		if(rn.nextBoolean()) {
			if((position+var) > max_X) {
				ret = max_X;
			}
			else {
				ret = position+var;
			}
		}
		else {
			if((position-var) < 0) {
				ret = 0;
			}
			else {
				ret = position-var;
			}
		}
		return ret;
	}

	/*
	 * 
	 * 
	 */
	private double mutationColor(double couleur, double mut) {
		double ret;
		Random rn = new Random();
		if(rn.nextBoolean()) {
			if((couleur+mut) > 1.0) {
				ret = 1.0;
			}
			else {
				ret = couleur+mut;
			}
		}
		else {
			if((couleur-mut) < 0.0) {
				ret = 0.0;
			}
			else {
				ret = couleur-mut;
			}
		}
		return ret;
	}




	public Color getColor() {
		return (Color) this.getFill();
	}

}
