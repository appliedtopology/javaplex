package edu.stanford.math.plex4.homology.streams.storage_structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

public class HashedStorageStructure implements StreamStorageStructure<Simplex> {
	
	private class HashedStorageStructureIterator implements Iterator<Simplex> {
		private final HashedStorageStructure storageStructure;
		private int fiIndex = 0;
		private int dIndex = 0;
		private int fIndexKey;
		private int dimKey;
		private Iterator<Simplex> segmentIterator;
		
		private int[] fIndexKeys;
		private int[] dimensionKeys;
		
		public HashedStorageStructureIterator(HashedStorageStructure storageStructure) {
			this.storageStructure = storageStructure;
			this.fIndexKeys = this.storageStructure.items.keys();
			Arrays.sort(this.fIndexKeys);
			fiIndex = 0;
			fIndexKey = fIndexKeys[fiIndex];
			dimensionKeys = this.storageStructure.items.get(fIndexKey).keys();
			Arrays.sort(dimensionKeys);
			dIndex = 0;
			dimKey = dimensionKeys[dIndex];
			Collections.sort(storageStructure.items.get(fIndexKey).get(dimKey), storageStructure.basisComparator);
			segmentIterator = storageStructure.items.get(fIndexKey).get(dimKey).iterator();
		}
		
		public boolean hasNext() {
			return ((fiIndex < fIndexKeys.length - 1) || (dIndex < dimensionKeys.length - 1)  || segmentIterator.hasNext());
		}

		public Simplex next() {
			if (!segmentIterator.hasNext()) {
				if (dIndex == dimensionKeys.length - 1) {
					if (fiIndex == fIndexKeys.length - 1) {
						// nothing to do - can't advance further
						return null;
					} else {
						// fiIndex < fIndexKeys.length - 1
						// we increment the filtration index
						fiIndex++;
						fIndexKey = fIndexKeys[fiIndex];
						dimensionKeys = this.storageStructure.items.get(fIndexKey).keys();
						Arrays.sort(dimensionKeys);
						dIndex = 0;
						dimKey = dimensionKeys[dIndex];
						Collections.sort(storageStructure.items.get(fIndexKey).get(dimKey), storageStructure.basisComparator);
						segmentIterator = storageStructure.items.get(fIndexKey).get(dimKey).iterator();
						return segmentIterator.next();
					}
				} else {
					// (dIndex < dimensionKeys.length - 1)
					dIndex++;
					dimKey = dimensionKeys[dIndex];
					Collections.sort(storageStructure.items.get(fIndexKey).get(dimKey), storageStructure.basisComparator);
					segmentIterator = storageStructure.items.get(fIndexKey).get(dimKey).iterator();
					return segmentIterator.next();
				}
			} else {
				// segmentIterator.hasNext()
				return segmentIterator.next();
			}
		}

		public void remove() {
			segmentIterator.remove();
		}
		
	}
	
	private final TIntObjectHashMap<TIntObjectHashMap<ArrayList<Simplex>>> items = new TIntObjectHashMap<TIntObjectHashMap<ArrayList<Simplex>>>();
	//private final ArrayList<ArrayList<ArrayList<Simplex>>> items = new ArrayList<ArrayList<ArrayList<Simplex>>>();
	private final TObjectIntHashMap<Simplex> filtrationIndices = new TObjectIntHashMap<Simplex>();
	
	/**
	 * Comparator which provides ordering of elements of the stream.
	 */
	private final Comparator<Simplex> basisComparator;
	private boolean finalized = false;
	
	public HashedStorageStructure(Comparator<Simplex> basisComparator) {
		this.basisComparator = basisComparator;

	}
	public void addElement(Simplex basisElement, int filtrationIndex) {
		int dimension = basisElement.getDimension();
		this.ensureCapacity(filtrationIndex, dimension);
		items.get(filtrationIndex).get(dimension).add(basisElement);
		this.filtrationIndices.put(basisElement, filtrationIndex);
	}

	public boolean containsElement(Simplex basisElement) {
		return this.filtrationIndices.containsKey(basisElement);
	}

	public Comparator<Simplex> getBasisComparator() {
		return this.basisComparator;
	}

	public int getFiltrationIndex(Simplex basisElement) {
		return this.filtrationIndices.get(basisElement);
	}

	public int getSize() {
		return this.filtrationIndices.size();
	}

	public boolean isFinalized() {
		return this.finalized;
	}

	public void removeElement(Simplex basisElement) {
		int filtrationIndex = this.filtrationIndices.get(basisElement);
		if (!this.filtrationIndices.containsKey(basisElement)) {
			return;
		}
		int dimension = basisElement.getDimension();
		
		this.filtrationIndices.remove(basisElement);
		items.get(filtrationIndex).get(dimension).remove(basisElement);
	}

	public void setAsFinalized() {
		this.finalized = true;
	}

	public void sortByFiltration() {
		// Nothing to do
	}

	public void updateOrAddElement(Simplex basisElement, int newFiltrationIndex) {
		if (this.containsElement(basisElement)) {
			int oldFiltrationIndex = this.filtrationIndices.get(basisElement);
			int dimension = basisElement.getDimension();
			
			// remove old item
			items.get(oldFiltrationIndex).get(dimension).remove(basisElement);
			// add new item
			this.ensureCapacity(newFiltrationIndex, dimension);
			items.get(newFiltrationIndex).get(dimension).add(basisElement);
			
			this.filtrationIndices.adjustOrPutValue(basisElement, newFiltrationIndex, newFiltrationIndex);
		} else {
			this.addElement(basisElement, newFiltrationIndex);
		}
	}

	public Iterator<Simplex> iterator() {
		return new HashedStorageStructureIterator(this);
	}
	
	private void ensureCapacity(int filtrationIndex, int dimension) {
		if (!this.items.containsKey(filtrationIndex)) {
			items.put(filtrationIndex, new TIntObjectHashMap<ArrayList<Simplex>>());
		}
		if (!this.items.get(filtrationIndex).containsKey(dimension)) {
			items.get(filtrationIndex).put(dimension, new ArrayList<Simplex>());
		}
	}
}
