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
	private List<Individual> listBest;
	private Individual best;
	private Color[][] target;
	
	private final static int MAXX=100;
	private final static int MAXY=149;
 
	
	//initialiser la population avec le d'individus souhaité
		public Population(int popSize) {
			individuals = new ArrayList<Individual>();
			for (int i=0;i< popSize;i++) {
				individuals.add(new Individual(3));
			}	
			
			listBest = new ArrayList<Individual>();
		}

	
	//récuprére le meilleur individu de la population selon son fitnessScore
	public Individual fittest(){
		double maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (maxFit <= individuals.get(i).getFitness()) {
                maxFit = individuals.get(i).getFitness();
                maxFitIndex = i;
            }
        }
        fittest = individuals.get(maxFitIndex).getFitness();
        return individuals.get(maxFitIndex);	
	}
	
	
	public void add(Individual ind) {
		individuals.add(ind);
	}
	
	public void sortByFitness() {
		Collections.sort(individuals, new IndividualComparator());
		best = individuals.get(0);
	}
	
	public Individual getBest() {
		this.sortByFitness();
		return best;
	}
	
	//retourner une liste des meilleurs pour les croiser 
	//prendre le meilleure en premier
	//fixer la barre au fitnessScore du meilleur précédent
	//choisir le meilleur par rapport au fitnessScore
	//mettre à jour le fitnessScore
	public void selection(int selectionSize) {
		//elitism
		for(int i=0; i<selectionSize; i++) {
			listBest.add(individuals.get(i));
		}
		
		System.out.println("BEST :"+listBest.get(0).getFitness());
	}
	
	public void crossoverOneEach() {
		
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		
		for(int i = 0; i < listBest.size()-1; i++) {
			individuals.add(listBest.get(i).crossoverOneEach(listBest.get(i+1)));
			individuals.add(listBest.get(i+1).crossoverOneEach(listBest.get(i)));
			i++;
		}
	}
	
	public void crossoverTwoEach() {
		
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		//individuals.clear();
		
		for(int i = 0; i < listBest.size()-1; i++) {
			individuals.add(listBest.get(i).crossoverTwoEach(listBest.get(i+1)));
			individuals.add(listBest.get(i+1).crossoverTwoEach(listBest.get(i)));
			i++;
		}
	}
	
public void crossoverThreeEach() {
		
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		//individuals.clear();
		
		for(int i = 0; i < listBest.size()-1; i++) {
			individuals.add(listBest.get(i).crossoverThreeEach(listBest.get(i+1)));
			individuals.add(listBest.get(i+1).crossoverThreeEach(listBest.get(i)));
			i++;
		}
	}
	
	
	public void crossoverMiddle() {
		
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		
		//individuals.clear();
		
		for(int i = 0; i < listBest.size()-1; i++) {
			individuals.add(listBest.get(i).crossoverMiddle(listBest.get(i+1)));
			individuals.add(listBest.get(i+1).crossoverMiddle(listBest.get(i)));
			i++;
		}
	}
	
	public void crossoverRandom() {
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		
		//individuals.clear();
		
		for(int i = 0; i < listBest.size()-1; i++) {
			individuals.add(listBest.get(i).crossoverRandom(listBest.get(i+1)));
			individuals.add(listBest.get(i+1).crossoverRandom(listBest.get(i)));
			i++;
		}
	}
	
	
	public Individual testA() {
		sortByFitness();
		listBest.clear();
		this.selection(20);
		individuals.clear();
		this.crossoverOneEach();
		this.crossoverTwoEach();
		this.crossoverThreeEach();
		return this.getBest();
	}
	
}
