package edu.stanford.math.plex4.homology;

import edu.stanford.math.plex4.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex4.array_utility.ArrayGeneration;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;

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
