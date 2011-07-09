package edu.stanford.math.plex4.example_tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.zigzag.bootstrap.WitnessBootstrapper;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.landmark.ExplicitLandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;

public class WitnessBootstrapTest {
	@Before
	public void setUp() {}

	@After
	public void tearDown() {}
	
	@Test
	public void testVdSExample() throws IOException {
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(6);
		double maxDistance = 0.0;
		
		RandomUtility.initializeWithSeed(0);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		List<LandmarkSelector<double[]>> list = new ArrayList<LandmarkSelector<double[]>>();
		
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{0, 2, 4}));
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{1, 3, 5}));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(metricSpace, list, 1, maxDistance);
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");
		System.out.println(barcodes);
	}
	
	@Test
	public void testSimpleIdentityExample() throws IOException {
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(6);
		double maxDistance = 0.0;
		
		RandomUtility.initializeWithSeed(0);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		List<LandmarkSelector<double[]>> list = new ArrayList<LandmarkSelector<double[]>>();
		
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{0, 2, 4}));
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{0, 2, 4}));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(metricSpace, list, 1, maxDistance);
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");
		System.out.println(barcodes);
	}
	
	//@Test
	public void testIdentityExample() throws IOException {
		int n = 10000;
		int l = 20;
		int dimension = 2;
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, dimension);
		double maxDistance = 0.1;
		
		RandomUtility.initializeWithSeed(0);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		int[] indices = RandomUtility.randomSubset(l, n).toArray();
		
		List<LandmarkSelector<double[]>> list = new ArrayList<LandmarkSelector<double[]>>();
		
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, indices));
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, indices));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(metricSpace, list, dimension, maxDistance);
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");
		System.out.println(barcodes);
	}
	
	public static int[] range(int start, int step, int end) {
		int length = (end - start) / step;
		
		int[] result = new int[length];
		int value = start;
		for (int i = 0; i < length; i++) {
			result[i] = value;
			value += step;
		}
		
		return result;
	}
}


