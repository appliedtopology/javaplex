/**
 * 
 */
package edu.stanford.math.plex_plus.graph_metric;

import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;
import edu.stanford.math.plex_plus.utility.Infinity;

/**
 * @author Andris
 *
 */
public class ShortestPathMetric implements GraphMetric {
	private final AbstractUndirectedGraph graph;
	private final int[][] pathLengths;
	private final int size;
	
	public ShortestPathMetric(AbstractUndirectedGraph graph) {
		this.graph = graph;
		this.size = graph.getNumVertices();
		this.pathLengths = ArrayUtility2.newIntMatrix(this.size, this.size);
		
		this.computeShortestPaths();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace#size()
	 */
	@Override
	public int size() {
		return this.size;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.interfaces.IntMetricSpace#distance(int, int)
	 */
	@Override
	public double distance(int i, int j) {
		return this.pathLengths[i][j];
	}

	/**
	 * This function computes all of the distances between pairs of vertices using
	 * the Floyd-Warshall algorithm.
	 */
	private void computeShortestPaths() {
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (graph.containsEdge(i, j)) {
					pathLengths[i][j] = 1;
				} else {
					pathLengths[i][j] = Infinity.Int.getPositiveInfinity();
				}
			}
		}
		
		for (int k = 0; k < this.size; k++) {
			for (int i = 0; i < this.size; i++) {
				for (int j = 0; j < this.size; j++) {
					pathLengths[i][j] = Math.min(pathLengths[i][j], pathLengths[i][k] + pathLengths[k][j]);
				}
			}
		}
	}
}
