package edu.stanford.math.plex4.homology.zigzag;

public class IndexLinearizer {
	private final int J;

	public IndexLinearizer(int I, int J) {
		this.J = J;
	}

	public int getLinearIndex(int i, int j) {
		return i * J + j;
	}

	public int getFirstIndex(int linearIndex) {
		return linearIndex / J;
	}

	public int getSecondIndex(int linearIndex) {
		return linearIndex % J;
	}
}
