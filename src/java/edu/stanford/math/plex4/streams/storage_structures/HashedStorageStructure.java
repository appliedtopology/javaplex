/**
 * 
 */
package edu.stanford.math.plex4.streams.storage_structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class provides a stream storage mechanism that is based on a multi-layer map. The motivation
 * behind the design of this class is that the ordering of chain basis elements (e.g. simplices) is 
 * first determined by filtration index, then by dimension, and then by some other ordering (e.g.
 * lexicographical ordering on the vertices). This storage structure uses a sequence of maps to store
 * a basis element. One can think of it as a tree, where the first node is split on the filtration index,
 * the second layer is split on dimension. This class was inspired by the original version of java plex.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class HashedStorageStructure<T extends PrimitiveBasisElement> implements StreamStorageStructure<T> {
	/**
	 * This multilayered map maps filtration index -> dimension -> basis element list. Thus, an element is stored first by
	 * its filtration value, and then by its dimension.
	 */
	private final TIntObjectHashMap<TIntObjectHashMap<List<T>>> indexDimensionObjectMap = new TIntObjectHashMap<TIntObjectHashMap<List<T>>>();
	
	/**
	 * This stores the filtration indices of the basis elements.
	 */
	private final TObjectIntHashMap<T> filtrationIndices = new TObjectIntHashMap<T>();
	
	/**
	 * Indicates whether the storage structure is finalized or not.
	 */
	private boolean isFinalized = false;
	
	/**
	 * Comparator which provides ordering of elements of the stream.
	 */
	private final Comparator<T> basisComparator;
	
	/**
	 * This construction initializes the class with a comparator for comparing the basis elements.
	 * 
	 * @param basisComparator the comparator
	 */
	public HashedStorageStructure(Comparator<T> basisComparator) {
		this.basisComparator = basisComparator;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return new HashedStorageStructureIterator<T>(this.indexDimensionObjectMap);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#addElement(java.lang.Object, int)
	 */
	public void addElement(T basisElement, int filtrationIndex) {
		TIntObjectHashMap<List<T>> dimensionMap = null;
		if (!this.indexDimensionObjectMap.containsKey(filtrationIndex)) {
			dimensionMap = new TIntObjectHashMap<List<T>>();
			this.indexDimensionObjectMap.put(filtrationIndex, dimensionMap);
		} else {
			dimensionMap = this.indexDimensionObjectMap.get(filtrationIndex);
		}
		
		int dimension = basisElement.getDimension();
		List<T> elementList = null;
		
		if (!dimensionMap.containsKey(dimension)) {
			elementList = new ArrayList<T>();
			dimensionMap.put(dimension, elementList);
		} else {
			elementList = dimensionMap.get(dimension);
		}
		
		elementList.add(basisElement);
		
		this.filtrationIndices.put(basisElement, filtrationIndex);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#removeElement(java.lang.Object)
	 */
	public void removeElement(T basisElement) {
		if (!this.filtrationIndices.containsKey(basisElement)) {
			return;
		}
		
		int filtrationIndex = this.filtrationIndices.get(basisElement);
		int dimension = basisElement.getDimension();
		
		this.indexDimensionObjectMap.get(filtrationIndex).get(dimension).remove(basisElement);
		
		// make sure that we do not store empty lists in the storage structure
		if (this.indexDimensionObjectMap.get(filtrationIndex).get(dimension).isEmpty()) {
			this.indexDimensionObjectMap.get(filtrationIndex).remove(dimension);
		}
		
		// remove empty filtration-index map
		if (this.indexDimensionObjectMap.get(filtrationIndex).isEmpty()) {
			this.indexDimensionObjectMap.remove(filtrationIndex);
		}
		
		this.filtrationIndices.remove(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#containsElement(java.lang.Object)
	 */
	public boolean containsElement(T basisElement) {
		return this.filtrationIndices.containsKey(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getFiltrationIndex(java.lang.Object)
	 */
	public int getFiltrationIndex(T basisElement) {
		return this.filtrationIndices.get(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#isFinalized()
	 */
	public boolean isFinalized() {
		return this.isFinalized;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getBasisComparator()
	 */
	public Comparator<T> getBasisComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getSize()
	 */
	public int getSize() {
		return this.filtrationIndices.size();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#finalizeStructure()
	 */
	public void finalizeStructure() {
		// we must go through the map and sort the list segments
		
		for (TIntObjectIterator<TIntObjectHashMap<List<T>>> filtrationIndexIterator = this.indexDimensionObjectMap.iterator(); filtrationIndexIterator.hasNext(); ) {
			filtrationIndexIterator.advance();
			TIntObjectHashMap<List<T>> dimensionMap = filtrationIndexIterator.value();
			
			for (TIntObjectIterator<List<T>> dimensionIterator = dimensionMap.iterator(); dimensionIterator.hasNext(); ) {
				dimensionIterator.advance();
				Collections.sort(dimensionIterator.value(), this.basisComparator);
			}
		}
		
		this.isFinalized = true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getMaximumFiltrationIndex()
	 */
	public int getMaximumFiltrationIndex() {
		int maxIndex = Integer.MIN_VALUE;
		for (TIntObjectIterator<TIntObjectHashMap<List<T>>> filtrationIndexIterator = this.indexDimensionObjectMap.iterator(); filtrationIndexIterator.hasNext(); ) {
			filtrationIndexIterator.advance();
			if (filtrationIndexIterator.key() > maxIndex) {
				maxIndex = filtrationIndexIterator.key();
			}
		}
		
		return maxIndex;
	}
	
	public int getMinimumFiltrationIndex() {
		int minIndex = Integer.MAX_VALUE;
		for (TIntObjectIterator<TIntObjectHashMap<List<T>>> filtrationIndexIterator = this.indexDimensionObjectMap.iterator(); filtrationIndexIterator.hasNext(); ) {
			filtrationIndexIterator.advance();
			if (filtrationIndexIterator.key() < minIndex) {
				minIndex = filtrationIndexIterator.key();
			}
		}
		
		return minIndex;
	}
}
