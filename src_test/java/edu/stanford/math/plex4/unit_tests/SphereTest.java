package edu.stanford.math.plex4.unit_tests;

import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;

public class SphereTest {
	public static AbstractFilteredStream<Simplex> getSimplicialSphere(int dimension) {
		return SimplexStreamExamples.getSimplicialSphere(dimension);
	}
}
