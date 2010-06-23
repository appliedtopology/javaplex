package edu.stanford.math.plex_plus.homology.complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.hash.THashSet;

/**
 * @author Andrew Tausz
 *
 */
public class IntSimplicialComplex<M extends ChainBasisElement> extends IntChainComplex<M> {
	private final List<M> simplices = new ArrayList<M>();
	private final int dimension;
	private final int[] skeletonSizes;
	private final TObjectIntHashMap<M> locationCache = new TObjectIntHashMap<M>();
	private final TIntIntHashMap skeletonStartIndices = new TIntIntHashMap();
	
	private final Comparator<M> comparator;
	
	/**
	 * Temporary coboundary data structure.
	 * 
	 * TODO: Rethink how the coboundaries are computed and stored.
	 */
	private final Map<M, IntFormalSum<M>> coboundaryMap = new HashMap<M, IntFormalSum<M>>();

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

		/*
		 * Precompute the skeleton start indices.
		 */
		int currentDimension = Integer.MIN_VALUE;
		int tempDimension = 0;
		int n = this.simplices.size();
		for (int index = 0; index < n; index++) {
			M simplex = this.simplices.get(index);
			
			/*
			 * Precompute the skeleton start indices.
			 */
			tempDimension = simplex.getDimension();
			if (tempDimension != currentDimension) {
				currentDimension = tempDimension;
				this.skeletonStartIndices.put(currentDimension, index);
			}
			
			/*
			 * Update coboundary structure.
			 */
			if (tempDimension > 0) {
				ChainBasisElement[] boundaryElements = simplex.getBoundaryArray();
				int coefficient = 1;
				for (ChainBasisElement boundaryElement: boundaryElements) {
					if (!this.coboundaryMap.containsKey(boundaryElement)) {
						this.coboundaryMap.put((M) boundaryElement, new IntFormalSum<M>());
					}
					this.coboundaryMap.get(boundaryElement).put(coefficient, simplex);
					coefficient *= -1;
				}
			}
		}
	}

	@Override
	public IntFormalSum<M> computeBoundary(M element) {
		IntFormalSum<M> boundary = new IntFormalSum<M>();
		ChainBasisElement[] boundaryElements = element.getBoundaryArray();
		int coefficient = 1;
		for (ChainBasisElement boundaryElement: boundaryElements) {
			boundary.put(coefficient, (M) boundaryElement);
			coefficient *= -1;
		}
		return boundary;
	}

	@Override
	public IntFormalSum<M> computeCoboundary(ChainBasisElement element) {
		if (!this.coboundaryMap.containsKey(element)) {
			return new IntFormalSum<M>();
		}
		return this.coboundaryMap.get(element);
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
	public int getIndexWithinSkeleton(M element) {
		int index = this.getIndex(element);
		int skeletonStart = this.skeletonStartIndices.get(element.getDimension());
		return (index - skeletonStart);
	}

	@Override
	public Set<M> getSkeleton(int k) {
		THashSet<M> skeleton = new THashSet<M>();
		int startIndex = this.skeletonStartIndices.get(k);
		int numSimplices = this.simplices.size();
		
		if (!this.skeletonStartIndices.contains(k)) {
			// there are no simplices of dimension k
			return skeleton;
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

	@Override
	public int getDimension(M element) {
		return element.getDimension();
	}

	@Override
	public M getAtIndexWithinSkeleton(int index, int k) {
		return this.getAtIndex(index + this.skeletonStartIndices.get(k));
	}
}
