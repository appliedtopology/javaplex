package edu.stanford.math.plex4.bottleneck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.primitivelib.utility.Infinity;

public class BottleneckDistance {
	
	public static List<Interval<Double>> truncate(List<Interval<Double>> A, double minimumValue, double maximumValue) {
		List<Interval<Double>> result = new ArrayList<Interval<Double>>();
		
		double start, end;
		
		for (Interval<Double> interval: A) {
			if (interval.isLeftInfinite()) {
				start = minimumValue;
			} else {
				start = Math.max(minimumValue, interval.getStart());
			}
			
			if (interval.isRightInfinite()) {
				end = maximumValue;
			} else {
				end = Math.min(maximumValue, interval.getEnd());
			}
			
			result.add(Interval.makeFiniteRightOpenInterval(start, end));
		}
		
		return result;
	}
	
	public static List<Interval<Double>> filterLargest(List<Interval<Double>> A, int N) {
		if (A.size() <= N) {
			return A;
		}
		
		double[] diagonalDistances = new double[A.size()];
		
		for (int i = 0; i < A.size(); i++) {
			Interval<Double> interval = A.get(i);
			diagonalDistances[i] = BottleneckDistance.distanceToDiagonal(interval);
		}
		
		Arrays.sort(diagonalDistances);
		
		double minimumDiagonalDistance = diagonalDistances[diagonalDistances.length - N];
		
		List<Interval<Double>> result = new ArrayList<Interval<Double>>();
		
		for (int i = 0; i < A.size(); i++) {
			Interval<Double> interval = A.get(i);
			double distance = BottleneckDistance.distanceToDiagonal(interval);
			
			if (distance >= minimumDiagonalDistance) {
				result.add(interval);
			}
		}
		
		return result;
	}
	
	public static double computeBottleneckDistance(List<Interval<Double>> A, List<Interval<Double>> B) {
		
		int a = A.size();
		int b = B.size();
		
		int n = a + b;
		
		WeightedBipartiteGraph graph = new WeightedBipartiteGraph(n);
		
		double distance = 0;
		
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				distance = BottleneckDistance.distance(A.get(i), B.get(j));
				graph.addEdge(i, j, distance);
			}
		}
		
		for (int i = 0; i < a; i++) {
			distance = BottleneckDistance.distanceToDiagonal(A.get(i));
			graph.addEdge(i, b + i, distance);
		}
		
		for (int j = 0; j < b; j++) {
			distance = BottleneckDistance.distanceToDiagonal(B.get(j));
			graph.addEdge(a + j, j, distance);
		}
		
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				graph.addEdge(a + j, b + i, 0);
			}
		}
		
		double bottleneckDistance = graph.computePerfectMatchingThreshold();
		
		return bottleneckDistance;
	}
	
	static double distanceToDiagonal(Interval<Double> A) {
		if (!A.isLeftInfinite() && !A.isRightInfinite()) {
			return 0.5 * Math.abs(A.getEnd() - A.getStart());
		}
		
		return Infinity.Double.getPositiveInfinity();
	}
	
	static double distance(Interval<Double> A, Interval<Double> B) {
		if (!A.isLeftInfinite() && !B.isRightInfinite() && !A.isRightInfinite() && !B.isRightInfinite()) {
			double startDifference = Math.abs(A.getStart() - B.getStart());
			double endDifference = Math.abs(A.getEnd() - B.getEnd());
			return Math.max(startDifference, endDifference);
		}
		
		if (A.isLeftInfinite() && B.isLeftInfinite() && !A.isRightInfinite() && !B.isRightInfinite()) {
			return Math.abs(A.getEnd() - B.getEnd());
		}
		
		if (!A.isLeftInfinite() && !B.isLeftInfinite() && A.isRightInfinite() && B.isRightInfinite()) {
			return Math.abs(A.getStart() - B.getStart());
		}
		
		return Infinity.Double.getPositiveInfinity();
	}
}
