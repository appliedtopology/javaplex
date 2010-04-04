/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import edu.stanford.math.plex_plus.math.matrix.impl.sparse.DoubleSparseMatrix;
import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix;

/**
 * @author atausz
 *
 */
public class UndirectedWeightedListGraph implements AbstractWeightedGraph {
	private final DoubleAbstractMatrix matrix;
	private final int numVertices;
	
	public UndirectedWeightedListGraph(int numVertices) {
		this.numVertices = numVertices;
		this.matrix = new DoubleSparseMatrix(this.numVertices, this.numVertices);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractWeightedGraph#addEdge(int, int, double)
	 */
	@Override
	public void addEdge(int i, int j, double weight) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		this.matrix.set(x, y, weight);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractWeightedGraph#getWeight(int, int)
	 */
	@Override
	public double getWeight(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		return this.matrix.get(x, y);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		this.matrix.set(x, y, 1);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int i, int j) {
		return (this.getWeight(i, j) != 0);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumEdges()
	 */
	@Override
	public int getNumEdges() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumVertices()
	 */
	@Override
	public int getNumVertices() {
		return this.numVertices;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#removeEdge(int, int)
	 */
	@Override
	public void removeEdge(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		this.matrix.set(x, y, 0);
	}

}
