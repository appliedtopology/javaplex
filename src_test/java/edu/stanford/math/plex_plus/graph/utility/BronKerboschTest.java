package edu.stanford.math.plex_plus.graph.utility;

import edu.stanford.math.plex_plus.deprecated.BronKerboschAlgorithm;
import edu.stanford.math.plex_plus.graph.UndirectedListGraph;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class BronKerboschTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testGraph2();
	}

	/**
	 * "House graph" test
	 */
	public static void testGraph1() {
		UndirectedListGraph graph = new UndirectedListGraph(5);

		THashSet<TIntHashSet> cliques = BronKerboschAlgorithm.getAllMaximalCliques(graph);
		System.out.println(cliques.toString());

		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 0);
		graph.addEdge(0, 2);
		graph.addEdge(1, 3);
		graph.addEdge(0, 4);
		graph.addEdge(1, 4);

		cliques = BronKerboschAlgorithm.getAllMaximalCliques(graph);
		System.out.println(cliques.toString());
	}
	
	/**
	 * Complete graph / Erdos-Renyi graph
	 */
	public static void testGraph2() {
		int n = 100;
		double p = 0.8;
		
		UndirectedListGraph graph = new UndirectedListGraph(n);

		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (Math.random() > p) {
					graph.addEdge(i, j);
				}
			}
		}

		THashSet<TIntHashSet> cliques = BronKerboschAlgorithm.getAllMaximalCliques(graph);
		System.out.println(cliques.toString());
	}

}
