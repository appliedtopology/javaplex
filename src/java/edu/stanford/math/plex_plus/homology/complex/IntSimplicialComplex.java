/**
 * 
 */
package edu.stanford.math.plex_plus.homology.complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.simplex.AbstractSimplex;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.hash.THashSet;

/**
 * @author Andrew Tausz
 *
 */
public class IntSimplicialComplex<M extends AbstractSimplex> extends IntChainComplex<M> {
	private final List<M> simplices = new ArrayList<M>();
	private final int dimension;
	private final int[] skeletonSizes;
	private final TObjectIntHashMap<M> locationCache = new TObjectIntHashMap<M>();
	private final Comparator<M> comparator;
	
	/**
	 * 
	 */
	public IntSimplicialComplex(SimplexStream<M> stream, Comparator<M> comparator) {
		this.dimension = stream.getDimension();
		this.comparator = comparator;
		this.skeletonSizes = new int[this.dimension + 1];
		
		for (M element: stream) {
			this.simplices.add(element);
			this.skeletonSizes[element.getDimension()]++;
		}
		
		/*
		 * Sort the set of simplices. This is necessary because in the filtered
		 * complex, the simplices were first sorted by filtration value. Thus, it
		 * may not be sorted by the natural ordering on the simplices.
		 * 
		 * TODO: investigate the efficiency of this
		 */
		Collections.sort(this.simplices, comparator);
	}

	@Override
	public IntFormalSum<M> computeBoundary(M element) {
		IntFormalSum<M> boundary = new IntFormalSum<M>();
		AbstractSimplex[] boundaryElements = element.getBoundaryArray();
		int coefficient = 1;
		for (AbstractSimplex boundaryElement: boundaryElements) {
			boundary.put(coefficient, (M) boundaryElement);
			coefficient *= -1;
		}
		return boundary;
	}

	@Override
	public IntFormalSum<M> computeCoboundary(AbstractSimplex element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public M getAtIndex(int index) {
		return this.simplices.get(index);
	}

	@Override
	public int getBeginningOfSupport() {
		return 0;
	}

	@Override
	public int getEndOfSupport() {
		return this.dimension;
	}

	@Override
	public int getIndex(M element) {
		if (this.locationCache.contains(element)) {
			return this.locationCache.get(element);
		} else {
			// perform binary search to find element
			int index = Collections.binarySearch(this.simplices, element, this.comparator);
			
			// make sure that the element was actually found in the list
			if (!this.simplices.get(index).equals(element)) {
				throw new IllegalArgumentException();
			}
			this.locationCache.put(element, index);
			return index;
		}
	}

	@Override
	public Set<M> getSkeleton(int k) {
		THashSet<M> skeleton = new THashSet<M>();
		int startIndex = 0;
		int numSimplices = this.simplices.size();
		/*
		 * The simplices are stored in sorted order, sorted
		 * first by dimension. So we do a binary search to find
		 * the first simplex of dimension k.
		 * 
		 * If k == 0, we know that we can start at the beginning.
		 */
		
		if (this.simplices.get(numSimplices - 1).getDimension() < k) {
			// there are no simplices of dimension k
			return skeleton;
		}
		
		if (this.simplices.get(0).getDimension() > k) {
			// no simplices of dimension k
			return skeleton;
		}
		
		/*
		 * If this.simplices.get(0).getDimension() > k, then
		 * the starting index is 0, so we don't have to do binary
		 * search.
		 */
		if (this.simplices.get(0).getDimension() < k) {
			// lower index			
			int lowerIndex = 0;
			// upper index
			int upperIndex = numSimplices - 1;
			
			// at this point, dim(simplices[lowerIndex]) < k and
			// dim(simplices[upperIndex]) >= k
			
			// We are ready to do binary search
			
			while (upperIndex > lowerIndex + 1) {
				int midIndex = (upperIndex + lowerIndex) / 2;
				if (this.simplices.get(midIndex).getDimension() < k) {
					lowerIndex = midIndex;
				} else {
					upperIndex = midIndex;
				}
			}
			
			startIndex = upperIndex;
			
		}
		
		for (int i = startIndex; i < numSimplices; i++) {
			M simplex = this.simplices.get(i);
			if (simplex.getDimension() != k) {
				break;
			}
			skeleton.add(simplex);
		}		
		
		return skeleton;
	}

	@Override
	public Iterator<M> iterator() {
		return this.simplices.iterator();
	}

	@Override
	public int getSkeletonSize(int k) {
		return this.skeletonSizes[k];
	}

}
