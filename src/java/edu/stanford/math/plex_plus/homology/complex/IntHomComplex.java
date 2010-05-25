/**
 * 
 */
package edu.stanford.math.plex_plus.homology.complex;

import java.util.Iterator;
import java.util.Set;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.math.structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.math.structures.interfaces.IntRing;
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
		 * d(a* x b) = (d*a*) x b + a* x db
		 * 
		 * Where we use x to denote the tensor product, and d to denote the boundary
		 * operator on the chain complexes C and D, and d* to be the coboundary operator.
		 * 
		 */
		
		IntFormalSum<M> aCoboundary = this.C.computeCoboundary(a);
		
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
			int coefficient = iterator.value();
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

	@Override
	public int getBeginningOfSupport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEndOfSupport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSkeletonSize(int k) {
		// TODO Auto-generated method stub
		return 0;
	}
}
