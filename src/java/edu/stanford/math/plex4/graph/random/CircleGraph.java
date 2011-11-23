package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * Class that represents a graph with a circle "shape"
 * 
 * @author Tim Harrington
 * @date Dec 20, 2008
 */
public class CircleGraph extends GraphInstanceGenerator {
	protected int numNodes; // each node is connected to k neighbours
	protected int numNeighbors; // probability of rewiring each edge
	protected double rewiringProbability;
	protected int numRewired;

	public CircleGraph(int numNodes, int numNeighbors,
			double rewiringProbability) {
		this.numNodes = numNodes;
		this.numNeighbors = numNeighbors - numNeighbors % 2;
		this.rewiringProbability = rewiringProbability;
	}

	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(numNodes);
		return this.generateWithRewiring(graph);
	}

	/**
	 * @param dataDimension
	 * @param p
	 * @param k
	 */
	protected AbstractUndirectedGraph generateWithRewiring(AbstractUndirectedGraph graph) {
		int n = this.numNodes;

		for (int i = 0; i < this.numNodes; i++) {
			for (int j = 0; j < this.numNeighbors / 2; j++) {
				if (RandomUtility.nextBernoulli(this.rewiringProbability) == 1) { // rewire
																				// the
																				// connection
					this.numRewired += 1;
					int randNode = RandomUtility.nextUniformInt(0, n - 1);
					while (randNode == i) { // don't allow randNode to equal i
						randNode = RandomUtility.nextUniformInt(0, n - 1);
					}
					graph.addEdge(i, randNode);
				} else { // don't rewire the connection
					// check if we need to wrap the indices around
					if (i + j + 1 > n - 1) { // we need to
						graph.addEdge(i, i + j + 1 - n);
					} else { // we don't need to
						graph.addEdge(i, i + j + 1);
					}
				}
			}
		}
		return graph;
	}

	
	
	@Override
	public String toString() {
		return new String("CircleGraph(" + this.numNodes + ", " + this.numNeighbors + ", " + this.rewiringProbability + ")");
	}

	/**
	 * @param dataDimension
	 * @param k
	 */
	/*
	 * protected void generateWithoutRewiring(int n, int k) { for (int i=0; i<n;
	 * i++) { for (int j=0; j<(k-k%2)/2; j++) { // check if we need to wrap the
	 * indices around if (i+j+1>n-1) { // we need to graph.addEdge(i,i+j+1-n); }
	 * else { // we don't need to graph.addEdge(i,i+j+1); } } } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.stanford.cme.smallworld.ArtificialNetwork#generateNeighbors()
	 */
	/*
	 * @Override public void generateNeighbors() { int n = this.getNodeCount();
	 * double p = this.getRewireProb(); int k = this.getNeighborCount(); if
	 * (Math.min(p,1-p) != 0) { this.generateWithRewiring(n, p, k); } else {
	 * this.generateWithoutRewiring(n, k); } }
	 */
	public String correctBettiSequence() {
		return "BN{1, 1}";
	}
}
