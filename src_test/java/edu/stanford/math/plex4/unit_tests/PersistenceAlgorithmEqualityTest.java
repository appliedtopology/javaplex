package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.FilteredStreamInterface;
import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.autogen.homology.PersistentCohomologyPrototype;
import edu.stanford.math.plex4.examples.CellStreamExamples;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.PersistenceAlgorithmTester;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;

/**
 * This class contains test for verifying that the different persistence algorithms produce the
 * same results.
 * 
 * @author Andrew Tausz
 *
 */
public class PersistenceAlgorithmEqualityTest {
	
	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	//@Test
	public void testPersistentCohomology() {
		int maxDimension = 4;
		
		List<AbstractFilteredStream<Simplex>> streams = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		streams.add(SimplexStreamExamples.getZomorodianCarlssonExample());
		streams.add(SimplexStreamExamples.getFilteredTriangle());
		streams.add(SimplexStreamExamples.getTriangle());
		streams.add(SimplexStreamExamples.getTetrahedron());
		streams.add(SimplexStreamExamples.getTorus());
		streams.add(SimplexStreamExamples.getCircle(7));
		streams.add(SimplexStreamExamples.getOctahedron());
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = new ArrayList<AbstractPersistenceAlgorithm<Simplex>>();
		
		algorithms.add(PersistenceAlgorithmInterface.getPlex3SimplicialAbsoluteHomology(maxDimension));
		algorithms.add(new PersistentCohomologyPrototype<Simplex>(ModularIntField.getInstance(11), SimplexComparator.getInstance(), 0, maxDimension));
		
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests various small examples of filtered simplicial complexes.
	 */
	@Test
	public void testSmallSimplexStreams() {
		int maxDimension = 4;
		
		List<AbstractFilteredStream<Simplex>> streams = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		streams.add(SimplexStreamExamples.getZomorodianCarlssonExample());
		streams.add(SimplexStreamExamples.getFilteredTriangle());
		streams.add(SimplexStreamExamples.getTriangle());
		streams.add(SimplexStreamExamples.getTetrahedron());
		streams.add(SimplexStreamExamples.getTorus());
		streams.add(SimplexStreamExamples.getCircle(7));
		streams.add(SimplexStreamExamples.getOctahedron());
		
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension);
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests various small examples of filtered cell complexes. Note that we only test
	 * the orientable examples, due to differing results due to torsion.
	 */
	@Test
	public void testSmallCellStreams() {
		int maxDimension = 4;
		
		List<AbstractFilteredStream<Cell>> streams = new ArrayList<AbstractFilteredStream<Cell>>();
		
		streams.add(CellStreamExamples.getMorozovJohanssonExample());
		streams.add(CellStreamExamples.getCellularSphere(maxDimension - 1));
		streams.add(CellStreamExamples.getCellularTorus());
		
		List<AbstractPersistenceAlgorithm<Cell>> algorithms = PersistenceAlgorithmInterface.getAllPlex4CellularAbsoluteHomologyAlgorithms(maxDimension);
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests the algorithms on Vietoris-Rips complexes generated from point clouds.
	 */
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
			streams.add(FilteredStreamInterface.createPlex4VietorisRipsStream(pointCloud, maxDimension + 1, maxFiltrationValue, numDivisions));
		}
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		//List<AbstractPersistenceAlgorithm<Simplex>> algorithms = new ArrayList<AbstractPersistenceAlgorithm<Simplex>>();
		//algorithms.add(new PersistentCohomologyPrototype<Simplex>(ModularIntField.getInstance(11), SimplexComparator.getInstance(), 0, maxDimension));
		
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests the algorithms on Lazy-Witness complexes generated from point clouds.
	 */
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
			streams.add(FilteredStreamInterface.createPlex4LazyWitnessStream(landmarkSet, maxDimension, maxFiltrationValue, numDivisions));
		}
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests a complex that contains approximately 500,000 simplices. It compares the
	 * efficiency of the different algorithms on a large complex.
	 */
	@Test
	public void testLargeFigure8Complex() {
		final int n = 220;
		final int maxDimension = 4;
		final double maxFiltrationValue = 0.5;
		final int numDivisions = 10;
		
		double[][] points = PointCloudExamples.getRandomFigure8Points(n);
		AbstractFilteredStream<Simplex> stream = FilteredStreamInterface.createPlex4VietorisRipsStream(points, maxDimension + 1, maxFiltrationValue, numDivisions);
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		PersistenceAlgorithmTester.verifyEquality(algorithms, stream);
	}
	
	/**
	 * This function compares the algorithms on a Vietoris-Rips stream generated from sampling a 6-dimensional sphere.
	 */
	@Test
	public void testHighDimensionalSphere() {
		final int n = 48;
		final int sphereDimension = 6;
		final double maxFiltrationValue = 1.5;
		final int numDivisions = 10;
		
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, sphereDimension);
		AbstractFilteredStream<Simplex> stream = FilteredStreamInterface.createPlex4VietorisRipsStream(points, sphereDimension + 1, maxFiltrationValue, numDivisions);
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(sphereDimension);
		PersistenceAlgorithmTester.verifyEquality(algorithms, stream);
	}
}
