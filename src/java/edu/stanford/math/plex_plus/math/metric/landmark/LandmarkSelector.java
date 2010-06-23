package edu.stanford.math.plex_plus.math.metric.landmark;

import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;

/**
 * This abstract class defines the functionality of a landmark set within an arbitrary
 * finite metric space. A landmark set is used as an approximation to the whole metric space
 * for efficiency purposes. It also tends to denoise the dataset under investigation.
 * 
 * A class that derives from LandmarkSelector must implement the computeLandmarkSet function
 * which implements the computation of the landmark set.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying type of the metric space
 */
public abstract class LandmarkSelector<T> implements IntFiniteMetricSpace {
	protected final FiniteMetricSpace<T> metricSpace;
	protected final int landmarkSetSize;
	protected final int[] indexMapping;
	
	public LandmarkSelector(FiniteMetricSpace<T> metricSpace, int landmarkSetSize) {
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
	
	@Override
	public int size() {
		return this.landmarkSetSize;
	}

	@Override
	public double distance(int i, int j) {
		return this.metricSpace.distance(this.indexMapping[i], this.indexMapping[j]);
	}
	
	protected abstract int[] computeLandmarkSet();
}
