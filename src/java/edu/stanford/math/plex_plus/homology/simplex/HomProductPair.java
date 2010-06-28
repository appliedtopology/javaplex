/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex;

import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;


/**
 * @author Andrew Tausz
 *
 */
public class HomProductPair<T extends ChainBasisElement, U extends ChainBasisElement> extends GenericPair<T, U> implements ChainBasisElement {

	public HomProductPair(T first, U second) {
		super(first, second);
	}

	@Override
	public ChainBasisElement[] getBoundaryArray() {
		/*
		 * p = degree of a
		 * 
		 * d(a x b) = da x b + (-1)^p a x db
		 */
		
		ChainBasisElement[] d_a = this.first.getBoundaryArray();
		ChainBasisElement[] d_b = this.second.getBoundaryArray();
		
		ChainBasisElement[] boundary = new ChainBasisElement[d_a.length + d_b.length];
		
		int currentDimension = this.getDimension();
		
		for (int i = 0; i < d_a.length; i++) {
			boundary[i] = new HomProductPair<ChainBasisElement, ChainBasisElement>(d_a[i], this.second);
			if (boundary[i].getDimension() != currentDimension - 1) {
				ExceptionUtility.verifyEqual(boundary[i].getDimension(), currentDimension - 1);
			}
		}
		
		for (int i = 0; i < d_b.length; i++) {
			boundary[i + d_a.length] = new HomProductPair<ChainBasisElement, ChainBasisElement>(this.first, d_b[i]);
			if (boundary[i].getDimension() != currentDimension - 1) {
				ExceptionUtility.verifyEqual(boundary[i].getDimension(), currentDimension - 1);
			}
		}
		
		return boundary;
	}

	@Override
	public int[] getBoundaryCoefficients() {
		int[] a_coefficients = this.first.getBoundaryCoefficients();
		int[] b_coefficients = this.second.getBoundaryCoefficients();
		
		int[] coefficients = new int[a_coefficients.length + b_coefficients.length];
		
		/*
		 * Compute (-1)^p
		 */
		int p = this.first.getDimension();
		int multiplier = (p % 2 == 0 ? 1 : -1);
		
		for (int i = 0; i < a_coefficients.length; i++) {
			coefficients[i] = a_coefficients[i];
		}
		
		for (int i = 0; i < b_coefficients.length; i++) {
			coefficients[i + a_coefficients.length] = multiplier * b_coefficients[i];
		}
		
		return coefficients;
	}

	@Override
	public int getDimension() {
		return (this.second.getDimension() - this.first.getDimension());
	}

	@Override
	public String toString() {
		return (first.toString() + "->" + second.toString());
	}
}
