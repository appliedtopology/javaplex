package edu.stanford.math.plex_plus.homology.complex;

import java.util.Iterator;
import java.util.Set;

import edu.stanford.math.plex_plus.algebraic_structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntRing;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.set.hash.THashSet;

/**
 * @author Andrew Tausz
 *
 */
public class IntHomComplex<M> extends IntChainComplex<GenericPair<M, M>> {
	private final IntChainComplex<M> C;
	private final IntChainComplex<M> D;
	private final IntFreeModule<GenericPair<M, M>> module;
	
	
	
	public IntHomComplex(IntChainComplex<M> C, IntChainComplex<M> D, IntRing ring) {
		this.C = C;
		this.D = D;
		this.module = new IntFreeModule<GenericPair<M, M>>(ring);
	}

	@Override
	public IntFormalSum<GenericPair<M, M>> computeBoundary(GenericPair<M, M> element) {
		IntFormalSum<GenericPair<M, M>> result = new IntFormalSum<GenericPair<M, M>>();
		M a = element.getFirst();
		M b = element.getSecond();
		
		/*
		 * Compute the boundary of the tensor a* x b by the following formula:
		 * 
		 * d(a* x b) = (d*a*) x b + (-1)^p * a* x db
		 * 
		 * Where we use x to denote the tensor product, and d to denote the boundary
		 * operator on the chain complexes C and D, and d* to be the coboundary operator.
		 * Also, p is the dimension of a.
		 * 
		 */
		
		IntFormalSum<M> aCoboundary = this.C.computeCoboundary(a);
		int aDimension = this.C.getDimension(a);
		int multiplier = (aDimension % 2 == 0 ? 1 : -1);
		
		for (TObjectIntIterator<M> iterator = aCoboundary.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			M coboundaryElement = iterator.key();
			int coefficient = iterator.value();
			this.module.addObject(result, coefficient, new GenericPair<M, M>(coboundaryElement, b));
		}
		
		IntFormalSum<M> bBoundary = this.D.computeBoundary(b);
		
		for (TObjectIntIterator<M> iterator = bBoundary.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			M boundaryElement = iterator.key();
			int coefficient = multiplier * iterator.value();
			this.module.addObject(result, coefficient, new GenericPair<M, M>(a, boundaryElement));
		}
		
		return result;
	}

