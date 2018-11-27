package imageProcessing;
import junit.framework.*;

public class IndividualTest extends TestCase {

	public void testConstructeur() throws Exception{
		ConvexPolygon.max_X = 100;
		ConvexPolygon.max_Y = 149;
		Individual ind = new Individual(3);
		
		assertTrue("Test size", ind.getIndividu().size() == 50);
	}
	
}
