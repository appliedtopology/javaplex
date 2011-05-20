package edu.stanford.math.plex4.homology.zigzag;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.zigzag.IntervalTracker.Descriptor;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TObjectIntHashMap;
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
public class HomologyBasisTracker<U extends PrimitiveBasisElement> implements AbstractBasisTracker<U> {
	
	private class BirthDescriptor<V> {
		public int internalIndex;
		public int externalIndex;
		public int dimension;
		public V generator;
		
		public BirthDescriptor(int internalIndex, int externalIndex, int dimension) {
			super();
			this.internalIndex = internalIndex;
			this.externalIndex = externalIndex;
			this.dimension = dimension;
		}
		
		@Override
		public String toString() {
			return "BirthDescriptor [dimension=" + dimension + ", externalIndex=" + externalIndex + ", generator=" + generator + ", internalIndex="
					+ internalIndex + "]";
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

	protected Comparator<U> basisComparator;
	protected final Comparator<Integer> integerComparator = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}
	};
	
	
	
	protected final Comparator<U> indexedComparator = new Comparator<U>() {
		public int compare(U arg0, U arg1) {
			int i0 = objectIndexMap.get(arg0);
			int i1 = objectIndexMap.get(arg1);
			if (i0 > i1) {
				return 1;
			}
			
			if (i0 < i1) {
				return -1;
			}
			
			return basisComparator.compare(arg0, arg1);
		}
	};
	
	protected final Comparator<U> comparator = new Comparator<U>() {
		public int compare(U arg0, U arg1) {
			//int i0 = internalExternalIndexMap.get(objectIndexMap.get(arg0));
			//int i1 =internalExternalIndexMap.get(objectIndexMap.get(arg1));
			int i0 = objectIndexMap.get(arg0);
			int i1 = objectIndexMap.get(arg1);
			if (i0 > i1) {
				return 1;
			}
			
			if (i0 < i1) {
				return -1;
			}
			
			return basisComparator.compare(arg0, arg1);
		}
	};

	protected Map<Integer, IntSparseFormalSum<U>> Z = new THashMap<Integer, IntSparseFormalSum<U>>();
	protected Map<Integer, IntSparseFormalSum<Integer>> B = new THashMap<Integer, IntSparseFormalSum<Integer>>();
	protected Map<Integer, IntSparseFormalSum<U>> C = new THashMap<Integer, IntSparseFormalSum<U>>();

	//protected Map<Integer, BirthDescriptor<IntSparseFormalSum<U>>> birthDescriptors = new THashMap<Integer, BirthDescriptor<IntSparseFormalSum<U>>>();
	
	protected TIntIntHashMap internalExternalIndexMap = new TIntIntHashMap();

	protected TObjectIntHashMap<U> objectIndexMap = new TObjectIntHashMap<U>();
	
	protected Set<U> basisElements = new THashSet<U>();

	protected int internalIndex = 0;

	protected AnnotatedIntervalTracker<Integer, IntSparseFormalSum<U>> intervalTracker = new AnnotatedIntervalTracker<Integer, IntSparseFormalSum<U>>();
	
	protected boolean verify = true;
	