	@Override
	public IntFormalSum<GenericPair<M, M>> computeCoboundary(GenericPair<M, M> element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPair<M, M> getAtIndex(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(GenericPair<M, M> element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<GenericPair<M, M>> getSkeleton(int n) {
		THashSet<GenericPair<M, M>> skeleton = new THashSet<GenericPair<M, M>>();		
		
		/*
		 * Hom_n(C, D) = \Prod_{p \in Z} C^p x D_{p+n}
		 * 
		 * Thus we want
		 * p >= beginning of support of C
		 * p <= end of support of C
		 * 
		 * p+n >= beginning of support of D
		 * p+n <= end of support of D
		 */
		
		int min_p = Math.max(this.C.getBeginningOfSupport(), this.D.getBeginningOfSupport() - n);
		int max_p = Math.min(this.C.getEndOfSupport(), this.D.getEndOfSupport() - n);
		
		/*
		 * For each p, add the basis corresponding to
		 * C^p x D_{p+n}
		 */
		for (int p = min_p; p <= max_p; p++) {
			Set<M> C_p = this.C.getSkeleton(p);
			Set<M> D_pn = this.D.getSkeleton(p + n);
			
			for (M C_p_element: C_p) {
				for (M D_pn_element: D_pn) {
					skeleton.add(new GenericPair<M, M>(C_p_element, D_pn_element));
				}
			}
		}
		
		return skeleton;
	}

	@Override
	public Iterator<GenericPair<M, M>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Explanation for support computation:
	 * 
	 * 				 __
	 * Hom_n(C, D) = || C^p x D_{p+n}
	 * 
	 * Let {m_C, ..., M_C}, {m_D, ..., M_D} be the supports of 
	 * C and D, respectively. Then we need to enforce the following
	 * 
	 * p + n >= m_D
	 * n >= m_D - p, for all p such that m_C <= p <= M_C
	 * So, we need n >= m_D - M_C
	 * 
	 * Also,
	 * 
	 * p + n <= M_D
	 * n <= M_D - p, for all p such that m_C <= p <= M_C
	 * So, we need n <= M_D - m_C
	 * 
	 * So: 	beginning of support = m_D - M_C
	 * 		end of support		 = M_D - m_C
	 * 
	 */
	
	/*
	 * (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.complex.IntChainComplex#getBeginningOfSupport()
	 */
	@Override
	public int getBeginningOfSupport() {
		return (this.D.getBeginningOfSupport() - this.C.getEndOfSupport());
	}

	/*
	 * (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.complex.IntChainComplex#getEndOfSupport()
	 */
	@Override
	public int getEndOfSupport() {
		return (this.D.getEndOfSupport() - this.C.getBeginningOfSupport());
	}

	@Override
	public int getSkeletonSize(int n) {
		/*
		 * Hom_n(C, D) = \Prod_{p \in Z} C^p x D_{p+n}
		 * 
		 * Thus we want
		 * p >= beginning of support of C
		 * p <= end of support of C
		 * 
		 * p+n >= beginning of support of D
		 * p+n <= end of support of D
		 * 
		 * Size of n-skeleton of Hom-complex is 
		 * \sum_{p \in Z} |C^p| * |D_{p_n}|
		 * 
		 */
		
		int min_p = Math.max(this.C.getBeginningOfSupport(), this.D.getBeginningOfSupport() - n);
		int max_p = Math.min(this.C.getEndOfSupport(), this.D.getEndOfSupport() - n);
		
		int size = 0;
		
		for (int p = min_p; p <= max_p; p++) {
			size += this.C.getSkeletonSize(p) * this.D.getSkeletonSize(p + n);
		}
		
		return size;
	}

	@Override
	public int getIndexWithinSkeleton(GenericPair<M, M> element) {
		/*
		 * Suppose a* x b is in C^p x D_{p+n}.
		 * 
		 * 
		 * 
		 */
		
		M a = element.getFirst();
		M b = element.getSecond();
		
		int p = C.getDimension(a);
		int n = D.getDimension(b) - p;
		
		int min_p = Math.max(this.C.getBeginningOfSupport(), this.D.getBeginningOfSupport() - n);
		
		/*
		 * To calculate the index of a* x b, we use the following information:
		 * Elements within the skeleton are ordered as follows:
		 * - first by p
		 * - then by a
		 * - then by b
		 * 
		 * The size of n-skeleton of Hom-complex is 
		 * \sum_{p \in Z} |C^p| * |D_{p_n}|
		 * 
		 * So we first compute how many "blocks" are before p, then we compute the index
		 * of a, and then the index of b.
		 * 
		 * e.g.
		 * 
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * 
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * 
		 * xxxxxxxx
		 * xxX
		 * 
		 * total_preceding_block_sizes = 8 * 3 + 8 * 4 = 56
		 * a_index = 1
		 * b_index = 2
		 * 
		 */
		
		int total_preceding_block_sizes = 0;		
		for (int p_index = min_p; p_index < p; p_index++) {
			total_preceding_block_sizes += this.C.getSkeletonSize(p_index) * this.D.getSkeletonSize(p_index + n);
		}
		
		int a_index = C.getIndexWithinSkeleton(a);
		int b_index = D.getIndexWithinSkeleton(b);
		
		int final_block_width = D.getSkeletonSize(p + n);
		
		return total_preceding_block_sizes + a_index * final_block_width + b_index;
	}

	@Override
	public int getDimension(GenericPair<M, M> element) {
		/*
		 * If a* x b is in C^p x D_{p + n}, then dim(a* x b) = n
		 * which is equal to the dimension fo the second component
		 * minus the dimension of the first.
		 */
		
		return (D.getDimension(element.getSecond()) - C.getDimension(element.getFirst()));
	}

	@Override
	public GenericPair<M, M> getAtIndexWithinSkeleton(int index, int n) {
		int min_p = Math.max(this.C.getBeginningOfSupport(), this.D.getBeginningOfSupport() - n);
		
		/*
		 * To calculate the index of a* x b, we use the following information:
		 * Elements within the skeleton are ordered as follows:
		 * - first by p
		 * - then by a
		 * - then by b
		 * 
		 * The size of n-skeleton of Hom-complex is 
		 * \sum_{p \in Z} |C^p| * |D_{p_n}|
		 * 
		 * So we first compute how many "blocks" are before p, then we compute the index
		 * of a, and then the index of b.
		 * 
		 * e.g.
		 * 
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * 
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * xxxxxxxx
		 * 
		 * xxxxxxxx
		 * xxX
		 * 
		 * total_preceding_block_sizes = 8 * 3 + 8 * 4 = 56
		 * a_index = 1
		 * b_index = 2
		 * 
		 */
		
		int total_preceding_block_sizes = 0;
		int p_index = min_p;
		while (total_preceding_block_sizes < index) {
			total_preceding_block_sizes += this.C.getSkeletonSize(p_index) * this.D.getSkeletonSize(p_index + n);
			p_index++;
		}
	
		if (total_preceding_block_sizes > index) {
			p_index--;
			total_preceding_block_sizes -= this.C.getSkeletonSize(p_index) * this.D.getSkeletonSize(p_index + n);
		}
		
		int block_index = index - total_preceding_block_sizes;
		
		int final_block_width = D.getSkeletonSize(p_index + n);
		
		int a_index = block_index / final_block_width;
		int b_index = block_index % final_block_width;
		
		return new GenericPair<M, M>(this.C.getAtIndexWithinSkeleton(a_index, p_index), this.D.getAtIndexWithinSkeleton(b_index, p_index + n));
	}
}
