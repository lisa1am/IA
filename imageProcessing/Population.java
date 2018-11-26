package imageProcessing;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Population {
	
	private List<Individual> individuals;
	private int genSize;
	private double fittest;
	private Population best;
	private Color[][] target;
	
	private final static int MAXX=100;
	private final static int MAXY=149;
 
	
	
	//initialiser le "target" = le vecteur contenant les couleurs originales pixel par pixel
	public void initContext(){
		try {
		int maxX=0, maxY=0;
		BufferedImage bi = ImageIO.read(new File("monaLisa-100.jpg"));
		maxX = bi.getWidth();
		maxY = bi.getHeight();
			//System.out.println("x= "+maxX+" y= "+maxY);
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
	  }catch(IOException e){
    	System.err.println(e);
    	System.exit(9);
	  }
	}
	
	
	//initialiser la population avec le d'individus souhaité
	public Population(int popSize) {
		individuals = new ArrayList<Individual>();
		for (int i=0;i<200;i++) {
			individuals.add(new Individual(3));
		}
		initContext();
		
	}


	
	//récuprére le meilleur de la population selon son fitnessScore
	public Individual fittest(){
		double maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (maxFit <= individuals.get(i).fitnessScore(target)) {
                maxFit = individuals.get(i).fitnessScore(target);
                maxFitIndex = i;
            }
        }
        fittest = individuals.get(maxFitIndex).fitnessScore(target);
        return individuals.get(maxFitIndex);
		
	}
	
	//calculer le fitness de l'ensemble de la population par rapport à l'image
	//pour pouvoir mettre une condition d'arrêt pour la boucle de reproduction
	public double getPopulationFitness() {
		
		//placer les individus de la population sur une image pour lire pixel par pixel et comparer
		Group image = new Group();
		double fitness=0;
		for(Individual poly : best.individuals) {
			 image.getChildren().add(poly.getPolygon());
		}
	    WritableImage wimg = new WritableImage(MAXX,MAXY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		
		for (int i=0;i<MAXX;i++){
			for (int j=0;j<MAXY;j++){
					Color c = pr.getColor(i, j);
					fitness += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
					+Math.pow(c.getRed()-target[i][j].getRed(),2)
					+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
			}
		}
		return fitness;
	}
	
	public void add(Individual poly) {
		individuals.add(poly);
	}
	
	public void sortByFitness() {
		Collections.sort(individuals, new IndividualComparator());
	}
	
	//retourner une liste des meilleurs pour les croiser 
	//prendre le meilleure en premier
	//fixer la barre au fitnessScore du meilleur précédent
	//choisir le meilleur par rapport au fitnessScore
	//mettre à jour le fitnessScore
	public void selection(int selectionSize) {
		sortByFitness();
		//elitism
		for(int i=0; i<selectionSize; i++) {
			best.add(individuals.get(i));
		}
	}
	
	
	
	
}
