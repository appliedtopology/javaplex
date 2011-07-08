package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.FilteredStreamInterface;
import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.PersistenceAlgorithmTester;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.zigzag.ZigZagHomology;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class ExperimentalEqualityTest {
	IntAbstractField intField = ModularIntField.getInstance(11);
	
	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	public List<AbstractPersistenceAlgorithm<Simplex>> getAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = new ArrayList<AbstractPersistenceAlgorithm<Simplex>>();
		algorithms.add(PersistenceAlgorithmInterface.getPlex3SimplicialAbsoluteHomology(maxDimension));
		algorithms.add(new ZigZagHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension - 1));
		
		return algorithms;
	}
	
	/**
	 * This function tests various small examples of filtered simplicial complexes.
	 */
	@Test
	public void testSmallSimplexStreams() {
		int maxDimension = 4;
		
		List<AbstractFilteredStream<Simplex>> streams = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		streams.add(SimplexStreamExamples.getFilteredTriangle());
		streams.add(SimplexStreamExamples.getZomorodianCarlssonExample());
		streams.add(SimplexStreamExamples.getTriangle());
		streams.add(SimplexStreamExamples.getTetrahedron());
		streams.add(SimplexStreamExamples.getTorus());
		streams.add(SimplexStreamExamples.getCircle(7));
		streams.add(SimplexStreamExamples.getOctahedron());
		streams.add(SimplexStreamExamples.getIcosahedron());
		streams.add(SimplexStreamExamples.getAnnulus(4, 10));
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = getAlgorithms(maxDimension);
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
	
	/**
	 * This function tests the algorithms on Vietoris-Rips complexes generated from point clouds.
	 */
	@Test
	public void testVietorisRipsPointClouds() {
		final int n = 120;
		final int maxDimension = 4;
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
		
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = getAlgorithms(maxDimension);
		PersistenceAlgorithmTester.verifyEquality(algorithms, streams);
	}
}
