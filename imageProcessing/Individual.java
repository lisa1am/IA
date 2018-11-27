package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Individual implements Comparable{
	
	private double fitness;
	private ArrayList<ConvexPolygon> individu;
	
	public Individual(int nbPoints) {
		this.individu = new ArrayList<ConvexPolygon>();
		for(int i = 0; i < 50; i++) {
			ConvexPolygon cp = new ConvexPolygon(3);
			individu.add(cp);
		}
		this.fitnessScore();
	}

	
	public void fitnessScore() {
		String targetImage = "monaLisa-100.jpg";
		Color[][] target=null;
		int maxX=0;
    	int maxY=0;
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
		System.out.println("Read target image " + targetImage + " " + maxX + "x" + maxY);


		// formation de l'image par superposition des polygones
		Group image = new Group();
		for (ConvexPolygon p : individu)
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
		System.out.println("Fitness Score d'un individu"+Math.sqrt(res));
		this.fitness = res;
	}

	public double getFitness() {
		this.fitnessScore();
		return this.fitness;
	}

		
	public Individual getIndividual() {
		return this;
	}

	

	@Override
	public int compareTo(Object o) {
		Individual ind = (Individual) o;
		if(this.getFitness()<ind.getFitness()) {
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
