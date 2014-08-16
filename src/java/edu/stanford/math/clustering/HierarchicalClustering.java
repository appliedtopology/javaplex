package edu.stanford.math.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import gnu.trove.TIntHashSet;

public abstract class HierarchicalClustering {
	public static double INFINITY = Double.POSITIVE_INFINITY;

	protected final AbstractIntMetricSpace metricSpace;

	protected double[] mergeDistances;
	protected int[][] mergedPairs;

	public HierarchicalClustering(AbstractIntMetricSpace metricSpace) {
		this.metricSpace = metricSpace;
		this.performClustering();
	}

	public double[] getMergedDistances() {
		return this.mergeDistances;
	}

	public static List<Set<Integer>> getImpliedClusters(DisjointSetSystem setSystem) {
		Map<Integer, Set<Integer>> clusters = new HashMap<Integer, Set<Integer>>();

		int N = setSystem.size();

		for (int i = 0; i < N; i++) {
			int assignment = setSystem.find(i);

			if (!clusters.containsKey(assignment))
				clusters.put(assignment, new HashSet<Integer>());
			clusters.get(assignment).add(i);
		}

		ArrayList<Set<Integer>> result = new ArrayList<Set<Integer>>(clusters.values());

		return result;
	}

	public static List<TIntHashSet> getImpliedClustersTrove(DisjointSetSystem setSystem) {
		Map<Integer, TIntHashSet> clusters = new HashMap<Integer, TIntHashSet>();

		int N = setSystem.size();

		for (int i = 0; i < N; i++) {
			int assignment = setSystem.find(i);

			if (!clusters.containsKey(assignment))
				clusters.put(assignment, new TIntHashSet());
			clusters.get(assignment).add(i);
		}

		ArrayList<TIntHashSet> result = new ArrayList<TIntHashSet>(clusters.values());

		return result;
	}

	public DisjointSetSystem thresholdByDistance(double maxDistance) {
		int N = metricSpace.size();

		DisjointSetSystem setSystem = new DisjointSetSystem(N);

		for (int s = 0; mergeDistances[s] <= maxDistance; s++) {
			if (mergedPairs[s] == null)
				break;

			setSystem.union(mergedPairs[s][0], mergedPairs[s][1]);
		}

		return setSystem;
	}

	public DisjointSetSystem thresholdByNumClusters(int numClusters) {
		int N = metricSpace.size();

		DisjointSetSystem setSystem = new DisjointSetSystem(N);

		if (numClusters > N)
			numClusters = N;

		if (numClusters < 1)
			numClusters = 1;

		for (int s = 0; s < N - numClusters; s++) {
			if (mergedPairs[s] == null)
				break;

			setSystem.union(mergedPairs[s][0], mergedPairs[s][1]);
		}

		return setSystem;
	}

	public abstract void performClustering();

}
