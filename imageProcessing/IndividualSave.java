package imageProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class IndividualSave extends ConvexPolygon implements Comparable{
	

	private double fitness;
	
	public IndividualSave(int nbPoints) {
		// J'ai rajouté ca sinon erreur de borne sur les tests
		super(nbPoints);
	}
	
	public List<Point> points(){
		return points;
	}
	
	public double fitnessScore(Color[][] target) {
	    Group image = new Group();
	    image.getChildren().add(this);
	    WritableImage wimg = new WritableImage(max_X,max_Y);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		
		for (int i=0;i<max_X;i++){
			for (int j=0;j<max_Y;j++){
				if(this.contains(i, j)) {
					Color c = pr.getColor(i, j);
					fitness += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
					+Math.pow(c.getRed()-target[i][j].getRed(),2)
					+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
				}
			}
		}
		return fitness;
	}



		
	public IndividualSave getIndividualSave() {
		return this;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void setColor(Color c) {
		this.setFill(c) ;
	}
	
	public Color getColor() {
		return (Color)this.getFill();
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
	
	public void mutate() {
		Random rn = new Random();
		int index=0;
		int choix;
		
		//parcourir les points et aléatoirement changer x et/ou y ou non
		//for(Point p : points) {
		for(int i=0; i < points.size(); i++) {
			choix=rn.nextInt(4);
			Point p;
			switch (choix) {
			case 0 :
				//points.add(index,new Point(p.getX(), rn.nextInt(MAXY)));
				
				p = new Point(points.get(i).getX(), rn.nextInt(max_Y));
				
//				System.out.println("Cas 1");
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
//				System.out.println(p.getX()+"	"+p.getY());
				
				points.set(i, p);
				
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			case 1 :
				//points.add(index, new Point(rn.nextInt(MAXX), p.getY()));
				
				p = new Point(rn.nextInt(max_X), points.get(i).getY());
			
//				System.out.println("Cas 2");
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
//				System.out.println(p.getX()+"	"+p.getY());
				
				points.set(i, p);
				
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			case 2 :
				//points.add(index, new Point(rn.nextInt(MAXX), rn.nextInt(MAXY)));
				
				p = new Point(rn.nextInt(max_X), rn.nextInt(max_Y));
				
//				System.out.println("Cas 3");
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
//				System.out.println(p.getX()+"	"+p.getY());
				
				points.set(i, p);
				
//				System.out.println(points.get(i).getX()+"	"+points.get(i).getY());
				break;
			case 3 :
//				System.out.println("Cas break");
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
//			System.out.println("red");
			this.setColor(Color.color(this.mutationColor(red, mutation),green, blue));
			break;
		case 1 :
			//changer blue
//			System.out.println("blue");
			this.setFill(Color.color(red,green, this.mutationColor(blue, mutation))) ;
			break;
		case 2 :
			//changer green
//			System.out.println("green");
			this.setFill(Color.color(red,this.mutationColor(green, mutation), blue)) ;
			break;
		case 3 :
//			System.out.println("red, green");
			this.setFill(Color.color(this.mutationColor(red, mutation),this.mutationColor(green, mutation), blue)) ;
			break;

		case 4 :
//			System.out.println("red, blue");
			this.setFill(Color.color(this.mutationColor(red, mutation), green, this.mutationColor(blue, mutation))) ;
			break;

		case 5 :
//			System.out.println("green, blue");
			this.setFill(Color.color(red, this.mutationColor(green, mutation), this.mutationColor(blue, mutation))) ;
			break;

		case 6 :
//			System.out.println("3 couleurs");
			this.setFill(Color.color(this.mutationColor(red, mutation), this.mutationColor(green, mutation), this.mutationColor(blue, mutation))) ;
			break;
		case 7 :
//			System.out.println("BREAK");
			break;
		}
	}
	
	
	private double mutationColor(double couleur, double var) {
		double ret;
		Random rn = new Random();

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
	
	
	public IndividualSave crossover(IndividualSave ind) {
		Random rn = new Random();
		IndividualSave child;
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
			child= new IndividualSave(((int)Math.random() * ((max - min) + 1)) + min);
		}else {
			//UN SEUL PARENT À LA FOIS
			if(rn.nextBoolean()) {
				//PARENT 1
				child = new IndividualSave(min);
			}else {
				//PARENT 2
				child = new IndividualSave(max);
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
		IndividualSave poly = (IndividualSave) o;
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
