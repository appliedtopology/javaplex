package edu.stanford.math.plex4.graph.utility;

import edu.stanford.math.plex4.array_utility.ArrayCreation;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.Infinity;

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
		double[][] pathLengths = ArrayCreation.newDoubleMatrix(n, n);
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
}
