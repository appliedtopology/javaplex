package edu.stanford.math.plex4.homology;

import static org.junit.Assert.assertTrue;

import edu.stanford.math.plex4.api.FilteredStreamInterface;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.test_utility.Timing;

/**
 * This class contains functions for comparing streams (filtered chain
 * complexes) for equality.
 * 
 * @author Andrew Tausz
 * 
 */
public class StreamTester {

	/**
	 * This function verifies that the Vietoris-Rips streams constructed by plex
	 * 3 and plex 4 are equal.
	 * 
	 * @param points
	 *            the set of points in Euclidean space
	 * @param maxDimension
	 *            the maximum dimension of the complex
	 * @param maxFiltrationValue
	 *            the maximum filtration value (maximum distance allowable for
	 *            an edge)
	 * @param numDivisions
	 *            the number of divisions of the interval [0,
	 *            maxFiltrationValue]
	 */
	public static void compareVietorisRipsStreams(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		Timing.restart();
		AbstractFilteredStream<Simplex> plex3Stream = FilteredStreamInterface.createPlex3VietorisRipsStream(points, maxDimension, maxFiltrationValue,
				numDivisions);
		Timing.stopAndDisplay("Plex 3 Vietoris-Rips Stream");

		Timing.restart();
		AbstractFilteredStream<Simplex> plex4Stream = FilteredStreamInterface.createPlex4VietorisRipsStream(points, maxDimension, maxFiltrationValue,
				numDivisions);
		Timing.stopAndDisplay("Plex 4 Vietoris-Rips Stream");

		verifyEqual(plex3Stream, plex4Stream);
	}

	/**
	 * This function verifies that the Lazy-Witness streams constructed by plex
	 * 3 and plex 4 are equal.
	 * 
	 * @param selector
	 *            the selection of landmark points within a metric space
	 * @param maxDimension
	 *            the maximum dimension of the complex
	 * @param maxFiltrationValue
	 *            the maximum filtration value (maximum distance allowable for
	 *            an edge)
	 * @param numDivisions
	 *            the number of divisions of the interval [0,
	 *            maxFiltrationValue]
	 */
	public static void compareLazyWitnessStreams(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		Timing.restart();
		AbstractFilteredStream<Simplex> plex3Stream = FilteredStreamInterface.createPlex3LazyWitnessStream(selector, maxDimension, maxFiltrationValue,
				numDivisions);
		Timing.stopAndDisplay("Plex 3 Lazy-Witness Stream");

		Timing.restart();
		AbstractFilteredStream<Simplex> plex4Stream = FilteredStreamInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue,
				numDivisions);
		Timing.stopAndDisplay("Plex 4 Lazy-Witness Stream");

		verifyEqual(plex3Stream, plex4Stream);
	}

	/**
	 * This function verifies that the Witness streams constructed by plex 3 and
	 * plex 4 are equal.
	 * 
	 * @param selector
	 *            the selection of landmark points within a metric space
	 * @param maxDimension
	 *            the maximum dimension of the complex
	 * @param maxFiltrationValue
	 *            the maximum filtration value (maximum distance allowable for
	 *            an edge)
	 * @param numDivisions
	 *            the number of divisions of the interval [0,
	 *            maxFiltrationValue]
	 */
	public static void compareWitnessStreams(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		Timing.restart();
		AbstractFilteredStream<Simplex> plex3Stream = FilteredStreamInterface
				.createPlex3WitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
		Timing.stopAndDisplay("Plex 3 Witness Stream");

		Timing.restart();
		AbstractFilteredStream<Simplex> plex4Stream = FilteredStreamInterface
				.createPlex4WitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
		Timing.stopAndDisplay("Plex 4 Witness Stream");

		verifyEqual(plex3Stream, plex4Stream);
	}

	/**
	 * This function compares two AbstractFilteredStream objects to make sure
	 * that they contain the same set of objects with the same filtration
	 * indices.
	 * 
	 * @param <T>
	 *            the underlying basis type for the filtered chain complexes
	 * @param stream1
	 *            the first stream
	 * @param stream2
	 *            the second stream
	 */
	public static <T> void verifyEqual(AbstractFilteredStream<T> stream1, AbstractFilteredStream<T> stream2) {
		assertTrue("Streams have different sizes.", stream1.getSize() == stream2.getSize());
		for (T S : stream1) {
			assertTrue("Streams have different simplices.", stream2.containsElement(S));
			assertTrue("Streams have different filtration indices.", stream1.getFiltrationIndex(S) == stream2.getFiltrationIndex(S));
		}
	}
}
