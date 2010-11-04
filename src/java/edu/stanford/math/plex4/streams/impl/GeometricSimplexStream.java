/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import java.util.Iterator;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;

/**
 * This class defines a geometric realization of a filtered simplicial
 * complex, ie. one in which the vertices are elements of Euclidean space.
 * A geometric simplex stream is defined to be a simplex stream along
 * with a mapping of the set of vertices to Euclidean space, which in this
 * case is an object of type AbstractObjectMetricSpace<double[]>.
 * 
 * @author Andrew Tausz
 *
 */
public class GeometricSimplexStream implements AbstractFilteredStream<Simplex>, AbstractObjectMetricSpace<double[]> {
	
	/**
	 * The underlying filtered simplicial complex.
	 */
	private final AbstractFilteredStream<Simplex> stream;
	
	/**
	 * The Euclidean metric consisting of the vertices of the complex.
	 */
	private final AbstractObjectMetricSpace<double[]> metricSpace;
	
	/**
	 * This constructor initializes the geometric complex from an abstract filtered complex and a metric space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param metricSpace the Euclidean metric space of vertices
	 */
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, AbstractObjectMetricSpace<double[]> metricSpace) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(metricSpace);
		this.stream = stream;
		this.metricSpace = metricSpace;
	}
	
	/**
	 * This constructor initializes the geometric complex from an abstract simplicial complex and double array
	 * whose rows consist of the Euclidean coordinates of the vertices.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param points the array of points
	 */
	public GeometricSimplexStream(AbstractFilteredStream<Simplex> stream, double[][] points) {
		ExceptionUtility.verifyNonNull(stream);
		ExceptionUtility.verifyNonNull(points);
		this.stream = stream;
		this.metricSpace = new EuclideanMetricSpace(points);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#finalizeStream()
	 */
	public void finalizeStream() {
		this.stream.finalizeStream();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getFiltrationIndex(java.lang.Object)
	 */
	public int getFiltrationIndex(Simplex basisElement) {
		return this.stream.getFiltrationIndex(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#isFinalized()
	 */
	public boolean isFinalized() {
		return this.stream.isFinalized();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Simplex> iterator() {
		return this.stream.iterator();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace#getPoint(int)
	 */
	public double[] getPoint(int index) {
		return this.metricSpace.getPoint(index);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace#size()
	 */
	public int size() {
		return this.metricSpace.size();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace#distance(int, int)
	 */
	public double distance(int i, int j) {
		return this.metricSpace.distance(i, j);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace#distance(java.lang.Object, java.lang.Object)
	 */
	public double distance(double[] a, double[] b) {
		return this.metricSpace.distance(a, b);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundary(java.lang.Object)
	 */
	public Simplex[] getBoundary(Simplex simplex) {
		return simplex.getBoundaryArray();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundaryCoefficients(java.lang.Object)
	 */
	public int[] getBoundaryCoefficients(Simplex simplex) {
		return simplex.getBoundaryCoefficients();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getDimension(java.lang.Object)
	 */
	public int getDimension(Simplex element) {
		return this.stream.getDimension(element);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace#getPoints()
	 */
	public double[][] getPoints() {
		return this.metricSpace.getPoints();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getSize()
	 */
	public int getSize() {
		return this.stream.getSize();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getMaximumFiltrationIndex()
	 */
	public int getMaximumFiltrationIndex() {
		return this.stream.getMaximumFiltrationIndex();
	}
}
