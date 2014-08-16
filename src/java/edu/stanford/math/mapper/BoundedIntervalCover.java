package edu.stanford.math.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.Interval;

public class BoundedIntervalCover implements Iterable<Interval<Double>> {
	private double min, max;
	private int n;
	private double overlap;
	private List<Interval<Double>> intervals = new ArrayList<Interval<Double>>();

	public BoundedIntervalCover(double min, double max, int n, double overlap) {
		this.min = min;
		this.max = max;
		this.n = n;
		this.overlap = overlap;
		
		this.initialize();
	}

	void initialize() {
		double width = (max - min) / ((double) n);
		double overhang = width * this.overlap / 2;
		for (int i = 0; i < n; i++) {
			double start = min + i * width;
			double end = start + width;
			start = Math.max(start - overhang, min);
			end = Math.min(end + overhang, max);
			this.intervals.add(Interval.makeFiniteClosedInterval(start, end));
		}
	}

	public Iterator<Interval<Double>> iterator() {
		return this.intervals.iterator();
	}
}
