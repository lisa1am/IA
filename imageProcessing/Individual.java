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


	public Individual(int nbPoints) {
		Random rn = new Random();
		this.individu = new ArrayList<ConvexPolygon>();
		this.Init();		
		//this.fitnessScore();
	}


	public ArrayList<ConvexPolygon> getIndividu(){
		return this.individu;
	}

	public void Init() {
		targetImage = "monaLisa-200.jpg";
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
	
	public double localFitness() {
		double fit=0;
		boolean contains= false;
		int localArea=0;
		
		Group image = new Group();
		for(ConvexPolygon p : individu) {
			image.getChildren().add(p);
		}
		
		
		WritableImage wimg = new WritableImage(maxX,maxY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		
		
		//System.out.println("minx= "+minX+" maxx= "+maxX+" miny= "+minY+" maxy= "+maxY);
		System.out.println("checking local fitness");
		for(int i=0; i<maxX; i++) {
			for(int j=0; j<maxY; j++) {
				Color c = pr.getColor(i, j);
				for(ConvexPolygon p : individu) {
					contains=contains||(p.contains(i,j));
				}
				if(contains){
					localArea++;
					fit += Math.abs(c.getBlue()-target[i][j].getBlue())
							+Math.abs(c.getRed()-target[i][j].getRed())
							+Math.abs(c.getGreen()-target[i][j].getGreen());
				}
				
			}
		}
		fit= (1-(fit/(3*area)))*100;
		//fit = Math.sqrt(fit);
		//fit=(1-( Math.sqrt(fit)/localArea))*100;
		System.out.println("LOCAL FITNESS = "+fit);
		area=localArea;
		return(fit);
	}
	
	
	public void fitnessScore() {
		
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
		for (int i=0;i<maxX;i++){
			for (int j=0;j<maxY;j++){
				area++;
				Color c = pr.getColor(i, j);
				res += Math.abs(c.getBlue()-target[i][j].getBlue())
						+Math.abs(c.getRed()-target[i][j].getRed())
						+Math.abs(c.getGreen()-target[i][j].getGreen());
			}
		}
		//System.out.println("Fitness Score d'un individual: "+Math.sqrt(res));

		this.fitness = (1-(res/(3*area)))*100;
	}

	public double getFitness() {
		return this.fitness;
	}

	public void putPolygons(int nbPoly) {
		
		Random rn = new Random();
		ConvexPolygon poly, tmpPoly;
		double fitness;
		
		
		
		// 50 polygons
		for(int i=0; i<nbPoly; i++) {
			poly = new ConvexPolygon(rn.nextInt(4)+5);
			individu.add(i, poly);
			fitness = this.localFitness();
			
			while(this.localFitness()<60){
				tmpPoly = new ConvexPolygon(rn.nextInt(4)+5);
				individu.set(i, tmpPoly);
				if(fitness>this.localFitness()) {
					System.out.println("old = "+fitness+" new = "+this.localFitness());
					fitness = this.localFitness();
					individu.set(i,tmpPoly);
				}	
			}
			System.out.println("*****************"+i);
		}
		fitnessScore();
		System.out.println("FITNESS ----- = "+ this.fitness);
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
