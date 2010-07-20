package edu.stanford.math.plex4.graph_metric;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;

public class SpectralGraphUtility {
	
	public static DoubleMatrix2D getLaplacianMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		DoubleMatrix2D laplacian = DoubleFactory2D.sparse.make(n, n);
		int[] degrees = new int[n];
		for (int i = 0; i < n; i++) {
			TIntSet set = graph.getLowerNeighbors(i);
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
