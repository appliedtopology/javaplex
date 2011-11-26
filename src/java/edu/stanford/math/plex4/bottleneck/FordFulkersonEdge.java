package edu.stanford.math.plex4.bottleneck;

/**
 * @author Tim Harrington
 * @date Mar 12, 2009
 *
 * This is from
 * http://www-b2.is.tokushima-u.ac.jp/~ikeda/suuri/maxflow/Maxflow.java File:
 * Maxflow.java based on Ford-Fulkerson Method Copyright (C) 1997, 1998 K. Ikeda
 */
public class FordFulkersonEdge {
	public int rndd_plus; /* initial vertex of this edge */
	public int rndd_minus; /* terminal vertex of this edge */
	public int delta_plus; /* edge starts from rndd_plus */
	public int delta_minus; /* edge terminates at rndd_minus */
	public int capacity; /* capacity */
	public int flow; /* flow */
	public int st;

	/*
	 * This field is used to store extra information about the edge and is not
	 * used by the max flow algorithm
	 */
	public double bottleneck = 0;

	/**
	 * @param rndd_plus
	 * @param rndd_minus
	 * @param capacity
	 */
	public FordFulkersonEdge(int rndd_plus, int rndd_minus, int capacity) {
		this.rndd_plus = rndd_plus;
		this.rndd_minus = rndd_minus;
		this.capacity = capacity;
	}

	public FordFulkersonEdge(int rndd_plus, int rndd_minus, int capacity,
			double bottleneck) {
		this.rndd_plus = rndd_plus;
		this.rndd_minus = rndd_minus;
		this.capacity = capacity;
		this.bottleneck = bottleneck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// return "from="+this.rndd_plus + " to="+this.rndd_plus+
		// " cap="+this.capacity + " bn="+this.bottleneck;
		return Double.toString(this.bottleneck);
	}

}
