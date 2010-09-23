/**
 * 
 */
package edu.stanford.math.plex4.homology.streams.derived;

import java.util.Comparator;

import edu.stanford.math.plex4.array_utility.ArrayCreation;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.datastructures.pairs.GenericPairComparator;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.interfaces.DerivedStream;
import edu.stanford.math.plex4.utility.ExceptionUtility;


/**
 * 
 * @author Andrew Tausz
 *
 */
public class TensorStream<T, U> extends DerivedStream<GenericPair<T, U>>{
	protected final AbstractFilteredStream<T> stream1;
	protected final AbstractFilteredStream<U> stream2;
	
	public TensorStream(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2, Comparator<T> TComparator, Comparator<U> UComparator) {
		super(new GenericPairComparator<T, U>(TComparator, UComparator));
		this.stream1 = stream1;
		this.stream2 = stream2;
	}

	public GenericPair<T, U>[] getBoundary(GenericPair<T, U> basisElement) {
		/*
		 * p = degree of a
		 * 
		 * d(a x b) = da x b + (-1)^p a x db
		 */
		
		T a = basisElement.getFirst();
		U b = basisElement.getSecond();
		
		T[] d_a = this.stream1.getBoundary(a);
		U[] d_b = this.stream2.getBoundary(b);
	
		GenericPair<T, U>[] boundary = ArrayCreation.newGenericArray(d_a.length + d_b.length, basisElement);
		
		int currentDimension = this.getDimension(basisElement);
		
		for (int i = 0; i < d_a.length; i++) {
			boundary[i] = new GenericPair<T, U>(d_a[i], b);
			ExceptionUtility.verifyEqual(this.getDimension(boundary[i]), currentDimension - 1);
		}
		
		for (int i = 0; i < d_b.length; i++) {
			boundary[i + d_a.length] = new GenericPair<T, U>(a, d_b[i]);
			ExceptionUtility.verifyEqual(this.getDimension(boundary[i + d_a.length]), currentDimension - 1);
		}
		
		return boundary;
	}

	public int[] getBoundaryCoefficients(GenericPair<T, U> basisElement) {
		T a = basisElement.getFirst();
		U b = basisElement.getSecond();
		int[] a_coefficients = this.stream1.getBoundaryCoefficients(a);
		int[] b_coefficients = this.stream2.getBoundaryCoefficients(b);
		
		int[] coefficients = new int[a_coefficients.length + b_coefficients.length];
		
		int n = this.getDimension(basisElement);
		
		/*
		 * Compute (-1)^p
		 */
		int p = this.stream1.getDimension(a);
		int multiplier = ((n + 1) % 2 == 0 ? 1 : -1);
		
		for (int i = 0; i < a_coefficients.length; i++) {
			coefficients[i] = a_coefficients[i];
		}
		
		for (int i = 0; i < b_coefficients.length; i++) {
			coefficients[i + a_coefficients.length] = multiplier * b_coefficients[i];
		}
		
		return coefficients;
	}

	public int getDimension(GenericPair<T, U> basisElement) {
		return (this.stream1.getDimension(basisElement.getFirst()) + this.stream2.getDimension(basisElement.getSecond()));
	}

	@Override
	protected void constructDerivedStream() {
		for (T a: this.stream1) {
			double a_filtration = this.stream1.getFiltrationValue(a);
			for (U b: this.stream2) {
				double b_filtration = this.stream2.getFiltrationValue(b);
				this.storageStructure.addElement(new GenericPair<T, U>(a, b), Math.max(a_filtration, b_filtration));
			}
		}
	}
	
	@Override
	protected void finalizeUnderlyingStreams() {
		if (!this.stream1.isFinalized()) {
			this.stream1.finalizeStream();
		}
		
		if (!this.stream2.isFinalized()) {
			this.stream2.finalizeStream();
		}
	}	
}
