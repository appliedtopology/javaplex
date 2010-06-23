/**
 * 
 */
package edu.stanford.math.plex_plus.graph_metric;

import cern.colt.matrix.DoubleMatrix2D;
import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.math.numerical_linear_algebra.LanczosAlgorithm;

/**
 * @author Andrew Tausz
 *
 */
public class SpectralEmbedding extends GraphEmbedding {

	public SpectralEmbedding(AbstractUndirectedGraph graph, int dimension) {
		super(graph, dimension);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph_metric.GraphEmbedding#computeEmbedding(int)
	 */
	@Override
	protected double[][] computeEmbedding(int dimension) {
		DoubleMatrix2D laplacian = SpectralGraphUtility.getLaplacianMatrix(this.graph);
		
		LanczosAlgorithm lanczos = new LanczosAlgorithm();
		
		lanczos.decompose(laplacian, dimension);
		DoubleMatrix2D eigenvectorMatrix = lanczos.getEigenvectors();
		
		int m = this.graph.getNumVertices();
		int n = dimension;
		
		double[][] embedding = new double[graph.getNumVertices()][dimension];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				embedding[i][j] = eigenvectorMatrix.getQuick(i, j);
			}
		}
		
		return embedding;
	}

}
