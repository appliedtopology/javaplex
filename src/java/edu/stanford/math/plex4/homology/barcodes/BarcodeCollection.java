package edu.stanford.math.plex4.homology.barcodes;

import java.util.List;

public class BarcodeCollection<T extends Comparable<T>> extends AnnotatedBarcodeCollection<T, Object> {

	/**
	 * This function adds the specified finite interval (start, end} at the
	 * supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param end the ending point of the interval
	 */
	public void addInterval(int dimension, T start, T end) {
		this.addInterval(dimension, Interval.makeInterval(start, end, useLeftClosedDefault, useRightClosedDefault, false, false), null);
	}

	/**
	 * This function adds the specified semi-infinite interval {start, infinity}
	 * at the supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addRightInfiniteInterval(int dimension, T start) {
		this.addInterval(dimension, Interval.makeInterval(start, null, useLeftClosedDefault, useRightClosedDefault, false, true), null);
	}

	/**
	 * This function adds the specified semi-infinite interval {-infinity, end}
	 * at the supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 */
	public void addLeftInfiniteInterval(int dimension, T end) {
		this.addInterval(dimension, Interval.makeInterval(null, end, useLeftClosedDefault, useRightClosedDefault, true, false), null);
	}
	
	public void addInterval(int dimension, Interval<T> interval) {
		this.addInterval(dimension, interval, null);
	}
	
	public static <T extends Comparable<T>, G> BarcodeCollection<T> forgetGeneratorType(AnnotatedBarcodeCollection<T, G> annotatedBarcodeCollection) {	
		BarcodeCollection<T> barcodeCollection = new BarcodeCollection<T>();
		barcodeCollection.setLeftClosedDefault(annotatedBarcodeCollection.useLeftClosedDefault);
		barcodeCollection.setRightClosedDefault(annotatedBarcodeCollection.useRightClosedDefault);
		
		for (Integer dimension: annotatedBarcodeCollection.intervals.keySet()) {
			List<Interval<T>> intervalList = annotatedBarcodeCollection.intervals.get(dimension);

			for (Interval<T> interval: intervalList) {
				barcodeCollection.addInterval(dimension, interval);
			}
		}
		
		return barcodeCollection;
	}
}
