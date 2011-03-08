package edu.stanford.math.plex4.homology.chain_basis;

import java.util.Comparator;

public class SimplexPairComparator implements Comparator<SimplexPair> {

	private static final SimplexComparator simplexComparator = SimplexComparator.getInstance(); 
	
	/**
	 * This is the single instantiation of the class.
	 */
	private static final SimplexPairComparator instance = new SimplexPairComparator();	
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private SimplexPairComparator() {}
	
	/**
	 * This static function returns the one instance of the class.
	 * 
	 * @return the instance of the class
	 */
	public static SimplexPairComparator getInstance() {
		return instance;
	}
	
	public int compare(SimplexPair arg0, SimplexPair arg1) {
		int dim0 = arg0.getDimension();
		int dim1 = arg1.getDimension();
		
		if (dim0 > dim1) {
			return 1;
		} else if (dim0 < dim1) {
			return -1;
		}
		
		int comparison = simplexComparator.compare(arg0.getFirst(), arg1.getFirst());
		if (comparison != 0) {
			return comparison;
		}
		
		return simplexComparator.compare(arg0.getSecond(), arg1.getSecond());
	}

}
