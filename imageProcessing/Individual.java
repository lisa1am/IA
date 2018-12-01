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
import javafx.stage.Stage;

public class Individual implements Comparable{

	private double fitness;
	private ArrayList<ConvexPolygon> individu;
	String targetImage;
	Color[][] target;
	int maxX;
	int maxY;


	public Individual(int nbPoints) {
		this.individu = new ArrayList<ConvexPolygon>();
		for(int i = 0; i < 50; i++) {
			ConvexPolygon cp = new ConvexPolygon(nbPoints);
			individu.add(cp);
		}
		this.MiseaJour();
		this.fitnessScore();
	}
	
	public Individual(Individual ind) {
		this.individu= ind.individu;
		this.fitness= ind.fitness;
		this.maxX= ind.maxX;
		this.maxY= ind.maxY;
		this.target= ind.target;
	}

	public Individual() {
		this.MiseaJour();
		this.individu = new ArrayList<ConvexPolygon>();
	}

	public ArrayList<ConvexPolygon> getIndividu(){

		return this.individu;
	}

	public void MiseaJour() {
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
	//System.out.println("Read target image " + targetImage + " " + maxX + "x" + maxY);
	public void fitnessScore() {

		// formation de l'image par superposition des polygones
		Group image = new Group();
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(ConvexPolygon.max_X);
		rectangle.setHeight(ConvexPolygon.max_Y);
		rectangle.setFill(Color.BLACK);
		image.getChildren().add(rectangle);
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
				Color c = pr.getColor(i, j);
				res += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
						+Math.pow(c.getRed()-target[i][j].getRed(),2)
						+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
			}
		}
		//System.out.println("Fitness Score d'un individual: "+Math.sqrt(res));

		this.fitness = Math.sqrt(res);
	}

	public double getFitness() {
		return this.fitness;
	}


	public Individual getIndividual() {
		return this;
	}

	public Individual crossoverOneEach(Individual ind) {
		Individual ret = new Individual();

		for(int i = 0; i < this.individu.size(); i++) {
			if(i%2 == 0) {
				ret.individu.add(this.individu.get(i));
			}
			else {
				ret.individu.add(ind.individu.get(i));
			}
		}
		//ret.fitnessScore();
		//System.out.println("AVANT : "+ind.getFitness()+" et "+this.getFitness()+" enfant = "+ret.getFitness());
		// mutation d'un enfant avec une chance de 5%
		//ret.mutation(5);
		//System.out.println("APRES : "+ind.getFitness()+" et "+this.getFitness()+" enfant = "+ret.getFitness());
		//calcul de son fitnessScore qu'il est été muté ou pas
		ret.fitnessScore();
		return ret;
	}




	public void mutation(int probability, int rate) {
		Random rn = new Random();
		int pourc = rn.nextInt(101);
		
		
		if(pourc < probability) {
			//System.out.println("__________________________________");
			//System.out.println("AVANT THIS: "+this.getFitness());			
			for(int i = 0; i < this.individu.size(); i++) {
				this.individu.get(i).mutate(rate);
			}
			this.fitnessScore();
			//this.fitnessScore();
			//System.out.println("APRES TMP: "+tmpInd.getFitness()+"\n");
		}
	}



	public void setIndividu(ArrayList<ConvexPolygon> liste) {
		this.individu.clear();
		this.individu=liste;
		this.fitnessScore();
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

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *  NE SERT A RIEN DESSOUS CA, ENFIN JE M EN SERS PAS
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 	
	 */
	public Individual crossoverTwoEach(Individual ind) {
		Individual ret = new Individual();
		int index = 0;
		for(int i = 0; i < this.individu.size(); i++) {
			if(index < 2) {
				ret.individu.add(this.individu.get(i));
				index++;
			}
			else {
				ret.individu.add(ind.individu.get(i));
				index++;
			}

			if(index == 4) {
				index = 0;
			}
		}
		ret.fitnessScore();
		return ret;
	}

	public Individual crossoverThreeEach(Individual ind) {
		Individual ret = new Individual();
		int index = 0;
		for(int i = 0; i < this.individu.size(); i++) {
			if(index < 3) {
				ret.individu.add(this.individu.get(i));
				index++;
			}
			else {
				ret.individu.add(ind.individu.get(i));
				index++;
			}

			if(index == 6) {
				index = 0;
			}
		}
		ret.fitnessScore();
		return ret;
	}

	public Individual crossoverMiddle(Individual ind) {
		Individual ret = new Individual();
		for(int i = 0; i < this.individu.size(); i++) {
			if(i <= 24) {
				ret.individu.add(this.individu.get(i));
			}
			else {
				ret.individu.add(ind.individu.get(i));
			}
		}
		ret.fitnessScore();
		return ret;
	}

	public Individual crossoverRandom(Individual ind) {
		Individual ret = new Individual();
		Random rn = new Random();
		int random;

		int insert;

		for(int i = 0; i < this.individu.size(); i++) {
			insert = i;
			if(i%2 == 0) {
				while(ret.individu.contains(this.individu.get(insert))) {
					insert++;
					if(insert == 50) {
						insert = 0;
					}
				}
				ret.individu.add(this.individu.get(insert));
			}
			else {
				while(ret.individu.contains(ind.individu.get(insert))) {
					insert++;
					if(insert == this.individu.size()) {
						insert = 0;
					}
				}
				ret.individu.add(ind.individu.get(insert));
			}
		}
		ret.fitnessScore();
//		if(ret.getFitness() < ind.getFitness() && ret.getFitness() < this.getFitness()) {
//		System.out.println("Enfant entre : "+this.getFitness()+" et "+ind.getFitness()+" = = = "+ret.getFitness());
//		}
		return ret;
	}
}
