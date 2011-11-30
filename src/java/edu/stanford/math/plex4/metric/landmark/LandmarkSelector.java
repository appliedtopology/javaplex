package edu.stanford.math.plex4.metric.landmark;

import java.util.List;

import edu.stanford.math.plex4.metric.impl.TruncatedPriorityQueue;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntHashSet;

/**
 * <p>
 * This abstract class defines the functionality of a landmark set within an
 * arbitrary finite metric space. A landmark set is used as an approximation to
 * the whole metric space for efficiency purposes. It also tends to denoise the
 * dataset under investigation. For further information about landmark sets,
 * please consult the paper "Topological estimation using witness complexes", by
 * Vin de Silva and Gunnar Carlsson.
 * </p>
 * 
 * <p>
 * A class that derives from LandmarkSelector must implement the
 * computeLandmarkSet function which implements the computation of the landmark
 * set.
 * </p>
 * 
 * @author Andrew Tausz
 * 
 * @param <T>
 *            the underlying type of the metric space
 */
public abstract class LandmarkSelector<T> implements AbstractSearchableMetricSpace<T> {

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
	 * This constructor initializes the landmark selector with a finite metric
	 * space, and a size parameter.
	 * 
	 * @param metricSpace
	 *            the metric space to build the landmarks set in
	 * @param landmarkSetSize
	 *            the size of the landmark set
	 */
	public LandmarkSelector(AbstractSearchableMetricSpace<T> metricSpace, int landmarkSetSize) {
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyLessThanOrEqual(landmarkSetSize, metricSpace.size());

		this.metricSpace = metricSpace;
		this.landmarkSetSize = landmarkSetSize;

		this.indexMapping = this.computeLandmarkSet();
	}

	public LandmarkSelector(AbstractSearchableMetricSpace<T> metricSpace, int[] indices) {
		ExceptionUtility.verifyNonNull(metricSpace);
		ExceptionUtility.verifyLessThanOrEqual(indices.length, metricSpace.size());

		this.metricSpace = metricSpace;
		this.landmarkSetSize = indices.length;

		this.indexMapping = indices;
	}

	/**
	 * This function returns the index of the i-th landmark point.
	 * 
	 * @param i
	 *            the index within the set of landmark points
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

	public T[] getPoints() {
		throw new UnsupportedOperationException();
	}

	/**
	 * This function constructs the set of landmark points. It must return an
	 * array of size landmarkSetSize, which contains the indices of the landmark
	 * points within the metric space.
	 * 
	 * @return an array of indices corresponding to the landmark points in the
	 *         metric space
	 */
	protected abstract int[] computeLandmarkSet();

	/**
	 * This function returns the maximum distance between points in the landmark
	 * selection and points not in the selection. In other words, it returns R =
	 * max_{x in X} d(x, L), where d(x, L) = min_{l in L} d(x, l).
	 * 
	 * @return the maximum distance between points in the landmark selection and
	 *         points not in it
	 */
	public double getMaxDistanceFromPointsToLandmarks() {
		int[] landmarkArray = this.getLandmarkPoints();
		TIntHashSet landmarkSet = toSet(this.getLandmarkPoints());

		double maxMinDistance = 0;

		for (int x = 0; x < this.getUnderlyingMetricSpace().size(); x++) {
			if (landmarkSet.contains(x)) {
				continue;
			}

			double minDistance = Infinity.Double.getPositiveInfinity();
			for (int l : landmarkArray) {
				double distance = this.getUnderlyingMetricSpace().distance(x, l);
				if (distance < minDistance) {
					minDistance = distance;
				}
			}

			if (minDistance > maxMinDistance) {
				maxMinDistance = minDistance;
			}
		}

		return maxMinDistance;
	}

	private static TIntHashSet toSet(int[] values) {
		TIntHashSet result = new TIntHashSet();

		for (int value : values) {
			result.add(value);
		}

		return result;
	}

	public TIntHashSet getClosedNeighborhood(T queryPoint, double epsilon) {
		TIntHashSet result = new TIntHashSet();
		for (int i = 0; i < this.landmarkSetSize; i++) {
			if (this.distance(this.getPoint(i), queryPoint) <= epsilon) {
				result.add(i);
			}
		}

		return result;
	}

	public TIntHashSet getOpenNeighborhood(T queryPoint, double epsilon) {
		TIntHashSet result = new TIntHashSet();
		for (int i = 0; i < this.landmarkSetSize; i++) {
			if (this.distance(this.getPoint(i), queryPoint) <= epsilon) {
				result.add(i);
			}
		}

		return result;
	}

	public TIntHashSet getKNearestNeighbors(T queryPoint, int k) {
		TruncatedPriorityQueue<Integer> tpq = new TruncatedPriorityQueue<Integer>(k);

		for (int i = 0; i < this.landmarkSetSize; i++) {
			T element = this.getPoint(i);
			
			if (element.equals(queryPoint)) {
				continue;
			}

			double distance = this.distance(queryPoint, element);
			tpq.insert(i, distance);
		}

		TIntHashSet result = new TIntHashSet();

		List<Integer> indices = tpq.getIndices();
		for (Integer index : indices) {
			result.add(index);
		}

		return result;
	}

	public int getNearestPointIndex(T queryPoint) {
		int minIndex = 0;
		double distance = 0, minDistance = 0;

		for (int i = 0; i < this.landmarkSetSize; i++) {
			distance = this.distance(this.getPoint(i), queryPoint);
			if (i == 0 || distance < minDistance) {
				minDistance = distance;
				minIndex = i;
			}
		}

		return minIndex;
	}
}
