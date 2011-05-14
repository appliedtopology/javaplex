package edu.stanford.math.plex4.example_tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.zigzag.RipsBootstrapper;
import edu.stanford.math.plex4.homology.zigzag.WitnessBootstrapper;
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
		IntBarcodeCollection barcodes = bootstrapper.performBootstrap();
		
		barcodes.draw();
		
		System.out.println(barcodes);
	}
	
	//@Test
	public void testWitness1() {
		double[][] points = PointCloudExamples.getDisjointPatches(30);
		double maxDistance = 0.2;
		
		AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		System.out.println(persistence.computeIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
	}
	
	//@Test
	public void testWitness() throws IOException {
		double[][] points = PointCloudExamples.getDisjointPatches(30);
		double maxDistance = 0.9;
		
		RandomUtility.initializeWithSeed(0);
		
		//AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getDefaultSimplicialAlgorithm(2);
		//System.out.println(persistence.computeIntervals(Plex4.createVietorisRipsStream(points, 2, maxDistance)));
		
		WitnessBootstrapper<double[]> bootstrapper = new WitnessBootstrapper<double[]>(new EuclideanMetricSpace(points), maxDistance, 0, 2, 5);
		
		IntBarcodeCollection barcodes = bootstrapper.performBootstrap();
		
		barcodes.draw();
		
		System.out.println(barcodes);
	}
	
	@Test
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
		
		IntBarcodeCollection barcodes = bootstrapper.performBootstrapShort();
		
		barcodes.draw();
		
		System.out.println(barcodes);
	}
}


