package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.IntBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TIntObjectIterator;
import gnu.trove.TObjectIntIterator;

public class ZigZagUtility {
	public static IntBarcodeCollection union(IntBarcodeCollection a, IntBarcodeCollection b) {
		IntBarcodeCollection c = new IntBarcodeCollection();
		
		for (TIntObjectIterator<IntBarcode> iterator = a.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			
			int dimension = iterator.key();
			IntBarcode barcode = iterator.value();
			
			for (IntHalfOpenInterval interval: barcode) {
				c.addInterval(dimension, interval);
			}
		}
		
		for (TIntObjectIterator<IntBarcode> iterator = b.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			
			int dimension = iterator.key();
			IntBarcode barcode = iterator.value();
			
			for (IntHalfOpenInterval interval: barcode) {
				c.addInterval(dimension, interval);
			}
		}
		
		return c;
	}
	
	public static <X extends PrimitiveBasisElement> IntSparseFormalSum<X> createNewSum(int[] coefficients, PrimitiveBasisElement[] objects) {
		IntSparseFormalSum<X> result = new IntSparseFormalSum<X>();
		
		for (int i = 0; i < coefficients.length; i++) {
			result.put(coefficients[i], (X) objects[i]);
		}
		
		return result;
	}
	
	public static <X, Y> X findColumnWithGivenLow(THashMap<X, IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

		for (X columnIndex: m.keySet()) {
			IntSparseFormalSum<Y> column = m.get(columnIndex);
			Y columnLow = low(column, comparator);
			if (columnLow != null && low.equals(columnLow)) {
				return columnIndex;
			}
		}

		return null;
	}

	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	public static <X> X low(IntSparseFormalSum<X> chain, Comparator<X> comparator) {

		X maxObject = null;

		if (chain == null) {
			//System.out.println("stop");
			return null;
		}

		for (TObjectIntIterator<X> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || comparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}

		return maxObject;
	}

	/*
	public static <X, Y> HomologyBasisTracker<ObjectObjectPair<X, Y>> tensorProduct(HomologyBasisTracker<X> x, HomologyBasisTracker<Y> y) {
		TensorStream<X, Y> tensorStream = new TensorStream<X, Y>(x.stream, y.stream);
		HomologyBasisTracker<ObjectObjectPair<X, Y>> result = new HomologyBasisTracker<ObjectObjectPair<X, Y>>(x.field, tensorStream.getBasisComparator(), tensorStream);
		IndexLinearizer linearizer = new IndexLinearizer(x.Z.size(), y.Z.size());

		for (Integer x_index: x.Z.keySet()) {
			IntSparseFormalSum<X> x_col= x.Z.get(x_index);
			for (Integer y_index: y.Z.keySet()) {
				int linearIndex = linearizer.getLinearIndex(x_index, y_index);
				IntSparseFormalSum<Y> y_col= y.Z.get(y_index);

				result.Z.put(linearIndex, tensorProduct(x_col, y_col, x.field));
				result.ZIndices.add(linearIndex);
			}
		}

		Map<Integer, IntSparseFormalSum<X>> Z_X = x.Z;
		Map<Integer, IntSparseFormalSum<Y>> Z_Y = y.Z;

		Map<X, IntSparseFormalSum<X>> C_X = x.C;
		Map<Y, IntSparseFormalSum<Y>> C_Y = y.C;


		Map<X, IntSparseFormalSum<Integer>> B_X = x.getB();
		Map<X, IntSparseFormalSum<Integer>> H_X = x.getH();

		Map<Y, IntSparseFormalSum<Integer>> B_Y = y.getB();
		Map<Y, IntSparseFormalSum<Integer>> H_Y = y.getH();

		Map<X, IntSparseFormalSum<X>> ZXBX = multiply(Z_X, B_X, x.field);
		Map<X, IntSparseFormalSum<X>> ZXHX = multiply(Z_X, H_X, x.field);
		Map<Y, IntSparseFormalSum<Y>> ZYHY = multiply(Z_Y, H_Y, y.field);

		result.B = join(tensorProduct(B_X, B_Y, x.field, linearizer), join(tensorProduct(H_X, B_Y, x.field, linearizer), tensorProduct(B_X, H_Y, x.field, linearizer)));

		result.C = join(tensorProduct(ZXBX, C_Y, x.field), join(tensorProduct(ZXHX, C_Y, x.field), tensorProduct(C_X, ZYHY, x.field)));



		{
			Collection<IntSparseFormalSum<Integer>> V_X = x.getV();
			Collection<IntSparseFormalSum<Integer>> B_Y = y.getB();

			for (IntSparseFormalSum<Integer> v: V_X) {
				for (IntSparseFormalSum<Integer> b: B_Y) {
					result.B.add(tensorProduct(v, b, x.field, linearizer));
				}
			}
		}

		{
			Collection<IntSparseFormalSum<Integer>> B_X = x.getB();
			Collection<IntSparseFormalSum<Integer>> V_Y = y.getV();

			for (IntSparseFormalSum<Integer> b: B_X) {
				for (IntSparseFormalSum<Integer> v: V_Y) {
					result.B.add(tensorProduct(b, v, x.field, linearizer));
				}
			}
		}

		return result;
	}
	 */

