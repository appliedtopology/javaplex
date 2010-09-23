package edu.stanford.math.plex4.math.metric.impl;

import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntHashSet;

public abstract class GenericFiniteMetricSpace<T> implements SearchableFiniteMetricSpace<T> {
	protected final T[] elements;
	
	GenericFiniteMetricSpace(T[] array) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyPositive(array.length);

		this.elements = array;
	}
	
	public T[] getPoints() {
		return this.elements;
	}
	
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k) {
		// TODO: complete
		return null;
	}
	
	public int getNearestPoint(T queryPoint) {
		ExceptionUtility.verifyNonNull(queryPoint);
		
		double minimumDistance = Double.MAX_VALUE;
		double currentDistance = 0;
		int nearestIndex = 0;
		int n = this.elements.length;
		
		for (int i = 0; i < n; i++) {
			currentDistance = this.distance(queryPoint, this.elements[i]);
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				nearestIndex = i;
			}
		}
		
		return nearestIndex;
	}

	public TIntHashSet getOpenNeighborhood(T queryPoint, double epsilon) {
		ExceptionUtility.verifyNonNull(queryPoint);
		ExceptionUtility.verifyNonNegative(epsilon);
		
		TIntHashSet neighborhood = new TIntHashSet();
		if (epsilon == 0) {
			return neighborhood;
		}
		
		int n = this.elements.length;
		
		for (int i = 0; i < n; i++) {
			if (this.distance(queryPoint, this.elements[i]) < epsilon) {
				neighborhood.add(i);
			}	
		}

		return neighborhood;
	}
	
	public TIntHashSet getClosedNeighborhood(T queryPoint, double epsilon) {
		ExceptionUtility.verifyNonNull(queryPoint);
		ExceptionUtility.verifyNonNegative(epsilon);
		
		TIntHashSet neighborhood = new TIntHashSet();
		if (epsilon == 0) {
			return neighborhood;
		}
		
		int n = this.elements.length;
		
		for (int i = 0; i < n; i++) {
			if (this.distance(queryPoint, this.elements[i]) <= epsilon) {
				neighborhood.add(i);
			}	
		}

		return neighborhood;
	}

	public int size() {
		return this.elements.length;
	}

	public abstract double distance(T a, T b);

	public double distance(int i, int j) {
		return this.distance(this.elements[i], this.elements[j]);
	}

	public T getPoint(int index) {
		ExceptionUtility.verifyIndex(this.elements.length, index);
		return this.elements[index];
	}
}
