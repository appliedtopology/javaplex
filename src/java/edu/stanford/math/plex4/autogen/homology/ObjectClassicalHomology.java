package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Generated;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import gnu.trove.THashMap;
import gnu.trove.THashSet;





/**
 * <p>This class performs the persistent homology algorithm as outlined in
 * the paper "Computing Persistent Homology" by Zomorodian and Carlsson.</p>
 * 
 * <p>F the underlying type of the coefficient field</p>
 * <p>U the underlying basis type</p>
 *
 * @author autogen
 *
 * 
 */
@Generated(value = { "edu.stanford.math.plex4.generation.GeneratorDriver" })
public class ObjectClassicalHomology<F, U> extends AbstractPersistenceAlgorithm<U> {
		protected final ObjectAbstractField<F> field;
		private final ObjectAlgebraicFreeModule<F, U> chainModule;
	private final Comparator<U> basisComparator;
	private final int maxDimension;
	private final int minDimension;
	
	private THashSet<U> markedSimplices = null;
	private THashMap<U, ObjectSparseFormalSum<F, U>> T = null;
	private Comparator<U> filteredComparator = null;
	
		/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type F
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public ObjectClassicalHomology(ObjectAbstractField<F> field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.chainModule = new ObjectAlgebraicFreeModule<F, U>(this.field);
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
		
	public BarcodeCollection<Integer> computeIndexIntervals(AbstractFilteredStream<U> stream) {
		BarcodeCollection<Integer> barcodeCollection = new BarcodeCollection<Integer>();
		
		this.filteredComparator = new FilteredComparator<U>(stream, this.basisComparator);
		this.markedSimplices = new THashSet<U>();
		this.T = new THashMap<U, ObjectSparseFormalSum<F, U>>();
		
		for (U simplex : stream) {
			
			if (stream.getDimension(simplex) > maxDimension + 1) {
				continue;
			}
			
			if (stream.getDimension(simplex) < this.minDimension) {
				continue;
			}
			
			/*
			 * Translation from paper:
			 * 
			 * sigma_j = simplex
			 * sigma_i = d.maxObject();
			 * 
			 */

			this.T.remove(simplex);
			
			ObjectSparseFormalSum<F, U> d = this.removePivotRows(simplex, stream);
			
			if (d.isEmpty()) {
				this.markedSimplices.add(simplex);
			} else {
				U sigma_j = simplex;
				U sigma_i = getMaximumObject(d);
				int k = stream.getDimension(sigma_i);
				
				// store j and d in U[i]
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
		
		for (U simplex : this.markedSimplices) {
			if (!this.T.containsKey(simplex) || this.T.get(simplex).isEmpty()) {
				int k = stream.getDimension(simplex);
				if (k < maxDimension) {
					barcodeCollection.addRightInfiniteInterval(k, stream.getFiltrationIndex(simplex));
				}
			}
		}
	
		this.T = null;
		this.markedSimplices = null;
		this.filteredComparator = null;
		
		return barcodeCollection;
	}
	
	private ObjectSparseFormalSum<F, U> removePivotRows(U simplex, AbstractFilteredStream<U> stream) {
		ObjectSparseFormalSum<F, U> d = chainModule.createNewSum(stream.getBoundaryCoefficients(simplex), stream.getBoundary(simplex));

				
				
		for (Iterator<Map.Entry<U, F>> iterator = d.iterator(); iterator.hasNext(); ) {
			Map.Entry<U, F> entry = iterator.next();
			
			if (!this.markedSimplices.contains(entry.getKey())) {
				iterator.remove();
			}
		}
		
				
				
		U sigma_i = null;
				F q = this.field.getZero();
				while (!d.isEmpty()) {
			sigma_i = getMaximumObject(d);
			
			if (!this.T.containsKey(sigma_i) || this.T.get(sigma_i).isEmpty()) {
				break;
			}
			
			q = T.get(sigma_i).getCoefficient(sigma_i);
			
						if (field.isZero(q)) {
				break;
			}
						
						chainModule.accumulate(d, T.get(sigma_i), field.invert(q));
					}
		
		return d;
	}

	
	private U getMaximumObject(ObjectSparseFormalSum<F, U> chain) {
		U maxObject = null;
		
				
				
		for (Iterator<Map.Entry<U, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Map.Entry<U, F> entry = iterator.next();
			if (maxObject == null || this.filteredComparator.compare(entry.getKey(), maxObject) > 0) {
				maxObject = entry.getKey();
			}
		}
		
		
		
		return maxObject;
	}
}