	public static <X, Y, Z, W> Map<ObjectObjectPair<X, Y>, IntSparseFormalSum<ObjectObjectPair<Z, W>>> tensorProduct(Map<X, IntSparseFormalSum<Z>> m, Map<Y, IntSparseFormalSum<W>> n, IntAbstractField field) {
		Map<ObjectObjectPair<X, Y>, IntSparseFormalSum<ObjectObjectPair<Z, W>>> result = new THashMap<ObjectObjectPair<X, Y>, IntSparseFormalSum<ObjectObjectPair<Z, W>>>();

		for (X m_col: m.keySet()) {
			for (Y n_col: n.keySet()) {
				ObjectObjectPair<X, Y> new_col = new ObjectObjectPair<X, Y>(m_col, n_col);

				result.put(new_col, tensorProduct(m.get(m_col), n.get(n_col), field));
			}
		}

		return result;
	}

	public static <X, Y> Map<ObjectObjectPair<X, Y>, IntSparseFormalSum<Integer>> tensorProduct(Map<X, IntSparseFormalSum<Integer>> m, Map<Y, IntSparseFormalSum<Integer>> n, IntAbstractField field, IndexLinearizer linearizer) {
		Map<ObjectObjectPair<X, Y>, IntSparseFormalSum<Integer>> result = new THashMap<ObjectObjectPair<X, Y>, IntSparseFormalSum<Integer>>();

		for (X m_col: m.keySet()) {
			for (Y n_col: n.keySet()) {
				ObjectObjectPair<X, Y> new_col = new ObjectObjectPair<X, Y>(m_col, n_col);

				result.put(new_col, tensorProduct(m.get(m_col), n.get(n_col), field, linearizer));
			}
		}

		return result;
	}

	public static <X, Y> Map<X, IntSparseFormalSum<Y>> join(Map<X, IntSparseFormalSum<Y>> m, Map<X, IntSparseFormalSum<Y>> n) {
		Map<X, IntSparseFormalSum<Y>> result = new THashMap<X, IntSparseFormalSum<Y>>();

		for (X col: m.keySet()) {
			result.put(col, m.get(col));
		}

		for (X col: n.keySet()) {
			result.put(col, n.get(col));
		}

		return result;
	}

	public static IntSparseFormalSum<Integer> tensorProduct(IntSparseFormalSum<Integer> a, IntSparseFormalSum<Integer> b, IntAbstractField field, IndexLinearizer linearizer) {
		IntSparseFormalSum<Integer> result = new IntSparseFormalSum<Integer>();

		for (TObjectIntIterator<Integer> a_iterator = a.iterator(); a_iterator.hasNext(); ) {
			a_iterator.advance();

			for (TObjectIntIterator<Integer> b_iterator = b.iterator(); b_iterator.hasNext(); ) {
				b_iterator.advance();

				int newCoefficient = field.multiply(a_iterator.value(), b_iterator.value());
				if (!field.isZero(newCoefficient)) {
					result.put(newCoefficient, linearizer.getLinearIndex(a_iterator.key(), b_iterator.key()));
				}
			}
		}

		return result;
	}

