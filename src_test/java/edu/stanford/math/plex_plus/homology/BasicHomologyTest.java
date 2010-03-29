package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.ArraySimplex;
import edu.stanford.math.plex_plus.homology.FullSimplicialComplex;
import edu.stanford.math.plex_plus.homology.SimplicialComplex;



public class BasicHomologyTest {	
	public static void main(String[] args) {
		basicTest();
	}
	
	public static void basicTest() {
		SimplicialComplex complex = new FullSimplicialComplex();
		complex.addSimplex(new ArraySimplex(new int[]{0, 1}));
		complex.addSimplex(new ArraySimplex(new int[]{1, 2}));
		complex.addSimplex(new ArraySimplex(new int[]{2, 0}));
		complex.addSimplex(new ArraySimplex(new int[]{2, 3, 4, 5}));
		System.out.println(complex.toString());
		System.out.println(complex.getBoundaryMatrix(1).toString());	
	}
}
