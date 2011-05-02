package edu.stanford.math.plex4.homology.chain_basis;

import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class SimplexPair extends ObjectObjectPair<Simplex, Simplex> implements PrimitiveBasisElement {

	public SimplexPair(Simplex first, Simplex second) {
		super(first, second);
	}
	
	public SimplexPair(int[] first, int[] second) {
		super(new Simplex(first), new Simplex(second));
	}
	
	public SimplexPair(ObjectObjectPair<Simplex, Simplex> pair) {
		super(pair);
	}
	
	public static SimplexPair createPair(Simplex first, Simplex second) {
		return new SimplexPair(first, second);
	}
	
	public static SimplexPair createPair(int first, int second) {
		return new SimplexPair(Simplex.makeSimplex(first), Simplex.makeSimplex(second));
	}
	
	public static SimplexPair createPair(int[] first, int[] second) {
		return new SimplexPair(Simplex.makeSimplex(first), Simplex.makeSimplex(second));
	}

	public PrimitiveBasisElement[] getBoundaryArray() {
		Simplex[] firstBoundary = this.first.getBoundaryArray();
		Simplex[] secondBoundary = this.second.getBoundaryArray();
		SimplexPair[] boundary = new SimplexPair[firstBoundary.length + secondBoundary.length];
		
		int i = 0;
		for (Simplex d_sigma: firstBoundary) {
			boundary[i] = new SimplexPair(d_sigma, this.second);
			i++;
		}
		
		for (Simplex d_tau: secondBoundary) {
			boundary[i] = new SimplexPair(this.first, d_tau);
			i++;
		}
		
		return boundary;
	}

	public int[] getBoundaryCoefficients() {
		int[] firstBoundary = this.first.getBoundaryCoefficients();
		int[] secondBoundary = this.second.getBoundaryCoefficients();
		int[] boundary = new int[firstBoundary.length + secondBoundary.length];
		
		for (int i = 0; i < firstBoundary.length; i++) {
			boundary[i] = firstBoundary[i];
		}
		
		int multiplier = (this.first.getDimension() % 2 == 0 ? 1 : -1);
		
		for (int i = 0; i < secondBoundary.length; i++) {
			boundary[i + firstBoundary.length] = multiplier * secondBoundary[i];
		}
		
		return boundary;
	}

	public int getDimension() {
		return first.getDimension() + second.getDimension();
	}
	
}
