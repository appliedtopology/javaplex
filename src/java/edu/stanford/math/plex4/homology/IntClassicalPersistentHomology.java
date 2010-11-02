package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TObjectIntIterator;

/**
 * This class performs the persistent homology algorithm as outlined in
 * the paper "Computing Persistent Homology" by Zomorodian and Carlsson.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class IntClassicalPersistentHomology<T extends PrimitiveBasisElement> {
	private final IntAbstractField field;
	private final IntAlgebraicFreeModule<T> chainModule;
	private final Comparator<T> comparator;
	
	private THashSet<T> markedSimplices = null;
	private THashMap<T, IntSparseFormalSum<T>> T = null;
	private Comparator<T> filteredComparator = null;
	
	public IntClassicalPersistentHomology(IntAbstractField field, Comparator<T> comparator) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<T>(this.field);
		this.comparator = comparator;
	}
	
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<T> stream, int maxDimension) {
		IntBarcodeCollection barcodeCollection = new IntBarcodeCollection();
		
		this.filteredComparator = new FilteredComparator<T>(stream, this.comparator);
		this.markedSimplices = new THashSet<T>();
		this.T = new THashMap<T, IntSparseFormalSum<T>>();
		
		for (T simplex : stream) {
			
			if (simplex.getDimension() > maxDimension + 1) {
				break;
			}
			
			/*
			 * Translation from paper:
			 * 
			 * sigma_j = simplex
			 * sigma_i = d.maxObject();
			 * 
			 */

			this.T.remove(simplex);
			
			IntSparseFormalSum<T> d = this.removePivotRows(simplex, stream);
			
			if (d.isEmpty()) {
				this.markedSimplices.add(simplex);
			} else {
				T sigma_j = simplex;
				T sigma_i = getMaximumObject(d);
				int k = sigma_i.getDimension();
				
				// store j and d in T[i]
				this.T.put(sigma_i, d);
				
				// store interval
				int index_i = stream.getFiltrationIndex(sigma_i);
				int index_j = stream.getFiltrationIndex(sigma_j);
				
				assert (index_i <= index_j);
				
				
				// don't store intervals that are simultaneously created and destroyed
				if ((index_j - index_i > 0) && k < maxDimension) {
					barcodeCollection.addInterval(k, index_i, index_j);
				}
			}
		}
		
		for (T simplex : this.markedSimplices) {
			if (!this.T.containsKey(simplex) || this.T.get(simplex).isEmpty()) {
				int k = simplex.getDimension();
				if (k < maxDimension) {
					barcodeCollection.addRightInfiniteInterval(k, stream.getFiltrationIndex(simplex));
				}
			}
		}
	
		this.T = null;
		this.markedSimplices = null;
		
		return barcodeCollection;
	}
	
	private IntSparseFormalSum<T> removePivotRows(T simplex, AbstractFilteredStream<T> stream) {;
		IntSparseFormalSum<T> d = chainModule.createNewSum(stream.getBoundaryCoefficients(simplex), stream.getBoundary(simplex));

		// remove unmarked terms from d
		for (TObjectIntIterator<T> iterator = d.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (!this.markedSimplices.contains(iterator.key())) {
				iterator.remove();
			}
		}
		
		T sigma_i = null;
		int q = 0;
		while (!d.isEmpty()) {
			sigma_i = getMaximumObject(d);
			
			if (!this.T.containsKey(sigma_i) || this.T.get(sigma_i).isEmpty()) {
				break;
			}
			
			q = T.get(sigma_i).getCoefficient(sigma_i);
			
			if (q == 0) {
				break;
			}
			
			chainModule.accumulate(d, T.get(sigma_i), field.invert(q));
		}
		
		return d;
	}

	
	private T getMaximumObject(IntSparseFormalSum<T> chain) {
		T maxObject = null;

		for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null) {
				maxObject = iterator.key();
			}
			
			if (this.filteredComparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}
		
		return maxObject;
	}
}
