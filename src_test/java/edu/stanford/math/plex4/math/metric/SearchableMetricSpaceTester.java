package edu.stanford.math.plex4.math.metric;

import static org.junit.Assert.assertTrue;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

public class SearchableMetricSpaceTester {

	/**
	 * This function verifies that the the nearest neighbor query produces a point that 
	 * is indeed closest to the query point.
	 * 
	 * @param <M>
	 * @param metricSpace
	 */
	public static <M> void verifyNearestPoints(SearchableFiniteMetricSpace<M> metricSpace) {
		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);

			int nearestPointIndex = metricSpace.getNearestPoint(queryPoint);

			assertTrue("Nearest neighbor should not be equal to the query point.", nearestPointIndex != i);

			double nearestDistance = metricSpace.distance(i, nearestPointIndex);

			// make sure that all other points are not closer than the nearest point
			for (int j = 0; j < metricSpace.size(); j++) {
				if (i != j) {
					double distance = metricSpace.distance(i, j);
					assertTrue("Claimed nearest point is not the nearest.", nearestDistance <= distance);
				}
			}
		}
	}
	
	public static <M> void verifyNearestPoints(SearchableFiniteMetricSpace<M> metricSpace, M[] queryPoints) {
		for (M queryPoint: queryPoints) {
			verifyNearestPoint(metricSpace, queryPoint);
		}
	}
	
	public static <M> void verifyNearestPoint(SearchableFiniteMetricSpace<M> metricSpace, M queryPoint) {
		int nearestPointIndex = metricSpace.getNearestPoint(queryPoint);
		M nearestPoint = metricSpace.getPoint(nearestPointIndex);
		double nearestDistance = metricSpace.distance(queryPoint, nearestPoint);
		
		// make sure that all other points are not closer than the nearest point
		for (int j = 0; j < metricSpace.size(); j++) {
			double distance = metricSpace.distance(queryPoint, metricSpace.getPoint(j));
			assertTrue("Claimed nearest point is not the nearest.", nearestDistance <= distance);
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
	public static <M> void verifyNeighborhoods(SearchableFiniteMetricSpace<M> metricSpace, double epsilon) {

		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);

			TIntHashSet neighborhood = metricSpace.getNeighborhood(queryPoint, epsilon);

			// make sure that the points in the neighborhood satisfy d(i, j) < epsilon 
			for (TIntIterator iterator = neighborhood.iterator(); iterator.hasNext(); ) {
				int j = iterator.next();
				double distance = metricSpace.distance(i, j);

				assertTrue("Epsilon neighborhood of point contains point further than epsilon away.", distance < epsilon);
			}

			// make sure that points outside of the neighborhood satisfy d(i, j) >= epsilon
			for (int j = 0; j < metricSpace.size(); j++) {
				if (neighborhood.contains(j) || i ==j) {
					continue;
				}

				double distance = metricSpace.distance(i, j);

				assertTrue("Epsilon neighborhood of point does not contain valid point.", distance >= epsilon);
			}
		}
	}
	
	
}
