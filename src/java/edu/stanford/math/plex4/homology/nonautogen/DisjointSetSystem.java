package edu.stanford.math.plex4.homology.nonautogen;

@Deprecated
public class DisjointSetSystem {
	int capacity = 16;
	int size = 0;
	int[] parents = null;
	int[] ranks = null;
	
	public DisjointSetSystem(int size) {
		while (capacity < size) {
			capacity *= 2;
		}
		
		this.size = size;
		this.parents = new int[capacity];
		this.ranks = new int[capacity];
		for (int i = 0; i < this.size; i++) {
			this.parents[i] = i;
		}
	}
	
	public DisjointSetSystem(DisjointSetSystem set) {
		this.capacity = set.capacity;
		this.size = set.size;
		this.parents = new int[capacity];
		this.ranks = new int[capacity];
		for (int i = 0; i < this.size; i++) {
			this.parents[i] = set.parents[i];
			this.ranks[i] = set.ranks[i];
		}
	}
	
	public void grow(int newSize) {
		int newCapacity = capacity;
		while (newCapacity < newSize) {
			newCapacity *= 2;
		}
		
		if (newCapacity != this.capacity) {
			int[] newParents = new int[newCapacity];
			int[] newRanks = new int[newCapacity];
			
			for (int i = 0; i < this.size; i++) {
				newParents[i] = this.parents[i];
				newRanks[i] = this.ranks[i];
			}
			
			this.parents = newParents;
			this.ranks = newRanks;
			this.capacity = newCapacity;
		}
		
		this.size = newSize;
	}
	
	/**
	 * Returns the index of the representative
	 * @param index
	 * @return the index of the representative
	 */
	public int find(int index) {
		if (parents[index] == index) {
			return index;
		} else {
			parents[index] = find(parents[index]);
			return parents[index];
		}
	}
	
	public void union(int x, int y) {
		int xRoot = this.find(x);
		int yRoot = this.find(y);
		if (ranks[xRoot] > ranks[yRoot]) {
			parents[yRoot] = xRoot;
		} else if (ranks[xRoot] < ranks[yRoot]) {
			parents[xRoot] = yRoot;
		} else if (xRoot != yRoot) {
			parents[yRoot] = xRoot;
			ranks[xRoot]++;
		}
	}
}
