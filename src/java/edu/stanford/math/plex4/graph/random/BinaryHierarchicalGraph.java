package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * Makes a random hierarchical graph by considering points P = {1,2,...,n} and
 * partitions C_{i,j}, where j is the granularity. C_{i,j+1} refines C_{i,j}.
 * The probability of an edge between p_1 and p_2 is a function of the largest
 * value of j such that there exists C_{i,j} with p_1,p_2 in C_{i,j}.
 * 
 * This is a first attempt approximation to see how PLEX characterizes graphs
 * with structure like this.
 * 
 * @author Tim Harrington
 * @date Feb 8, 2009
 * 
 */
public class BinaryHierarchicalGraph extends GraphInstanceGenerator {

	private static final long serialVersionUID = 830228704317022605L;

	/**
	 * Stores the probabilities that govern edge creation at each hierarchy
	 * level.
	 */
	double[] probabilities;

	protected int numNodes;
	protected int k; // the k that satisfies [2^k <= n < 2^{k+1}]
	protected double maxProbability;

	// set to true to turn on debugging messages
	protected boolean debugFlag = false;

	/**
	 * @param n
	 *            the number of nodes
	 */
	public BinaryHierarchicalGraph(int n) {
		this.numNodes = n;
		this.maxProbability = 1;
	}

	/**
	 * @param n
	 *            the number of nodes in the graph
	 * @param maxProbability
	 *            the maximum edge probability
	 */
	public BinaryHierarchicalGraph(int n, double maxProbability) {
		this.numNodes = n;
		this.maxProbability = maxProbability;
	}

	/**
	 * Creates a HierarchicalGraph without any edges. Convenience method for use
	 * by classes that extend HierarchicalGraph
	 * 
	 * @param n
	 *            the number of nodes
	 * @param dummy
	 *            a dummy variable used to force this constructor to be used
	 */
	protected BinaryHierarchicalGraph(int n, boolean dummy) {
		this.numNodes = n;
		this.maxProbability = 1;
	}

	public double[] getProbabilities() {
		return probabilities;
	}

	@Override
	public AbstractUndirectedGraph generate() {
		return this.construct(this.maxProbability);
	}

	/**
	 * Construct the Hierarchical graph.
	 * 
	 * As an approximation, this algorithm considers a binary tree with n nodes,
	 * labeled 1,2,..,n in a BFS. For example,
	 * 
	 * 1 / \ 3 2 / \ / \ 7 6 5 4 ...
	 * 
	 * Only the leaves of the tree are associated with nodes. To build a binary
	 * tree with n leaves it takes 2^m nodes, where m=floor(ln(n)/ln(2)).
	 * 
	 * The probability of edge (a,b) existing is a function of the depth of the
	 * first common ancestor in the tree. For example, if D = maxDepth then
	 * 
	 * i,j-depth probability of (i,j) in E 1 p=[1/D]^q 2 p=[2/D]^q 3 p=[3/D]^q
	 * ... D-1 p=[(D-1)/D]^q D p=[D/D]^q
	 * 
	 */
	protected AbstractUndirectedGraph construct(double maxProbability) {

		int n = this.numNodes;

		/*
		 * We want to construct the smallest binary tree of size 2^d-1 such that
		 * there are at least n leaves. That is, we need to find the k that
		 * satisfies [2^k <= n < 2^{k+1}]. For example,
		 * 
		 * 1 k=0 if n=1 / \ 3 2 k=1 if n=2 / \ / \ 7 6 5 4 k=2 if n=3,..,4
		 * .............. k=3 if n=5,..,8 ................ k=4 if n=9,..,16
		 */

		int k = getTreeDepth(n);
		double[] probabilities;
		// Construct one probability for each common ancestor depth d=1..k-1.
		probabilities = getProbabilities(k, maxProbability);
		// debug(DoubleArrayReaderWriter.toMatlabString(probabilities));
		// Get the index range i=n0,n0+1,...,n1-1,n1 of the leaves of the
		// binary tree that correspond to the nodes we are interested in.
		int n0 = (int) Math.pow(2d, k);
		int n1 = n0 + n; // get last index value in last row
		debug("The range of leaf nodes is i=" + n0 + ",...," + n1);

		this.probabilities = probabilities;
		// Construct the edges
		return constructEdges(n0, n1, k, probabilities);
	}

	/**
	 * We need one probability for each potential common ancestor depth
	 * d=1..k-1. They are constructed here.
	 * 
	 * @param k
	 *            a value such that (2^k <= n <= (2^{k+1})-1)
	 * @param dampingFactor
	 *            a scaling factor in the open set (0,1)
	 * @return
	 */
	protected double[] getProbabilities(int k, double dampingFactor) {
		// The size is k since the possible depths are d=0,1,..,k-1
		double[] probs = new double[k];
		double denom = k;
		for (int d = 0; d < k; d++) {
			denom = Math.exp(k - d) / Math.exp(1);
			probs[d] = (1 / denom) * dampingFactor;
		}
		return probs;
	}

	/**
	 * Construct the edges of the graph using probabilities in the array probs.
	 * 
	 * @param n0
	 *            the starting index of the binary tree leaves
	 * @param n1
	 *            the ending index of the binary tree leaves
	 * @param k
	 *            a value such that [2^k <= n < 2^{k+1}]
	 * @param probs
	 *            an array of probabilities for each common ancestor depth
	 */
	protected AbstractUndirectedGraph constructEdges(int n0, int n1, int k, double[] probs) {
		AbstractUndirectedGraph graph = this.initializeGraph(numNodes);

		// Construct a binary tree to compute common ancestors.
		int size = ((int) Math.pow(2, k + 1)) - 1;
		BinaryTree bt = new BinaryTree(size);

		// Construct the edges
		double p;
		int d;
		for (int i = n0; i < n1; i++) {
			for (int j = i + 1; j < n1; j++) {
				d = bt.commonAncestorDepth(i, j);
				debug("(" + i + "," + j + ") common depth is " + d);
				p = probs[d];
				if (p == 0)
					continue;
				if (p == 1 || RandomUtility.nextBernoulli(p) == 1) {
					graph.addEdge(i - n0, j - n0);
				}
			}
		}

		return graph;
	}

	/**
	 * We want to construct the smallest binary tree of size 2^{d+1}-1 (d>0)
	 * such that there are at least n leaves. That is, we need to find k that
	 * satisfies 2^k <= n < 2^{k+1} To do this we solve for k and let d=ceil(k).
	 * 
	 * @return the value of k that satisfies [2^k <= n < 2^{k+1}].
	 */
	protected int getTreeDepth(int n) {
		// solve for n = 2^{k+1} - 2^k
		double k = Math.log(n) / Math.log(2);
		int depth = (int) Math.ceil(k);
		debug("Tree depth is " + depth + ".");
		return depth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Hierarchical(" + this.numNodes + ")";
	}

	protected void debug(String msg) {
		if (debugFlag) {
			System.out.println(msg);
		}
	}

}
