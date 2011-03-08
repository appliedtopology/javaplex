/**
 * 
 */
package edu.stanford.math.plex4.homology.chain_basis;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.utility.HomologyUtility;

/**
 * This class defines the standard ordering on the Simplex class and
 * implements the Comparator<Simplex> interface. The ordering of the
 * simplices is done first by dimension. If two simplices have the
 * same dimension, their vertices are then compared in lexicographical 
 * order. 
 * 
 * This class implements the singleton design pattern. Thus it cannot
 * be instantiated. Instead the user must call the static getInstance()
 * function in order to get the actual instance for use. 
 * 
 * Examples:
 * [4, 5, 7] > [9, 10];
 * [1, 2, 3] > [1, 2, 2];
 * [1, 2, 3] < [1, 3, 2];
 * 
 * 
 * @author Andrew Tausz
 *
 */
public class SimplexComparator implements Comparator<Simplex> {
	
	/**
	 * This is the single instantiation of the class.
	 */
	private static final SimplexComparator instance = new SimplexComparator();	
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private SimplexComparator() {}
	
	/**
	 * This static function returns the one instance of the class.
	 * 
	 * @return the instance of the class
	 */
	public static SimplexComparator getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Simplex o1, Simplex o2) {		
		// call the compareIntArrays function which actually performs the comparison
		return HomologyUtility.compareIntArrays(o1.getVertices(), o2.getVertices());
	}

}
