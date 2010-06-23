package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.homology.complex.IntSimplicialComplex;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.SimplexComparator;
import edu.stanford.math.plex_plus.homology.simplex_streams.ExplicitStream;

public class HomComplexTest {
	public static void main(String[] args) {
		testHomTriangles();
	}
	
	public static void testHomTriangles() {
		int m = 6;
		int n = 3;
		
		IntField field = ModularIntField.getInstance(3);
		
		ExplicitStream<Simplex> stream1 = new ExplicitStream<Simplex>(SimplexComparator.getInstance());
		ExplicitStream<Simplex> stream2 = new ExplicitStream<Simplex>(SimplexComparator.getInstance());
		
		for (int i = 0; i < m; i++) {
			stream1.addSimplex(new Simplex(new int[]{i}), 0);
			stream1.addSimplex(new Simplex(new int[]{i, (i + 1) % m}), 0);
		}

		for (int i = 0; i < n; i++) {
			stream2.addSimplex(new Simplex(new int[]{i}), 0);
			stream2.addSimplex(new Simplex(new int[]{i, (i + 1) % n}), 0);
		}
		
		stream1.finalizeStream();
		stream2.finalizeStream();
		
		IntSimplicialComplex<Simplex> complex1 = new IntSimplicialComplex<Simplex>(stream1, SimplexComparator.getInstance());
		IntSimplicialComplex<Simplex> complex2 = new IntSimplicialComplex<Simplex>(stream2, SimplexComparator.getInstance());
		
		HomCompexComputation.performComputation(complex1, complex2, field);
	}
}
