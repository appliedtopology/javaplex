/**
 * 
 */
package edu.stanford.math.plex4.homology.filtration;


/**
 * This class implements the FiltrationConverter interface for static complexes. 
 * It simply sets all of the filtration values and indices to 0.
 * 
 * @author Andrew Tausz
 *
 */
public class StaticConverter extends FiltrationConverter {
	/**
	 * This is the single instance of the class
	 */
	private static StaticConverter instance = new StaticConverter();
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private StaticConverter(){}
	
	/**
	 * This returns the single instance of the class.
	 * 
	 * @return the single instance of the class
	 */
	public static StaticConverter getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationIndex(double)
	 */
	public int getFiltrationIndex(double filtrationValue) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationValue(int)
	 */
	public double getFiltrationValue(int filtrationIndex) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#computeInducedFiltrationValue(double, double)
	 */
	public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getInitialFiltrationValue()
	 */
	public double getInitialFiltrationValue() {
		return 0;
	}
}