	public static boolean debug = false;
	
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
		//this.comparator = basisComparator;
	}
	
	private static void log(String... args) {
		if (!debug) {
			return;
		}
		
		for (String arg: args) {
			System.out.println(arg);
		}
	}
	
	//public void setBasisComparator(Comparator<U> basisComparator) {
	//	this.basisComparator = basisComparator;
	//	this.comparator = basisComparator;
	//}
	
	public AnnotatedIntervalTracker<Integer, IntSparseFormalSum<U>> getIntervalTracker() {
		return this.intervalTracker;
	}
	
	public IntBarcodeCollection getFiniteBarcodes() {
		return this.intervalTracker.getFiniteBarcodes();
	}
	
	public IntBarcodeCollection getInfiniteBarcodes() {
		return this.intervalTracker.getInfiniteBarcodes();
	}

	public IntBarcodeCollection getBarcodes() {
		return this.intervalTracker.getBarcodes();
	}
	
	public IntAnnotatedBarcodeCollection<IntSparseFormalSum<U>> getFiniteAnnotatedBarcodes() {
		return this.intervalTracker.getFiniteAnnotatedBarcodes();
	}
	
	public IntAnnotatedBarcodeCollection<IntSparseFormalSum<U>> getInfiniteAnnotatedBarcodes() {
		return this.intervalTracker.getInfiniteAnnotatedBarcodes();
	}

	public IntAnnotatedBarcodeCollection<IntSparseFormalSum<U>> getAnnotatedBarcodes() {
		return this.intervalTracker.getAnnotatedBarcodes();
	}

	public void add(U sigma) {
		this.add(sigma, this.internalIndex);
	}

	@SuppressWarnings("unchecked")
	public void add(U sigma, int externalIndex) {
		if (this.basisElements.contains(sigma)) {
			log("Invalid add!");
		}
		
		U[] faces = (U[]) sigma.getBoundaryArray();
		for (U face: faces) {
			if (!this.basisElements.contains(face)) {
				log("Invalid add!");
			}
		}
		
		this.objectIndexMap.put(sigma, internalIndex);
		this.internalExternalIndexMap.put(internalIndex, externalIndex);
		this.basisElements.add(sigma);

		IntSparseFormalSum<U> boundary = BasisTrackingUtility.createNewSum(sigma.getBoundaryCoefficients(), sigma.getBoundaryArray());

		// compute representation of boundary of sigma in terms of cycles Z_i

		// reduce boundary among the cycles
		ObjectObjectPair<IntSparseFormalSum<Integer>, IntSparseFormalSum<U>> Zpair = reduce(Z, boundary, this.comparator, chainModule);
		IntSparseFormalSum<Integer> v = Zpair.getFirst();
		IntSparseFormalSum<U> reduction = Zpair.getSecond();
		
		// verification
		if (!reduction.isEmpty()) {
			log("Invalid Reduction!!!");
		}

		ObjectObjectPair<IntSparseFormalSum<Integer>, IntSparseFormalSum<Integer>> Bpair = reduce(B, v, this.integerComparator, integerChainModule);
		IntSparseFormalSum<Integer> u = Bpair.getFirst();
		IntSparseFormalSum<Integer> v_prime = Bpair.getSecond();

		if (v_prime.isEmpty()) {
			// Birth
			// d sigma is already a boundary
			IntSparseFormalSum<U> newCycle = chainModule.subtract(BasisTrackingUtility.multiply(C, u, this.field), chainModule.createNewSum(sigma));

			int insertionPoint = BasisTrackingUtility.appendColumn(Z, newCycle);

			BirthDescriptor<IntSparseFormalSum<U>> descriptor = new BirthDescriptor<IntSparseFormalSum<U>>(internalIndex, externalIndex, sigma.getDimension());
			descriptor.generator = newCycle;
			
			this.intervalTracker.startInterval(insertionPoint, externalIndex, sigma.getDimension(), newCycle);
			
			log(internalIndex + ": Birth (" + externalIndex + ", _) @ " + sigma.getDimension() +  " - addition of " + sigma);
		} else {
			// Death

			IntSparseFormalSum<U> newChain = chainModule.subtract(BasisTrackingUtility.multiply(C, u, this.field), chainModule.createNewSum(sigma));

			BasisTrackingUtility.appendColumn(C, newChain);
			BasisTrackingUtility.appendColumn(B, integerChainModule.negate(v_prime));
			//BasisTrackingUtility.appendColumn(B, v_prime);

			// update birth vector
			Integer j = BasisTrackingUtility.low(v_prime, this.integerComparator);
			
			this.intervalTracker.endInterval(j, externalIndex);
			
			log(internalIndex + ": Death (" + this.internalExternalIndexMap.get(j) + ", " + externalIndex + ") @ " + (sigma.getDimension() - 1) +  " - addition of " + sigma);
		}

		internalIndex++;

		this.checkInvariant();
	}

	public void remove(U sigma) {
		this.remove(sigma, this.internalIndex);
	}

	public void remove(U sigma, int externalIndex) {
		if (!this.basisElements.contains(sigma)) {
			log("Invalid removal!");
		}
		
		this.basisElements.remove(sigma);
		this.objectIndexMap.remove(sigma);
		
		this.internalExternalIndexMap.put(internalIndex, externalIndex);

		// try to set j to be the first cycle that contains sigma
		List<Integer> indices = BasisTrackingUtility.getAscendingIndicesContainingElement(Z, sigma, this.integerComparator);
		Integer j = null;

		if (indices.size() > 0) {
			j = indices.get(0);
		}

		if (j == null) {
			// Birth - there is no cycle in matrix Z that contains sigma

			// Let j be the index of the first column in C that contains sigma
			j = BasisTrackingUtility.findFirstIndexContainingElement(C, sigma, this.integerComparator);

			// Let l be the index of the row of the lowest nonzero element in B[j]
			Integer l = BasisTrackingUtility.low(B.get(j), this.integerComparator);

			// 1. Prepend D C[j] to Z_i to get Z_i'. Prepend i+1 to the birth vector b_i to get
			// b_{i + 1}
			IntSparseFormalSum<U> DC_j = BasisTrackingUtility.computeBoundary(C.get(j), chainModule);
			int prependLocation = BasisTrackingUtility.prependColumn(Z, DC_j);

			this.intervalTracker.startInterval(prependLocation, externalIndex, sigma.getDimension() - 1, DC_j);
			
			
			log(internalIndex + ": Birth (" + externalIndex + ", _) @ " + (sigma.getDimension() - 1) +  " - removal of " + sigma);

			// 2. Let c = C[j][sigma]. Let r_sigma be the row of sigma in matrix C. We 
			// prepend the row -r_sigma/c to the matrix B_i to get B_i'.

			IntSparseFormalSum<U> C_j = C.get(j);
			int c = C_j.getCoefficient(sigma);
			IntSparseFormalSum<Integer> r_sigma = BasisTrackingUtility.getRow(C, sigma);
			IntSparseFormalSum<Integer> n_r_sigma_c = integerChainModule.multiply(field.negate(field.invert(c)), r_sigma);
			
			BasisTrackingUtility.writeRow(B, n_r_sigma_c, prependLocation);

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
				U s = BasisTrackingUtility.low(Z.get(j), this.comparator);

				if (s == null) {
					break;
				}

				//Integer k = this.findColumnWithGivenLow(Z, s, this.filteredComparator);
				Integer k = null;

				List<Integer> k_candidates = BasisTrackingUtility.getAscendingIndicesWithGivenLow(Z, s, this.integerComparator, this.comparator);
				
				for (Integer k_candidate: k_candidates) {
					if (!k_candidate.equals(j)) {
						k = k_candidate;
						break;
					}
				}
				
				if (k == null) {
					break;
				}

				int s_jk = field.divide(Z.get(j).getCoefficient(s), Z.get(k).getCoefficient(s));
				int negative_s_jk = field.negate(s_jk);
				
				if (k.equals(j)) {
					log("Concurrent Modification!");
				}
				
				chainModule.accumulate(Z.get(j), Z.get(k), negative_s_jk);

				// in B, add row j multiplied by s_jk to row k
				for (Integer columnIndex: B.keySet()) {
					integerChainModule.accumulate(B.get(columnIndex), k, field.multiply(B.get(columnIndex).getCoefficient(j), s_jk));
				}
			}
			
			
			while (true) {
				U s = BasisTrackingUtility.low(Z.get(prependLocation), this.comparator);

				if (s == null) {
					break;
				}

				//Integer k = this.findColumnWithGivenLow(Z, s, this.filteredComparator);
				Integer k = null;

				List<Integer> k_candidates = BasisTrackingUtility.getAscendingIndicesWithGivenLow(Z, s, this.integerComparator, this.comparator);
				
				for (Integer k_candidate: k_candidates) {
					if (!k_candidate.equals(prependLocation)) {
						k = k_candidate;
						break;
					}
				}
				
				if (k == null) {
					break;
				}

				int s_jk = field.divide(Z.get(prependLocation).getCoefficient(s), Z.get(k).getCoefficient(s));
				int negative_s_jk = field.negate(s_jk);
				chainModule.accumulate(Z.get(prependLocation), Z.get(k), negative_s_jk);

				// in B, add row j multiplied by s_jk to row k
				for (Integer columnIndex: B.keySet()) {
					integerChainModule.accumulate(B.get(columnIndex), k, field.multiply(B.get(columnIndex).getCoefficient(prependLocation), s_jk));
				}
			}
			
		} else {
			
			if (internalIndex == 1051) {
				log("stop");
			}
			
			// Death
			// Z_j is the first cycle that contains simplex sigma

			// update birth vector	
			this.intervalTracker.endInterval(j, externalIndex);
			
			log(internalIndex + ": Death (" + this.internalExternalIndexMap.get(j) + ", " + externalIndex + ") @ " + (sigma.getDimension()) +  " - removal of " + sigma);

			//Map<Integer, IntSparseFormalSum<U>> Z_i1 = new THashMap<Integer, IntSparseFormalSum<U>>();
			
			// TODO: ************** CHECK
			// 1. Change basis to remove sigma from matrix Z
			// In other words, we make sure that column j is the only one containing sigma
			for (int k_index = 0; k_index < indices.size(); k_index++) {
				int k = indices.get(k_index);
				if (k != j && Z.get(k).containsObject(sigma)) {

					U low_Zi_k = BasisTrackingUtility.low(Z.get(k), this.comparator);
					int sigma_jk = field.divide(Z.get(k).getCoefficient(sigma), Z.get(j).getCoefficient(sigma));
					int negative_sigma_jk = field.negate(sigma_jk);
					
					//Z_i1.put(k, chainModule.add(Z.get(k), chainModule.multiply(negative_sigma_jk, Z.get(j))));
					chainModule.accumulate(Z.get(k), Z.get(j), negative_sigma_jk);

					// in B, add row k multiplied by sigma_jk to row j
					for (Integer BIndex: B.keySet()) {
						IntSparseFormalSum<Integer> column = B.get(BIndex);
						integerChainModule.accumulate(column, j, field.multiply(column.getCoefficient(k), sigma_jk));
					}

					//U low_Zi1_k = BasisTrackingUtility.low(Z_i1.get(k), this.comparator);
					U low_Zi1_k = BasisTrackingUtility.low(Z.get(k), this.comparator);
					if (low_Zi1_k != null && low_Zi1_k != null && this.comparator.compare(low_Zi1_k, low_Zi_k) < 0) {
						// TODO: Do we need this????????
						//j = k;
					}
				}
			}
			
			// 2. Subtract cycle (C[k][sigma]/Z[j][sigma]) * Z[j] from every chain C[k]
			for (Integer CIndex: C.keySet()) {
				IntSparseFormalSum<U> Ck = C.get(CIndex);
				int coefficient = field.negate(field.divide(Ck.getCoefficient(sigma), Z.get(j).getCoefficient(sigma)));
				chainModule.accumulate(C.get(CIndex), Z.get(j), coefficient);
			}

			/*
			for (Integer k: Z_i1.keySet()) {
				if (Z.containsKey(k)) {
					Z.remove(k);
				}
				Z.put(k, Z_i1.get(k));
			}
			
			Z_i1 = null;
			*/
			
			// 3. Drop Z_{i+1}[j], the corresponding entry in vectors b_i and idx_i, row j 
			// from B_i, row sigma from C_i and Z (as well as row and column of sigma from D_i)
			Z.remove(j);

			if (!BasisTrackingUtility.getAscendingIndicesContainingElement(Z, sigma, this.integerComparator).isEmpty()) {
				@SuppressWarnings("unused")
				List<Integer> list = BasisTrackingUtility.getAscendingIndicesContainingElement(Z, sigma, this.integerComparator);
				log("Invalid removal!");
			}

			// remove row j from B_i
			for (Integer BIndex: B.keySet()) {
				if (B.get(BIndex).containsObject(j)) {
					log("B is not in correct form!");
				}
				B.get(BIndex).remove(j);
			}

			// remove row sigma from C_i
			for (Integer CIndex: C.keySet()) {
				if (C.get(CIndex).containsObject(sigma)) {
					log("C is not in correct form!");
				}
				C.get(CIndex).remove(sigma);
			}

			// remove row sigma from Z_i
			for (Integer ZIndex: Z.keySet()) {
				if (Z.get(ZIndex).containsObject(sigma)) {
					log("Z is not in correct form!");
				}
				Z.get(ZIndex).remove(sigma);
			}
		}

		this.checkInvariant();

		internalIndex++;
	}

	public static <X, Y> ObjectObjectPair<IntSparseFormalSum<X>, IntSparseFormalSum<Y>> reduce(Map<X, IntSparseFormalSum<Y>> M, IntSparseFormalSum<Y> column, Comparator<Y> comparator, IntAlgebraicFreeModule<Y> chainModule) {
		Map<Y, X> lowMap = new THashMap<Y, X>();
		
		for (X key: M.keySet()) {
			IntSparseFormalSum<Y> chain = M.get(key);
			Y low = BasisTrackingUtility.low(chain, comparator);
			if (low != null) {
				if (lowMap.containsKey(low)) {
					log("Matrix is not reduced!");
				}
				
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
			Y low = BasisTrackingUtility.low(reduction, comparator);
			
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
	
	public void checkInvariant() {
		// Check that DZ = 0
		for (Integer ZIndex: Z.keySet()) {
			IntSparseFormalSum<U> Zk = Z.get(ZIndex);
			IntSparseFormalSum<U> DCk = BasisTrackingUtility.computeBoundary(Zk, chainModule);
			if (!DCk.isEmpty()) {
				log("Invariant Violated!");
			}
		}
		
		// check that DC = ZB
		for (Integer CIndex: C.keySet()) {
			IntSparseFormalSum<U> Ck = C.get(CIndex);
			IntSparseFormalSum<U> DCk = BasisTrackingUtility.computeBoundary(Ck, chainModule);

			IntSparseFormalSum<U> ZBk = BasisTrackingUtility.multiply(this.Z, this.B.get(CIndex), this.field);

			if (!DCk.equals(ZBk)) {
				log("Invariant Violated!");
			}
		}
		
		checkReduced(Z, this.comparator);
		checkReduced(B, this.integerComparator);
	}
	
	protected <X, Y> void checkReduced(Map<X, IntSparseFormalSum<Y>> map, Comparator<Y> comparator) {
		Map<Y, X> lowMap = new THashMap<Y, X>();
		for (X index: map.keySet()) {
			Y lowElement = BasisTrackingUtility.low(map.get(index), comparator);
			if (lowMap.containsKey(lowElement)) {
				log("Matrix is not reduced!");
			}
			lowMap.put(lowElement, index);
		}
	}
	
	protected boolean isCycle(IntSparseFormalSum<U> chain) {
		ObjectObjectPair<IntSparseFormalSum<Integer>, IntSparseFormalSum<U>> Zpair = reduce(Z, chain, this.comparator, chainModule);
		IntSparseFormalSum<U> reduction = Zpair.getSecond();
		
		return reduction.isEmpty();
	}
	
	protected boolean isBoundary(IntSparseFormalSum<U> cycle) {

		ObjectObjectPair<IntSparseFormalSum<Integer>, IntSparseFormalSum<U>> Zpair = reduce(Z, cycle, this.comparator, chainModule);
		IntSparseFormalSum<Integer> v = Zpair.getFirst();
		IntSparseFormalSum<U> reduction = Zpair.getSecond();
		
		// if it is not a cycle, it can't be a boundary
		if (!reduction.isEmpty()) {
			return false;
		}

		ObjectObjectPair<IntSparseFormalSum<Integer>, IntSparseFormalSum<Integer>> Bpair = reduce(B, v, this.integerComparator, integerChainModule);
		IntSparseFormalSum<Integer> v_prime = Bpair.getSecond();
		
		return v_prime.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public static AnnotatedIntervalTracker<Integer, IntSparseFormalSum<SimplexPair>> doSomething(AnnotatedIntervalTracker<Integer, IntSparseFormalSum<Simplex>> accumulator, HomologyBasisTracker<SimplexPair> Z, int Z_Index, HomologyBasisTracker<Simplex> X) {
		IntSparseFormalSum<Simplex> projection = null;
		
		Map<Integer, Integer> joinMap = new HashMap<Integer, Integer>();
		
		for (Entry<Integer, Descriptor<?>> Z_entry: Z.intervalTracker.openIntervals.entrySet()) {
			Integer Z_key = Z_entry.getKey();
			Descriptor<IntSparseFormalSum<SimplexPair>> Z_descriptor = (Descriptor<IntSparseFormalSum<SimplexPair>>) Z_entry.getValue();
			int Z_dimension = Z_descriptor.dimension;
			IntSparseFormalSum<SimplexPair> Z_chain = Z_descriptor.generator;
			projection = SimplexStreamUtility.projectFirst(Z_chain, X.chainModule);
			projection = SimplexStreamUtility.filterByDimension(projection, Z_dimension);
			
			for (Entry<Integer, Descriptor<?>> X_entry: accumulator.openIntervals.entrySet()) {
				Integer X_key = X_entry.getKey();
				Descriptor<IntSparseFormalSum<Simplex>> X_descriptor = (Descriptor<IntSparseFormalSum<Simplex>>) X_entry.getValue();
				int X_dimension = X_descriptor.dimension;
				IntSparseFormalSum<Simplex> X_chain = X_descriptor.generator;
				
				if (X_dimension == Z_dimension) {
					IntSparseFormalSum<Simplex> difference = X.chainModule.subtract(X_chain, projection);
					
					if (X.isBoundary(difference)) {
						joinMap.put(X_key, Z_key);
					}
				}
			}
		}
		
		AnnotatedIntervalTracker<Integer, IntSparseFormalSum<SimplexPair>> result = AnnotatedIntervalTracker.join(accumulator, Z.intervalTracker, joinMap, Z_Index);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static AnnotatedIntervalTracker<Integer, IntSparseFormalSum<Simplex>> doSomethingSecond(AnnotatedIntervalTracker<Integer, IntSparseFormalSum<SimplexPair>> Z_tracker, HomologyBasisTracker<Simplex> Y, int Y_index) {
		IntSparseFormalSum<Simplex> projection = null;
		
		Map<Integer, Integer> joinMap = new HashMap<Integer, Integer>();
		
		for (Entry<Integer, Descriptor<?>> Z_entry: Z_tracker.openIntervals.entrySet()) {
			Integer Z_key = Z_entry.getKey();
			Descriptor<IntSparseFormalSum<SimplexPair>> Z_descriptor = (Descriptor<IntSparseFormalSum<SimplexPair>>) Z_entry.getValue();
			int Z_dimension = Z_descriptor.dimension;
			IntSparseFormalSum<SimplexPair> Z_chain = Z_descriptor.generator;
			projection = SimplexStreamUtility.projectSecond(Z_chain, Y.chainModule);
			projection = SimplexStreamUtility.filterByDimension(projection, Z_dimension);
			
			for (Entry<Integer, Descriptor<?>> Y_entry: Y.intervalTracker.openIntervals.entrySet()) {
				Integer Y_key = Y_entry.getKey();
				Descriptor<IntSparseFormalSum<Simplex>> Y_descriptor = (Descriptor<IntSparseFormalSum<Simplex>>) Y_entry.getValue();
				int Y_dimension = Y_descriptor.dimension;
				IntSparseFormalSum<Simplex> Y_chain = Y_descriptor.generator;
				
				if (Y_dimension == Z_dimension) {
					IntSparseFormalSum<Simplex> difference = Y.chainModule.subtract(Y_chain, projection);
					
					if (Y.isBoundary(difference)) {
						joinMap.put(Z_key, Y_key);
					}
				}
			}
		}
		
		AnnotatedIntervalTracker<Integer, IntSparseFormalSum<Simplex>> result = AnnotatedIntervalTracker.join(Z_tracker, Y.intervalTracker, joinMap, Y_index);
		return result;
	}
}
