/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.interfaces;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex_plus.homology.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.plex_plus.homology.streams.storage_structures.StreamStorageStructureFactory;

/**
 * This abstract class provides functionality for a filtered chain complex which is built from
 * one or more underlying filtered chain complexes. An example would be the tensor product
 * construction of two chain complexes. 
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type
 */
public abstract class DerivedStream<T> implements AbstractFilteredStream<T> {
	
	/**
	 * This object implements the actual storage structure for the stream.
	 */
	protected final StreamStorageStructure<T> storageStructure;
	
	/**
	 * This constructor constructs the stream with the supplied StreamStorageStructure.
	 * 
	 * @param storageStructure the StreamStorageStructure to use
	 */
	protected DerivedStream(StreamStorageStructure<T> storageStructure) {
		this.storageStructure = storageStructure;
	}
	
	/**
	 * This constructor construct the stream with the default storage scheme.
	 * 
	 * @param comparator a Comparator which defines an ordering for the storage scheme
	 */
	protected DerivedStream(Comparator<T> comparator) {
		this.storageStructure = StreamStorageStructureFactory.getDefaultStorageStructure(comparator);
	}
	
	/**
	 * This function constructs the contents of the actual derived stream.
	 */
	protected abstract void constructDerivedStream();
	
	/**
	 * This function finalizes all of the underlying streams that this stream relies on.
	 */
	protected abstract void finalizeUnderlyingStreams();
	
	public final Iterator<T> iterator() {
		return this.storageStructure.iterator();
	}

	public final void finalizeStream() {
		this.finalizeUnderlyingStreams();
		this.constructDerivedStream();
		this.storageStructure.sortByFiltration();
		this.storageStructure.setAsFinalized();
	}

	public final double getFiltrationValue(T basisElement) {
		return this.storageStructure.getFiltrationValue(basisElement);
	}

	public final boolean isFinalized() {
		return this.storageStructure.isFinalized();
	}
	
	public Comparator<T> getDerivedComparator() {
		return this.storageStructure.getBasisComparator();
	}
}
