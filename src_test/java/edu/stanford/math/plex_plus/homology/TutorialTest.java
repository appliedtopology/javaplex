package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex_plus.array_utility.ArrayGeneration;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.impl.ExplicitStream;

public class TutorialTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testSphere();
	}

	
	private static void testSphere() {
		int d = 10;
		
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());
		stream.addElement(new Simplex(ArrayGeneration.range(0, d + 2)), 0);
		stream.ensureAllFaces();
		//System.out.println(stream);
		stream.removeElementIfPresent(new Simplex(ArrayGeneration.range(0, d + 2)));
		stream.finalizeStream();
		//System.out.println(stream);
		
		PersistentHomologyTest.testGenericDualityPersistentCohomology(stream, SimplexComparator.getInstance(), RationalField.getInstance());
	}
}
