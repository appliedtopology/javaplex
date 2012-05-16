package edu.stanford.math.plex4.homology.barcodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.utility.Infinity;

public class BarcodeUtility {

	/**
	 * This function returns an array containing the set of endpoint of the
	 * intervals.
	 * 
	 * @param <G>
	 * @param collection
	 *            the barcode collection
	 * @param dimension
	 *            the dimension at which to get the endpoints
	 * @param skipInfiniteIntervals
	 *            boolean flag indicating whether to skip infinite intervals
	 * @return an n x 2 array of doubles containing the endpoints of the
	 *         intervals
	 */
	public static <G> double[][] getEndpoints(AnnotatedBarcodeCollection<Double, G> collection, int dimension, boolean skipInfiniteIntervals) {
		List<Interval<Double>> list = collection.getIntervalsAtDimension(dimension);
		Collections.sort(list);
		Collections.reverse(list);
		return getEndpoints(list, dimension, skipInfiniteIntervals);
	}

	/**
	 * This function returns an array containing the set of endpoint of the
	 * intervals.
	 * 
	 * @param <G>
	 * @param intervals
	 *            the list of intervals
	 * @param dimension
	 *            the dimension at which to get the endpoints
	 * @param skipInfiniteIntervals
	 *            boolean flag indicating whether to skip infinite intervals
	 * @return an n x 2 array of doubles containing the endpoints of the
	 *         intervals
	 */
	public static double[][] getEndpoints(List<Interval<Double>> intervals, int dimension, boolean skipInfiniteIntervals) {

		List<double[]> endpointList = new ArrayList<double[]>();

		for (Interval<Double> interval : intervals) {
			if (interval.isInfinite() && skipInfiniteIntervals) {
				continue;
			}

			double start, end;

			if (interval.isLeftInfinite() && interval.isRightInfinite()) {
				start = Double.NEGATIVE_INFINITY;
				end = Double.POSITIVE_INFINITY;
			} else if (interval.isLeftInfinite()) {
				start = Double.NEGATIVE_INFINITY;
				end = interval.getEnd();
			} else if (interval.isRightInfinite()) {
				start = interval.getStart();
				end = Double.POSITIVE_INFINITY;
			} else {
				start = interval.getStart();
				end = interval.getEnd();
			}

			endpointList.add(new double[] { start, end });
		}

		double[][] endpointArray = new double[endpointList.size()][];
		for (int i = 0; i < endpointList.size(); i++) {
			endpointArray[i] = endpointList.get(i);
		}

		return endpointArray;
	}

	public static List<Interval<Double>> getLongestBarcodes(List<Interval<Double>> intervals, int k) {
		if (k >= intervals.size()) {
			return intervals;
		}
		
		ArrayList<Interval<Double>> intervalsCopy = new ArrayList<Interval<Double>>(intervals);
		Collections.sort(intervalsCopy, IntervalLengthComparator.getInstance());
		Collections.reverse(intervalsCopy);

		ArrayList<Interval<Double>> result = new ArrayList<Interval<Double>>();

		for (int i = 0; i < k; i++) {
			result.add(intervalsCopy.get(i));
		}

		intervalsCopy = null;

		return result;
	}

	public static <G> List<ObjectObjectPair<Interval<Double>, G>> getLongestAnnotatedBarcodes(List<ObjectObjectPair<Interval<Double>, G>> intervals, int k) {

		if (k >= intervals.size()) {
			return intervals;
		}
		
		final IntervalLengthComparator intervalComparator = IntervalLengthComparator.getInstance();

		Comparator<ObjectObjectPair<Interval<Double>, G>> comparator = new Comparator<ObjectObjectPair<Interval<Double>, G>>() {

			public int compare(ObjectObjectPair<Interval<Double>, G> o1, ObjectObjectPair<Interval<Double>, G> o2) {
				return intervalComparator.compare(o1.getFirst(), o2.getFirst());
			}
		};

		ArrayList<ObjectObjectPair<Interval<Double>, G>> intervalsCopy = new ArrayList<ObjectObjectPair<Interval<Double>, G>>(intervals);
		Collections.sort(intervalsCopy, comparator);
		Collections.reverse(intervalsCopy);

		ArrayList<ObjectObjectPair<Interval<Double>, G>> result = new ArrayList<ObjectObjectPair<Interval<Double>, G>>();

		for (int i = 0; i < k; i++) {
			result.add(intervalsCopy.get(i));
		}

		intervalsCopy = null;

		return result;
	}
	
	public static double getLength(Interval<Double> interval) {
		if (interval.isInfinite()) {
			return Infinity.Double.getPositiveInfinity();
		}
		
		return (interval.getEnd() - interval.getStart());
	}
	
	/**
	 * Given a collection of intervals, this function returns those intervals with length greater than or equal to the minimum specified.
	 * 
	 * @param intervals the set of intervals
	 * @param minimumLength the minimum length
	 * @return those intervals longer than minimumLength
	 */
	public static List<Interval<Double>> filterIntervalsByMinimumLength(Iterable<Interval<Double>> intervals, double minimumLength) {
		ArrayList<Interval<Double>> filteredIntervals = new ArrayList<Interval<Double>>();
		
		for (Interval<Double> interval: intervals) {
			if (getLength(interval) >= minimumLength) {
				filteredIntervals.add(interval);
			}
		}
		
		return filteredIntervals;
	}
	
	/**
	 * Given a collection of intervals object pairs, this function returns those intervals with length greater than or equal to the minimum specified.
	 * 
	 * @param intervals the set of intervals
	 * @param minimumLength the minimum length
	 * @return those intervals longer than minimumLength
	 */
	public static <G> List<ObjectObjectPair<Interval<Double>, G>> filterIntervalPairsByMinimumLength(Iterable<ObjectObjectPair<Interval<Double>, G>> intervals, double minimumLength) {
		ArrayList<ObjectObjectPair<Interval<Double>, G>> result = new ArrayList<ObjectObjectPair<Interval<Double>, G>>();
		
		for (ObjectObjectPair<Interval<Double>, G> pair: intervals) {
			Interval<Double> interval = pair.getFirst();
			if (getLength(interval) >= minimumLength) {
				result.add(pair);
			}
		}
		
		return result;
	}
}
