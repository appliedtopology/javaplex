package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.math.structures.impl.ModularIntField;




/**
 * Very basic tests of homology related data structures.
 * 
 * @author Andrew Tausz
 *
 */
public class BasicHomologyTest {	
	public static void main(String[] args) {
		basicTest();
	}
	
	public static void basicTest() {
		// create a new simplicial complex
		FullSimplicialComplex stream = new FullSimplicialComplex();
		
		// add simplices
		stream.addSimplex(new ArraySimplex(new int[]{0, 1}));
		stream.addSimplex(new ArraySimplex(new int[]{1, 2}));
		stream.addSimplex(new ArraySimplex(new int[]{2, 0}));
		//stream.addSimplex(new ArraySimplex(new int[]{2, 3, 4, 5}));
		
		// print the entire complex
		System.out.println(stream.toString());
		
		PersistentHomology homology = new PersistentHomology(ModularIntField.getInstance(7));
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes);
	}
}
