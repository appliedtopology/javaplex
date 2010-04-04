package edu.stanford.math.plex_plus.math.matrix;

import edu.stanford.math.plex_plus.math.matrix.impl.sparse.DoubleSparseVector;


public class DoubleSparseVectorStructureTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DoubleSparseVector a = new DoubleSparseVector(10);
		DoubleSparseVector b = new DoubleSparseVector(10);
		a.set(1, 1);
		a.set(4, 1);
		a.set(3, 1);
		a.set(7, 1);
		
		b.set(2, 1);
		b.set(4, 1);
		b.set(5, 1);
		//b.set(7, 1);
		b.set(9, 1);
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.innerProduct(b));
	}

}
