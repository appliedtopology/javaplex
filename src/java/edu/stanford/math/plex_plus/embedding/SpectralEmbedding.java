/**
 * 
 */
package edu.stanford.math.plex_plus.embedding;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.graph_metric.SpectralGraphUtility;


/**
 * @author Andrew Tausz
 *
 */
public class SpectralEmbedding implements GraphEmbedding {
	private static final SpectralEmbedding instance = new SpectralEmbedding();
	
	/**
	 * Prevent instantiation.
	 */
	private SpectralEmbedding() {};
	
	public static SpectralEmbedding getInstance() {
		return instance;
	}
	
	@Override
	public double[][] computeEmbedding(AbstractUndirectedGraph graph, int dimension) {
		DoubleMatrix2D laplacian = SpectralGraphUtility.getLaplacianMatrix(graph);
		
		EigenvalueDecomposition ed = new EigenvalueDecomposition(laplacian);
		
		DoubleMatrix2D eigenvectorMatrix = ed.getV();
		
		int m = graph.getNumVertices();
		int n = dimension;
		
		double[][] embedding = new double[m][];
		
		for (int i = 0; i < m; i++) {
			embedding[i] = new double[n];
			for (int j = 0; j < n; j++) {
				embedding[i][j] = eigenvectorMatrix.getQuick(i, j);
			}
		}
		
		return embedding;
	}

}
