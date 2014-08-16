package edu.stanford.math.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import edu.stanford.math.clustering.DisjointSetSystem;
import edu.stanford.math.clustering.HierarchicalClustering;
import edu.stanford.math.clustering.SingleLinkageClustering;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.utility.RandomUtility;

public class ClusteringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int n = 40;
		int d = 2;

		RandomUtility.initializeWithSeed(0);

		double[][] points = PointCloudExamples.getGaussianPoints(n, d);
		/*
		 * double[][] points = new double[][]{ new double[]{1}, new double[]{2},
		 * 
		 * new double[]{6}, new double[]{7.1}, new double[]{9}, };
		 */
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);

		SingleLinkageClustering clustering = new SingleLinkageClustering(metricSpace);

		double[] mergeTimes = clustering.getMergedDistances();

		System.out.println(Arrays.toString(mergeTimes));

		// List<Set<Integer>> clusters = clustering.getClusterAssignments(4);

		DisjointSetSystem setSystem = clustering.thresholdByNumClusters(3);

		List<Set<Integer>> clusters = HierarchicalClustering.getImpliedClusters(setSystem);

		System.out.println(clusters);
	}
}
