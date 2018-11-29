package imageProcessing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;



public class ConvexPolygon extends Polygon {

	static final int maxNumPoints=210;
	static Random gen = new Random();
	NumberFormat nf = new DecimalFormat("##.00");
	static int max_X;
	static int max_Y;
	List<Point> points = new ArrayList<Point>();


	// randomly generates a polygon
	public ConvexPolygon(int numPoints){
		super();
		genRandomConvexPolygone(numPoints);
		//System.out.println(points.size());
		int r = gen.nextInt(256);
		int g = gen.nextInt(256);
		int b = gen.nextInt(256);
		this.setFill(Color.rgb(r, g, b));
		this.setOpacity(gen.nextDouble());
	}

	public ConvexPolygon(){
		super();
	}


	public List<Point> pointsList() {
		return points;
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

	public void mutate(int rate) {
		Random rn = new Random();
		int index=0;
		int choix;

		
			//System.out.println("INNNNNSSSSIIIIIDDDDEEEEE----------------------------------------------");
			//parcourir les points et aléatoirement changer x et/ou y ou non
			//for(Point p : points) {

			choix=rn.nextInt(3);
			int i = rn.nextInt(points.size());
			Point p;
			switch (choix) {
			case 0 :
				//points.add(index,new Point(p.getX(), rn.nextInt(MAXY)));

				p = new Point(points.get(i).getX(), this.mutationPositionY(points.get(i).getY()));

				//					System.out.println("Cas 1");
				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				//					System.out.println(p.getX()+"	"+p.getY());

				points.set(i, p);

				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			case 1 :
				//points.add(index, new Point(rn.nextInt(MAXX), p.getY()));

				p = new Point(this.mutationPositionX(points.get(i).getX()), points.get(i).getY());

				//					System.out.println("Cas 2");
				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				//					System.out.println(p.getX()+"	"+p.getY());

				points.set(i, p);

				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			case 2 :
				//points.add(index, new Point(rn.nextInt(MAXX), rn.nextInt(MAXY)));

				p = new Point(this.mutationPositionX(points.get(i).getX()), this.mutationPositionY(points.get(i).getY()));

				//					System.out.println("Cas 3");
				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				//					System.out.println(p.getX()+"	"+p.getY());

				points.set(i, p);

				//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			}

			//aléatoirement : prendre changer une nuance de couleur
			Color clr = (Color) this.getFill();
			double red = clr.getRed();
			double blue = clr.getBlue();
			double green = clr.getGreen();



			choix = rn.nextInt(3);
			switch(choix) {
			case 0 :
				//changer red
				//				System.out.println("red");
				this.setFill(Color.color(this.mutationColor(red),green, blue));
				break;
			case 1 :
				//changer blue
				//				System.out.println("blue");
				this.setFill(Color.color(red,green, this.mutationColor(blue))) ;
				break;
			case 2 :
				//changer green
				//				System.out.println("green");
				this.setFill(Color.color(red,this.mutationColor(green), blue)) ;
				break;
			}
			
			if(rn.nextBoolean()) {
				this.setOpacity(this.mutateOpacity(this.getOpacity()));
			}
		
	}

	/*
	 * 
	 */
	private double mutateOpacity(double opacity) {
		double ret;
		Random rn = new Random();
		double var = rn.nextInt(11);
		var = var / 100;

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
		return ret;
	}
	
	/*
	 * 
	 * 
	 */
	private double mutationPositionX(double position) {
		double ret;
		Random rn = new Random();
		int var = rn.nextInt(12);

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
	private double mutationPositionY(double position) {
		double ret;
		Random rn = new Random();
		int var = rn.nextInt(12);

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
	private double mutationColor(double couleur) {
		double ret;
		Random rn = new Random();
		double var = rn.nextInt(12);
		var = var / 100;
		if(rn.nextBoolean()) {
			if((couleur+var) > 1.0) {
				ret = 1.0;
			}
			else {
				ret = couleur+var;
			}
		}
		else {
			if((couleur-var) < 0.0) {
				ret = 0.0;
			}
			else {
				ret = couleur-var;
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
	public void mutateA(int pourcentage) {
		Random rn = new Random();
		int index=0;
		int choix;

		int pourc = rn.nextInt(101);
		if(pourc < pourcentage) {
			//parcourir les points et aléatoirement changer x et/ou y ou non
			//for(Point p : points) {
			for(int i=0; i < points.size(); i++) {
				choix=rn.nextInt(4);
				Point p;
				switch (choix) {
				case 0 :
					//points.add(index,new Point(p.getX(), rn.nextInt(MAXY)));

					p = new Point(points.get(i).getX(), rn.nextInt(max_Y));

					//					System.out.println("Cas 1");
					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					//					System.out.println(p.getX()+"	"+p.getY());

					points.set(i, p);

					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					break;
				case 1 :
					//points.add(index, new Point(rn.nextInt(MAXX), p.getY()));

					p = new Point(rn.nextInt(max_X), points.get(i).getY());

					//					System.out.println("Cas 2");
					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					//					System.out.println(p.getX()+"	"+p.getY());

					points.set(i, p);

					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					break;
				case 2 :
					//points.add(index, new Point(rn.nextInt(MAXX), rn.nextInt(MAXY)));

					p = new Point(rn.nextInt(max_X), rn.nextInt(max_Y));

					//					System.out.println("Cas 3");
					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					//					System.out.println(p.getX()+"	"+p.getY());

					points.set(i, p);

					//					System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
					break;
				case 3 :
					//					System.out.println("Cas break");
					break;
				}
				index++;
			}

			//aléatoirement : prendre changer une nuance de couleur
			Color clr = (Color) this.getFill();
			double red = clr.getRed();
			double blue = clr.getBlue();
			double green = clr.getGreen();

			double mutation = rn.nextInt(21);
			mutation = mutation / 100;

			choix = rn.nextInt(8);
			switch(choix) {
			case 0 :
				//changer red
				//				System.out.println("red");
				this.setFill(Color.color(this.mutationColor(red),green, blue));
				break;
			case 1 :
				//changer blue
				//				System.out.println("blue");
				this.setFill(Color.color(red,green, this.mutationColor(blue))) ;
				break;
			case 2 :
				//changer green
				//				System.out.println("green");
				this.setFill(Color.color(red,this.mutationColor(green), blue)) ;
				break;
			case 3 :
				//				System.out.println("red, green");
				this.setFill(Color.color(this.mutationColor(red),this.mutationColor(green), blue)) ;
				break;

			case 4 :
				//				System.out.println("red, blue");
				this.setFill(Color.color(this.mutationColor(red), green, this.mutationColor(blue))) ;
				break;

			case 5 :
				//				System.out.println("green, blue");
				this.setFill(Color.color(red, this.mutationColor(green), this.mutationColor(blue))) ;
				break;

			case 6 :
				//				System.out.println("3 couleurs");
				this.setFill(Color.color(this.mutationColor(red), this.mutationColor(green), this.mutationColor(blue))) ;
				break;
			case 7 :
				//				System.out.println("BREAK");
				break;
			}
		}
	}
	

}
