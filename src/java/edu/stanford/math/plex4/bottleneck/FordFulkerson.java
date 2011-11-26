package edu.stanford.math.plex4.bottleneck;

/**
 * 
 * The Ford-Fulkerson Algorithm
 * 
 * 
 * @author Tim Harrington
 * @date Mar 12, 2009
 * 
 * 
 *       NOTICE: This is from
 *       http://www-b2.is.tokushima-u.ac.jp/~ikeda/suuri/maxflow/Maxflow.java
 *       File: Maxflow.java based on Ford-Fulkerson Method Copyright (C) 1997,
 *       1998 K. Ikeda
 */

public class FordFulkerson {

	int n, m;
	int snode, tnode; /* start node, terminate node */
	int step;
	FordFulkersonNode[] v;
	FordFulkersonEdge[] e;

	/**
	 * Inputs a graph to compute max flow/min cut for. The source node must be
	 * v[0] and the sink must be v[v.length-1].
	 * 
	 * @param v
	 * @param e
	 */
	public int maxFlow(FordFulkersonNode[] v, FordFulkersonEdge[] e, int snode,
			int tnode) {
		this.v = v;
		this.e = e;
		this.m = e.length;
		this.n = v.length;
		this.snode = snode;
		this.tnode = tnode;
		rdb();
		solve();
		int flow = 0;
		for (int i = 0; i < e.length; i++) {
			if (e[i].rndd_minus == tnode) {
				flow += e[i].flow;
			}
		}
		return flow;
	}

	protected void solve() {
		// initialize
		for (int i = 0; i < m; i++)
			e[i].flow = 0;
		stpath();
		step = 1;
		// find the max flow/min cut
		while (step != 0) {
			if (step == 1) {
				/* mark an s-t path */
				if (v[tnode].dist < 0)
					return;
				for (int i = tnode; v[i].prev >= 0; i = v[i].prev) {
					e[v[i].p_edge].st++;
				}
				/* check the outcome */
				if (v[tnode].dist < 0) {
					step = 0;
				} else {
					step = 2;
				}
			} else if (step == 2) {
				/* augment the flow */
				int i, j, a, f;

				f = v[tnode].l;
				for (i = tnode; (j = v[i].prev) >= 0; i = j) {
					a = v[i].p_edge;
					if (e[a].rndd_minus == i)
						e[a].flow += f;
					else if (e[a].rndd_plus == i)
						e[a].flow -= f;
				}

				/* find an s-t path */
				stpath();

				/* set the next step */
				step = 1;
			}
		}
	}

	protected void rdb() {
		int i, k;
		for (i = 0; i < n; i++)
			v[i].delta_plus = v[i].delta_minus = -1;
		for (i = 0; i < m; i++)
			e[i].delta_plus = e[i].delta_minus = -1;
		for (i = 0; i < m; i++) {
			k = e[i].rndd_plus;
			if (v[k].delta_plus == -1)
				v[k].delta_plus = i;
			else {
				k = v[k].delta_plus;
				while (e[k].delta_plus >= 0)
					k = e[k].delta_plus;
				e[k].delta_plus = i;
			}
			k = e[i].rndd_minus;
			if (v[k].delta_minus == -1)
				v[k].delta_minus = i;
			else {
				k = v[k].delta_minus;
				while (e[k].delta_minus >= 0)
					k = e[k].delta_minus;
				e[k].delta_minus = i;
			}
		}
	}

	protected void stpath() {
		int u[] = new int[1000], ni, no;
		int i, j, d;

		for (i = 0; i < n; i++) {
			v[i].prev = v[i].dist = -1;
			v[i].l = 0;
		}
		for (i = 0; i < m; i++)
			e[i].st = -1;
		ni = no = 0;
		d = 0;
		u[ni] = snode;
		v[snode].dist = 0;
		j = v[snode].delta_plus;
		i = 0;
		while (j >= 0) {
			if (i < e[j].capacity)
				i = e[j].capacity;
			j = e[j].delta_plus;
		}
		v[snode].l = i;

		for (; no <= ni; no++) {
			d = v[u[no]].dist;
			for (j = v[u[no]].delta_plus; j >= 0; j = e[j].delta_plus) {
				if (e[j].capacity - e[j].flow == 0)
					continue;
				i = e[j].rndd_minus;
				if (v[i].dist < 0) {
					v[i].dist = d + 1;
					v[i].prev = u[no];
					v[i].p_edge = j;
					v[i].l = Math.min(v[u[no]].l, e[j].capacity - e[j].flow);
					e[j].st++;
					u[++ni] = i;
				}
			}
			for (j = v[u[no]].delta_minus; j >= 0; j = e[j].delta_minus) {
				if (e[j].flow == 0)
					continue;
				i = e[j].rndd_plus;
				if (v[i].dist < 0) {
					v[i].dist = d + 1;
					v[i].prev = u[no];
					v[i].p_edge = j;
					v[i].l = Math.min(v[u[no]].l, e[j].flow);
					e[j].st++;
					u[++ni] = i;
				}
			}
		}
	}

}
