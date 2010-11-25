package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.List;

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

	
	public void verifyHomotopyProperty() {
		List<AbstractFilteredStream<Simplex>> complexes = new ArrayList<AbstractFilteredStream<Simplex>>();
		
		complexes.add(SimplexStreamExamples.getCircle(4));
		complexes.add(SimplexStreamExamples.getCircle(6));
		complexes.add(SimplexStreamExamples.getTetrahedron());
		complexes.add(SimplexStreamExamples.getOctahedron());
		
		int n = complexes.size();
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				HomTester.verifyHomotopies(complexes.get(i), complexes.get(j));
			}
		}
	}
}
