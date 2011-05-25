package edu.stanford.math.plex4.homology.filtration;



/**
 * <p>This class implements a conversion between filtration values and filtration
 * indices that is linear and increasing. If we let f_k be the filtration value
 * at index k and N is the number of divisions, then this mapping satisfies:
 * <ul>
 * <li>f_k = min + k * delta</li>
 * <li>f_0 = min</li>
 * <li>f_{N-1} = max</li>
 * </ul>
 * </p>
 * 
 * @author Andrew Tausz
 *
 */
public class IncreasingLinearConverter extends FiltrationConverter {
	/*
	 * f_k = min + delta * k
	 * f_0 = min
	 * f_{N-1} = max
	 * Therefore:
	 * delta = (max - min) / (delta - 1)
	 * k = (f_k - min) / delta
	 * 
	 */
	
	/**
	 * The number of divisions of the range [min, max)
	 */
	private final int numDivisions;
	
	/**
	 * The minimum filtration value, aka. min.
	 */
	private final double minFiltrationValue;
	
	/**
	 * The maximum filtration value, aka. max.
	 */
	private final double maxFiltrationValue;
	
	/**
	 * The granularity, or spacing between filtration values.
	 */
	private final double delta;
	
	/**
	 * This constructor initializes the object with the number of divisions in the range, and the
	 * minimum and maximum values.
	 * 
	 * @param numDivisions the number of divisions to use in the range
	 * @param minFiltrationValue the minimum filtration value
	 * @param maxFiltrationValue the maximum filtration value
	 */
	public IncreasingLinearConverter(int numDivisions, double minFiltrationValue, double maxFiltrationValue) {
		this.numDivisions = numDivisions;
		this.minFiltrationValue = minFiltrationValue;
		this.maxFiltrationValue = maxFiltrationValue;
		this.delta = (this.maxFiltrationValue - this.minFiltrationValue) / (double) (this.numDivisions);
	}
	
	/**
	 * This initializes the object with a default minimum filtration value of zero.
	 * 
	 * @param numDivisions the number of divisions in the range
	 * @param maxFiltrationValue the maximum filtration value
	 */
	public IncreasingLinearConverter(int numDivisions, double maxFiltrationValue) {
		this(numDivisions, 0, maxFiltrationValue);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationIndex(double)
	 */
	public int getFiltrationIndex(double filtrationValue) {
		if (this.delta == 0) {
			return 0;
		}
		return (int) ((filtrationValue - this.minFiltrationValue) / this.delta);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationValue(int)
	 */
	public double getFiltrationValue(int filtrationIndex) {
		return (this.minFiltrationValue + this.delta * filtrationIndex);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#computeInducedFiltrationValue(double, double)
	 */
	public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
		return Math.max(filtrationValue1, filtrationValue2);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getInitialFiltrationValue()
	 */
	public double getInitialFiltrationValue(){
		return this.minFiltrationValue;
	}
	
	/**
	 * @return the number of divisions
	 */
	public int getNumDivisions() {
		return numDivisions;
	}

	/**
	 * @return the minimum filtration value
	 */
	public double getMinFiltrationValue() {
		return minFiltrationValue;
	}

	/**
	 * @return the maximum filtration value
	 */
	public double getMaxFiltrationValue() {
		return maxFiltrationValue;
	}

	/**
	 * @return the spacing between the points
	 */
	public double getDelta() {
		return delta;
	}
}
