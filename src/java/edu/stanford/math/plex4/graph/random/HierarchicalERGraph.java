/**
 * 
 */
package edu.stanford.math.plex4.graph.random;

/**
 * @author Tim Harrington
 * @date May 6, 2009
 * 
 */
public class HierarchicalERGraph extends BinaryHierarchicalGraph {

	protected int rootedGraphSize;
	protected int hgLeafCount;
	protected double erParameter;

	/**
	 * Note: n and m should each be a power of 2
	 * 
	 * @param n
	 *            - the number of hierarchical graph leaves (this parameter is
	 *            rounded up to the nearest power of 2)
	 * @param m
	 *            - the size of the ER graph rooted at each hierarchical graph
	 *            leaf (this parameter is rounded up to the nearest power of 2)
	 * @param maxProbability
	 * @param erParameter
	 */
	public HierarchicalERGraph(int n, int m, double maxProbability,
			double erParameter) {
		super(roundUpToPowerOf2(n) * roundUpToPowerOf2(m), true);
		this.rootedGraphSize = roundUpToPowerOf2(m);
		this.hgLeafCount = roundUpToPowerOf2(n);
		this.erParameter = erParameter;
		this.maxProbability = maxProbability;
	}

	protected static int roundUpToPowerOf2(int m) {
		double p = Math.ceil(Math.log(m) / Math.log(2));
		return (int) Math.pow(2, p);
	}

	/**
	 * We need one probability for each potential common ancestor depth
	 * d=1..k-1. They are constructed here.
	 * 
	 * The ER graphs that are rooted at a leaf in the hierarchical graph are
	 * treated as additional j levels of hierarchy. Therefore, the probability
	 * annotations for the last j levels is set to a constant to simulate the ER
	 * construction.
	 * 
	 * @param k
	 *            a value such that (2^k <= n <= (2^{k+1})-1)
	 * @param dampingFactor
	 *            a scaling factor in the open set (0,1)
	 * @return
	 */
	protected double[] getProbabilities(int k, double dampingFactor) {
		double[] probs = new double[k];

		// Get the number of levels that correspond to each rooted HR graph
		int rLevels = k - getTreeDepth(this.hgLeafCount);
		int hLevels = k - rLevels;

		/*
		 * The first hLevels positions in the array probs are used to store the
		 * probability annotations corresponding to the hierarchical graph
		 * leaves. The last rLevels (rooted levels) are used to store the
		 * Erdos-Renyi parameter.
		 */

		// The size is k since the possible depths are d=0,1,..,k-1
		double denom = 0;
		for (int i = 0; i < hLevels; i++) {
			denom = Math.exp(hLevels - i) / Math.exp(1);
			probs[i] = (1 / denom) * dampingFactor;
		}

		for (int j = 0; j < rLevels; j++) {
			probs[hLevels + j] = this.erParameter;
		}

		return probs;
	}

	protected int getERHierarchyLevelCount(int totalLevels) {
		return totalLevels - getTreeDepth(this.hgLeafCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HierarchicalER(" + this.hgLeafCount + ","
				+ this.rootedGraphSize + "," + this.maxProbability + ","
				+ this.erParameter + ")";
	}

	public double getErParameter() {
		return erParameter;
	}

	public int getHgLeafCount() {
		return hgLeafCount;
	}

	public int getRootedGraphSize() {
		return rootedGraphSize;
	}

}
