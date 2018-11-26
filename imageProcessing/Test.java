package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
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
			System.out.println("x= "+maxX+" y= "+maxY);
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
		
		// génération de 100 triangles
		// générer des polygones avec nombre de sommets aléatoire ne dépassant pas 5
		// ( maxPolygons +3 ) -> pour commencer des polygones à trois commets
		// (i%maxPoints)+3 -> pour générer un nombre de sommets aléatoire ne dépassant pas 5
		// on ajoute le 3 pour éviter quand le modulo =0,1 et 2
		int maxPoints = 5;
		int maxPolygons = 6;
		Random rn = new Random();
		List<ConvexPolygon> ls = new ArrayList<ConvexPolygon>();
		for (int i=3;i<(maxPolygons+3);i++) {
			ls.add(new ConvexPolygon(rn.nextInt(maxPoints)+3));
			System.out.println("List size = "+ ls.size());
		}
			
		
		// formation de l'image par superposition des polygones
		Group image = new Group();
		for (ConvexPolygon p : ls) {
			image.getChildren().add(p);
		}
			
		
		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de 
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		WritableImage wimg = new WritableImage(maxX,maxY);
		image.snapshot(null,wimg);
		PixelReader pr = wimg.getPixelReader();
		
		// On utilise le PixelReader pour lire chaque couleur
		// ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
		// de la couleur du pixel qcible et celui du pixel de l'image générée	
		double res=0;
		for (int i=0;i<maxX;i++){
			for (int j=0;j<maxY;j++){
				Color c = pr.getColor(i, j);
				res += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
				+Math.pow(c.getRed()-target[i][j].getRed(),2)
				+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
			}
		}
		System.out.println("Population fitness : "+Math.sqrt(res));
		
		
		double fit=0;
		int polygonArea =0;
		for(ConvexPolygon cp : ls) {
			for (int i=0;i<maxX;i++){
				for (int j=0;j<maxY;j++){
					if(cp.contains(i, j)) {
						Color c = pr.getColor(i, j);
						fit += Math.pow(c.getBlue()-target[i][j].getBlue(),2)
						+Math.pow(c.getRed()-target[i][j].getRed(),2)
						+Math.pow(c.getGreen()-target[i][j].getGreen(),2);
						polygonArea++;
					}
				}
			}
			fit=Math.sqrt(fit);
			System.out.println("Polygon fitness :"+fit);
			fit=0;
		}
		
		
		
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
