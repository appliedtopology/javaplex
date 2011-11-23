package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;

/**
 * Implements a Barabasi-Albert (BA) random graph model.
 * 
 * From Wikipedia: The Barabasi-Albert (BA) model is an algorithm for generating
 * random scale-free networks using a preferential attachment mechanism.
 * Scale-free networks are widely observed in natural and man-made systems,
 * including the Internet, the world wide web, citation networks, and some
 * social networks.
 * 
 * @author Tim Harrington
 * @date Feb 7, 2009
 * 
 */
public class BAGraph extends GraphInstanceGenerator {

	protected int nParam;
	protected int mParam;

	public BAGraph(int n, int m) {
		this.nParam = n;
		this.mParam = m;
	}

	/**
	 * This algorithm makes a power law graph with n nodes. First it makes a
	 * connected starter graph with m nodes. Then it adds new nodes with up to m
	 * edges to existing nodes. An edge to node i is added with probability
	 * 
	 * p_i = \frac{k_i}{\displaystyle\sum_j k_j},
	 * 
	 * where k_j is the degree of node j.
	 * 
	 * @param dataDimension
	 *            the size of the graph
	 * @param numDataPoints
	 *            the number of potential edges to add at each step where m<<n
	 */
	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(this.nParam);

		graph = this.makeStarterGraph(this.mParam, graph);

		for (int j = this.mParam; j < this.nParam; j++) { // j is the index of
															// the next node to
															// connect

			// add m edges from j to the existing graph
			for (int k = 0; k < this.mParam; k++) {
				// get the degree sequence of the first j-1 nodes
				int[] d = java.util.Arrays.copyOfRange(graph.getDegreeSequence(), 0, j);
				if (d.length != j)
					throw new IllegalStateException(); // sanity check

				// choose the next node to connect to
				double[] p = getProbabilityPartition(d);
				double x = RandomUtility.nextUniform();
				int i = whichSegmentHasX(p, x);

				// connect to that node
				graph.addEdge(j, i);
			}
		}
		return graph;
	}

	/**
	 * Returns the segment i such that x is in [p[i],p[i+1]]. The partition p
	 * has n+1 values representing n segments. Therefore the returned value will
	 * always be between 0 and n-1.
	 * 
	 * @param p
	 * @param x
	 * @return
	 */
	protected int whichSegmentHasX(double[] p, double x) {

		for (int i = 0; i < p.length - 1; i++) {
			if (x <= p[i + 1]) {
				return i;
			}
		}

		// if the code ever reaches this point then there is a bug
		throw new IllegalStateException();
	}

	/**
	 * Constructs a partition p of [0,1] such that p[i+1]-p[i] is proportional
	 * to the degree of node i in the list d. The return value p satisfies p[i]
	 * <= p[i+1], p[0]==0, and p[end] = 1.
	 * 
	 * @param d
	 * @return
	 */
	protected double[] getProbabilityPartition(int[] d) {
		double[] p = new double[d.length + 1];
		p[0] = 0d;

		/*
		 * Add 1 to each element of d so that nodes with degree zero have a
		 * chance at being used for an edge
		 */
		int[] dplus1 = IntArrayMath.scalarAdd(d, 1);
		double sum = IntArrayMath.sum(dplus1);

		/*
		 * Construct the partition with each segment proportional to the degree
		 * of the corresponding node.
		 */
		for (int i = 1; i <= d.length; i++) {
			double deg = d[i - 1];
			p[i] = p[i - 1] + deg / sum;
		}

		// correct rounding errors
		p[p.length - 1] = 1;

		return p;
	}

	/**
	 * Make a starter graph, in this case a line
	 * 
	 * TODO: what kind of starter graph??
	 * 
	 * @param numDataPoints
	 * @return degree sum of the starter graph
	 */
	protected AbstractUndirectedGraph makeStarterGraph(int size, AbstractUndirectedGraph graph) {
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (i == j)
					continue;
				graph.addEdge(j, i);
			}
		}
		return graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BAGraph(" + this.nParam + "," + this.mParam + ")";
	}

}
