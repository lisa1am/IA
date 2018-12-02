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

public class Test extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage myStage){
		String targetImage = "monaLisa-200.jpg";
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
		

		Random rn = new Random();
		/*Individual ind = new Individual(5);
		//int nbp = rn.nextInt(4)+3;
		ConvexPolygon tmpPoly;
		
		for(int i=0; i<50; i++) {
			System.out.println("_______________________________________________");
			tmpPoly = new ConvexPolygon(rn.nextInt(4)+3);
			ind.getIndividu().add(i,tmpPoly);
			double fitness = tmpPoly.checkfitness(target);
			System.out.println("fitness = "+tmpPoly.getFitness());
			System.out.println("area = "+tmpPoly.getArea());
			
			while(tmpPoly.getFitness()<80) {
				System.out.println("HERE");
				tmpPoly = new ConvexPolygon(rn.nextInt(4)+3);
				//tmpPoly.mutate();
				if(fitness>tmpPoly.checkfitness(target)) {
					System.out.println("area = "+tmpPoly.getArea());
					System.out.println("old = "+fitness+" new = "+tmpPoly.getFitness());
					fitness = tmpPoly.getFitness();
					ind.getIndividu().set(i,tmpPoly);
				}	
			}
			System.out.println("fitness = "+ tmpPoly.getFitness());
			System.out.println(tmpPoly);
		}*/
		
		Individual ind = new Individual(rn.nextInt(4)+3);
		ind.putPolygons(50);
		System.out.println("IND SIZE = "+ind.getIndividu().size());
		
		
		Group image = new Group();
		
		
		for (ConvexPolygon p : ind.getIndividu()) {
			image.getChildren().add(p);
		}
		
		//MUTATE
		
		
		
		
		
			
		
		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de 
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		WritableImage wimg = new WritableImage(maxX,maxY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		// On utilise le PixelReader pour lire chaque couleur
		// ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
		// de la couleur du pixel cible et celui du pixel de l'image générée	
		/*double res=0;
		for (int i=0;i<maxX;i++){
			for (int j=0;j<maxY;j++){
				Color c = pr.getColor(i, j);
				res += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
						+Math.pow(c.getRed()-target[i][j].getRed(),2)
						+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
			}
		}
		System.out.println(Math.sqrt(res));*/
		
		// Stockage de l'image dans un fichier .png
		RenderedImage renderedImage = SwingFXUtils.fromFXImage(wimg, null); 
		try {
			ImageIO.write(renderedImage, "png", new File("test.png"));
			System.out.println("wrote image in " + "test.png");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// affichage de l'image dans l'interface graphique
		Scene scene = new Scene(image,maxX, maxY);
		myStage.setScene(scene);
		myStage.show();
		
	}

}