package edu.stanford.math.plex4.graph.utility;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

public class GraphUtility {
	
	/**
	 * This function computes all of the distances between pairs of vertices using
	 * the Floyd-Warshall algorithm.
	 *
	 * @param graph
	 * @return
	 */
	public static double[][] computeShortestPaths(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		double[][] pathLengths = DoubleArrayUtility.createMatrix(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (graph.containsEdge(i, j)) {
					pathLengths[i][j] = 1;
				} else {
					pathLengths[i][j] = Infinity.Double.getPositiveInfinity();
				}
			}
		}
		
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					pathLengths[i][j] = Math.min(pathLengths[i][j], pathLengths[i][k] + pathLengths[k][j]);
				}
			}
		}
		return pathLengths;
	}
	
	public static DoubleMatrix2D getLaplacianMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		DoubleMatrix2D laplacian = DoubleFactory2D.sparse.make(n, n);
		int[] degrees = new int[n];
		for (int i = 0; i < n; i++) {
			TIntHashSet set = graph.getLowerNeighbors(i);
			for (TIntIterator iterator = set.iterator(); iterator.hasNext(); ) {
				int j = iterator.next();
				laplacian.setQuick(i, j, -1);
				laplacian.setQuick(j, i, -1);
				degrees[i]++;
				degrees[j]++;
			}
		}
		
		for (int i = 0; i < n; i++) {
			laplacian.setQuick(i, i, degrees[i]);
		}
		
		return laplacian;
	}
}
