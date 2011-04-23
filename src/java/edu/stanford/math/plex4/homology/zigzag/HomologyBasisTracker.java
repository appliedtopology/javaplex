package edu.stanford.math.plex4.homology.zigzag;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TIntIntHashMap;

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
public class HomologyBasisTracker<U extends PrimitiveBasisElement> implements AbstractBasisTracker<U> {
	private class BirthDescriptor {
		public int internalIndex;
		public int externalIndex;
		public int dimension;
		
		public BirthDescriptor(int internalIndex, int externalIndex, int dimension) {
			super();
			this.internalIndex = internalIndex;
			this.externalIndex = externalIndex;
			this.dimension = dimension;
		}

		@Override
		public String toString() {
			return "BirthDescriptor [dimension=" + dimension + ", externalIndex=" + externalIndex + ", internalIndex=" + internalIndex + "]";
		}
	}
	
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final IntAbstractField field;

	/**
	 * These objects performs the chain computations.
	 */
	protected final IntAlgebraicFreeModule<U> chainModule;
	protected final IntAlgebraicFreeModule<Integer> integerChainModule;

	protected final Comparator<U> basisComparator;
	protected final Comparator<Integer> integerComparator = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};

	protected Map<Integer, IntSparseFormalSum<U>> Z = new THashMap<Integer, IntSparseFormalSum<U>>();
	protected Map<Integer, IntSparseFormalSum<Integer>> B = new THashMap<Integer, IntSparseFormalSum<Integer>>();
	protected Map<Integer, IntSparseFormalSum<U>> C = new THashMap<Integer, IntSparseFormalSum<U>>();

	protected Map<Integer, BirthDescriptor> birthDescriptors = new THashMap<Integer, BirthDescriptor>();
	
	protected TIntIntHashMap internalExternalIndexMap = new TIntIntHashMap();

	protected Set<U> basisElements = new THashSet<U>();

	protected int internalIndex = 0;

	protected IntBarcodeCollection barcodes = new IntBarcodeCollection();

	protected boolean verify = true;
	
	/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type int
	 * @param basisComparator a comparator on the basis type U
	 */
	public HomologyBasisTracker(IntAbstractField field, Comparator<U> basisComparator) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.integerChainModule = new IntAlgebraicFreeModule<Integer>(this.field);
		this.basisComparator = basisComparator;
	}
	
	public IntBarcodeCollection getFiniteBarcodes() {
		return this.barcodes;
	}
	
	public IntBarcodeCollection getInfiniteBarcodes() {
		IntBarcodeCollection collection = new IntBarcodeCollection();
		for (Integer key: this.birthDescriptors.keySet()) {
			collection.addRightInfiniteInterval(this.birthDescriptors.get(key).dimension, this.birthDescriptors.get(key).externalIndex);
		}
		
		return collection;
	}

	public IntBarcodeCollection getBarcodes() {
		return ZigZagUtility.union(this.getFiniteBarcodes(), this.getInfiniteBarcodes());
	}

	public void add(U sigma) {
		this.add(sigma, this.internalIndex);
	}

	public void add(U sigma, int externalIndex) {
		this.internalExternalIndexMap.put(internalIndex, externalIndex);
		this.basisElements.add(sigma);

		IntSparseFormalSum<U> boundary = ZigZagUtility.createNewSum(sigma.getBoundaryCoefficients(), sigma.getBoundaryArray());

		// compute representation of boundary of sigma in terms of cycles Z_i
		IntSparseFormalSum<Integer> v = new IntSparseFormalSum<Integer>();

		// reduce boundary among the cycles
		while (true) {
			U lowElement = ZigZagUtility.low(boundary, this.basisComparator);
			if (lowElement == null) {
				break;
			}

			Integer columnWithLow = ZigZagUtility.findColumnWithGivenLow(Z, lowElement, this.basisComparator);
			if (columnWithLow == null) {
				
				columnWithLow = ZigZagUtility.findFirstIndexContainingElement(Z, lowElement, this.integerComparator);
				IntSparseFormalSum<U> boundaryOfColumn = ZigZagUtility.computeBoundary(Z.get(columnWithLow), chainModule);
				break;
			}

			int a = Z.get(columnWithLow).getCoefficient(lowElement);
			int b = boundary.getCoefficient(lowElement);
			int multiplier = this.field.negate(this.field.divide(b, a));
			chainModule.accumulate(boundary, Z.get(columnWithLow), multiplier);

			v.put(field.negate(multiplier), columnWithLow);
		}

		// verification
		if (!boundary.isEmpty()) {
			System.out.println("Invalid Reduction!!!");
		}
		
		IntSparseFormalSum<Integer> u = new IntSparseFormalSum<Integer>();

		while (true) {
			Integer lowElement = ZigZagUtility.low(v, this.integerComparator);
			if (lowElement == null) {
				break;
			}

			Integer columnWithLow = ZigZagUtility.findColumnWithGivenLow(B, lowElement, this.integerComparator);
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
			IntSparseFormalSum<U> newCycle = chainModule.subtract(ZigZagUtility.multiply(C, u, this.field), chainModule.createNewSum(sigma));

			int insertionPoint = ZigZagUtility.appendColumn(Z, newCycle);

			BirthDescriptor descriptor = new BirthDescriptor(internalIndex, externalIndex, sigma.getDimension());
			this.birthDescriptors.put(insertionPoint, descriptor);
			
			System.out.println(internalIndex + ": Birth (" + externalIndex + ", _) @ " + sigma.getDimension() +  " - addition of " + sigma);
		} else {
			// Death

			IntSparseFormalSum<U> newChain = chainModule.subtract(ZigZagUtility.multiply(C, u, this.field), chainModule.createNewSum(sigma));

			ZigZagUtility.appendColumn(C, newChain);
			ZigZagUtility.appendColumn(B, integerChainModule.negate(v_prime));
			//ZigZagUtility.appendColumn(B, v_prime);

			// update birth vector
			Integer j = ZigZagUtility.low(v_prime, this.integerComparator);
			
			BirthDescriptor descriptor = this.birthDescriptors.get(j);
			
			assert (descriptor != null);
			
			if (descriptor != null) {
				if (descriptor.externalIndex != externalIndex) {
					this.barcodes.addInterval(descriptor.dimension, descriptor.externalIndex, externalIndex);
				}
			}
			
			this.birthDescriptors.remove(j);
			System.out.println(internalIndex + ": Death (" + this.internalExternalIndexMap.get(j) + ", " + externalIndex + ") @ " + (sigma.getDimension() - 1) +  " - addition of " + sigma);
		}

		internalIndex++;

		this.checkInvariant();
	}

	public void remove(U sigma) {
		this.remove(sigma, this.internalIndex);
	}

	public void remove(U sigma, int externalIndex) {
		this.internalExternalIndexMap.put(internalIndex, externalIndex);

		// try to set j to be the first cycle that contains sigma
		List<Integer> indices = ZigZagUtility.getAscendingIndicesContainingElement(Z, sigma, this.integerComparator);
		Integer j = null;

		if (indices.size() > 0) {
			j = indices.get(0);
		}

		if (j == null) {
			// Birth - there is no cycle in matrix Z that contains sigma

			// Let j be the index of the first column in C that contains sigma
			j = ZigZagUtility.findFirstIndexContainingElement(C, sigma, this.integerComparator);

			// Let l be the index of the row of the lowest nonzero element in B[j]
			Integer l = ZigZagUtility.low(B.get(j), this.integerComparator);

			// 1. Prepend D C[j] to Z_i to get Z_i'. Prepend i+1 to the birth vector b_i to get
			// b_{i + 1}
			IntSparseFormalSum<U> DC_j = ZigZagUtility.computeBoundary(C.get(j), chainModule);
			int prependLocation = ZigZagUtility.prependColumn(Z, DC_j);


			BirthDescriptor descriptor = new BirthDescriptor(internalIndex, externalIndex, sigma.getDimension() - 1);
			this.birthDescriptors.put(prependLocation, descriptor);
			System.out.println(internalIndex + ": Birth (" + externalIndex + ", _) @ " + (sigma.getDimension() - 1) +  " - removal of " + sigma);

			// 2. Let c = C[j][sigma]. Let r_sigma be the row of sigma in matrix C. We 
			// prepend the row -r_sigma/c to the matrix B_i to get B_i'.

			IntSparseFormalSum<U> C_j = C.get(j);
			int c = C_j.getCoefficient(sigma);
			IntSparseFormalSum<Integer> r_sigma = ZigZagUtility.getRow(C, sigma);
			IntSparseFormalSum<Integer> n_r_sigma_c = integerChainModule.multiply(field.negate(field.invert(c)), r_sigma);
			
			ZigZagUtility.writeRow(B, n_r_sigma_c, prependLocation);

			// 3. Subtract (r_sigma[k]/c) * C[j] from every column C[k] to get C'.
			for (Integer k: C.keySet()) {
				// TODO: Do we need this guard?
				if (k.equals(j)) {
					continue;
				}

				int coefficient = n_r_sigma_c.getCoefficient(k);
				chainModule.accumulate(C.get(k), C_j, coefficient);
			}

			// 4. Subtract (B'[k][l]/B'[j][l]) * B'[j] from every other column B'[k].
			IntSparseFormalSum<Integer> B_j = B.get(j);
			for (Integer k: B.keySet()) {
				if (k.equals(j)) {
					continue;
				}

				int coefficient = field.negate(field.divide(B.get(k).getCoefficient(l), B_j.getCoefficient(l)));
				integerChainModule.accumulate(B.get(k), B_j, coefficient);
			}

			// 5. Drop row l and column j from B' to get B_{i+1}.
			B.remove(j);

			for (Integer k: B.keySet()) {
				B.get(k).remove(l);
			}

			// 5. Drop column l from Z'
			Z.remove(l);

			// 5. Drop column j from C to get C_{i+1}
			C.remove(j);

			// 6. Reduce Z_{i+1} initially set to Z_i
			while (true) {
				U s = ZigZagUtility.low(Z.get(j), this.basisComparator);

				if (s == null) {
					break;
				}

				//Integer k = this.findColumnWithGivenLow(Z, s, this.filteredComparator);
				Integer k = null;

				for (Integer k_candidate: Z.keySet()) {
					if (k_candidate < j) {
						U low_candidate = ZigZagUtility.low(Z.get(k_candidate), this.basisComparator);
						if (low_candidate != null && low_candidate.equals(s)) {
							k = k_candidate;
							break;
						}
					}
				}

				if (k == null) {
					break;
				}

				int s_jk = field.divide(Z.get(j).getCoefficient(s), Z.get(k).getCoefficient(s));
				int negative_s_jk = field.negate(s_jk);
				chainModule.accumulate(Z.get(j), Z.get(k), negative_s_jk);

				// in B, add row j multiplied by s_jk to row k
				for (Integer columnIndex: B.keySet()) {
					IntSparseFormalSum<Integer> column = B.get(columnIndex);
					integerChainModule.accumulate(column, k, field.multiply(column.getCoefficient(j), s_jk));
				}
			}
		} else {
			// Death
			// Z_j is the first cycle that contains simplex sigma

			// update birth vector
			BirthDescriptor descriptor = this.birthDescriptors.get(j);
			
			assert (descriptor != null);
			
			if (descriptor != null) {
				if (descriptor.externalIndex != externalIndex) {
					this.barcodes.addInterval(descriptor.dimension, descriptor.externalIndex, externalIndex);
				}
			}
			
			this.birthDescriptors.remove(j);
			
			System.out.println(internalIndex + ": Death (" + this.internalExternalIndexMap.get(j) + ", " + externalIndex + ") @ " + (sigma.getDimension()) +  " - removal of " + sigma);

			// TODO: ************** CHECK
			// 1. Change basis to remove sigma from matrix Z
			for (int k_index = 1; k_index < indices.size(); k_index++) {
				int k = indices.get(k_index);
				if (k > j && Z.get(k).containsObject(sigma)) {

					U low_Zi_k = ZigZagUtility.low(Z.get(k), this.basisComparator);
					int sigma_jk = field.divide(Z.get(k).getCoefficient(sigma), Z.get(j).getCoefficient(sigma));
					int negative_sigma_jk = field.negate(sigma_jk);
					chainModule.accumulate(Z.get(k), Z.get(j), negative_sigma_jk);

					// in B, add row k multiplied by sigma_jk to row j
					for (Integer BIndex: B.keySet()) {
						IntSparseFormalSum<Integer> column = B.get(BIndex);
						integerChainModule.accumulate(column, j, field.multiply(column.getCoefficient(k), sigma_jk));
					}

					U low_Zi1_k = ZigZagUtility.low(Z.get(k), this.basisComparator);
					if (low_Zi1_k != null && low_Zi1_k != null && this.basisComparator.compare(low_Zi1_k, low_Zi_k) > 0) {
						// TODO: Do we need this????????
						//j = k;
					}
				}
			}
			
			// 2. Subtract cycle (C[k][sigma]/Z[j][sigma]) * Z[j] from every chain C[k]
			for (Integer CIndex: C.keySet()) {
				IntSparseFormalSum<U> Ck = C.get(CIndex);
				int coefficient = field.negate(field.divide(Ck.getCoefficient(sigma), Z.get(j).getCoefficient(sigma)));
				chainModule.accumulate(Ck, Z.get(j), coefficient);
			}

			// 3. Drop Z_{i+1}[j], the corresponding entry in vectors b_i and idx_i, row j 
			// from B_i, row sigma from C_i and Z (as well as row and column of sigma from D_i)
			Z.remove(j);

			if (!ZigZagUtility.getAscendingIndicesContainingElement(Z, sigma, this.integerComparator).isEmpty()) {
				System.out.println("Invalid removal!");
			}

			// remove row j from B_i
			for (Integer BIndex: B.keySet()) {
				B.get(BIndex).remove(j);
			}

			// remove row sigma from C_i
			for (Integer CIndex: C.keySet()) {
				C.get(CIndex).remove(sigma);
			}

			// remove row sigma from Z_i
			for (Integer ZIndex: Z.keySet()) {
				Z.get(ZIndex).remove(sigma);
			}
		}

		this.checkInvariant();

		internalIndex++;
	}

	public void checkInvariant() {
		// Check that DZ = 0
		for (Integer ZIndex: Z.keySet()) {
			IntSparseFormalSum<U> Zk = Z.get(ZIndex);
			IntSparseFormalSum<U> DCk = ZigZagUtility.computeBoundary(Zk, chainModule);
			if (!DCk.isEmpty()) {
				System.out.println("Invariant Violated!");
			}
		}
		
		// check that DC = ZB
		for (Integer CIndex: C.keySet()) {
			IntSparseFormalSum<U> Ck = C.get(CIndex);
			IntSparseFormalSum<U> DCk = ZigZagUtility.computeBoundary(Ck, chainModule);

			IntSparseFormalSum<U> ZBk = ZigZagUtility.multiply(this.Z, this.B.get(CIndex), this.field);

			if (!DCk.equals(ZBk)) {
				System.out.println("Invariant Violated!");
			}
		}
		
		checkReduced(Z, this.basisComparator);
		checkReduced(B, this.integerComparator);
	}
	
	protected <X, Y> void checkReduced(Map<X, IntSparseFormalSum<Y>> map, Comparator<Y> comparator) {
		Map<Y, X> lowMap = new THashMap<Y, X>();
		for (X index: map.keySet()) {
			Y lowElement = ZigZagUtility.low(map.get(index), comparator);
			if (lowMap.containsKey(lowElement)) {
				System.out.println("Matrix is not reduced!");
			}
		}
	}
}
