package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;

/**
 * @author Tim Harrington
 * @date Apr 1, 2009
 * 
 */
public class BinaryTreeGraph extends GraphInstanceGenerator {
	private static final long serialVersionUID = 379015239255681800L;
	protected int n;

	public BinaryTreeGraph(int n) {
		this.n = n;
	}

	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(n);
		int maxDepth = 0;
		while (n >> maxDepth != 0) {
			maxDepth++;
		}

		/*
         *  Construct a binary tree by walking the left edge 1,2,4,8,...
         *  At the i-th level, iterate k from 2^i to 2^(i+1) and add an 
         *  edge to the two children of k for each k. For example, at 
         *  level 1 iterate from k=2 to k=3 and: 
         *      for k=2 add (2,4) and (2,5) to the graph
         *      for k=3 add (3,6) and (3,7) to the graph
         *  Alternatively,
         *      for k=2 add (2,2<<1) and (2,2<<1|1) to the graph
         *      for k=3 add (3,3<<1) and (3,3<<1|1) to the graph
         *  
         *         1         
         *       /   \
         *      3     2      
         *     / \   / \
         *    7  6  5   4
         *    ... 
         */ 

		outer: for (int i = 0; i < maxDepth; i++) {
			for (int j = (1 << i); j < (1 << (i + 1)); j++) {
				int k = j << 1;
				if (k > n)
					break outer;
				graph.addEdge(j - 1, k - 1);
				int l = (j << 1) | 1;
				if (l > n)
					break outer;
				graph.addEdge(j - 1, l - 1);
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
		return "BinaryTreeGraph(" + this.n + ")";
	}

}
