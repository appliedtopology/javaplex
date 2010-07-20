package edu.stanford.math.plex4.homology.streams.interfaces;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructureFactory;

/**
 * This class implements a filtered chain complex where the underlying type
 * is a geometric primitive (a Simplex or a Cell). Thus the appropriate
 * homological functions are defined by the geometric properties of the
 * underlying type. For example, the homological dimension is given by
 * the actual geometric dimension, and the boundary is the geometric boundary
 * of the cell or simplex in question.
 * 
 * Note that this class does not actually implement a storage mechanism, but
 * allows a user to define one via a supplied StreamStorageStructure object.
 * In the event that the user does not supply one, it reverts to the default
 * one.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the stream
 */
public abstract class PrimitiveStream<T extends PrimitiveBasisElement> implements AbstractFilteredStream<T>  {
	
	/**
	 * This object implements the actual storage structure for the stream.
	 */
	protected final StreamStorageStructure<T> storageStructure;
	
	/**
	 * This constructor constructs the stream with the supplied StreamStorageStructure.
	 * 
	 * @param storageStructure the StreamStorageStructure to use
	 */
	protected PrimitiveStream(StreamStorageStructure<T> storageStructure) {
		this.storageStructure = storageStructure;
	}
	
	/**
	 * This constructor construct the stream with the default storage scheme.
	 * 
	 * @param comparator a Comparator which defines an ordering for the storage scheme
	 */
	protected PrimitiveStream(Comparator<T> comparator) {
		this.storageStructure = StreamStorageStructureFactory.getDefaultStorageStructure(comparator);
	}
	
	/**
	 * This abstract function performs the construction of the filtered chain complex. For
	 * example, this might construct the complex from a metric space via a Vietoris-Rips
	 * or witness construction.
	 */
	protected abstract void constructComplex();

	@SuppressWarnings("unchecked")
	public final T[] getBoundary(T basisElement) {
		return (T[]) basisElement.getBoundaryArray();
	}

	public final int[] getBoundaryCoefficients(T basisElement) {
		return basisElement.getBoundaryCoefficients();
	}
	
	public final int getDimension(T basisElement) {
		return basisElement.getDimension();
	}
	
	public final Iterator<T> iterator() {
		return this.storageStructure.iterator();
	}

	public final void finalizeStream() {
		this.constructComplex();
		this.storageStructure.sortByFiltration();
		this.storageStructure.setAsFinalized();
	}

	public final double getFiltrationValue(T basisElement) {
		return this.storageStructure.getFiltrationValue(basisElement);
	}

	public final boolean isFinalized() {
		return this.storageStructure.isFinalized();
	}
	
	/**
	 * This function validates the stream to make sure that it
	 * contains a valid filtered simplicial or cell complex. It checks the
	 * two following conditions:
	 * 1. For each element in the complex, all of the faces of the simplex
	 * also belong to the complex.
	 * 2. The faces of each simplex have filtration values that are
	 * less than or equal to those of its cofaces.
	 * 
	 * @return true if the stream is consistent, false otherwise
	 */
	public boolean validate() {	
		for (T basisElement: this.storageStructure) {
			double filtrationValue = this.getFiltrationValue(basisElement);

			// get the boundary
			T[] boundary = this.getBoundary(basisElement);

			// make sure that each boundary element is also inside the
			// complex with a filtration value less than or equal to the
			// current simplex
			for (T face: boundary) {
				
				// if the face's filtration value is greater than that of the
				// current simplex, the stream is also inconsistent
				if (this.storageStructure.getFiltrationValue(face) > filtrationValue) {
					return false;
				}
			}
		}

		// all simplices in the complex have been checked - good, return true
		return true;
	}
}
