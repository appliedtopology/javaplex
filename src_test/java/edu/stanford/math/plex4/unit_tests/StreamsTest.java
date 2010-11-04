package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.new_version.StreamTester;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;

public class StreamsTest {
	private final List<double[][]> pointClouds = new ArrayList<double[][]>();
	private final int n = 400;
	private final int l = 300;
	private final int d = 4;
	private final int maxDimension = 2;
	private final double maxFiltrationValue = 0.3;
	private final int numDivisions = 20;
	
	@Before
	public void setUp() {
		pointClouds.add(PointCloudExamples.getEquispacedCirclePoints(n));
		pointClouds.add(PointCloudExamples.getGaussianPoints(n, d));
		pointClouds.add(PointCloudExamples.getRandomFigure8Points(n));
		pointClouds.add(PointCloudExamples.getRandomSpherePoints(n, d - 1));
	}

	@After
	public void tearDown() {}
	
	@Test
	public void testVietorisRips() {
		for (double[][] pointCloud: pointClouds) {
			StreamTester.compareVietorisRipsStreams(pointCloud, maxDimension, maxFiltrationValue, numDivisions);
		}
	}
	
	
	public void testLazyWitness() {
		for (double[][] pointCloud: pointClouds) {
			LandmarkSelector<double[]> landmarkSet = new RandomLandmarkSelector<double[]>(new EuclideanMetricSpace(pointCloud), l);
			StreamTester.compareLazyWitnessStreams(landmarkSet, maxDimension, maxFiltrationValue, numDivisions);
		}
	}
}
