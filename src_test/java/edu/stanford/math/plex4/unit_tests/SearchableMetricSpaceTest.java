package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.math.metric.SearchableMetricSpaceTester;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.impl.KDEuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.utility.MetricUtility;
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
	
	private int d = 4;

	@Before
	public void setUp() {
		int n = 10000;
		
		//pointCloudExamples2D.add(PointCloudExamples.getHouseExample());
		//pointCloudExamples2D.add(PointCloudExamples.getSquare());
		//pointCloudExamples2D.add(PointCloudExamples.getRandomFigure8Points(n));
		//pointCloudExamples2D.add(PointCloudExamples.getEquispacedCirclePoints(n));
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
		Timing.start();
		for (double[][] pointCloud: this.pointCloudExamples) {
			SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
		Timing.stopAndDisplay("Non KD");
	}
	
	@Test
	public void testKDMetricSpace2D() {
		Timing.start();
		for (double[][] pointCloud: this.pointCloudExamples) {
			SearchableFiniteMetricSpace<double[]> metricSpace = new KDEuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
		Timing.stopAndDisplay("KD");
	}
}
