package edu.stanford.math.plex4.metric;

import static org.junit.Assert.assertTrue;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

/**
 * This class contains functions for testing the validity of the
 * subclasses of AbstractSearchableMetricSpace. It verifies that the
 * claimed nearest points are indeed the nearest, etc.
 *  
 * @author Andrew Tausz
 */
public class SearchableMetricSpaceTester {
	private static final int K_MAX = 4;
	
	/**
	 * This function verifies that the claimed nearest point to the query point is indeed the nearest point.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 * @param queryPoint the query point
	 */
	public static <M> void verifyNearestPoint(AbstractSearchableMetricSpace<M> metricSpace, M queryPoint) {
		int nearestPointIndex = metricSpace.getNearestPointIndex(queryPoint);
		M nearestPoint = metricSpace.getPoint(nearestPointIndex);
		double nearestDistance = metricSpace.distance(queryPoint, nearestPoint);
		
		// make sure that all other points are not closer than the nearest point
		for (int j = 0; j < metricSpace.size(); j++) {
			if (queryPoint.equals(metricSpace.getPoint(j))) {
				continue;
			}
			double distance = metricSpace.distance(queryPoint, metricSpace.getPoint(j));
			assertTrue("Claimed nearest point is not the nearest.", nearestDistance <= distance);
		}
	}
	
	/**
	 * This function verifies that the k-nearest neighborhood returned is indeed the correct neighborhood.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 * @param queryPoint the query point
	 */
	public static <M> void verifyKNearestPoint(AbstractSearchableMetricSpace<M> metricSpace, M queryPoint) {
		int k = Math.min(metricSpace.size(), K_MAX);
		TIntHashSet kNearestNeighbors = metricSpace.getKNearestNeighbors(queryPoint, k);
		
		// make sure that k points were returned
		assertTrue("k-Nearest Neighborhood does not contain k points.", kNearestNeighbors.size() == k);
		
		// make sure that non k-nearest points are not closer than k-nearest points
		double max_k_nn_distance = 0;
		for (TIntIterator iterator = kNearestNeighbors.iterator(); iterator.hasNext(); ) {
			int index = iterator.next();
			max_k_nn_distance = Math.max(max_k_nn_distance, metricSpace.distance(queryPoint, metricSpace.getPoint(index)));
		}
		
		for (int j = 0; j < metricSpace.size(); j++) {
			if (!kNearestNeighbors.contains(j)) {
				// make sure that distance is greater than or equal to max_k_nn_distance
				double distance = metricSpace.distance(queryPoint, metricSpace.getPoint(j));
				
				if (max_k_nn_distance > distance) {
					System.out.println("stop");
				}
				
				assertTrue("k-Nearest Neighborhood does not contain near point.", max_k_nn_distance <= distance);
			}
		}
	}

	/**
	 * This function performs the verifyNearestPoint test using each point in the metric space as a query point.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 */
	public static <M> void verifyNearestPoints(AbstractSearchableMetricSpace<M> metricSpace) {
		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);
			verifyNearestPoint(metricSpace, queryPoint);
		}
	}
	
	/**
	 * This function performs the verifyKNearestPoint test using each point in the metric space as a query point.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 */
	public static <M> void verifyKNearestPoints(AbstractSearchableMetricSpace<M> metricSpace) {
		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);
			verifyKNearestPoint(metricSpace, queryPoint);
		}
	}
	
	/**
	 * This function performs the nearest point verification for each point in the given array.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 * @param queryPoints the set of query points
	 */
	public static <M> void verifyNearestPoints(AbstractSearchableMetricSpace<M> metricSpace, M[] queryPoints) {
		for (M queryPoint: queryPoints) {
			verifyNearestPoint(metricSpace, queryPoint);
		}
	}
	
	/**
	 * This function performs the k-nearest point verification for each point in the given array.
	 * 
	 * @param <M>
	 * @param metricSpace the metric space to test
	 * @param queryPoints the set of query points
	 */
	public static <M> void verifyKNearestPoints(AbstractSearchableMetricSpace<M> metricSpace, M[] queryPoints) {
		for (M queryPoint: queryPoints) {
			verifyKNearestPoint(metricSpace, queryPoint);
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
	public static <M> void verifyNeighborhoods(AbstractSearchableMetricSpace<M> metricSpace, double epsilon) {

		for (int i = 0; i < metricSpace.size(); i++) {
			M queryPoint = metricSpace.getPoint(i);

			TIntHashSet neighborhood = metricSpace.getOpenNeighborhood(queryPoint, epsilon);

			// make sure that the points in the neighborhood satisfy d(i, j) < epsilon 
			for (TIntIterator iterator = neighborhood.iterator(); iterator.hasNext(); ) {
				int j = iterator.next();
				double distance = metricSpace.distance(i, j);

				assertTrue("Epsilon neighborhood of point contains point further than epsilon away.", distance < epsilon);
			}

			// make sure that points outside of the neighborhood satisfy d(i, j) >= epsilon
			for (int j = 0; j < metricSpace.size(); j++) {
				if (neighborhood.contains(j) || i == j) {
					continue;
				}

				double distance = metricSpace.distance(i, j);

				assertTrue("Epsilon neighborhood of point does not contain valid point.", distance >= epsilon);
			}
		}
	}
}
