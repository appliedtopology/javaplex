package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TObjectIntIterator;

/**
 * This class contains various static functions for assisting with zigzag homology computations.
 * 
 * @author Andrew Tausz
 *
 */
public class BasisTrackingUtility {
	
	/**
	 * This function creates a new formal sum from the given set of coefficients and objects.
	 * 
	 * @param <X>
	 * @param coefficients the array of coefficients
	 * @param objects the array of basis elements - they must be of type X
	 * @return a formal sum containing the given coefficients and basis elements
	 */
	@SuppressWarnings("unchecked")
	public static <X extends PrimitiveBasisElement> IntSparseFormalSum<X> createNewSum(int[] coefficients, PrimitiveBasisElement[] objects) {
		IntSparseFormalSum<X> result = new IntSparseFormalSum<X>();
		
		for (int i = 0; i < coefficients.length; i++) {
			result.put(coefficients[i], (X) objects[i]);
		}
		
		return result;
	}
		
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param <X> the underlying type of the basis elements in the chain
	 * @param chain the chain
	 * @param comparator the comparator to use
	 * @return the lowest element in the chain
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

	/**
	 * This function performs matrix multiplication. The matrix is represented by a sparse set of sparse columns. In other
	 * words, this function computes the linear combination of the columns in the specified map with coefficients given by the
	 * vector v. In the matrix m, the keys represent the column labels, and the values are the columns at that particular index.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param m the matrix as a list of columns
	 * @param v the vector
	 * @param field the field over which to perform the computations
	 * @return the matrix vector product
	 */
	public static <X, Y> IntSparseFormalSum<Y> multiply(Map<X, IntSparseFormalSum<Y>> m, IntSparseFormalSum<X> v, IntAbstractField field) {
		IntSparseFormalSum<Y> result = new IntSparseFormalSum<Y>();
		IntAlgebraicFreeModule<Y> YChainModule = new IntAlgebraicFreeModule<Y>(field);

		for (TObjectIntIterator<X> iterator = v.iterator(); iterator.hasNext(); ) {
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

	/**
	 * This function performs matrix-matrix multiplication. The matrices are represented by a sparse set of sparse columns.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param <Z>
	 * @param a the first matrix
	 * @param b the second matrix
	 * @param field the field over which to perform the computations
	 * @return the matrix-matrix product a*b
	 */
	public static <X, Y, Z> Map<Z, IntSparseFormalSum<Y>> multiply(Map<X, IntSparseFormalSum<Y>> a, Map<Z, IntSparseFormalSum<X>> b, IntAbstractField field) {
		Map<Z, IntSparseFormalSum<Y>> result = new THashMap<Z, IntSparseFormalSum<Y>>();

		for (Z col: b.keySet()) {
			result.put(col, multiply(a, b.get(col), field));
		}

		return result;
	}

	/**
	 * This function finds the index of the first column such that the column's low value is the value specified. If no
	 * such column exists, it returns -1.
	 * 
	 * @param <Y>
	 * @param m the set of columns to search
	 * @param low the low value to search for
	 * @param comparator the comparator which performs the comparison within type Y
	 * @return the index of the first column with the given low, and -1 if no such column exists
	 */
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

	/**
	 * This function finds the index of the first column such that the column's low value is the value specified. If no
	 * such column exists, it returns null.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param m the set of columns to search
	 * @param low the low value to search for
	 * @param comparator the comparator which performs the comparison within type Y
	 * @return the index of the first column with the given low, and null if no such column exists
	 */
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

	/**
	 * This function computes the boundary of a given chain, by linear extension.
	 * 
	 * @param <X>
	 * @param chain the chain to compute the boundary of
	 * @param chainModule the module that performs the computations
	 * @return the boundary of the given chain
	 */
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

	/**
	 * This function returns the lowest index such that the given element is found in the chain at that index. If no such
	 * index is found, it returns null.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param m the set of chains
	 * @param element the element to search for
	 * @param comparator provides comparison for the indices of m
	 * @return the lowest index of the chains containing the given element
	 */
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
	
	/**
	 * This function returns the greatest index such that the given element is found in the chain at that index. If no such
	 * index is found, it returns null.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param m the set of chains
	 * @param element the element to search for
	 * @param comparator provides comparison for the indices of m
	 * @return the greatest index of the chains containing the given element
	 */
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

	/**
	 * This function returns the set of indices such that the chains at the indices contain the given element. The indices
	 * are returned in sorted order. In the event that no indices are found, it returns the empty list.
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param m the set of chains
	 * @param element the element to search for
	 * @param comparator provides comparison for the indices of m
	 * @return the ascending set of indices containing the element
	 */
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

	public static <X, Y> ObjectObjectPair<IntSparseFormalSum<X>, IntSparseFormalSum<Y>> reduce(Map<X, IntSparseFormalSum<Y>> M, IntSparseFormalSum<Y> column, Comparator<Y> comparator, IntAlgebraicFreeModule<Y> chainModule) {
		Map<Y, X> lowMap = new THashMap<Y, X>();
	
		for (X key: M.keySet()) {
			IntSparseFormalSum<Y> chain = M.get(key);
			Y low = low(chain, comparator);
			if (low != null) {
				lowMap.put(low, key);
			}
		}
	
		IntSparseFormalSum<Y> reduction = new IntSparseFormalSum<Y>();
		IntSparseFormalSum<X> coefficients = new IntSparseFormalSum<X>();
	
		IntAbstractField field = (IntAbstractField) chainModule.getRing();
	
		for (TObjectIntIterator<Y> iterator = column.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			reduction.put(iterator.value(), iterator.key());
		}
	
		while (true) {
			Y low = low(reduction, comparator);
	
			if (low == null) {
				break;
			}
	
			X columnWithLow = lowMap.get(low);
			if (columnWithLow == null) {
				break;
	
				//List<Integer> columns = BasisTrackingUtility.getAscendingIndicesContainingElement(Z, lowElement, this.integerComparator);
				//int randomIndex = RandomUtility.nextUniformInt(0, columns.size() - 1);
				//columnWithLow = columns.get(randomIndex);
				//columnWithLow = BasisTrackingUtility.findLastIndexContainingElement(Z, lowElement, this.integerComparator);
				//IntSparseFormalSum<U> boundaryOfColumn = BasisTrackingUtility.computeBoundary(Z.get(columnWithLow), chainModule);
				//break;
			}
	
			int a = M.get(columnWithLow).getCoefficient(low);
			int b = reduction.getCoefficient(low);
			int multiplier = field.negate(field.divide(b, a));
			chainModule.accumulate(reduction, M.get(columnWithLow), multiplier);
	
			coefficients.put(field.negate(multiplier), columnWithLow);
		}
	
		return new ObjectObjectPair<IntSparseFormalSum<X>, IntSparseFormalSum<Y>>(coefficients, reduction);
	}
}
