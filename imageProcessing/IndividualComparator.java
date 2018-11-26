package imageProcessing;

import java.util.Comparator;

public class IndividualComparator implements Comparator<Individual>{

	public IndividualComparator() {
		super();
	}
	
	@Override
	public int compare(Individual poly1, Individual poly2) {
		return poly1.compareTo(poly2);
	}
}
