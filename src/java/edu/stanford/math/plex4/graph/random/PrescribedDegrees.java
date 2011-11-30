package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.autogen.array.IntArrayManipulation;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;

/**
 * Implements a random prescribed degree graph by the algorithm presented in
 * J.Blitstein and P.Diaconis
 * 
 * @author Santiago Akle
 * @author Tim Harrington
 * 
 */
public class PrescribedDegrees extends GraphInstanceGenerator {

	private static final long serialVersionUID = -5243083558152110213L;
	protected int[] degreeSequence;
	protected int numNodes;

	public PrescribedDegrees(int[] degreeSequence) {
		// sort the degree sequence
		int[] ds = IntArrayManipulation.sortDescending(degreeSequence);

		// check if the degree sequence is graphical
		if (!isGraphical(ds)) {
			throw new IllegalArgumentException("Infeasible degree sequence");
		}

		this.degreeSequence = ds;
		this.numNodes = this.degreeSequence.length;
	}

	/**
	 * Implements "Sequential Algorithm For Random Graph with Given Degrees"
	 * from "A SEQUENTIAL IMPORTANCE SAMPLING ALGORITHM FOR GENERATING RANDOM
	 * GRAPHS WITH PRESCRIBED DEGREES" By Joseph Blitzstein and Persi Diaconis
	 * 
	 * @param argd
	 */
	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(this.numNodes);

		// copy the array so we can change it
		int[] d = java.util.Arrays.copyOf(this.degreeSequence,
				this.degreeSequence.length);

		// Initialize variables
		double[] p;
		double x;
		int[] candidateList;
		int node0 = 0, node1 = 0, i = 0;

		// STEP 2
		while (IntArrayMath.max(d) > 0) { // while there are still edges to add

			// STEP 3
			node0 = minPositivePosition(d);
			//System.out.println("NEW OUTER LOOP ITERATION (node0=" + node0 + ")");

			while (d[node0] > 0) { // while node0 needs a higher degree in the
									// graph

				// STEP 4
				candidateList = getCandidateList(d, node0, graph);
				//System.out.println("candidate list: " + ArrayUtility.toMatlabString(candidateList));

				// STEP 5
				p = getProbabilityPartition(candidateList);
				x = RandomUtility.nextUniform();
				i = whichSegmentHasX(p, x);
				node1 = candidateList[i];

				// STEP 6
				//System.out.println("adding edge: (" + node0 + "," + node1 + ")");
				graph.addEdge(node0, node1);

				d = multiMinus(d, node0, node1);
				//System.out.println("updated degree sequence: " + ArrayUtility.toMatlabString(d));
			}
		}

		return graph;
	}

	/**
	 * Checks the Erdos-Gallai criteria for a particular value of k.
	 * 
	 * @param d
	 * @param k
	 * @return
	 */
	protected static boolean erdosGallaiCriteria(int[] d, int k) {
		int n = d.length;

		int leftSum = 0;
		for (int i = 1; i <= k; i++) {
			leftSum += d[i - 1];
		}

		int rightSum = k * (k - 1);
		for (int i = k + 1; i <= n; i++) {
			rightSum += Math.min(k, d[i - 1]);
		}

		return (leftSum <= rightSum);
	}

	/**
	 * Determines if a degree sequence is realizable using the Erdos-Gallai
	 * Criteria
	 * 
	 * @param degreeSequence
	 * @return True if there exists at least one graph with this degree
	 *         sequence.
	 */
	public static boolean isGraphical(int[] d) {
		// check that all entries are nonnegative
		for (int j = 0; j < d.length; j++) {
			if (d[j] < 0)
				return false;
		}

		// check that sum_{i} d[i] = 0 mod 2
		int sum = 0;
		for (int j = 0; j < d.length; j++) {
			sum += d[j];
		}
		if (sum % 2 != 0) {
			return false;
		}

		// check the Erdos-Gallai criteria
		for (int k = 1; k <= d.length; k++) {
			if (!erdosGallaiCriteria(d, k))
				return false;
		}

		return true;
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
	 * Computes the set of j!=i such that multiminus(d,i,j) is graphical and
	 * (i,j) is not already an edge.
	 * 
	 * @param d
	 * @param i
	 * @return
	 */
	protected int[] getCandidateList(int[] d, int i, AbstractUndirectedGraph graph) {
		int m = -1;
		int[] list = new int[d.length];
		for (int k = 0; k < d.length; k++) {
			if (k == i)
				continue;
			if (isGraphical(IntArrayManipulation.sortDescending(multiMinus(d, i, k)))) {
				if (!graph.containsEdge(i, k)) {
					m++;
					list[m] = k;
				}
			}
		}
		int[] retval = java.util.Arrays.copyOfRange(list, 0, m + 1);
		return retval;
	}

	/**
	 * Constructs a partition p of [0,1] such that p[i+1]-p[i] is proportional
	 * to the degree of node i in the list d. The return value p satisfies p[i]
	 * <= p[i+1], p[0]==0, and p[end] = 1.
	 * 
	 * @param d
	 *            degree sequence
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
	 * Returns the array ds with 1 subtracted from the i-th element and 1
	 * subtracted from the j-th element.
	 * 
	 * @param ds
	 * @param i
	 * @param j
	 * @return
	 */
	protected int[] multiMinus(int[] ds, int i, int j) {
		int[] retval = java.util.Arrays.copyOfRange(ds, 0, ds.length);
		retval[i] -= 1;
		retval[j] -= 1;
		return retval;
	}

	/**
	 * Returns the first index of the smallest positive element or -1 if there
	 * are no positive entries
	 * 
	 * @param arr
	 * @return
	 */
	public static int minPositivePosition(int[] arr) {
		int i = -1;
		for (int j = 1; j < arr.length; j++) {
			if (((arr[j] > 0) && i == -1)
					|| (i >= 0 && ((arr[j] < arr[i])) && arr[j] > 0)) {
				i = j;
			}
		}
		return i;
	}

	@Override
	public String toString() {
		return "PrescribedDegreeGraph";
	}
}
