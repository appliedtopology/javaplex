package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.FilteredComplexInterface;
import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.new_version.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.new_version.PersistenceAlgorithmTester;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;

public class PersistenceAlgorithmEqualityTest {
	
	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	@Test
	public void testVietorisRipsPointClouds() {
		final int n = 120;
		final int maxDimension = 5;
		final double maxFiltrationValue = 0.5;
		final int numDivisions = 10;
		
		List<double[][]> pointClouds = new ArrayList<double[][]>();
		pointClouds.add(PointCloudExamples.getEquispacedCirclePoints(n));
		pointClouds.add(PointCloudExamples.getGaussianPoints(n, maxDimension));
		pointClouds.add(PointCloudExamples.getRandomFigure8Points(n));
		pointClouds.add(PointCloudExamples.getRandomSpherePoints(maxDimension * n, maxDimension - 1));
		
		List<AbstractFilteredStream<Simplex>> streams = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		for (double[][] pointCloud: pointClouds) {
			streams.add(FilteredComplexInterface.createPlex4VietorisRipsStream(pointCloud, maxDimension + 1, maxFiltrationValue, numDivisions));
		}
		
		PersistenceAlgorithmTester<Simplex> tester = new PersistenceAlgorithmTester<Simplex>();
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		for (AbstractPersistenceAlgorithm<Simplex> algorithm : algorithms) {
			tester.addAlgorithm(algorithm);
		}
		
		tester.verifyEqualityAll(streams);
	}
	
	@Test
	public void testLazyWitnessPointClouds() {
		final int n = 500;
		final int l = 50;
		final int maxDimension = 3;
		final double maxFiltrationValue = 0.3;
		final int numDivisions = 10;
		
		List<double[][]> pointClouds = new ArrayList<double[][]>();
		pointClouds.add(PointCloudExamples.getEquispacedCirclePoints(n));
		pointClouds.add(PointCloudExamples.getGaussianPoints(n, maxDimension));
		pointClouds.add(PointCloudExamples.getRandomFigure8Points(n));
		pointClouds.add(PointCloudExamples.getRandomSpherePoints(maxDimension * n, maxDimension - 1));
		
		List<AbstractFilteredStream<Simplex>> streams = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		for (double[][] pointCloud: pointClouds) {
			LandmarkSelector<double[]> landmarkSet = new RandomLandmarkSelector<double[]>(new EuclideanMetricSpace(pointCloud), l);
			streams.add(FilteredComplexInterface.createPlex4LazyWitnessStream(landmarkSet, maxDimension, maxFiltrationValue, numDivisions));
		}
		
		PersistenceAlgorithmTester<Simplex> tester = new PersistenceAlgorithmTester<Simplex>();
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		for (AbstractPersistenceAlgorithm<Simplex> algorithm : algorithms) {
			tester.addAlgorithm(algorithm);
		}
		
		tester.verifyEqualityAll(streams);
	}
	
	@Test
	public void testLargeFigure8Complex() {
		final int n = 220;
		final int maxDimension = 4;
		final double maxFiltrationValue = 0.5;
		final int numDivisions = 10;
		
		double[][] points = PointCloudExamples.getRandomFigure8Points(n);
		AbstractFilteredStream<Simplex> stream = FilteredComplexInterface.createPlex4VietorisRipsStream(points, maxDimension + 1, maxFiltrationValue, numDivisions);
		PersistenceAlgorithmTester<Simplex> tester = new PersistenceAlgorithmTester<Simplex>();
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		for (AbstractPersistenceAlgorithm<Simplex> algorithm : algorithms) {
			tester.addAlgorithm(algorithm);
		}
		
		tester.verifyEquality(stream);
	}
	
	@Test
	public void testHighDimensionalSphere() {
		final int n = 48;
		final int sphereDimension = 6;
		final double maxFiltrationValue = 1.5;
		final int numDivisions = 10;
		
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, sphereDimension);
		AbstractFilteredStream<Simplex> stream = FilteredComplexInterface.createPlex4VietorisRipsStream(points, sphereDimension + 1, maxFiltrationValue, numDivisions);
		PersistenceAlgorithmTester<Simplex> tester = new PersistenceAlgorithmTester<Simplex>();
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(sphereDimension);
		for (AbstractPersistenceAlgorithm<Simplex> algorithm : algorithms) {
			tester.addAlgorithm(algorithm);
		}
		
		tester.verifyEquality(stream);
	}
}
