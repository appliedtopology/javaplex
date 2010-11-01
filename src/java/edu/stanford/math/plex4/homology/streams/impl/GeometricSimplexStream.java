/**
 * 
 */
package edu.stanford.math.plex4.homology.streams.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex4.embedding.GraphEmbedding;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.kd.KDEuclideanMetricSpace;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;

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
public class GeometricSimplexStream implements AbstractFilteredStream<Simplex>, AbstractObjectMetricSpace<double[]> {
	private final AbstractFilteredStream<Simplex> stream;
	private final AbstractObjectMetricSpace<double[]> metricSpace;
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.stream = stream;
		this.metricSpace = metricSpace;
	}
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, double[][] points) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(points);
		this.stream = stream;
		this.metricSpace = new KDEuclideanMetricSpace(points);
	}
	
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, GraphEmbedding embedding, int dimension) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(embedding);
		this.stream = stream;
		this.metricSpace = new KDEuclideanMetricSpace(embedding.computeEmbedding(this.construct1Skeleton(stream), dimension));
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
	
	public void finalizeStream() {
		this.stream.finalizeStream();
	}

	public int getFiltrationIndex(Simplex basisElement) {
		return this.stream.getFiltrationIndex(basisElement);
	}
	
	public double getFiltrationValue(Simplex basisElement) {
		return this.stream.getFiltrationValue(basisElement);
	}

	public boolean isFinalized() {
		return this.stream.isFinalized();
	}

	public Iterator<Simplex> iterator() {
		return this.stream.iterator();
	}

	public double[] getPoint(int index) {
		return this.metricSpace.getPoint(index);
	}

	public int size() {
		return this.metricSpace.size();
	}

	public double distance(int i, int j) {
		return this.metricSpace.distance(i, j);
	}
	
	public double distance(double[] a, double[] b) {
		return this.metricSpace.distance(a, b);
	}

	public Simplex[] getBoundary(Simplex simplex) {
		return simplex.getBoundaryArray();
	}

	public int[] getBoundaryCoefficients(Simplex simplex) {
		return simplex.getBoundaryCoefficients();
	}

	public int getDimension(Simplex element) {
		return this.stream.getDimension(element);
	}

	public double[][] getPoints() {
		return this.metricSpace.getPoints();
	}
}
