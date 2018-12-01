package imageProcessing;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	private int select;
	int genNumber;
	
	private final static int MAXX=100;
	private final static int MAXY=149;
 
	
	//initialiser la population avec le d'individus souhaité
		public Population(int popSize) {
			individuals = new ArrayList<Individual>();
			for (int i=0;i< popSize;i++) {
				individuals.add(new Individual(3));
			}	
			
			listBest = new ArrayList<Individual>();
			this.select = 20;
			this.genNumber = 1;
		}
	
	
	public void add(Individual ind) {
		individuals.add(ind);
	}
	
	public void sortByFitness() {
		Collections.sort(individuals, new IndividualComparator());
		this.best = this.individuals.get(0);
		System.out.println("BEST : "+this.best.getFitness());
	}
	
	public void sortByFitnessBest() {
		Collections.sort(listBest, new IndividualComparator());
		this.best = this.listBest.get(0);
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
		
		//System.out.println("BEST :"+listBest.get(0).getFitness());
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
		
		for(int j=0; j < listBest.size(); j++) {
			individuals.add(listBest.get(j));
		}
		
		
	}
	
	public void crossoverOneFOREach(int proba, int rate) {
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		
		System.out.println("\n \n");
		
		for(int i = 0; i < listBest.size(); i++) {
			for(int j = 0; j < listBest.size(); j++) {
				if(i != j) {
				individuals.add(listBest.get(i).crossoverOneEach(listBest.get(j)));
				}
			}
		}
		
		for(int j=0; j < listBest.size(); j++) {	
			individuals.add(listBest.get(j));
		}
		

		this.mutation(proba, rate);
		
		
		
	}
	
	public void crossoverOneFOREachRandom(int probability, int rate) {
		for(int j = 0; j < listBest.size(); j++) {
			System.out.println("Best numero : "+(j+1)+" : "+listBest.get(j).getFitness());;
		}
		for(int j=0; j < listBest.size(); j++) {	
			individuals.add(listBest.get(j));
		}
		System.out.println("\n \n");
		
		for(int t = 0; t < 5; t++) {
		for(int i = 0; i < listBest.size(); i++) {
			Collections.shuffle(listBest.get(i).getIndividu());
			//listBest.get(i).fitnessScore();
			//System.out.println("SHUFFLE : "+(i+1)+" : "+listBest.get(i).getFitness());;

			
		}
		
		for(int i = 0; i < listBest.size(); i++) {
			for(int j = 0; j < listBest.size(); j++) {
				if(i != j) {
				individuals.add(listBest.get(i).crossoverRandom(listBest.get(j)));
				}
			}
		}
		}
		
		
		

		this.mutation(probability, rate);	
	}
	
	public void mutation(int probability, int rate) {
		for(int i = 0; i< individuals.size(); i++) {
			individuals.get(i).mutation(probability, rate);
			individuals.get(i).fitnessScore();
		}
		this.sortByFitness();
		this.selection(genSize);
		this.sortByFitnessBest();
	}
	
	
	public Individual testA() {
		System.out.println("\n ********************** GENERATION "+this.genNumber+" ********************** \n");
		sortByFitness();
		
//		for(int i = 0; i < individuals.size(); i++) {
//			System.out.println("Indiv : "+individuals.get(i).getFitness());
//		}
		
		listBest.clear();
		this.selection(this.select);
		

		individuals.clear();
		sortByFitnessBest();
		this.crossoverOneFOREachRandom(3, 5);
		

		this.genNumber++;
		return this.getBest();
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * NO UTILITY
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
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
}
