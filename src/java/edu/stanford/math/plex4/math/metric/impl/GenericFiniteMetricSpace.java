package edu.stanford.math.plex4.math.metric.impl;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.set.hash.TIntHashSet;

public abstract class GenericFiniteMetricSpace<T> implements SearchableFiniteMetricSpace<T> {
	protected final List<T> elements = new ArrayList<T>();
	
	GenericFiniteMetricSpace(T[] array) {
		for (T element: array) {
			this.elements.add(element);
		}
	}
	
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k) {
		// TODO: complete
		return null;
	}
	
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

	public int size() {
		return this.elements.size();
	}

	public abstract double distance(T a, T b);

	public double distance(int i, int j) {
		return this.distance(this.elements.get(i), this.elements.get(j));
	}

	public T getPoint(int index) {
		ExceptionUtility.verifyIndex(this.elements.size(), index);
		return this.elements.get(index);
	}
}