	public static <X, Y> IntSparseFormalSum<ObjectObjectPair<X, Y>> tensorProduct(IntSparseFormalSum<X> a, IntSparseFormalSum<Y> b, IntAbstractField field) {
		IntSparseFormalSum<ObjectObjectPair<X, Y>> result = new IntSparseFormalSum<ObjectObjectPair<X, Y>>();

		for (TObjectIntIterator<X> a_iterator = a.iterator(); a_iterator.hasNext(); ) {
			a_iterator.advance();

			for (TObjectIntIterator<Y> b_iterator = b.iterator(); b_iterator.hasNext(); ) {
				b_iterator.advance();

				int newCoefficient = field.multiply(a_iterator.value(), b_iterator.value());
				if (!field.isZero(newCoefficient)) {
					result.put(newCoefficient, new ObjectObjectPair<X, Y>(a_iterator.key(), b_iterator.key()));
				}
			}
		}

		return result;
	}

	public static <X, Y> IntSparseFormalSum<Y> multiply(Map<X, IntSparseFormalSum<Y>> m, IntSparseFormalSum<X> x, IntAbstractField field) {
		IntSparseFormalSum<Y> result = new IntSparseFormalSum<Y>();
		IntAlgebraicFreeModule<Y> YChainModule = new IntAlgebraicFreeModule<Y>(field);

		for (TObjectIntIterator<X> iterator = x.iterator(); iterator.hasNext(); ) {
			iterator.advance();

			int coefficient = iterator.value();
			X object = iterator.key();
			IntSparseFormalSum<Y> column = m.get(object);
			if (column != null) {
				YChainModule.accumulate(result, column, coefficient);
			}
		}

		return result;
	}

	public static <X, Y, Z> Map<Z, IntSparseFormalSum<Y>> multiply(Map<X, IntSparseFormalSum<Y>> a, Map<Z, IntSparseFormalSum<X>> b, IntAbstractField field) {
		Map<Z, IntSparseFormalSum<Y>> result = new THashMap<Z, IntSparseFormalSum<Y>>();

		for (Z col: b.keySet()) {
			result.put(col, multiply(a, b.get(col), field));
		}

		return result;
	}


	public static <Y> int findColumnWithGivenLow(List<IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

		for (int i = 0; i < m.size(); i++) {
			IntSparseFormalSum<Y> column = m.get(i);
			Y columnLow = low(column, comparator);
			if (columnLow != null && low.equals(columnLow)) {
				return i;
			}
		}

		return -1;
	}

	public static <X, Y> X findColumnWithGivenLow(Map<X, IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

		for (X columnIndex: m.keySet()) {
			IntSparseFormalSum<Y> column = m.get(columnIndex);
			Y columnLow = low(column, comparator);
			if (columnLow != null && low.equals(columnLow)) {
				return columnIndex;
			}
		}

		return null;
	}

	public static <X> List<X> union(List<X> x, List<X> y) {
		List<X> result = new ArrayList<X>();
		result.addAll(x);
		result.addAll(y);
		return result;
	}

	public static <X extends PrimitiveBasisElement> IntSparseFormalSum<X> computeBoundary(IntSparseFormalSum<X> chain, IntAlgebraicFreeModule<X> chainModule) {
		IntSparseFormalSum<X> result = new IntSparseFormalSum<X>();

		if (chain == null) {
			System.out.println("null chain");
		}
		
		for (TObjectIntIterator<X> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();

			IntSparseFormalSum<X> boundary = createNewSum(iterator.key().getBoundaryCoefficients(), iterator.key().getBoundaryArray());
			chainModule.accumulate(result, boundary, iterator.value());
		}

		return result;
	}

