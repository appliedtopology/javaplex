package edu.stanford.math.plex4.homology.barcodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedBarcodeCollection<T extends Comparable<T>, G> extends PersistenceInvariantDescriptor<Interval<T>, G> {
	protected boolean useLeftClosedDefault = true;
	protected boolean useRightClosedDefault = false;

	/**
	 * This function sets the property that determines whether the left endpoint
	 * of created intervals is closed or not.
	 * 
	 * @param value
	 */
	public void setLeftClosedDefault(boolean value) {
		this.useLeftClosedDefault = value;
	}

	/**
	 * This function sets the property that determines whether the right endpoint
	 * of created intervals is closed or not.
	 * 
	 * @param value
	 */
	public void setRightClosedDefault(boolean value) {
		this.useRightClosedDefault = value;
	}

	/**
	 * This function adds the specified finite interval (start, end} at the
	 * supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param end the ending point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addInterval(int dimension, T start, T end, G generatingCycle) {
		this.addInterval(dimension, Interval.makeInterval(start, end, useLeftClosedDefault, useRightClosedDefault, false, false), generatingCycle);
	}

	/**
	 * This function adds the specified semi-infinite interval {start, infinity}
	 * at the supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addRightInfiniteInterval(int dimension, T start, G generatingCycle) {
		this.addInterval(dimension, Interval.makeInterval(start, null, useLeftClosedDefault, useRightClosedDefault, false, true), generatingCycle);
	}

	/**
	 * This function adds the specified semi-infinite interval {-infinity, end}
	 * at the supplied dimension. The curly braces are meant to indicate that the
	 * closedness of the end points depends on the default state of the class.
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addLeftInfiniteInterval(int dimension, T end, G generatingCycle) {
		this.addInterval(dimension, Interval.makeInterval(null, end, useLeftClosedDefault, useRightClosedDefault, true, false), generatingCycle);
	}

	public AnnotatedBarcodeCollection<T, G> getInfiniteIntervals() {
		AnnotatedBarcodeCollection<T, G> result = new AnnotatedBarcodeCollection<T, G>();

		for (Integer dimension: this.intervals.keySet()) {
			List<Interval<T>> intervalList = this.intervals.get(dimension);
			List<G> generatorList = this.generators.get(dimension);

			for (int i = 0; i < intervalList.size(); i++) {
				Interval<T> interval = intervalList.get(i);
				G generator = generatorList.get(i);

				if (interval.isInfinite()) {
					result.addInterval(dimension, interval, generator);
				}
			}
		}

		return result;
	}

	public Map<Integer, Integer> getBettiNumbersMap(T point) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (Integer dimension: this.intervals.keySet()) {
			List<Interval<T>> intervalList = this.intervals.get(dimension);
			int count = 0;

			for (Interval<T> interval: intervalList) {
				if (interval.containsPoint(point)) {
					count++;
				}
			}

			if (count > 0) {
				result.put(dimension, count);
			}
		}

		return result;
	}

	public AnnotatedBarcodeCollection<T, G> filterByMaxDimension(int maxDimension) {
		AnnotatedBarcodeCollection<T, G> result = new AnnotatedBarcodeCollection<T, G>();
		result.useLeftClosedDefault = this.useLeftClosedDefault;
		result.useRightClosedDefault = this.useRightClosedDefault;
		
		for (Integer dimension: this.intervals.keySet()) {
			if (dimension > maxDimension) {
				continue;
			}

			List<Interval<T>> intervalList = this.intervals.get(dimension);
			List<G> generatorList = this.generators.get(dimension);

			for (int i = 0; i < intervalList.size(); i++) {
				Interval<T> interval = intervalList.get(i);
				G generator = generatorList.get(i);

				result.addInterval(dimension, interval, generator);
			}
		}

		return result;
	}
	
	public AnnotatedBarcodeCollection<T, G> filterPositiveMeasureIntervals() {
		AnnotatedBarcodeCollection<T, G> result = new AnnotatedBarcodeCollection<T, G>();
		result.useLeftClosedDefault = this.useLeftClosedDefault;
		result.useRightClosedDefault = this.useRightClosedDefault;
		
		for (Integer dimension: this.intervals.keySet()) {
			List<Interval<T>> intervalList = this.intervals.get(dimension);
			List<G> generatorList = this.generators.get(dimension);

			for (int i = 0; i < intervalList.size(); i++) {
				Interval<T> interval = intervalList.get(i);
				G generator = generatorList.get(i);
				
				if (interval.isInfinite()) {
					result.addInterval(dimension, interval, generator);
				} else {
					if (interval.getStart().compareTo(interval.getEnd()) < 0) {
						result.addInterval(dimension, interval, generator);
					}
				}
			}
		}

		return result;
	}
	
	public static <G> AnnotatedBarcodeCollection<Integer, G> filterEvenIntervals(AnnotatedBarcodeCollection<Integer, G> collection) {
		AnnotatedBarcodeCollection<Integer, G> result = new AnnotatedBarcodeCollection<Integer, G>();
		result.useLeftClosedDefault = collection.useLeftClosedDefault;
		result.useRightClosedDefault = collection.useRightClosedDefault;
		
		for (Integer dimension: collection.intervals.keySet()) {
			List<Interval<Integer>> intervalList = collection.intervals.get(dimension);
			List<G> generatorList = collection.generators.get(dimension);

			for (int i = 0; i < intervalList.size(); i++) {
				Interval<Integer> interval = intervalList.get(i);
				G generator = generatorList.get(i);
				
				if (interval.isLeftInfinite() && interval.isRightInfinite()) {
					result.addInterval(dimension, interval, generator);
				} else if (interval.isLeftInfinite()) {
					result.addInterval(dimension, Interval.makeInterval(null, interval.getEnd() / 2, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite()), generator);
				} else if (interval.isRightInfinite()) {
					result.addInterval(dimension, Interval.makeInterval(interval.getStart() / 2, null, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite()), generator);
				} else {
					if (interval.getStart().equals(interval.getEnd())) {
						// of form [x, x]
						Integer x = interval.getStart();
						
						if (x % 2 == 0) {
							result.addInterval(dimension, Interval.makeInterval(interval.getStart() / 2, interval.getEnd() / 2, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite()), generator);
						}
					} else {
						// of form [x, y]
						Integer x = interval.getStart();
						Integer y = interval.getEnd();
						
						if (x % 2 == 0) {
							// starts at even
							
							if (y % 2 == 1) {
								y = ((y - 1) / 2);
							} else {
								y = y / 2;
							}
							
							result.addInterval(dimension, Interval.makeInterval(x, y, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite()), generator);
						} else {
							// starts at odd
							
							if (y == x + 1) {
								// don't do anything
							} else {
								if (y % 2 == 1) {
									y = ((y - 1) / 2);
								} else {
									y = y / 2;
								}
								
								result.addInterval(dimension, Interval.makeInterval(x, y, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite()), generator);
							}
						}
					}
				}
			}
		}

		return result;
	}
}
