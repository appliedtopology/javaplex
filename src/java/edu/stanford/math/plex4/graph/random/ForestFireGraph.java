package edu.stanford.math.plex4.graph.random;

import java.util.Random;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Distributions;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import gnu.trove.TIntArrayList;

/**
 * This class creates a random graph generated from the forest fire model. This
 * is described on page 50 of "Community Structure in Large Networks", by
 * Mahoney, et al. See http://arxiv.org/abs/0810.1355
 * 
 * @author Andrew Tausz
 * @date February 7, 2009
 */
public class ForestFireGraph extends GraphInstanceGenerator {

	private static final long serialVersionUID = -872184123268309649L;
	protected double forwardBurningProbability;
	protected double backwardBurningProbability;
	protected RandomEngine randomEngine = AbstractDistribution
			.makeDefaultGenerator();
	protected Uniform uniformGenerator = new Uniform(this.randomEngine);
	protected int nodeCount;

	public ForestFireGraph(int n, double forwardBurningProbability,
			double backwardBurningProbability) {
		this.nodeCount = n;
		this.forwardBurningProbability = forwardBurningProbability;
		this.backwardBurningProbability = backwardBurningProbability;
	}

	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(this.nodeCount);
		
		boolean[] nodeAdded = new boolean[this.nodeCount];
		int currentNode = 0;

		for (int t = 0; t < this.nodeCount; t++) {
			// select a node not in the graph
			while (nodeAdded[currentNode] == true) {
				currentNode++;
			}
			// if we are in the first step, just go to the next iteration
			if (t == 0) {
				nodeAdded[currentNode] = true;
				continue;
			}
			// add a connection between the new node and a random node in the
			// existing graph
			int v = currentNode;
			int w = uniformGenerator.nextIntFromTo(0, t - 1);
			graph.addEdge(v, w);
			nodeAdded[currentNode] = true;
			graph = this.burnNodes(v, w, t, graph);
		}

		return graph;
	}

	private AbstractUndirectedGraph burnNodes(int v, int w, int t, AbstractUndirectedGraph graph) {
		double px = this.forwardBurningProbability
				/ (1 - this.forwardBurningProbability);
		double py = this.backwardBurningProbability
				/ (1 - this.backwardBurningProbability);
		int x = Distributions.nextGeometric(px, this.randomEngine);
		int y = Distributions.nextGeometric(py, this.randomEngine);
		x = Math.min(x, graph.getDegree(w));
		y = Math.min(y, graph.getDegree(w));
		int[] neighborsSet = graph.getNeighbors(w);
		TIntArrayList neighbors = new TIntArrayList();
		neighbors.add(neighborsSet);

		int count = 0;
		neighbors.shuffle(new Random());
		int neighbor;

		for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
			neighbor = neighbors.get(neighborIndex);
		
			if (!graph.containsEdge(v, neighbor) && (neighbor != v)) {
				graph.addEdge(v, neighbor);
				// this.BurnNodes(v, neighbor, t);
			}
			count++;
			if (count >= x) break;
		}
		return graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForestFire(" + this.nodeCount + ","
				+ this.forwardBurningProbability + ","
				+ this.backwardBurningProbability + ")";
	}

}
