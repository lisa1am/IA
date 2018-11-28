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
			ConvexPolygon cp = new ConvexPolygon(3);
			individu.add(cp);
		}
		this.MiseaJour();
		this.fitnessScore();
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
		ret.fitnessScore();
		return ret;
	}
	
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
		ArrayList<Integer> savethis = new ArrayList<Integer>();
		ArrayList<Integer> saveind = new ArrayList<Integer>();
		
		
		for(int i =0; i < 25; i++) {
			random = rn.nextInt(50);
			while(savethis.contains(random)) {
				random = rn.nextInt(50);
			}
			savethis.add(random);
		}
		
		for(int i =0; i < 25; i++) {
			random = rn.nextInt(50);
			while(saveind.contains(random)) {
				random = rn.nextInt(50);
			}
			saveind.add(random);
		}
		
		int insert;
		
		for(int i = 0; i < this.individu.size(); i++) {
			insert = i/2;
				if(i%2==0) {
					ret.individu.add(this.individu.get(savethis.get(insert)));
				}
				else {
					ret.individu.add(ind.individu.get(saveind.get(insert)));
				}
		}
		ret.fitnessScore();
		return ret;
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
