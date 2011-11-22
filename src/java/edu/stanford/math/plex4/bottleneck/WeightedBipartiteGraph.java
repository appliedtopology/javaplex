package edu.stanford.math.plex4.bottleneck;

import gnu.trove.THashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WeightedBipartiteGraph {
	class WeightedEdge {
		public int source;
		public int destination;
		public double weight;
	}

	private final Set<WeightedEdge> edges = new THashSet<WeightedEdge>();
	private final List<Double> weights = new ArrayList<Double>();

	private final int partitionSize;
	
	public WeightedBipartiteGraph(int partitionSize) {
		this.partitionSize = partitionSize;
	}
	
	public void addEdge(int source, int destination, double weight) {
		WeightedEdge edge = new WeightedEdge();
		edge.source = source;
		edge.destination = destination;
		edge.weight = weight;

		this.edges.add(edge);
		this.weights.add(weight);
	}

	public WeightedBipartiteGraph filterByMaximum(double maximumWeight) {
		WeightedBipartiteGraph graph = new WeightedBipartiteGraph(this.partitionSize);

		for (WeightedEdge edge : this.edges) {
			if (edge.weight <= maximumWeight) {
				graph.edges.add(edge);
				graph.weights.add(edge.weight);
			}
		}

		return graph;
	}

	public int computeMaximumCardinalityMatching() {
		FlowNetwork network = new FlowNetwork();
		
		for (WeightedEdge edge : this.edges) {
			int normalizedSourceIndex = edge.source;
			int normalizedDestinationIndex = this.partitionSize + edge.destination;
			network.addEdge(normalizedSourceIndex, normalizedDestinationIndex, 1);
		}
		
		int masterSourceIndex = -1;
		int masterSinkIndex = -2;
		
		for (int i = 0; i < this.partitionSize; i++) {
			network.addEdge(masterSourceIndex, i, 1);
			network.addEdge(this.partitionSize + i, masterSinkIndex, 1);
		}
		
		return network.maxFlow(masterSourceIndex, masterSinkIndex);
	}
	
	public boolean hasPerfectMatching() {
		int maximumMatchingCardinality = this.computeMaximumCardinalityMatching();
		return (maximumMatchingCardinality == this.partitionSize);
	}
	
	/**
	 * This function computes the smallest value, v, that when the current
	 * bipartite graph is filtered by v, a perfect matching exists.
	 * 
	 * If no perfect matching exists for all filtration values, it returns -1.
	 * 
	 * @return
	 */
	public double computePerfectMatchingThreshold() {
		Collections.sort(this.weights);
		
		if (!this.hasPerfectMatching()) {
			return -1;
		}
		
		// minimumValidIndex stores the index for which there is a known perfect matching
		int minimumValidIndex = this.weights.size() - 1;
		
		// check to see if there is a perfect matching with the minimum weight
		if (this.filterByMaximum(this.weights.get(0)).hasPerfectMatching()) {
			return this.weights.get(0);
		}
		
		int maximumInvalidIndex = 0;
		
		int currentIndex = 0;
		double currentWeight = 0;
		
		while (minimumValidIndex > maximumInvalidIndex + 1) {
			currentIndex = (maximumInvalidIndex + minimumValidIndex) / 2;
			currentWeight = this.weights.get(currentIndex);
			if (this.filterByMaximum(currentWeight).hasPerfectMatching()) {
				minimumValidIndex = currentIndex;
			} else {
				maximumInvalidIndex = currentIndex;
			}
		}
		
		return this.weights.get(minimumValidIndex);
	}
}
