package edu.stanford.math.plex4.metric.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.utility.ArrayUtility;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.utility.Infinity;

/**
 * This class contains various functions for dealing with metric spaces.
 * 
 * @author Andrew Tausz
 *
 */
public class MetricUtility {
	
	/**
	 * This function estimates the diameter of the supplied metric space. The
	 * diameter is defined as the smallest number r such that d(x, y) <= r for all
	 * x, y in the space. The estimate is obtained by randomly choosing pairs
	 * (i, j), and taking the maximum distance over all pairs. 
	 * 
	 * @param metricSpace the metric space to estimate the diameter of
	 * @param trials the number of random pairs to choose
	 * @return an estimate of the diameter of the metric space
	 */
	public static double estimateDiameter(AbstractIntMetricSpace metricSpace, int trials) {
		double diameterEstimate = 0;
		int n = metricSpace.size();
		
		for (int trial = 0; trial < trials; trial++) {
			int i = RandomUtility.nextUniformInt(0, n - 1);
			int j = RandomUtility.nextUniformIntExcluded(0, n - 1, i);
			double distance = metricSpace.distance(i, j);
			diameterEstimate = Math.max(distance, diameterEstimate);
		}
		
		return diameterEstimate * (((double) trials + 1) / ((double) trials));
	}
	
	/**
	 * This function performs randomized sampling to find the estimated diameter of the given
	 * metric space. It uses a default number of samples equal to 100.
	 * 
	 * @param metricSpace the metric space to estimate the diameter of
	 * @return an estimate of the diameter of the metric space
	 */
	public static double estimateDiameter(AbstractIntMetricSpace metricSpace) {
		return estimateDiameter(metricSpace, Math.min(metricSpace.size(), 100));
	}
	
	/**
	 * This function computes the coordinate-wise maxima of a given Euclidean metric space.
	 * 
	 * @param metricSpace the metric space to compute the maxima for
	 * @return the componentwise minima of the elements of the metric space
	 */
	public static double[] computeMaxima(AbstractObjectMetricSpace<double[]> metricSpace) {
		int dimension = metricSpace.getPoint(0).length;
		int numPoints = metricSpace.size();
		double[] maxima = new double[dimension];
		for (int j = 0; j < dimension; j++) {
			maxima[j] = Infinity.Double.getNegativeInfinity();
		}
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				maxima[j] = Math.max(maxima[j], metricSpace.getPoint(i)[j]);
			}
		}
		return maxima;
	}
	
	/**
	 * This function computes the coordinate-wise minima of a given Euclidean metric space.
	 * 
	 * @param metricSpace the metric space to compute the minima for
	 * @return the componentwise minima of the elements of the metric space
	 */
	public static double[] computeMinima(AbstractObjectMetricSpace<double[]> metricSpace) {
		int dimension = metricSpace.getPoint(0).length;
		int numPoints = metricSpace.size();
		double[] minima = new double[dimension];
		for (int j = 0; j < dimension; j++) {
			minima[j] = Infinity.Double.getPositiveInfinity();
		}
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				minima[j] = Math.min(minima[j], metricSpace.getPoint(i)[j]);
			}
		}
		return minima;
	}
	
	/**
	 * This function computes the centroid of the set of points within the metric space.
	 * 
	 * @param metricSpace the metric space to compute the centroid of
	 * @return the centroid of the given metric space
	 */
	public static double[] computeCentroid(AbstractObjectMetricSpace<double[]> metricSpace) {
		int dimension = metricSpace.getPoint(0).length;
		int numPoints = metricSpace.size();
		double[] means = new double[dimension];
		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimension; j++) {
				means[j] += metricSpace.getPoint(i)[j];
			}
		}
		
		for (int j = 0; j < dimension; j++) {
			means[j] /= numPoints;
		}
		return means;
	}

	public static double[] getAllDistances(AbstractIntMetricSpace metricSpace, int index) {
		int n = metricSpace.size();
		double[] distances = new double[n];
		
		for (int i = 0; i < n; i++) {
			distances[i] = metricSpace.distance(i, index);
		}
		
		return distances;
	}
	
	public static <T> double[] getKthNearestNeighborDistances(AbstractSearchableMetricSpace<T> metricSpace, int k) {
		int n = metricSpace.size();
		
		double[] neighborDistances = new double[n];
		
		for (int i = 0; i < n; i++) {
			double[] distances = getAllDistances(metricSpace, i);
			Arrays.sort(distances);
			neighborDistances[i] = distances[k];
		}
		
		return neighborDistances;
	}
	
	public static <T> int[] filterByNeighborDistance(AbstractSearchableMetricSpace<T> metricSpace, int k, double proportion) {
		int n = metricSpace.size();
		
		double[] neighborDistances = getKthNearestNeighborDistances(metricSpace, k);
		
		double[] sortedDistances = ArrayUtility.copyOf(neighborDistances, neighborDistances.length);
		Arrays.sort(sortedDistances);
		
		int cutoffIndex = (int) proportion * neighborDistances.length;
		
		if (cutoffIndex >= neighborDistances.length) {
			cutoffIndex = neighborDistances.length - 1;
		}
		
		if (cutoffIndex < 0) {
			cutoffIndex = 0;
		}
		
		double cutoffDistance = sortedDistances[cutoffIndex];
		
		List<Integer> filteredList = new ArrayList<Integer>();
		
		for (int i = 0; i < n; i++) {
			if (neighborDistances[i] <= cutoffDistance) {
				filteredList.add(i);
			}
		}
		
		int[] indices = new int[filteredList.size()];
		
		int j = 0;
		for (Integer index: filteredList) {
			indices[j] = index;
			j++;
		}
		
		return indices;
	}
}
