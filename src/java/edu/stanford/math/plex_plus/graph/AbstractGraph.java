package edu.stanford.math.plex_plus.graph;


public interface AbstractGraph {
	
	public int getNumVertices();
	public int getNumEdges();
	
	public boolean containsEdge(int i, int j);
	public void addEdge(int i, int j);
	public void removeEdge(int i, int j);
}
