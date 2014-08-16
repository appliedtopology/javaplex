package edu.stanford.math.clustering;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

public class SingleLinkageClustering extends HierarchicalClustering {

	public SingleLinkageClustering(AbstractIntMetricSpace metricSpace) {
		super(metricSpace);
	}

	public void performClustering() {
		int N = metricSpace.size();

		double[][] dist = new double[N][N];

		// index of closest point to i-th point
		int[] mindist = new int[N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == j)
					dist[i][j] = INFINITY;
				else
					dist[i][j] = metricSpace.distance(i, j);
				if (dist[i][j] < dist[i][mindist[i]])
					mindist[i] = j;
			}
		}

		this.mergeDistances = new double[N];
		this.mergedPairs = new int[N][];

		int[] parents = new int[N];

		for (int i = 0; i < N; i++)
			parents[i] = i;

		for (int s = 0; s < N - 1; s++) {

			// find closest pair of vectors (i1, i2)
			int i1 = 0;
			for (int i = 0; i < N; i++)
				if (dist[i][mindist[i]] < dist[i1][mindist[i1]])
					i1 = i;
			int i2 = mindist[i1];

			mergeDistances[s] = dist[i1][i2];

			// mergedPairs[s] = new int[] { parents[i1], parents[i2] };
			mergedPairs[s] = new int[] { i1, i2 };

			parents[i1] = parents[i2] = s + N;

			// overwrite row i1 with minimum of entries in row i1 and i2
			for (int j = 0; j < N; j++)
				if (dist[i2][j] < dist[i1][j])
					dist[i1][j] = dist[j][i1] = dist[i2][j];
			dist[i1][i1] = INFINITY;

			for (int i = 0; i < N; i++) {
				dist[i2][i] = dist[i][i2] = INFINITY;
			}

			// update mindist
			for (int j = 0; j < N; j++) {
				if (mindist[j] == i2)
					mindist[j] = i1;
				if (dist[i1][j] < dist[i1][mindist[i1]])
					mindist[i1] = j;
			}
		}
	}
}
