package edu.stanford.math.plex4.streams.derived;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
//import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.utility.Infinity;

public class TruncatedStream<T> implements AbstractFilteredStream<T> {
	private final AbstractFilteredStream<T> underlyingStream;
	private final double maxFiltrationValue;
	private final int maxFiltrationIndex;
	private final int size;

	public TruncatedStream(AbstractFilteredStream<T> stream, double maxFiltrationValue) {
		this.underlyingStream = stream;
		this.maxFiltrationValue = maxFiltrationValue;
		int tempMaxFiltrationIndex = Infinity.Int.getNegativeInfinity();
		int tempSize = 0;

		for (T element : this.underlyingStream) {
			if (this.underlyingStream.getFiltrationValue(element) > this.maxFiltrationValue) {
				break;
			}

			tempMaxFiltrationIndex = Math.max(tempMaxFiltrationIndex, this.underlyingStream.getFiltrationIndex(element));
			tempSize++;
		}

		this.size = tempSize;
		this.maxFiltrationIndex = tempMaxFiltrationIndex;
	}

	public void finalizeStream() {
		if (!this.underlyingStream.isFinalized()) {
			this.underlyingStream.finalizeStream();
		}
	}

	public Comparator<T> getBasisComparator() {
		return this.underlyingStream.getBasisComparator();
	}

	public T[] getBoundary(T basisElement) {
		return this.underlyingStream.getBoundary(basisElement);
	}

	public int[] getBoundaryCoefficients(T basisElement) {
		return this.underlyingStream.getBoundaryCoefficients(basisElement);
	}

	public int getDimension(T basisElement) {
		return this.underlyingStream.getDimension(basisElement);
	}

	public int getFiltrationIndex(T basisElement) {
		return this.getFiltrationIndex(basisElement);
	}

	public double getFiltrationValue(T basisElement) {
		return this.getFiltrationValue(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#containsElement(java.lang.Object)
	 */
	public boolean containsElement(T basisElement) {
		// TODO Implement this if you need it.
		throw new UnsupportedOperationException();
	}

	public int getMaximumFiltrationIndex() {
		return this.maxFiltrationIndex;
	}

	public int getMinimumFiltrationIndex() {
		return this.underlyingStream.getMinimumFiltrationIndex();
	}

	public int getSize() {
		return this.size;
	}

	public boolean isFinalized() {
		return this.underlyingStream.isFinalized();
	}

	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection) {
		return this.underlyingStream.transform(barcodeCollection);
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Iterator<T> internalIterator = underlyingStream.iterator();
			T next = null;

			{
				this.next = this.internalIterator.next();
			}

			public boolean hasNext() {
				if (next == null) {
					return false;
				}
				return (underlyingStream.getFiltrationValue(next) <= maxFiltrationValue);
			}

			public T next() {
				T returnValue = this.next;
				this.next = this.internalIterator.next();
				return returnValue;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
}
