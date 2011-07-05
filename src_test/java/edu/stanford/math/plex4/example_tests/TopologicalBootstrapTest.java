package edu.stanford.math.plex4.example_tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.zigzag.bootstrap.WitnessBootstrapper;
import edu.stanford.math.plex4.homology.zigzag.deprecated.RipsBootstrapper;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.landmark.ExplicitLandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;

public class TopologicalBootstrapTest {
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {}
	
	//@Test
	public void testRips() {
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(30);
		double maxDistance = 0.8;
		
		RandomUtility.initializeWithSeed(0);
		RipsBootstrapper bootstrapper = new RipsBootstrapper(points, maxDistance, 1, 3, 20);
		BarcodeCollection<Integer> barcodes = bootstrapper.performBootstrap();
		
		System.out.println(barcodes);
	}
	
	//@Test
	public void testWitness1() {
		double[][] points = PointCloudExamples.getDisjointPatches(30);
		double maxDistance = 0.2;
		
		AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		System.out.println(persistence.computeIndexIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
	}
	
	@Test
	public void testWitness() throws IOException {
		RandomUtility.initializeWithSeed(0);
		
		double[][] points = PointCloudExamples.getRandomFigure8Points(100);
		double maxDistance = 0.1;
		
		
		
		//AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		//System.out.println(persistence.computeIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(new EuclideanMetricSpace(points), maxDistance, 1, 20, 7);
		
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");

		System.out.println(barcodes);
	}
	
	//@Test
	public void test2Sphere() throws IOException {
		double[][] points = PointCloudExamples.getRandomSpherePoints(50, 2);
		double maxDistance = 0.4;
		int maxDimension = 2;
		
		RandomUtility.initializeWithSeed(0);
		
		//AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		//System.out.println(persistence.computeIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(new EuclideanMetricSpace(points), maxDistance, maxDimension, 2, 8);
		
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");
		
		System.out.println(barcodes);
	}
	
	//@Test
	public void testExplicitWitness() throws IOException {
		double[][] points = PointCloudExamples.getEquispacedCirclePoints(6);
		double maxDistance = 0.0;
		
		RandomUtility.initializeWithSeed(0);
		
		//AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		//System.out.println(persistence.computeIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		List<LandmarkSelector<double[]>> list = new ArrayList<LandmarkSelector<double[]>>();
		
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{0, 2, 4}));
		list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{1, 3, 5}));
		//list.add(new ExplicitLandmarkSelector<double[]>(metricSpace, new int[]{20, 21, 22, 23, 24}));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(metricSpace, list, 1, maxDistance);
		
		BarcodeCollection<Integer> barcodes = bootstrapper.performProjectionBootstrap();
		
		System.out.println("Zigzag barcodes");
		
		System.out.println(barcodes);
	}
}


