package edu.stanford.math.plex4.metric.impl;

import java.util.List;

import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import gnu.trove.TIntHashSet;

/**
 * This class implements the AbstractSearchableMetricSpace interface over type T. It stores the
 * elements of the metric space as an array.  
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public abstract class ObjectSearchableFiniteMetricSpace<T> implements AbstractSearchableMetricSpace<T> {
	
	/**
	 * This array stores the elements in the metric space
	 */
	protected final T[] elements;
	
	/**
	 * This constructor initializes the class with an array of elements.
	 * 
	 * @param array the array of elements that will be the points in the metric space
	 */
	public ObjectSearchableFiniteMetricSpace(T[] array) {
		this.elements = array;
	}
	
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k) {
		TruncatedPriorityQueue<Integer> tpq = new TruncatedPriorityQueue<Integer>(k);
		
		for (int i = 0; i < elements.length; i++) {
			T element = elements[i];
			if (element.equals(queryPoint)) {
				continue;
			}
			
			double distance = this.distance(queryPoint, element);
			tpq.insert(i, distance);
		}
		
		TIntHashSet result = new TIntHashSet();
		
		List<Integer> indices = tpq.getIndices();
		for (Integer index: indices) {
			result.add(index);
		}
		
		return result;
	}

	public int getNearestPointIndex(T queryPoint) {
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
		TIntHashSet neighborhood = new TIntHashSet();
		
		int n = this.elements.length;
		
		for (int i = 0; i < n; i++) {
			if (this.distance(queryPoint, this.elements[i]) <= epsilon) {
				neighborhood.add(i);
			}	
		}

		return neighborhood;
	}
	
	public abstract double distance(T a, T b);

	public T getPoint(int index) {
		return this.elements[index];
	}

	public T[] getPoints() {
		return this.elements;
	}

	public double distance(int i, int j) {
		return this.distance(this.elements[i], this.elements[j]);
	}

	public int size() {
		return this.elements.length;
	}

}
