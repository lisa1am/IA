package imageProcessing;

import javafx.scene.paint.Color;
import junit.framework.*;

public class IndividualTest extends TestCase {

	public void testConstructeur() throws Exception{
		int n = 3;
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(n);
		assertTrue(ind.points().size() == n);
		
		for(int i = 0; i < ind.points().size(); i++) {
			assertTrue("Borne max X",ind.points().get(i).getX() <= 100);
			assertTrue("Borne max Y",ind.points().get(i).getX() <= 149);
		}
		
		assertTrue("2 points différents", (ind.points().get(0).getX() != ind.points().get(1).getX()) || ind.points().get(0).getY() != ind.points().get(1).getY());
	}
	
	public void testColor() throws Exception{
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(3);
		Individual ind2 = new Individual(3);
		
		Color c1 = ind.getColor();
		Color c2 = ind2.getColor();
		
		assertTrue("Red c1", c1.getRed() >= 0 && c1.getRed()<= 1);
		assertTrue("Red c2", c2.getRed() >= 0 && c2.getRed()<= 1);
		
		assertTrue("Green c1", c1.getGreen() >= 0 && c1.getGreen()<= 1);
		assertTrue("Green c2", c2.getGreen() >= 0 && c2.getGreen()<= 1);
		
		assertTrue("Blue c1", c1.getBlue() >= 0 && c1.getBlue()<= 1);
		assertTrue("Blue c2", c2.getBlue() >= 0 && c2.getBlue()<= 1);
		
//		System.out.println(c1.getRed());
//		System.out.println(c2.getRed());
//		System.out.println(c1.getGreen());
		Color children = ind.avgRandomColor(c1, c2);
		
//		System.out.println(children.getRed());
		assertTrue("Red children compare", children.getRed() != c1.getRed() && children.getRed() != c2.getRed());
		
		assertTrue("Green children compare", children.getGreen() != c1.getGreen() && children.getGreen() != c2.getGreen());
		
		assertTrue("Blue children compare", children.getBlue() != c1.getBlue() && children.getBlue() != c2.getBlue());
		
		assertTrue("Red children bornes", children.getRed() >= 0 && children.getRed()<= 255);
		
		assertTrue("Green children bornes", children.getGreen() >= 0 && children.getGreen()<= 255);
		
		assertTrue("Blue children bornes", children.getBlue() >= 0 && children.getBlue()<= 255);
		
	}
	
	public void testOpacity() throws Exception{
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(3);
		Individual ind2 = new Individual(3);
		
		double o1 = ind.getOpacity();
		double o2 = ind2.getOpacity();
		
		assertTrue("Opacity o1", o1 >= 0 && o1 <= 1);
		assertTrue("Opacity o2", o2 >= 0 && o2 <= 1);
		
		double o = ind.avgRandomOpacity(o1, o2);
		
		assertTrue("Opacity compare", o != o1 && o != o2);
		
	}
	
	public void testMutationPoint() throws Exception{
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(3);
		Individual ind2 = new Individual(3);
		
		
		for(int i = 0; i < ind.points.size(); i++) {
			Point p = new Point(ind.points.get(i).getX(), ind.points.get(i).getY());
//			System.out.println(ind.points.get(i).getX());
//			System.out.println(ind.points.get(i).getY());
			ind2.points.set(i, p);
		}
		ind.mutate();
		
		assertTrue("Verification nb point egal", ind2.points.size() == ind.points.size());
		
//		for(int i = 0; i < ind.points.size(); i++) {
//			System.out.println("\n \n"+ind.points.get(i).getX());
//			System.out.println(ind2.points.get(i).getX());
//			System.out.println(ind.points.get(i).getY());
//			System.out.println(ind2.points.get(i).getY());
//			
//			assertTrue("Mutate point", (ind.points.get(i).getX() != ind2.points.get(i).getX()) || (ind.points.get(i).getY() != ind2.points.get(i).getY()) );
//		}
		boolean test = (ind.points.get(0).getX() != ind2.points.get(0).getX()) || (ind.points.get(0).getY() != ind2.points.get(0).getY()) || 
						(ind.points.get(1).getX() != ind2.points.get(1).getX()) || (ind.points.get(1).getY() != ind2.points.get(1).getY()) ||
						(ind.points.get(2).getX() != ind2.points.get(2).getX()) || (ind.points.get(2).getY() != ind2.points.get(2).getY());
		
		assertTrue("Mutate point", test );	
	}
	
	public void testMutationCouleur() throws Exception{
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(3);
		double red = ind.getColor().getRed();
		double green = ind.getColor().getGreen();
		double blue = ind.getColor().getBlue();
		
//		System.out.println("Couleur base : \n RED : "+red+"\n GREEN : "+green+"\n BLUE : "+blue);
		
		ind.mutate();
		
		double red2 = ind.getColor().getRed();
		double green2 = ind.getColor().getGreen();
		double blue2 = ind.getColor().getBlue();
		
//		System.out.println("Couleur mutation : \n RED : "+red2+"\n GREEN : "+green2+"\n BLUE : "+blue2);
		
		boolean test = red != red2 || green != green2 || blue != blue2;
	}
	
	
}
