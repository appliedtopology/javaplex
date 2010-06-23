package edu.stanford.math.plex_plus.math.metric.impl;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.set.hash.TIntHashSet;

public abstract class GenericFiniteMetricSpace<T> implements FiniteMetricSpace<T> {
	List<T> elements = new ArrayList<T>();
	
	@Override
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k) {
		// TODO: complete
		return null;
	}

	@Override
	public int getNearestPoint(T queryPoint) {
		ExceptionUtility.verifyNonNull(queryPoint);
		
		if (this.elements.isEmpty()) {
			throw new IllegalStateException();
		}
		
		double minimumDistance = Double.MAX_VALUE;
		double currentDistance = 0;
		int nearestIndex = 0;
		int n = this.elements.size();
		
		for (int i = 0; i < n; i++) {
			currentDistance = this.distance(queryPoint, this.elements.get(i));
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				nearestIndex = i;
			}
		}
		
		return nearestIndex;
	}

	@Override
	public TIntHashSet getNeighborhood(T queryPoint, double epsilon) {
		ExceptionUtility.verifyNonNull(queryPoint);
		ExceptionUtility.verifyNonNegative(epsilon);
		
		TIntHashSet neighborhood = new TIntHashSet();
		if (epsilon == 0) {
			return neighborhood;
		}
		
		int n = this.elements.size();
		
		for (int i = 0; i < n; i++) {
			if (this.distance(queryPoint, this.elements.get(i)) < epsilon) {
				neighborhood.add(i);
			}	
		}

		return neighborhood;
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	public abstract double distance(T a, T b);

	@Override
	public double distance(int i, int j) {
		return this.distance(this.elements.get(i), this.elements.get(j));
	}
	
	@Override
	public T getPoint(int index) {
		ExceptionUtility.verifyIndex(this.elements.size(), index);
		return this.elements.get(index);
	}
}
