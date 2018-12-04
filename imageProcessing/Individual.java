package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;


import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Individual implements Comparable{

	private double fitness;
	private ArrayList<ConvexPolygon> individu;
	String targetImage;
	Color[][] target;
	int maxX;
	int maxY;
	int area=0;


	public Individual() {
		Random rn = new Random();
		this.individu = new ArrayList<ConvexPolygon>();
		this.Init();		
		//this.fitnessScore();
	}
	
	public Individual(Individual ind) {
		this.individu = new ArrayList<ConvexPolygon>();
		for(ConvexPolygon p : ind.getIndividu()) {
			this.getIndividu().add(new ConvexPolygon(p));
		}
		this.maxX = ind.maxX;
		this.maxY = ind.maxY;
		this.target = ind.target;
		this.fitness = ind.fitness;
	}



	public ArrayList<ConvexPolygon> getIndividu(){
		return this.individu;
	}

	public void Init() {
		targetImage = "monaLisa-100.jpg";
		maxX = 0;
		maxY = 0;
		try{
			BufferedImage bi = ImageIO.read(new File(targetImage));
			maxX = bi.getWidth();
			maxY = bi.getHeight();
			ConvexPolygon.max_X= maxX;
			ConvexPolygon.max_Y= maxY;
			target = new Color[maxX][maxY];
			for (int i=0;i<maxX;i++){
				for (int j=0;j<maxY;j++){
					int argb = bi.getRGB(i, j);
					int b = (argb)&0xFF;
					int g = (argb>>8)&0xFF;
					int r = (argb>>16)&0xFF;
					int a = (argb>>24)&0xFF;
					target[i][j] = Color.rgb(r,g,b);
				}
			}
		}
		catch(IOException e){
			System.err.println(e);
			System.exit(9);
		}
	}
	
	public double localFitness(ConvexPolygon poly) {
		double fit=0;
		int localArea=0;
		
		Group image = new Group();
		for(ConvexPolygon p : individu) {
			image.getChildren().add(new ConvexPolygon(p.getColor(), p.points));
		}
		
		WritableImage wimg = new WritableImage(maxX,maxY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		
		for(int i=0; i<maxX; i++) {
			for(int j=0; j<maxY; j++) {
				Color c = pr.getColor(i, j);
		
				if(poly.contains(i, j)){
					localArea++;
					fit += Math.abs(c.getBlue()-target[i][j].getBlue())
							+Math.abs(c.getRed()-target[i][j].getRed())
							+Math.abs(c.getGreen()-target[i][j].getGreen());
				}
				
			}
		}
		fit = fit/(3*localArea);
		fit = 1- fit;
		fit = fit*100;
		System.out.println(fit);
		
		return (fit);
	}
	
	
	public double fitnessScore() {
		
		int area=0;
		// formation de l'image par superposition des polygones
		Group image = new Group();
		for (ConvexPolygon p : this.individu)
			image.getChildren().add(p);

		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de 
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		WritableImage wimg = new WritableImage(maxX,maxY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		// On utilise le PixelReader pour lire chaque couleur
		// ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
		// de la couleur du pixel cible et celui du pixel de l'image générée	
		double res=0;
		for(int i=0;i<maxX;i++){
			for(int j=0;j<maxY;j++){
				area++;
				Color c = pr.getColor(i, j);
				res += Math.abs(c.getBlue()-target[i][j].getBlue())
						+Math.abs(c.getRed()-target[i][j].getRed())
						+Math.abs(c.getGreen()-target[i][j].getGreen());
			}
		}
		//System.out.println("Fitness Score d'un individual: "+Math.sqrt(res));

		
		this.fitness = res/(3*area);
		this.fitness= 1 - this.fitness;
		this.fitness = this.fitness*100;
		return (this.fitness);
	}
	
	public void updateFitnessScore() {
		this.fitness= this.fitnessScore();
	}
	

	public double getFitness() {
		return this.fitness;
	}

	public void putPolygons(int nbPoly) {
	
		ConvexPolygon poly, before;
		Random rn = new Random();

		// 50 polygons
		//new
		for(int i=0; i<nbPoly; i++) {
			poly = new ConvexPolygon(3);
			
			while((poly.checkfitness(target)<80)||(poly.getArea()<((maxX*maxY)/200))) {
				//poly.mutate();
				poly = new ConvexPolygon(3);
			}
			
			individu.add(poly);	
			System.out.println("local fitness = "+poly.checkfitness(target));
			System.out.println(i);
		}
		
		//mutation
		/*for(int i=0; i<nbPoly; i++) {
			poly = new ConvexPolygon(3);
			before = new ConvexPolygon(poly);
			System.out.println(poly);
			
			while((poly.checkfitness(target)<40)||(poly.getArea()<((maxX*maxY)/200))) {
				poly.mutate();
				if(before.checkfitness(target)<poly.checkfitness(target)) {
					poly = new ConvexPolygon(before);
				}
			}
			individu.add(poly);
			System.out.println(poly);
			System.out.println("local fitness = "+poly.checkfitness(target));	
			
		}*/
		
		
		
		System.out.println("FITNESS = "+this.fitnessScore());
	}
	
	public void mutate() {
		Random rn = new Random();
		int nbPoly = rn.nextInt(this.getIndividu().size());
		int index;
		
		for(int i=0; i<nbPoly; i++) {
			//MUTATE
			System.out.println("IND BEFORE= "+this.fitnessScore());
			index = rn.nextInt(this.getIndividu().size());
			this.getIndividu().get(index).mutate();
			System.out.println("IND AFTER= "+this.fitnessScore());
			
		}
		updateFitnessScore();
	}
	
	
	
	public Individual draw() {
		
		Individual indTemp, ind = new Individual(this);
		
		while(ind.fitnessScore()<70) {	
			indTemp = new Individual(ind);
			ind.mutate();
			// l'individu était meilleur avant la mutation
			if(ind.fitnessScore()<indTemp.fitnessScore()) {
				ind = new Individual(indTemp);
			}
			System.out.println("IND FINAL = "+ ind.fitnessScore()+"\n\n");
		}
		return ind;
		
		
	}
	
	/*
	 * 
	 * 
	 * MUTATIONS
	 * 
	 * 
	 */
	
	
	public void mutation(int probability, int rate) {
		Random rn = new Random();
		int pourc = rn.nextInt(101);
		
		
		if(pourc < probability) {
			//System.out.println("__________________________________");
			//System.out.println("AVANT THIS: "+this.getFitness());			
			for(int i = 0; i < this.individu.size(); i++) {
				this.individu.get(i).mutate();
			}
			this.fitnessScore();
			//this.fitnessScore();
			//System.out.println("APRES TMP: "+tmpInd.getFitness()+"\n");
		}
	}
	
	@Override
	public int compareTo(Object o) {
		Individual ind = (Individual) o;
		if(this.getFitness() < ind.getFitness()) {
			return -1;
		}else {
			if(this.getFitness()>ind.getFitness()) {
				return 1;
			}else {
				return 0;
			}
		}
	}

}
