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


public class SearchableMetricSpaceTest {
	
	// 2-D point cloud examples
	private List<double[][]> pointCloudExamples2D = new ArrayList<double[][]>();
	
	// 2-D query point set
	private double[][] queryPointSet2D = null;
	

	@Before
	public void setUp() {
		int n = 1000;
		
		pointCloudExamples2D.add(PointCloudExamples.getHouseExample());
		pointCloudExamples2D.add(PointCloudExamples.getSquare());
		pointCloudExamples2D.add(PointCloudExamples.getRandomFigure8Points(n));
		pointCloudExamples2D.add(PointCloudExamples.getEquispacedCirclePoints(n));
		pointCloudExamples2D.add(PointCloudExamples.getRandomSpherePoints(n, 1));

		queryPointSet2D = PointCloudExamples.getGaussianPoints(n, 2);
	}

	@After
	public void tearDown() {
		pointCloudExamples2D = null;
		queryPointSet2D = null;
	}
	
	@Test
	public void testNonKDMetricSpace2D() {
		for (double[][] pointCloud: this.pointCloudExamples2D) {
			SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet2D);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
	}
	
	@Test
	public void testKDMetricSpace2D() {
		for (double[][] pointCloud: this.pointCloudExamples2D) {
			SearchableFiniteMetricSpace<double[]> metricSpace = new KDEuclideanMetricSpace(pointCloud);
			
			double epsilon = MetricUtility.estimateDiameter(metricSpace) / 5.0;
			
			SearchableMetricSpaceTester.verifyNearestPoints(metricSpace, queryPointSet2D);
			SearchableMetricSpaceTester.verifyNeighborhoods(metricSpace, epsilon);
		}
	}
}
