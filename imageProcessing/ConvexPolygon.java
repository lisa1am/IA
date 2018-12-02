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
	int maxX, maxY, minX, minY;
	List<Point> points = new ArrayList<Point>();


	//generates a random polygon
	public ConvexPolygon(int numPoints){
		super();
		genRandomConvexPolygone(numPoints);
		//System.out.println(points.size());
		int r = gen.nextInt(256);
		int g = gen.nextInt(256);
		int b = gen.nextInt(256);
		this.setFill(Color.rgb(r, g, b));
		this.setOpacity(gen.nextDouble());
		this.updateMinMax();
	}

	public ConvexPolygon(){
		super();
	}

	public void updateMinMax() {
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<Integer> Y = new ArrayList<Integer>();
		
		for(int i=0; i<points.size(); i++) {
			X.add(i,points.get(i).getX());
			Y.add(i,points.get(i).getY());
		}
		
		minX= Math.max(Collections.min(X),0);;
		minY= Math.max(Collections.min(Y),0);
		maxX= Math.min(Collections.max(X),max_X);
		maxY= Math.min(Collections.max(Y), max_Y);
		
	}

	public List<Point> pointsList() {
		return points;
	}
	
	public double checkfitness(Color[][] target) {
		double fitness=0;
		
		Group image = new Group();
		image.getChildren().add(this);
		
		WritableImage wimg = new WritableImage(max_X,max_Y);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		//System.out.println("minx= "+minX+" maxx= "+maxX+" miny= "+minY+" maxy= "+maxY);
		System.out.println("checking local fitness");
		for(int i=0; i<max_X; i++) {
			for(int j=0; j<max_Y; j++) {
				Color c = pr.getColor(i, j);
				//System.out.println("x="+i+" y="+j);
				//System.out.println("TARGET BLUE "+target[i][j].getBlue());
				if(this.contains(i, j)){
					fitness += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
							+Math.pow(c.getRed()-target[i][j].getRed(),2)
							+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
				}
				
			}
		}
		this.fitness=Math.sqrt(fitness)/this.area();
		return(Math.sqrt(fitness)/this.area());
	}

	public double getFitness() {
		return fitness;
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

	/*public void mutate(int rate) {
		Random rn = new Random();
		int randomRate = rn.nextInt(101); 
		int index=0;
		int choix = rn.nextInt(7);
		
		
		if(true) {
			switch(choix) {
			case 0 : 
				this.mutationPoint(6);
				//System.out.println("MUTATE POINT");
				break;
			case 1 :
				this.setFill(this.getColor().brighter());
				break;
			case 2 :
				this.setFill(this.getColor().darker());
				break;
			case 3 :
				this.mutateColor(5);
				//System.out.println("MUTATE COLOR");
				break;
			case 4:
				this.mutateOpacity(5);
				//System.out.println("MUTATE OPACITY");
				break;
			case 5:
				this.mutateInverse();
				//System.out.println("MUTATE INVRSE");
				break;
			case 6:
				this.mutateTranslate(rn.nextInt(max_X), rn.nextInt(max_Y));
				//System.out.println("MUTATE TRANSLATE");
				break;
			case 7:
				this.mutateChangePolygon();
				//System.out.println("MUTATE CHANGE POLYGON");
				break;
			
			}

		}

	}*/
	
	 public double area() {
	        double sum = 0.0;
	        for (int i = 0; i < points.size(); i++) {
	            sum = sum + points.get(i).crossProduct(points.get((i+1)%points.size()));
	        }
	        return Math.abs(sum)/2;
	    }
	
	public void mutate() {
		
		//DARKER
		//this.setFill(this.getColor().darker());
		
		//CHANGE COLOR
		//this.mutateColor();
		
		//TRANSLATE
		this.mutateTranslate(gen.nextInt(max_X), gen.nextInt(max_Y));
		
		
	}
	
	private void mutateTranslate(int distx, int disty) {
		for(int i=0; i<points.size(); i++) {
				points.get(i).translate(distx,  disty);
				System.out.println("translating .. ");
				//this.updateMinMax();
		}
	}
	
	private void mutateInverse() {
		Point tmp = points.get(0);
		for(int i=1; i<points.size(); i++) {
			points.set(i-1, points.get(i));
		}
		points.set(points.size()-1, tmp);
	}
	
	private void mutateChangePolygon() {
		this.mutateColor();
		this.mutationPoint(30);
		this.mutateOpacity(5);
		
	}

	private void mutateColor() {
		//aléatoirement : prendre changer une nuance de couleur
		Color clr = (Color) this.getColor();
		double red = clr.getRed();
		double blue = clr.getBlue();
		double green = clr.getGreen();


		int choix;
		double mut;
		Random rn = new Random();
		choix = rn.nextInt(5);
		mut = rn.nextInt(100)/100;
		switch(choix) {
		case 0 :
			//changer red
			double red_new = this.mutationColor(red, mut);
			this.setFill(Color.color(red_new,green, blue));
//			System.out.println("0 ---- COLOR red de "+red+" à "+red_new);


			break;
		case 1 :
			//changer blue
			double blue_new = this.mutationColor(blue, mut);
			this.setFill(Color.color(red,green, blue_new)) ;
//			System.out.println("0 ---- COLOR blue de "+blue+" à "+blue_new);
			break;
		case 2 :
			//changer green
			double green_new = this.mutationColor(green, mut);
			this.setFill(Color.color(red,green_new, blue));
//			System.out.println("0 ---- COLOR green de "+green+" à "+green_new);
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
		int choix;
		Random rn = new Random();
		choix=rn.nextInt(3);
		int i = rn.nextInt(points.size());
		Point p;
		switch (choix) {
		case 0 :			
			p = new Point(points.get(i).getX(), this.mutationPositionY(points.get(i).getY(), max_Y));

//			System.out.println("0 ---- POINT Y de "+points.get(i).getY()+" à "+p.getY());
			points.set(i, p);

			break;
		case 1 :
			p = new Point(this.mutationPositionX(points.get(i).getX(), max_X), points.get(i).getY());

//			System.out.println("1 ---- POINT X de "+points.get(i).getX()+" à "+p.getX());
			points.set(i, p);

			break;
		case 2 :

			p = new Point(this.mutationPositionX(points.get(i).getX(), max), this.mutationPositionY(points.get(i).getY(), max));

//			System.out.println("2 ---- POINT XY de "+points.get(i).getX()+" et "+points.get(i).getY()+" à "+p.getX()+" et "+p.getY());

			points.set(i, p);

			break;
		}
	}
	private void mutateOpacity(int max) {
		this.setOpacity(this.mutationOpacity(max));
	}
	/*
	 * 
	 */
	private double mutationOpacity(int max) {
		double ret;
		Random rn = new Random();
		double var = rn.nextInt();
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
			if((opacity-var) < 0.0) {
				ret = 0.0;
			}
			else {
				ret = opacity-var;
			}
		}
//		System.out.println("0 ---- OPACITY de "+opacity+" à "+ret);
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
	private int mutationPositionY(int position, int max) {
		int ret;
		Random rn = new Random();
		int var = rn.nextInt();

		if(rn.nextBoolean()) {
			if((position+var) > max_Y) {
				ret = max_Y;
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


	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * NO UTLITY
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public ConvexPolygon crossover(ConvexPolygon cp) {
		Random rn = new Random();
		ConvexPolygon child;
		int min=cp.points.size();
		int max=this.points.size();
		int temp;
		//garder le nombre de sommets de chaque parent
		if(this.points.size()<cp.points.size()) {
			temp=min;
			min=max;
			max=temp;
		}

		//NOMBRE DE SOMMETS
		if(rn.nextBoolean()) {
			//MÉLANGE DES PARENTS

			//aléatoirement : générer un enfant avec un nombre de sommets compris entre
			//le nombre de sommet de chacun des parents
			child= new ConvexPolygon(((int)Math.random() * ((max - min) + 1)) + min);
		}else {
			//UN SEUL PARENT À LA FOIS
			if(rn.nextBoolean()) {
				//PARENT 1
				child = new ConvexPolygon(min);
			}else {
				//PARENT 2
				child = new ConvexPolygon(max);
			}	
		}

		//COULEUR
		if(rn.nextBoolean()) {
			//MÉLANGE DES PARENTS

			//couleur : moyenne des couleur des deux parents avec des coeff random
			child.setFill(avgRandomColor(this.getColor(), cp.getColor()));
		}else {
			//UN SEUL PARENT À LA FOIS
			if(rn.nextBoolean()) {
				//PARENT 1
				child.setFill(this.getColor());

			}else {
				//PARENT 2
				child.setFill(cp.getColor());
			}

		}
		return child;

	}

	public Color avgRandomColor(Color c1, Color c2) {
		Random rn = new Random();
		// je divise par 100 sinon t'as un coef qui marche pas, et 101 car exclusif et pas inclusif
		double coef = rn.nextInt(101);
		coef = coef/100;

		double r1 =  c1.getRed();
		double r2 =  c2.getRed();
		double b1 =  c1.getBlue();
		double b2 =  c2.getBlue();
		double g1 =  c1.getGreen();
		double g2 =  c2.getGreen();


		//Clareté du code
		double r = (r1*coef)+(r2*(1-coef));
		double g = (g1*coef)+(g2*(1-coef));
		double b = (b1*coef)+(b2*(1-coef));

		Color c = Color.color(r, g, b);
		return c;
	}

	public double avgRandomOpacity(double o1, double o2) {
		Random rn = new Random();
		double coef = rn.nextInt(101); 
		coef = coef /100;
		double ret = o1*coef + o2*(1-coef);
		return ret;
	}


}
