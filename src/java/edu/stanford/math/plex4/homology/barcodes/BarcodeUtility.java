package edu.stanford.math.plex4.homology.barcodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.math.primitivelib.utility.Infinity;

public class BarcodeUtility {

	/**
	 * This function returns an array containing the set of endpoint of the intervals.
	 * 
	 * @param <G>
	 * @param collection the barcode collection
	 * @param dimension the dimension at which to get the endpoints
	 * @param skipInfiniteIntervals boolean flag indicating whether to skip infinite intervals
	 * @return an n x 2 array of doubles containing the endpoints of the intervals
	 */
	public static <G> double[][] getEndpoints(AnnotatedBarcodeCollection<Double, G> collection, int dimension, boolean skipInfiniteIntervals) {
		List<Interval<Double>> list = collection.getIntervalsAtDimension(dimension);
		Collections.sort(list);
		Collections.reverse(list);
		return getEndpoints(list, dimension, skipInfiniteIntervals);
	}

	/**
	 * This function returns an array containing the set of endpoint of the intervals.
	 * 
	 * @param <G>
	 * @param intervals the list of intervals
	 * @param dimension the dimension at which to get the endpoints
	 * @param skipInfiniteIntervals boolean flag indicating whether to skip infinite intervals
	 * @return an n x 2 array of doubles containing the endpoints of the intervals
	 */
	public static <G> double[][] getEndpoints(List<Interval<Double>> intervals, int dimension, boolean skipInfiniteIntervals) {

		List<double[]> endpointList = new ArrayList<double[]>();

		for (Interval<Double> interval: intervals) {
			if (interval.isInfinite() && skipInfiniteIntervals) {
				continue;
			}

			double start, end;

			if (interval.isLeftInfinite() && interval.isRightInfinite()) {
				start = Infinity.Double.getNegativeInfinity();
				end = Infinity.Double.getPositiveInfinity();
			} else if (interval.isLeftInfinite()) {
				start = Infinity.Double.getNegativeInfinity();
				end = interval.getEnd();
			} else if (interval.isRightInfinite()) {
				start = interval.getStart();
				end = Infinity.Double.getPositiveInfinity();
			} else {
				start = interval.getStart();
				end = interval.getEnd();
			}

			endpointList.add(new double[]{start, end});
		}

		double[][] endpointArray = new double[endpointList.size()][];
		for (int i = 0; i < endpointList.size(); i++) {
			endpointArray[i] = endpointList.get(i);
		}

		return endpointArray;
	}
}
