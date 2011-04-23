package edu.stanford.math.plex4.homology.nonautogen;

import java.util.List;

public class RipsBootstrapper<T> {
	private final T[] points;
	private final List<int[]> indexSelections;
	
	public RipsBootstrapper(T[] points, List<int[]> indexSelections) {
		this.points = points;
		this.indexSelections = indexSelections;
	}
}
