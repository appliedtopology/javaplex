/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex_plus.embedding.GraphEmbedding;
import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.graph.UndirectedListGraph;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.set.hash.TIntHashSet;

/**
 * This class defines a geometric realization of a filtered simplicial
 * complex, ie. one in which the vertices are elements of Euclidean space.
 * A geometric simplex stream is defined to be a simplex stream along
 * with a mapping of the set of vertices to Euclidean space, which in this
 * case is an object of type FiniteMetricSpace<double[]>.
 * 
 * @author Andrew Tausz
 *
 */
public class GeometricSimplexStream implements AbstractFilteredStream<Simplex>, FiniteMetricSpace<double[]> {
	private final AbstractFilteredStream<Simplex> stream;
	private final FiniteMetricSpace<double[]> metricSpace;
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, FiniteMetricSpace<double[]> metricSpace) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.stream = stream;
		this.metricSpace = metricSpace;
	}
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, double[][] points) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(points);
		this.stream = stream;
		this.metricSpace = new EuclideanMetricSpace(points);
	}
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, GraphEmbedding embedding, int dimension) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(embedding);
		this.stream = stream;
		this.metricSpace = new EuclideanMetricSpace(embedding.computeEmbedding(this.construct1Skeleton(stream), dimension));
	}
	
	private AbstractUndirectedGraph construct1Skeleton(AbstractFilteredStream<Simplex> stream) {
		List<Simplex> skeleton = new ArrayList<Simplex>();
		int maxVertexIndex = 0;
		
		for (Simplex simplex: stream) {
			if (simplex.getDimension() == 0) {
				maxVertexIndex = Math.max(maxVertexIndex, simplex.getVertices()[0]);
			}else if (simplex.getDimension() == 1) {
				skeleton.add(simplex);
			}
		}
		
		AbstractUndirectedGraph graph = new UndirectedListGraph(maxVertexIndex + 1);
		
		for (Simplex edge: skeleton) {
			int[] vertices = edge.getVertices();
			graph.addEdge(vertices[0], vertices[1]);
		}
		
		return graph;
	}
	
	@Override
	public void finalizeStream() {
		this.stream.finalizeStream();
	}

	@Override
	public double getFiltrationValue(Simplex simplex) {
		return this.stream.getFiltrationValue(simplex);
	}

	@Override
	public boolean isFinalized() {
		return this.stream.isFinalized();
	}

	@Override
	public Iterator<Simplex> iterator() {
		return this.stream.iterator();
	}

	@Override
	public TIntHashSet getKNearestNeighbors(double[] queryPoint, int k) {
		return this.metricSpace.getKNearestNeighbors(queryPoint, k);
	}

	@Override
	public int getNearestPoint(double[] queryPoint) {
		return this.metricSpace.getNearestPoint(queryPoint);
	}

	@Override
	public TIntHashSet getNeighborhood(double[] queryPoint, double epsilon) {
		return this.metricSpace.getNeighborhood(queryPoint, epsilon);
	}

	@Override
	public double[] getPoint(int index) {
		return this.metricSpace.getPoint(index);
	}

	@Override
	public int size() {
		return this.metricSpace.size();
	}

	@Override
	public double distance(int i, int j) {
		return this.metricSpace.distance(i, j);
	}

	@Override
	public Simplex[] getBoundary(Simplex simplex) {
		return simplex.getBoundaryArray();
	}

	@Override
	public int[] getBoundaryCoefficients(Simplex simplex) {
		return simplex.getBoundaryCoefficients();
	}

	@Override
	public int getDimension(Simplex element) {
		return this.stream.getDimension(element);
	}
}
