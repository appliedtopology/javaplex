package edu.stanford.math.plex4.graph.random;

import java.text.DecimalFormat;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

public class StochasticBlockmodel extends GraphInstanceGenerator {

	private static final long serialVersionUID = -4992866715916257875L;
	protected final double[][] probabilityMatrix;
	protected final int n;

	public StochasticBlockmodel(int n, double[][] probabilityMatrix) {
		this.probabilityMatrix = probabilityMatrix;
		this.n = n;
	}

	@Override
	public AbstractUndirectedGraph generate() {

		AbstractUndirectedGraph graph = this.initializeGraph(n);

		// number of classes
		int c = probabilityMatrix.length;

		int[] classAssignments = new int[this.n];

		// randomly generate classes
		for (int i = 0; i < this.n; i++) {
			classAssignments[i] = (int) Math.floor(c * RandomUtility.nextUniform());
		}

		for (int i = 0; i < this.n; i++) {
			int c_i = classAssignments[i];
			for (int j = i + 1; j < this.n; j++) {
				int c_j = classAssignments[j];

				double p = this.probabilityMatrix[c_i][c_j];
				// add edge with probability p
				if (RandomUtility.nextBernoulli(p) == 1) {
					graph.addEdge(i, j);
				}
			}
		}

		return graph;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		int c = probabilityMatrix.length;

		DecimalFormat df = new DecimalFormat("#.###");

		for (int i = 0; i < c; i++) {
			if (i > 0) {
				builder.append(";");
			}
			for (int j = i; j < c; j++) {
				if (j > i) {
					builder.append(",");
				}

				builder.append(df.format(this.probabilityMatrix[i][j]));
			}
		}

		return "StochasticBlock(" + this.n + ",[" + builder.toString() + "])";
	}
}
