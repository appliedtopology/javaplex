package edu.stanford.math.plex4.streams.storage_structures;

import gnu.trove.TIntObjectHashMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements the Iterator interface for a HashedStorageStructure. It allows
 * the user to traverse the structure in increasing order of filtration index and dimension.
 * 
 * @author Andrew Tausz
 *
 * @param <E>
 */
public class HashedStorageStructureIterator<E> implements Iterator<E> {
	private final TIntObjectHashMap<TIntObjectHashMap<List<E>>> indexDimensionObjectMap;
	private Iterator<E> listIterator = null;
	private int[] filtrationIndices = null;
	private int[] dimensions = null;
	
	private int filtrationIndexIndex = 0;
	private int dimensionIndex = 0;
	
	private int currentFiltrationIndex;
	private int currentDimension;
	
	/**
	 * This constructor accepts a filtration index -> dimension -> list mapping.
	 * 
	 * @param indexDimensionObjectMap
	 */
	public HashedStorageStructureIterator(TIntObjectHashMap<TIntObjectHashMap<List<E>>> indexDimensionObjectMap) {
		this.indexDimensionObjectMap = indexDimensionObjectMap;
		
		this.filtrationIndices = this.indexDimensionObjectMap.keys();
		Arrays.sort(this.filtrationIndices);
		this.filtrationIndexIndex = 0;
		this.currentFiltrationIndex = this.filtrationIndices[this.filtrationIndexIndex];
		
		this.dimensions = this.indexDimensionObjectMap.get(this.currentFiltrationIndex).keys();
		Arrays.sort(this.dimensions);
		this.dimensionIndex = 0;
		this.currentDimension = this.dimensions[this.dimensionIndex];
		
		this.listIterator = this.indexDimensionObjectMap.get(currentFiltrationIndex).get(currentDimension).iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (this.listIterator.hasNext()) {
			return true;
		}
		
		if (this.dimensionIndex < this.dimensions.length - 1) {
			return true;
		}
		
		if (this.filtrationIndexIndex < this.filtrationIndices.length - 1) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		if (this.advanceToNextIterator()) {
			return this.listIterator.next();
		}
		
		return null;
	}

	/**
	 * Currently we do not support removal from a hashed storage structure via an iterator.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This function advances the filtration and dimension indices until it can find a list
	 * segment that has a next element. If the current list segment has a next element, then 
	 * it does nothing. In the even that this method cannot find a non-empty list segment, it
	 * returns false, otherwise it returns true.
	 * 
	 * @return true if the listIterator has a next element and false otherwise
	 */
	private boolean advanceToNextIterator() {
		if (this.listIterator.hasNext()) {
			return true;
		}
		
		// move forward in dimension if possible
		if (this.dimensionIndex < this.dimensions.length - 1) {
			this.dimensionIndex++;
			this.currentDimension = this.dimensions[this.dimensionIndex];
			Arrays.sort(this.dimensions);
			this.listIterator = this.indexDimensionObjectMap.get(currentFiltrationIndex).get(currentDimension).iterator();
			return true;
		}
		
		// if we can't move forward in dimension, advance the filtration index if possible 
		if (this.filtrationIndexIndex < this.filtrationIndices.length - 1) {
			this.filtrationIndexIndex++;
			this.currentFiltrationIndex = this.filtrationIndices[this.filtrationIndexIndex];
			
			this.dimensions = this.indexDimensionObjectMap.get(this.currentFiltrationIndex).keys();
			Arrays.sort(this.dimensions);
			this.dimensionIndex = 0;
			this.currentDimension = this.dimensions[this.dimensionIndex];
			
			this.listIterator = this.indexDimensionObjectMap.get(currentFiltrationIndex).get(currentDimension).iterator();
			return true;
		}
		
		return false;
	}

}
