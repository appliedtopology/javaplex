package edu.stanford.math.plex4.example_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.HomTester;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

public class HomVerificationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void dumpHomInformation() {
		AbstractFilteredStream<Simplex> domain = SimplexStreamExamples.getCircle(3);
		AbstractFilteredStream<Simplex> codomain = SimplexStreamExamples.getCircle(3);
		HomTester.dumpHomInformation(domain, codomain);
	}
}
