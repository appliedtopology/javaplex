package edu.stanford.math.plex4.math.metric.interfaces;

public interface FiniteMetricSpace<T> extends IntFiniteMetricSpace {
	public T getPoint(int index);
	public double distance(T a, T b);
	public T[] getPoints();
}
