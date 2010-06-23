package edu.stanford.math.plex_plus.graph_metric;

import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import gnu.trove.set.hash.TIntHashSet;

public abstract class GraphEmbedding implements FiniteMetricSpace<double[]> {
	protected final EuclideanMetricSpace euclideanMetricSpace;
	protected final AbstractUndirectedGraph graph;
	
	public GraphEmbedding(AbstractUndirectedGraph graph, int dimension) {
		this.euclideanMetricSpace = new EuclideanMetricSpace(this.computeEmbedding(dimension));
		this.graph = graph;
	}

	protected abstract double[][] computeEmbedding(int dimension);
	
	public double[][] getPoints() {
		return this.euclideanMetricSpace.getPoints();
	}
	
	public EuclideanMetricSpace getEuclideanMetricSpace() {
		return this.euclideanMetricSpace;
	}
	
	@Override
	public TIntHashSet getKNearestNeighbors(double[] queryPoint, int k) {
		return this.euclideanMetricSpace.getKNearestNeighbors(queryPoint, k);
	}

	@Override
	public int getNearestPoint(double[] queryPoint) {
		return this.euclideanMetricSpace.getNearestPoint(queryPoint);
	}

	@Override
	public TIntHashSet getNeighborhood(double[] queryPoint, double epsilon) {
		return this.euclideanMetricSpace.getNeighborhood(queryPoint, epsilon);
	}

	@Override
	public int size() {
		return this.euclideanMetricSpace.size();
	}

	public double distance(double[] a, double[] b) {
		return this.euclideanMetricSpace.distance(a, b);
	}

	@Override
	public double[] getPoint(int index) {
		return this.euclideanMetricSpace.getPoint(index);
	}

	@Override
	public double distance(int i, int j) {
		return this.euclideanMetricSpace.distance(i, j);
	}
}
