package edu.stanford.math.plex4.graph.random;

import java.text.DecimalFormat;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * Implements the Erdos-Renyi G(n,p) model.
 * 
 * From Wikipedia: In the G(n, p) model, a graph is thought to be constructed by
 * connecting nodes randomly. Each edge is included in the graph with
 * probability p, with the presence or absence of any two distinct edges in the
 * graph being independent.
 * 
 * @author Tim Harrington
 * @date Dec 22, 2008
 * 
 */
public class ErdosRenyiGraph extends GraphInstanceGenerator {
	protected int n;
	protected double p;

	/**
	 * @param n
	 * @param k
	 */
	public ErdosRenyiGraph(int n, double p) {
		this.n = n;
		this.p = p;

	}

	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// don't make loops
				if (i == j)
					continue;
				// add edge with probability p
				if (RandomUtility.nextBernoulli(p) == 1) {
					graph.addEdge(i, j);
				}
			}
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
		DecimalFormat df = new DecimalFormat("#.###");
		return "ErdosRenyi(" + n + "," + df.format(p) + ")";
	}

}
