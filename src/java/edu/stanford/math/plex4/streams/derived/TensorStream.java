/**
 * 
 */
package edu.stanford.math.plex4.streams.derived;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.array.ObjectArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPairComparator;
import edu.stanford.math.primitivelib.collections.utility.PairwiseIterator;

/**
 * This implements the tensor product of two filtered chain complexes. For the definition
 * of the tensor product of complexes, one is invited to consult any standard textbook on
 * algebraic topology or homological algebra. Note that we define the filtration index for
 * a tensor product pair to be the maximum of the filtration indices of its components. This
 * definition was chosen since it was the only definition that makes sense - a pair comes
 * into existence exactly when both components have come into existence.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the first chain complex
 * @param <U> the underlying basis type of the second chain complex
 */
public class TensorStream<T, U> implements AbstractFilteredStream<ObjectObjectPair<T, U>> {
	
	/**
	 * The first factor in the tensor product. 
	 */
	protected final AbstractFilteredStream<T> stream1;
	
	/**
	 * The second factor in the tensor product.
	 */
	protected final AbstractFilteredStream<U> stream2;
	
	/**
	 * This constructor initializes the stream from two underlying filtered chain complexes.
	 * 
	 * @param stream1 the first chain complex
	 * @param stream2 the second chain complex
	 */
	public TensorStream(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2) {
		this.stream1 = stream1;
		this.stream2 = stream2;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundary(java.lang.Object)
	 */
	public ObjectObjectPair<T, U>[] getBoundary(ObjectObjectPair<T, U> basisElement) {
		/*
		 * p = degree of a
		 * 
		 * d(a x b) = da x b + (-1)^{n+1} a x db
		 */
		
		T a = basisElement.getFirst();
		U b = basisElement.getSecond();
		
		T[] d_a = this.stream1.getBoundary(a);
		U[] d_b = this.stream2.getBoundary(b);
	
		ObjectObjectPair<T, U>[] boundary = ObjectArrayUtility.createArray(d_a.length + d_b.length, basisElement);
		
		for (int i = 0; i < d_a.length; i++) {
			boundary[i] = new ObjectObjectPair<T, U>(d_a[i], b);
		}
		
		for (int i = 0; i < d_b.length; i++) {
			boundary[i + d_a.length] = new ObjectObjectPair<T, U>(a, d_b[i]);
		}
		
		return boundary;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundaryCoefficients(java.lang.Object)
	 */
	public int[] getBoundaryCoefficients(ObjectObjectPair<T, U> basisElement) {
		T a = basisElement.getFirst();
		U b = basisElement.getSecond();
		int[] a_coefficients = this.stream1.getBoundaryCoefficients(a);
		int[] b_coefficients = this.stream2.getBoundaryCoefficients(b);
		
		int[] coefficients = new int[a_coefficients.length + b_coefficients.length];

		int n = this.getDimension(basisElement);
		
		/*
		 * Compute (-1)^{n+1}
		 */
		int multiplier = ((n + 1) % 2 == 0 ? 1 : -1);
		
		for (int i = 0; i < a_coefficients.length; i++) {
			coefficients[i] = a_coefficients[i];
		}
		
		for (int i = 0; i < b_coefficients.length; i++) {
			coefficients[i + a_coefficients.length] = multiplier * b_coefficients[i];
		}
		
		return coefficients;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getDimension(java.lang.Object)
	 */
	public int getDimension(ObjectObjectPair<T, U> basisElement) {
		return (this.stream1.getDimension(basisElement.getFirst()) + this.stream2.getDimension(basisElement.getSecond()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<ObjectObjectPair<T, U>> iterator() {
		return new PairwiseIterator<T, U>(this.stream1, this.stream2);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getFiltrationIndex(java.lang.Object)
	 */
	public int getFiltrationIndex(ObjectObjectPair<T, U> basisElement) {
		return Math.max(this.stream1.getFiltrationIndex(basisElement.getFirst()), this.stream2.getFiltrationIndex(basisElement.getSecond()));
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#finalizeStream()
	 */
	public void finalizeStream() {
		if (!this.stream1.isFinalized()) {
			this.stream1.finalizeStream();
		}
		
		if (!this.stream2.isFinalized()) {
			this.stream2.finalizeStream();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#isFinalized()
	 */
	public boolean isFinalized() {
		return this.stream1.isFinalized() && this.stream2.isFinalized();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getSize()
	 */
	public int getSize() {
		return this.stream1.getSize() * this.stream2.getSize();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getMaximumFiltrationIndex()
	 */
	public int getMaximumFiltrationIndex() {
		return Math.max(this.stream1.getMaximumFiltrationIndex(), this.stream2.getMaximumFiltrationIndex());
	}

	public Comparator<ObjectObjectPair<T, U>> getBasisComparator() {
		return new ObjectObjectPairComparator<T, U>(this.stream1.getBasisComparator(), this.stream2.getBasisComparator());
	}
}
