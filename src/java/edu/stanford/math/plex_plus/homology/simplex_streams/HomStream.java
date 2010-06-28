package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Collections;
import java.util.Comparator;

import edu.stanford.math.plex_plus.datastructures.ReversedComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.HomComparator;
import edu.stanford.math.plex_plus.homology.simplex.HomProductPair;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class HomStream<T extends ChainBasisElement, U extends ChainBasisElement> extends BasicStream<HomProductPair<T, U>> {
	private final SimplexStream<T> stream1;
	private final SimplexStream<U> stream2;
	
	
	public HomStream(SimplexStream<T> stream1, SimplexStream<U> stream2, Comparator<T> TComparator, Comparator<U> UComparator) {
		super(new HomComparator<T, U>(new ReversedComparator<T>(TComparator), UComparator));
		this.stream1 = new DualStream<T>(stream1, TComparator);
		this.stream2 = stream2;
	}

	@Override
	public void finalizeStream() {
		if (!stream1.isFinalized()) {
			stream1.finalizeStream();
		}
		
		if (!stream2.isFinalized()) {
			stream2.finalizeStream();
		}
		
		this.constructComplex();
		
		this.isFinalized = true;
	}

	private void constructComplex() {
		for (T a: this.stream1) {
			double a_filtration = this.stream1.getFiltrationValue(a);
			for (U b: this.stream2) {
				double b_filtration = this.stream2.getFiltrationValue(b);
				this.addSimplexInternal(new HomProductPair<T, U>(a, b), Math.max(a_filtration, b_filtration));
			}
		}
		
		Comparator<DoubleGenericPair<HomProductPair<T, U>>> filteredComparator = new DoubleGenericPairComparator<HomProductPair<T, U>>(this.basisComparator);
		
		// sort simplices by filtration order
		Collections.sort(this.simplices, filteredComparator);
	}
	
	@Override
	public HomProductPair<T, U>[] getBoundary(HomProductPair<T, U> simplex) {
		/*
		 * p = degree of a
		 * 
		 * d(a x b) = da x b + (-1)^p a x db
		 */
		
		T a = simplex.getFirst();
		U b = simplex.getSecond();
		
		T[] d_a = this.stream1.getBoundary(a);
		U[] d_b = this.stream2.getBoundary(b);
		
		HomProductPair<T, U>[] boundary = (HomProductPair<T, U>[]) java.lang.reflect.Array.newInstance(simplex.getClass(), d_a.length + d_b.length);
		
		int currentDimension = simplex.getDimension();
		
		for (int i = 0; i < d_a.length; i++) {
			boundary[i] = new HomProductPair<T, U>(d_a[i], b);
			if (boundary[i].getDimension() != currentDimension - 1) {
				ExceptionUtility.verifyEqual(boundary[i].getDimension(), currentDimension - 1);
			}
		}
		
		for (int i = 0; i < d_b.length; i++) {
			boundary[i + d_a.length] = new HomProductPair<T, U>(a, d_b[i]);
			if (boundary[i].getDimension() != currentDimension - 1) {
				ExceptionUtility.verifyEqual(boundary[i].getDimension(), currentDimension - 1);
			}
		}
		
		return boundary;
	}

	@Override
	public int[] getBoundaryCoefficients(HomProductPair<T, U> simplex) {
		T a = simplex.getFirst();
		U b = simplex.getSecond();
		int[] a_coefficients = this.stream1.getBoundaryCoefficients(a);
		int[] b_coefficients = this.stream2.getBoundaryCoefficients(b);
		
		int[] coefficients = new int[a_coefficients.length + b_coefficients.length];
		
		/*
		 * Compute (-1)^p
		 */
		int p = a.getDimension();
		int multiplier = (p % 2 == 0 ? 1 : -1);
		
		for (int i = 0; i < a_coefficients.length; i++) {
			coefficients[i] = a_coefficients[i];
		}
		
		for (int i = 0; i < b_coefficients.length; i++) {
			coefficients[i + a_coefficients.length] = multiplier * b_coefficients[i];
		}
		
		return coefficients;
	}
}
