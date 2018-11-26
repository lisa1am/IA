package imageProcessing;

import javafx.scene.paint.Color;
import junit.framework.*;

public class IndividualTest extends TestCase {

	public void testConstructeur() throws Exception{
		int n = 5;
		
		Individual ind = new Individual(n);
		assertTrue(ind.points().size() == n);
		
		for(int i = 0; i < ind.points().size(); i++) {
			assertTrue("Borne max X",ind.points().get(i).getX() <= 100);
			assertTrue("Borne max Y",ind.points().get(i).getX() <= 149);
		}
		
		assertTrue("2 points différents", (ind.points().get(0).getX() != ind.points().get(1).getX()) || ind.points().get(0).getY() != ind.points().get(1).getY());
	}
	
	public void testColor() throws Exception{
		Individual ind = new Individual(3);
		Individual ind2 = new Individual(3);
		
		Color c1 = ind.getColor();
		Color c2 = ind2.getColor();
		
		assertTrue("Red c1", c1.getRed() >= 0 && c1.getRed()<= 255);
		assertTrue("Red c2", c2.getRed() >= 0 && c2.getRed()<= 255);
		
		assertTrue("Green c1", c1.getGreen() >= 0 && c1.getGreen()<= 255);
		assertTrue("Green c2", c2.getGreen() >= 0 && c2.getGreen()<= 255);
		
		assertTrue("Blue c1", c1.getBlue() >= 0 && c1.getBlue()<= 255);
		assertTrue("Blue c2", c2.getBlue() >= 0 && c2.getBlue()<= 255);
		
		Color children = ind.avgRandomColor(c1, c2);
		
		assertTrue("Red children", children.getRed() != c1.getRed() && children.getRed() != c2.getRed());
		
		assertTrue("Green children", children.getGreen() != c1.getGreen() && children.getGreen() != c2.getGreen());
		
		assertTrue("Blue children", children.getBlue() != c1.getBlue() && children.getBlue() != c2.getBlue());
	}
}
