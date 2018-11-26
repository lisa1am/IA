package imageProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Individual implements Comparable{
	
	private ConvexPolygon polygon;
	private double fitness;
	private List<Point> points;
	private final static int MAXX=100;
	private final static int MAXY=149;
	
	
	public Individual(int nbPoints) {
		polygon= new ConvexPolygon(nbPoints);
		points = new ArrayList<Point>();
		double x,y;
		for(int i=0; i<polygon.getPoints().size(); i++) {
			x = polygon.getPoints().get(i);
			y = polygon.getPoints().get(i);
			points.add(new Point(x,y));
			i++;
		}
	}
	
	public List<Point> points(){
		return points;
	}
	
	public double fitnessScore(Color[][] target) {
	    Group image = new Group();
	    image.getChildren().add(polygon);
	    WritableImage wimg = new WritableImage(MAXX,MAXY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		
		for (int i=0;i<MAXX;i++){
			for (int j=0;j<MAXY;j++){
				if(this.polygon.contains(i, j)) {
					Color c = pr.getColor(i, j);
					fitness += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
					+Math.pow(c.getRed()-target[i][j].getRed(),2)
					+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
				}
			}
		}
		return fitness;
	}



		
	public ConvexPolygon getPolygon() {
		return polygon;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void setColor(Color c) {
		polygon.setFill(c) ;
	}
	
	public Color getColor() {
		return (Color)polygon.getFill();
	}
	
	public Color avgRandomColor(Color c1, Color c2) {
		Random rn = new Random();
		int coef = rn.nextInt(100);
		int r1 = (int) c1.getRed();
		int r2 = (int) c2.getRed();
		int b1 = (int) c1.getBlue();
		int b2 = (int) c2.getBlue();
		int g1 = (int) c1.getGreen();
		int g2 = (int) c2.getGreen();
		Color c = Color.rgb((r1*coef+r2*(100-coef)),(g1*coef+g2*(100-coef)),(b1*coef+b2*(100-coef)));
		return c;
	}
	
	public void mutate() {
		Random rn = new Random();
		int index=0;
		int choix;
		
		//parcourir les points et aléatoirement changer x et/ou y ou non
		for(Point p : points) {
			choix=rn.nextInt(4);
			switch (choix) {
			case 0 :
				points.add(index,new Point(p.getX(), rn.nextInt(MAXY)));
				break;
			case 1 :
				points.add(index, new Point(rn.nextInt(MAXX), p.getY()));
				break;
			case 2 :
				points.add(index, new Point(rn.nextInt(MAXX), rn.nextInt(MAXY)));
				break;
			case 3 :
				break;
			}
			index++;
		}
		//aléatoirement : prendre changer une nuance de couleur
		Color clr = (Color) polygon.getFill();
		int red = (int)clr.getRed();
		int blue = (int)clr.getBlue();
		int green = (int)clr.getGreen();
		choix = rn.nextInt(4);
		switch(choix) {
		case 0 :
			//changer red
			this.setColor(Color.rgb(red+rn.nextInt(50),green, blue));
			break;
		case 1 :
			//changer blue
			polygon.setFill(Color.rgb(red,green, blue+rn.nextInt(50))) ;
			break;
		case 2 :
			//changer green
			polygon.setFill(Color.rgb(red,green+rn.nextInt(50), blue)) ;
			break;
		case 3 :
			break;
		}
	}
	
	public Individual crossover(Individual ind) {
		Random rn = new Random();
		Individual child;
		int min=ind.points.size();
		int max=this.points.size();
		int temp;
		//garder le nombre de sommets de chaque parent
		if(this.points.size()<ind.points.size()) {
			temp=min;
			min=max;
			max=temp;
		}
		
		//NOMBRE DE SOMMETS
		if(rn.nextBoolean()) {
			//MÉLANGE DES PARENTS
				
			//aléatoirement : générer un enfant avec un nombre de sommets compris entre
			//le nombre de sommet de chacun des parents
			child= new Individual(((int)Math.random() * ((max - min) + 1)) + min);
		}else {
			//UN SEUL PARENT À LA FOIS
			if(rn.nextBoolean()) {
				//PARENT 1
				child = new Individual(min);
			}else {
				//PARENT 2
				child = new Individual(max);
			}	
		}
	
		//COULEUR
		if(rn.nextBoolean()) {
			//MÉLANGE DES PARENTS
				
			//couleur : moyenne des couleur des deux parents avec des coeff random
			child.setColor(avgRandomColor(this.getColor(), ind.getColor()));
		}else {
			//UN SEUL PARENT À LA FOIS
			if(rn.nextBoolean()) {
				//PARENT 1
				child.setColor(this.getColor());
				
			}else {
				//PARENT 2
				child.setColor(ind.getColor());
			}
				
		}
		return child;
		
	}



	@Override
	public int compareTo(Object o) {
		Individual poly = (Individual) o;
		if(this.getFitness()<poly.getFitness()) {
			return -1;
		}else {
			if(this.getFitness()>poly.getFitness()) {
				return 1;
			}else {
				return 0;
			}
		}
	}

	
}
