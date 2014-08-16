package edu.stanford.math.mapper;

public class MapperSpecifier {
	double overlap = 0.2;
	int numIntervals = 40;
	int numHistogramBuckets = 20;
	
	public static MapperSpecifier create() {
		return new MapperSpecifier();
	}
	
	MapperSpecifier numIntervals(int numIntervals) {
		this.numIntervals = numIntervals;
		return this;
	}
	
	MapperSpecifier overlap(double overlap) {
		this.overlap = overlap;
		return this;
	}
	
	MapperSpecifier numHistogramBuckets(int numHistogramBuckets) {
		this.numHistogramBuckets = numHistogramBuckets;
		return this;
	}
}
