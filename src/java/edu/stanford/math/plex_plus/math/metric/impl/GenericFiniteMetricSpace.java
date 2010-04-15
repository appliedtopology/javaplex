package edu.stanford.math.plex_plus.math.metric.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex_plus.math.metric.interfaces.GenericAbstractFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public abstract class GenericFiniteMetricSpace<T> implements GenericAbstractFiniteMetricSpace<T> {
	List<T> elements = new ArrayList<T>();
	
	@Override
	public Set<T> getKNearestNeighbors(T queryPoint, int k) {
		// TODO: complete
		return null;
	}

	@Override
	public T getNearestPoint(T queryPoint) {
		ExceptionUtility.verifyNonNull(queryPoint);
		
		if (this.elements.isEmpty()) {
			return null;
		}
		
		double minimumDistance = Double.MAX_VALUE;
		double currentDistance = 0;
		T nearestPoint = null;
		for (T element : elements) {
			currentDistance = this.distance(queryPoint, element);
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				nearestPoint = element;
			}
		}
		
		return nearestPoint;
	}

	@Override
	public Set<T> getNeighborhood(T queryPoint, double epsilon) {
		ExceptionUtility.verifyNonNull(queryPoint);
		ExceptionUtility.verifyNonNegative(epsilon);
		
		Set<T> neighborhood = new HashSet<T>();
		if (epsilon == 0) {
			return neighborhood;
		}
		for (T element : elements) {
			if (this.distance(queryPoint, element) < epsilon) {
				neighborhood.add(element);
			}
		}
		return neighborhood;
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public abstract double distance(T a, T b);

}
