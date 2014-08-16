package edu.stanford.math.clustering;

public class DisjointSetSystem {
	protected int[] parents;
	protected int[] ranks;

	public DisjointSetSystem(int n) {
		this.parents = new int[n];
		this.ranks = new int[n];

		for (int i = 0; i < n; i++) {
			this.parents[i] = i;
		}
	}

	public void union(int x, int y) {
		int xRoot = this.find(x);
		int yRoot = this.find(y);

		if (xRoot == yRoot)
			return;

		if (this.ranks[xRoot] < this.ranks[yRoot]) {
			this.parents[xRoot] = yRoot;
		} else if (this.ranks[xRoot] > this.ranks[yRoot]) {
			this.parents[yRoot] = xRoot;
		} else {
			this.parents[yRoot] = xRoot;
			this.ranks[xRoot]++;
		}
	}

	public int find(int x) {
		if (this.parents[x] != x)
			this.parents[x] = this.find(this.parents[x]);
		return this.parents[x];
	}
	
	public int size() {
		return this.parents.length;
	}
}