	public static <X, Y> X findFirstIndexContainingElement(Map<X, IntSparseFormalSum<Y>> m, Y element, Comparator<X> comparator) {
		X index = null;
		Set<X> candidates = new THashSet<X>();

		for (X key: m.keySet()) {
			if (m.get(key).containsObject(element)) {
				candidates.add(key);
			}
		}

		for (X candidate: candidates) {
			if (index == null || comparator.compare(candidate, index) < 0) {
				index = candidate;
			}
		}

		return index;
	}
	
	public static <X, Y> X findLastIndexContainingElement(Map<X, IntSparseFormalSum<Y>> m, Y element, Comparator<X> comparator) {
		X index = null;
		Set<X> candidates = new THashSet<X>();

		for (X key: m.keySet()) {
			if (m.get(key).containsObject(element)) {
				candidates.add(key);
			}
		}

		for (X candidate: candidates) {
			if (index == null || comparator.compare(candidate, index) > 0) {
				index = candidate;
			}
		}

		return index;
	}

	public static <X, Y> List<X> getAscendingIndicesContainingElement(Map<X, IntSparseFormalSum<Y>> m, Y element, Comparator<X> comparator) {
		List<X> list = new ArrayList<X>();

		for (X key: m.keySet()) {
			if (m.get(key).containsObject(element)) {
				list.add(key);
			}
		}
		
		Collections.sort(list, comparator);

		return list;
	}
	
	public static <X, Y> List<X> getAscendingIndicesWithGivenLow(Map<X, IntSparseFormalSum<Y>> m, Y element, Comparator<X> comparator, Comparator<Y> comparatorY) {
		List<X> list = new ArrayList<X>();

		for (X key: m.keySet()) {
			Y low = low(m.get(key), comparatorY);
			if (low != null && low.equals(element)) {
				list.add(key);
			}
		}
		
		Collections.sort(list, comparator);

		return list;
	}
	
	public static <X, Y> List<X> getAscendingIndices(Map<X, Y> m, Comparator<X> comparator) {
		List<X> list = new ArrayList<X>();

		list.addAll(m.keySet());
		Collections.sort(list, comparator);

		return list;
	}

	public static <X> int getPrependIndex(Map<Integer, X> m) {
		if(m.keySet().isEmpty()) {
			return 0;
		}

		int minimumIndex = Integer.MAX_VALUE;

		for (int index: m.keySet()) {
			if (index < minimumIndex) {
				minimumIndex = index;
			}
		}

		return minimumIndex - 1;
	}

	public static <X> int getAppendIndex(Map<Integer, X> m) {
		if(m.keySet().isEmpty()) {
			return 0;
		}

		int maximumIndex = Integer.MIN_VALUE;

		for (int index: m.keySet()) {
			if (index > maximumIndex) {
				maximumIndex = index;
			}
		}

		return maximumIndex + 1;
	}

	public static <X> int prependColumn(Map<Integer, X> m, X col) {
		int index = getPrependIndex(m);
		m.put(index, col);
		return index;
	}

	public static <X> int appendColumn(Map<Integer, X> m, X col) {
		int index = getAppendIndex(m);
		m.put(index, col);
		return index;
	}

	public static <X> void writeRow(Map<X, IntSparseFormalSum<Integer>> m, IntSparseFormalSum<X> row, int rowIndex) {
		for (TObjectIntIterator<X> iterator = row.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			X key = iterator.key();

			if (!m.containsKey(key)) {
				m.put(key, new IntSparseFormalSum<Integer>());
			}

			m.get(key).put(iterator.value(), rowIndex);
		}
	}
	
	public static <X, Y> IntSparseFormalSum<X> getRow(Map<X, IntSparseFormalSum<Y>> m, Y row) {
		IntSparseFormalSum<X> result = new IntSparseFormalSum<X>();
		
		for (X colIndex: m.keySet()) {
			IntSparseFormalSum<Y> col = m.get(colIndex);
			if (col.containsObject(row)) {
				result.put(col.getCoefficient(row), colIndex);
			}
		}
		
		return result;
	}
}
