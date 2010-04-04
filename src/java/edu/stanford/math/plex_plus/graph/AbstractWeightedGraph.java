package edu.stanford.math.plex_plus.graph;

public interface AbstractWeightedGraph extends AbstractGraph {
	public double getWeight(int i, int j);
	public void addEdge(int i, int j, double weight);
}
