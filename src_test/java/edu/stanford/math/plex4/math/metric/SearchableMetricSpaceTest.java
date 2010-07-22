package edu.stanford.math.plex4.math.metric;

import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.impl.KDEuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.utility.MetricUtility;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

public class SearchableMetricSpaceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * This function verifies that the the nearest neighbor query produces a point that 
	 * is indeed closest to the query point.
	 * 
	 * @param <M>
	 * @param metricSpace
	 */
	private <M> void verifyNearestPoints(SearchableFiniteMetricSpace<M> metricSpace) {
		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);

			int nearestPointIndex = metricSpace.getNearestPoint(queryPoint);

			if (nearestPointIndex == i) {
				throw new IllegalStateException("Nearest neighbor should not be equal to the query point.");
			}

			double nearestDistance = metricSpace.distance(i, nearestPointIndex);

			// make sure that all other points are not closer than the nearest point
			for (int j = 0; j < metricSpace.size(); j++) {
				if (i != j) {
					double distance = metricSpace.distance(i, j);
					if (distance < nearestDistance) {
						throw new IllegalStateException("Point claimed to be nearest is not nearest.");
					}
				}
			}
		}
	}

	/**
	 * This function verifies that the epsilon neighborhood around each point in a searchable
	 * finite metric space is indeed an epsilon neighborhood. 
	 * 
	 * @param <M>
	 * @param metricSpace
	 * @param epsilon
	 */
	private <M> void verifyNeighborhoods(SearchableFiniteMetricSpace<M> metricSpace, double epsilon) {

		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);

			TIntHashSet neighborhood = metricSpace.getNeighborhood(queryPoint, epsilon);

			if (neighborhood.contains(i)) {
				throw new IllegalStateException("Epsilon neighborhood of a point should not contain the query point.");
			}

			// make sure that the points in the neighborhood satisfy d(i, j) < epsilon 
			for (TIntIterator iterator = neighborhood.iterator(); iterator.hasNext(); ) {
				int j = iterator.next();
				double distance = metricSpace.distance(i, j);

				if (distance >= epsilon) {
					throw new IllegalStateException("Epsilon neighborhood of point contains point further than epsilon away.");
				}
			}

			// make sure that points outside of the neighborhood satisfy d(i, j) >= epsilon
			for (int j = 0; j < metricSpace.size(); j++) {
				if (neighborhood.contains(j) || i ==j) {
					continue;
				}

				double distance = metricSpace.distance(i, j);

				if (distance < epsilon) {
					throw new IllegalStateException("Epsilon neighborhood of point does not contain valid point.");
				}
			}
		}
	}
	
	
}
