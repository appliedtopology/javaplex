package edu.stanford.math.plex4.metric.landmark;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;

/**
 * <p>This abstract class defines the functionality of a landmark set within an arbitrary
 * finite metric space. A landmark set is used as an approximation to the whole metric space
 * for efficiency purposes. It also tends to denoise the dataset under investigation. For 
 * further information about landmark sets, please consult the paper "Topological estimation
 * using witness complexes", by Vin de Silva and Gunnar Carlsson.</p> 
 * 
 * <p>A class that derives from LandmarkSelector must implement the computeLandmarkSet function
 * which implements the computation of the landmark set.</p>
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying type of the metric space
 */
public abstract class LandmarkSelector<T> implements AbstractObjectMetricSpace<T> {
	
	/**
	 * This is the underlying metric space upon which the landmark set is build.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;
	
	/**
	 * This is the size of the landmark set
	 */
	protected final int landmarkSetSize;
	
	/**
	 * This array holds the mapping between the indices of the landmark points,
	 * and the indices within the metric space.
	 */
	protected final int[] indexMapping;
	
	/**
	 * This constructor initializes the landmark selector with a finite metric space,
	 * and a size parameter.
	 * 
	 * @param metricSpace the metric space to build the landmarks set in
	 * @param landmarkSetSize the size of the landmark set
	 */
	public LandmarkSelector(AbstractSearchableMetricSpace<T> metricSpace, int landmarkSetSize) {
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyLessThanOrEqual(landmarkSetSize, metricSpace.size());
		
		this.metricSpace = metricSpace;
		this.landmarkSetSize = landmarkSetSize;
		
		this.indexMapping = this.computeLandmarkSet();
	}

	/**
	 * This function returns the index of the i-th landmark point.
	 * 
	 * @param i the index within the set of landmark points
	 * @return the index of the i-th landmark point
	 */
	public int getLandmarkIndex(int i) {
		return this.indexMapping[i];
	}
	
	public int[] getLandmarkPoints() {
		return this.indexMapping;
	}
	
	public int size() {
		return this.landmarkSetSize;
	}

	public double distance(int i, int j) {
		return this.metricSpace.distance(this.indexMapping[i], this.indexMapping[j]);
	}
	
	public double distance(T a, T b) {
		return this.metricSpace.distance(a, b);
	}
	
	public T getPoint(int index) {
		return this.metricSpace.getPoint(this.getLandmarkIndex(index));
	}
	
	public AbstractSearchableMetricSpace<T> getUnderlyingMetricSpace() {
		return this.metricSpace;
	}
	
	/**
	 * This function constructs the set of landmark points. It must return an array of
	 * size landmarkSetSize, which contains the indices of the landmark points within
	 * the metric space.
	 * 
	 * @return an array of indices corresponding to the landmark points in the metric space
	 */
	protected abstract int[] computeLandmarkSet();
	
	public T[] getPoints() {
		throw new UnsupportedOperationException();
	}
}
