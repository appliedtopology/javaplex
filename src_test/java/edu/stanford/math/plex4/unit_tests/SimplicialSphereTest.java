package edu.stanford.math.plex4.unit_tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.test_utility.Timing;

/**
 * This class tests different persistence algorithms on a simplicial sphere. Since plex 3 doesn't support
 * simplices of dimension greater than 7, this only tests plex 4 algorithms.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplicialSphereTest {
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This function tests the plex 4 algorithms on a 16 dimensional simplicial sphere.
	 */
	@Test
	public void testAbsoluteHomology() {
		final int sphereDimension = 16;
		final int maxHomologyDimension = sphereDimension + 5;
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllPlex4SimplicialAbsoluteHomologyAlgorithms(maxHomologyDimension);
		BarcodeCollection<Integer> correctAnswer = getSphereBarcodes(sphereDimension, maxHomologyDimension);
		AbstractFilteredStream<Simplex> stream = getSimplicialSphere(sphereDimension);
		System.out.println(String.format("Number of simplices in %d-sphere: %s", sphereDimension, stream.getSize()));
		for (AbstractPersistenceAlgorithm<Simplex> algorithm : algorithms) {
			Timing.restart();
			BarcodeCollection<Integer> collection = algorithm.computeIndexIntervals(stream);
			Timing.stopAndDisplay(algorithm.toString());
			assertTrue(correctAnswer.equals(collection));
		}
	}
	
	public static AbstractFilteredStream<Simplex> getSimplicialSphere(int dimension) {
		return SimplexStreamExamples.getSimplicialSphere(dimension);
	}
	
	public static BarcodeCollection<Integer> getSphereBarcodes(int sphereDimension, int maxBarcodeDimension) {
		BarcodeCollection<Integer> collection = new BarcodeCollection<Integer>();
		
		if (maxBarcodeDimension >= 0) {
			collection.addRightInfiniteInterval(0, 0);
		}
		
		if (maxBarcodeDimension >= sphereDimension) {
			collection.addRightInfiniteInterval(sphereDimension, 0);
		}
		
		return collection;
	}
}
