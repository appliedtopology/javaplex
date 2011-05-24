package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.kd.KDEuclideanMetricSpace;
import edu.stanford.math.plex4.metric.SearchableMetricSpaceTester;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.utility.MetricUtility;
import edu.stanford.math.plex4.test_utility.Timing;

/**
 * This class contains tests for verifying the functionality of the
 * metric space searching classes.
 * 
 * @author Andrew Tausz
 *
 */
public class SearchableMetricSpaceTest {
	
	// 2-D point cloud examples
	private List<double[][]> pointCloudExamples = new ArrayList<double[][]>();
	
	// 2-D query point set
	private double[][] queryPointSet = null;
	
	private int d = 3;

	@Before
	public void setUp() {
		int n = 20000;
		
		pointCloudExamples.add(PointCloudExamples.getRandomSpherePoints(n, d - 1));

		queryPointSet = PointCloudExamples.getGaussianPoints(n, d);
	}

	@After
	public void tearDown() {
		pointCloudExamples = null;
		queryPointSet = null;
	}
	
	@Test
	public void testNonKDMetricSpace2D() {
		Timing.restart();
		for (double[][] pointCloud: this.pointCloudExamples) {
			AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyKNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
		Timing.stopAndDisplay("Non KD");
	}
	
	@Test
	public void testKDMetricSpace2D() {
		Timing.restart();
		for (double[][] pointCloud: this.pointCloudExamples) {
			AbstractSearchableMetricSpace<double[]> metricSpace = new KDEuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyKNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
		Timing.stopAndDisplay("KD");
	}
}
