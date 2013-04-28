package edu.stanford.math.plex4.example_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;

public class CustomFiltrationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExample() {
	
		double[][] points = PointCloudExamples.getRandomCirclePoints(100);
		int maxDimension = 1;
		double[] filtrationValues = new double[] { 0, 0.01, 0.02, 0.05, 0.1, 0.11, 0.2, 0.3, 0.5 };
		VietorisRipsStream<double[]> stream = Plex4.createVietorisRipsStream(points, maxDimension + 1, filtrationValues);
		AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getDefaultSimplicialAlgorithm(maxDimension + 1);
		BarcodeCollection<Double> intervals = algorithm.computeIntervals(stream);
		System.out.println(intervals);
	}
}
