package edu.stanford.math.plex4.bottleneck;

/**
 * @author Tim Harrington
 * @date Mar 12, 2009
 *
 * This is from
 * http://www-b2.is.tokushima-u.ac.jp/~ikeda/suuri/maxflow/Maxflow.java File:
 * Maxflow.java based on Ford-Fulkerson Method Copyright (C) 1997, 1998 K. Ikeda
 */

public class FordFulkersonNode {
	public int delta_plus; /* edge starts from this node */
	public int delta_minus; /* edge terminates at this node */
	public int dist; /* distance from the start node */
	public int prev; /* previous node of the shortest path */
	public int p_edge;
	public int l;
	public String name;

	public FordFulkersonNode(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FordFulkersonNode [delta_minus=" + delta_minus + ", delta_plus=" + delta_plus + ", dist=" + dist + ", l=" + l + ", "
				+ (name != null ? "name=" + name + ", " : "") + "p_edge=" + p_edge + ", prev=" + prev + "]";
	}
}