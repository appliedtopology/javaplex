package edu.stanford.math.plex4.homology.nonautogen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.TObjectIntIterator;

/**
 * This class maintains the right-filtration for performing zig-zag homology.
 * It implements the algorithm described in the paper "Zigzag Persistent Homology
 * and Real-valued Functions" by Gunnar Carlsson, Vin de Silva, and Dmitriy Morozov.
 * 
 * TODO List:
 * - cache low values for the columns to prevent re-computing
 * 
 * @author Andrew Tausz
 *
 * @param <U>
 */
public class ZigZagPrototype<U> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final IntAbstractField field;

	/**
	 * These objects performs the chain computations.
	 */
	protected final IntAlgebraicFreeModule<U> chainModule;
	protected final IntAlgebraicFreeModule<Integer> integerChainModule;

	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<U> basisComparator;

	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected final Comparator<U> filteredComparator;
	protected final Comparator<Integer> integerComparator = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};

	protected final AbstractFilteredStream<U> stream;

	protected THashMap<Integer, IntSparseFormalSum<U>> Z = new THashMap<Integer, IntSparseFormalSum<U>>();
	protected THashMap<U, IntSparseFormalSum<Integer>> B = new THashMap<U, IntSparseFormalSum<Integer>>();
	protected THashMap<U, IntSparseFormalSum<U>> C = new THashMap<U, IntSparseFormalSum<U>>();
	//protected THashMap<U, Integer> ZLowMap = new THashMap<U, Integer>(); // maps low -> column with low
	//protected THashMap<Integer, U> BLowMap = new THashMap<Integer, U>();
	protected List<Integer> ZIndices = new ArrayList<Integer>(); // maintains ordering of Z columns
	protected List<U> CIndices = new ArrayList<U>();

	protected THashMap<Integer, U> birth = new THashMap<Integer, U>();
	protected List<U> idx = new ArrayList<U>();

	protected List<ObjectObjectPair<Integer, U>> pairs = new ArrayList<ObjectObjectPair<Integer, U>>();

	protected int i = 0;

	/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type int
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public ZigZagPrototype(IntAbstractField field, Comparator<U> basisComparator, AbstractFilteredStream<U> stream) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.integerChainModule = new IntAlgebraicFreeModule<Integer>(this.field);
		this.basisComparator = basisComparator;
		this.filteredComparator = new FilteredComparator<U>(stream, this.basisComparator);
		this.stream = stream;
	}

	public List<ObjectObjectPair<Integer, U>> getPairs() {
		return this.pairs;
	}

	public THashMap<Integer, U> getBirth() {
		return this.birth;
	}

	public void add(U sigma) {
		IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(sigma), stream.getBoundary(sigma));
		
		// compute representation of boundary of sigma in terms of cycles Z_i
		IntSparseFormalSum<Integer> v = new IntSparseFormalSum<Integer>();

		while (true) {
			U lowElement = this.low(boundary, this.filteredComparator);
			if (lowElement == null) {
				break;
			}

			Integer columnWithLow = this.findColumnWithGivenLow(Z, lowElement, this.filteredComparator);
			if (columnWithLow == null) {
				break;
			}

			int a = Z.get(columnWithLow).getCoefficient(lowElement);
			int b = boundary.getCoefficient(lowElement);
			int multiplier = this.field.negate(this.field.divide(b, a));
			chainModule.accumulate(boundary, Z.get(columnWithLow), multiplier);

			v.put(field.negate(multiplier), columnWithLow);
		}

		IntSparseFormalSum<U> u = new IntSparseFormalSum<U>();

		while (true) {
			Integer lowElement = this.low(v, this.integerComparator);
			if (lowElement == null) {
				break;
			}

			U columnWithLow = this.findColumnWithGivenLow(B, lowElement, this.integerComparator);
			if (columnWithLow == null) {
				break;
			}

			int a = B.get(columnWithLow).getCoefficient(lowElement);
			int b = v.getCoefficient(lowElement);
			int multiplier = this.field.negate(this.field.divide(b, a));
			integerChainModule.accumulate(v, B.get(columnWithLow), multiplier);

			u.put(field.negate(multiplier), columnWithLow);
		}

		IntSparseFormalSum<Integer> v_prime = v;

		if (v_prime.isEmpty()) {
			// Birth
			// d sigma is already a boundary
			IntSparseFormalSum<U> newCycle = chainModule.subtract(this.multiply(C, u), chainModule.createNewSum(sigma));
			Z.put(i, newCycle);
			ZIndices.add(i);

			birth.put(i, sigma);
			idx.add(sigma);
		} else {
			// Death

			IntSparseFormalSum<U> newChain = chainModule.subtract(this.multiply(C, u), chainModule.createNewSum(sigma));
			C.put(sigma, newChain);
			CIndices.add(sigma);

			B.put(sigma, v_prime);

			// update birth vector
			Integer j = this.low(v_prime, this.integerComparator);
			this.pairs.add(new ObjectObjectPair<Integer, U>(j, sigma));
		}

		i++;
	}

	public void remove(U sigma) {
		int j = -9999;
		boolean found = false;
		for (int k = 0; k < ZIndices.size(); k++) {
			int index = ZIndices.get(k);
			if (Z.get(index).containsObject(sigma)) {
				j = index;
				found = true;
			}
		}

		if (!found) {
			// Birth - there is no cycle in matrix Z that contains sigma
			U sigma_j = null;
			// Let j be the index of the first column in C that contains sigma
			for (int k = 0; k < CIndices.size(); k++) {
				U sigma_k = CIndices.get(k);
				if (C.get(sigma_k).containsObject(sigma)) {
					sigma_j = sigma_k;
					j = k;
				}
			}

			// Let l be the index of the row of the lowest nonzero element in B[j]
			Integer l = this.low(B.get(sigma_j), this.integerComparator);

			// 1. Prepend D C[j] to Z_i to get Z_i'. Prepend i+1 to the birth vector b_i to get
			// b_{i + 1}
			IntSparseFormalSum<U> DC_j = this.computeBoundary(C.get(sigma_j));
			Z.put(i, DC_j);
			ZIndices.add(0, i);

			birth.put(i, sigma);

			// 2. Let c = C[j][sigma]. Let r_sigma be the row of sigma in matrix C. We 
			// prepend the row -r_sigma/c to the matrix B_i to get B_i'.

			int c = C.get(sigma_j).getCoefficient(sigma);
			IntSparseFormalSum<U> r_sigma = new IntSparseFormalSum<U>();
			IntSparseFormalSum<U> C_j = C.get(sigma_j);

			for (U columnIndex: C.keySet()) {
				IntSparseFormalSum<U> column = C.get(columnIndex);
				if (column.containsObject(sigma)) {
					r_sigma.put(column.getCoefficient(sigma), columnIndex);
				}
			}

			for (TObjectIntIterator<U> iterator = r_sigma.iterator(); iterator.hasNext(); ) {
				iterator.advance();

				U colIndex = iterator.key();
				int coefficient = field.negate(field.divide(iterator.value(), c));

				if (!B.containsKey(colIndex)) {
					B.put(colIndex, new IntSparseFormalSum<Integer>());
				}

				integerChainModule.accumulate(B.get(colIndex), i, coefficient);
			}


			// 3. Subtract (r_sigma[k]/c) * C[j] from every column C[k] to get C'.
			for (U sigma_k: C.keySet()) {
				if (sigma_k.equals(sigma_j)) {
					continue;
				}
				
				int coefficient = field.negate(field.divide(r_sigma.getCoefficient(sigma_k), c));
				chainModule.accumulate(C.get(sigma_k), C_j, coefficient);
			}

			// 4. Subtract (B'[k][l]/B'[j][l]) * B'[j] from every other column B'[k].
			IntSparseFormalSum<Integer> B_j = B.get(sigma_j);
			for (U sigma_k: B.keySet()) {
				if (sigma_k.equals(sigma_j)) {
					continue;
				}

				int coefficient = field.negate(field.divide(B.get(sigma_k).getCoefficient(l), B_j.getCoefficient(l)));
				integerChainModule.accumulate(B.get(sigma_k), B_j, coefficient);
			}

			// 5. Drop row l and column j from B' to get B_{i+1}.
			B.remove(sigma_j);

			for (U sigma_k: B.keySet()) {
				B.get(sigma_k).remove(l);
			}

			// 5. Drop column l from Z'
			Z.remove(l);
			ZIndices.remove((Object) l);
			
			// 5. Drop column j from C to get C_{i+1}
			C.remove(sigma_j);
			CIndices.remove(sigma_j);

			// 6. Reduce Z_{i+1} initially set to Z_i
			while (true) {
				U s = this.low(Z.get(j), this.filteredComparator);

				//Integer k = this.findColumnWithGivenLow(Z, s, this.filteredComparator);
				Integer k = null;
				
				for (int k_candidate = 0; k_candidate < j; k_candidate++) {
					U low_candidate = this.low(Z.get(k_candidate), this.filteredComparator);
					if (low_candidate != null && low_candidate.equals(s)) {
						k = k_candidate;
						break;
					}
				}

				if (k == null) {
					break;
				}

				int s_jk = field.divide(Z.get(j).getCoefficient(s), Z.get(k).getCoefficient(s));
				int negative_s_jk = field.negate(s_jk);
				chainModule.accumulate(Z.get(j), Z.get(k), negative_s_jk);

				// in B, add row j multiplied by s_jk to row k
				for (U columnIndex: B.keySet()) {
					IntSparseFormalSum<Integer> column = B.get(columnIndex);
					integerChainModule.accumulate(column, k, field.multiply(column.getCoefficient(j), s_jk));
				}
			}
		} else {
			// Death
			// Z_j is the first cycle that contains simplex sigma

			// update birth vector
			this.pairs.add(new ObjectObjectPair<Integer, U>(j, sigma));

			// 1. Change basis to remove sigma from matrix Z
			for (int k = j + 1; k < ZIndices.size(); k++) {
				Integer sigma_k = ZIndices.get(k);
				if (Z.get(sigma_k).containsObject(sigma)) {
					U low_Zi_k = this.low(Z.get(sigma_k), this.filteredComparator);
					int sigma_jk = field.divide(Z.get(sigma_k).getCoefficient(sigma), Z.get(j).getCoefficient(sigma));
					int negative_sigma_jk = field.negate(sigma_jk);
					chainModule.accumulate(Z.get(sigma_k), Z.get(j), negative_sigma_jk);

					// in B, add row k multiplied by sigma_jk to row j
					for (U BIndex: B.keySet()) {
						IntSparseFormalSum<Integer> column = B.get(BIndex);
						integerChainModule.accumulate(column, j, field.multiply(column.getCoefficient(sigma_k), sigma_jk));
					}

					U low_Zi1_k = this.low(Z.get(sigma_k), this.filteredComparator);
					if (this.filteredComparator.compare(low_Zi1_k, low_Zi_k) > 0) {
						j = k;
					}
				}
			}

			// 2. Subtract cycle (C[k][sigma]/Z[j][sigma]) * Z[j] from every chain C[k]
			for (U CIndex: C.keySet()) {
				IntSparseFormalSum<U> Ck = C.get(CIndex);
				int coefficient = field.negate(field.divide(Ck.getCoefficient(sigma), Z.get(j).getCoefficient(sigma)));
				chainModule.accumulate(Ck, Z.get(j), coefficient);
			}

			// 3. Drop Z_{i+1}[j], the corresponding entry in vectors b_i and idx_i, row j 
			// from B_i, row sigma from C_i and Z (as well as row and column of sigma from D_i)
			Z.remove(j);
			ZIndices.remove((Object) j);
			
			birth.remove(j);
			idx.remove((Object) j);

			// remove row j from B_i
			for (U BIndex: B.keySet()) {
				B.get(BIndex).remove(j);
			}

			// remove row sigma from C_i
			for (U CIndex: C.keySet()) {
				C.get(CIndex).remove(sigma);
			}

			// remove row sigma from Z_i
			for (Integer ZIndex: Z.keySet()) {
				Z.get(ZIndex).remove(sigma);
			}
		}

		i++;
	}

	protected <X, Y> X findColumnWithGivenLow(THashMap<X, IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

		for (X columnIndex: m.keySet()) {
			IntSparseFormalSum<Y> column = m.get(columnIndex);
			Y columnLow = this.low(column, comparator);
			if (columnLow != null && low.equals(columnLow)) {
				return columnIndex;
			}
		}

		return null;
	}

	protected <X, Y> IntSparseFormalSum<Y> multiply(THashMap<X, IntSparseFormalSum<Y>> m, IntSparseFormalSum<X> x) {
		IntSparseFormalSum<Y> result = new IntSparseFormalSum<Y>();
		IntAlgebraicFreeModule<Y> YChainModule = new IntAlgebraicFreeModule<Y>(this.field);

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

	protected IntSparseFormalSum<U> computeBoundary(IntSparseFormalSum<U> chain) {
		IntSparseFormalSum<U> result = new IntSparseFormalSum<U>();

		for (TObjectIntIterator<U> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();

			IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(iterator.key()), stream.getBoundary(iterator.key()));
			chainModule.accumulate(result, boundary, iterator.value());
		}

		return result;
	}

	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected <X> X low(IntSparseFormalSum<X> chain, Comparator<X> comparator) {

		X maxObject = null;

		for (TObjectIntIterator<X> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || comparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}

		return maxObject;
	}
}
