/**
 * 
 */
package edu.stanford.math.plex_plus.homology.chain_basis;

import java.util.Comparator;

/**
 * This class provides functionality for comparing two cells within a CW complex.
 * The comparison mechanism first compares the dimensions of the cells. If the 
 * dimensions are equal then it looks at the cellId values. This class
 * implements the Comparator interface, and follows a singleton design pattern.
 * 
 * @author Andrew Tausz
 *
 */
public class CellComparator implements Comparator<Cell> {
	/**
	 * This is the single instantiation of the class.
	 */
	private static CellComparator instance = new CellComparator();	
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private CellComparator() {}
	
	/**
	 * This static function returns the one instance of the class.
	 * 
	 * @return the instance of the class
	 */
	public static CellComparator getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Cell arg0, Cell arg1) {
		int cell0dimension = arg0.getDimension();
		int cell1dimension = arg1.getDimension();
		
		/*
		 * Compare dimension.
		 */
		if (cell0dimension != cell1dimension) {
			return (cell0dimension - cell1dimension);
		}
		
		/*
		 * At this point, they have the same dimension,
		 * so compare the cellId's.
		 */
		return (arg0.getCellId() - arg1.getCellId());
	}

}
