package edu.stanford.math.plex_plus.homology.complex;

import java.util.Set;

import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import gnu.trove.iterator.TObjectIntIterator;

/**
 * This abstract class defines the functionality of 
 * @author Andris
 *
 * @param <M>
 */
public abstract class IntChainComplex<M> implements Iterable<M> {
	/**
	 * This function returns the boundary of an element of the chain 
	 * complex.
	 * 
	 * @param element
	 * @return
	 */
	public abstract IntFormalSum<M> computeBoundary(M element);
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public abstract IntFormalSum<M> computeCoboundary(M element);
	
	public abstract int getDimension(M element);
	
	/**
	 * 
	 * @return
	 */
	public abstract int getBeginningOfSupport();
	
	/**
	 * 
	 * @return
	 */
	public abstract int getEndOfSupport();
	
	/**
	 * This function returns the index of the supplied simplex,
	 * viewed as a member of the current chain complex.
	 * 
	 * @param element the simplex to query
	 * @return the index of the simplex within the complex
	 */
	public abstract int getIndex(M element);
	
	/**
	 * This function returns the element at the supplied index.
	 * This relies on the defined ordering on the class M.
	 * 
	 * @param index the index of the simplex to retrieve
	 * @return the simplex at the given index within the complex
	 */
	public abstract M getAtIndex(int index);
	
	/**
	 * This function returns the k-skeleton of the chain complex, which
	 * is defined to be the set of elements in the complex with dimension
	 * less than or equal to k.
	 * 
	 * @param k the dimension
	 * @return the k-skeleton of the chain complex
	 */
	public abstract Set<M> getSkeleton(int k);
	
	public abstract int getSkeletonSize(int k);
	
	public abstract int getIndexWithinSkeleton(M element);
	
	public abstract M getAtIndexWithinSkeleton(int index, int k);
	
	public int[][] getDenseBoundaryMatrix(int k) {
		Set<M> kSkeleton = this.getSkeleton(k);
		
		int[][] result = new int[this.getSkeletonSize(k - 1)][this.getSkeletonSize(k)];
		int j = 0;
		int i = 0;
		for (M element: kSkeleton) {
			j = this.getIndexWithinSkeleton(element);
			IntFormalSum<M> boundary = this.computeBoundary(element);
			for (TObjectIntIterator<M> iterator = boundary.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				i = this.getIndexWithinSkeleton(iterator.key());
				result[i][j] = iterator.value();
			}
		}
		return result;		
	}
	
	public IntFormalSum<M> getSumRepresentation(int[] vector, int k) {
		IntFormalSum<M> sum = new IntFormalSum<M>();
		
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] != 0) {
				sum.put(vector[i], this.getAtIndexWithinSkeleton(i, k));
			}
		}
		
		return sum;
	}
	
	public GenericFormalSum<Double, M> getSumRepresentation(double[] vector, int k) {
		GenericFormalSum<Double, M> sum = new GenericFormalSum<Double, M>();
		
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] != 0) {
				sum.put(vector[i], this.getAtIndexWithinSkeleton(i, k));
			}
		}
		
		return sum;
	}
}
