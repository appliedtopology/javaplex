package edu.stanford.math.plex4.homology.filtration;



/**
 * This class defines a filtration value conversion which simply defines the filtration
 * value to be equal to the filtration index. (i.e. f_i = i)
 * 
 * @author Andrew Tausz
 *
 */
public class IdentityConverter extends FiltrationConverter {

	/**
	 * This is the single instance of the class
	 */
	private static IdentityConverter instance = new IdentityConverter();
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private IdentityConverter(){}
	
	/**
	 * This returns the single instance of the class.
	 * 
	 * @return the single instance of the class
	 */
	public static IdentityConverter getInstance() {
		return instance;
	}
	
	@Override
	public int getFiltrationIndex(double filtrationValue) {
		return (int) filtrationValue;
	}

	@Override
	public double getFiltrationValue(int filtrationIndex) {
		return filtrationIndex;
	}

	@Override
	public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
		return Math.max(filtrationValue1, filtrationValue2);
	}

	@Override
	public double getInitialFiltrationValue() {
		return 0;
	}
}
