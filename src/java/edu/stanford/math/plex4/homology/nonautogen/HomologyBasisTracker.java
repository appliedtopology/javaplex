package edu.stanford.math.plex4.homology.nonautogen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.streams.derived.TensorStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.TObjectIntIterator;

public class HomologyBasisTracker<U> {
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

	protected int i = 0;
	
	protected Map<Integer, IntSparseFormalSum<U>> Z = new THashMap<Integer, IntSparseFormalSum<U>>();
	//protected Map<U, IntSparseFormalSum<Integer>> B = new THashMap<U, IntSparseFormalSum<Integer>>();
	protected List<IntSparseFormalSum<Integer>> B = new ArrayList<IntSparseFormalSum<Integer>>();
	protected THashMap<U, IntSparseFormalSum<U>> C = new THashMap<U, IntSparseFormalSum<U>>();
	//protected THashMap<U, Integer> ZLowMap = new THashMap<U, Integer>(); // maps low -> column with low
	//protected THashMap<Integer, U> BLowMap = new THashMap<Integer, U>();
	protected List<Integer> ZIndices = new ArrayList<Integer>(); // maintains ordering of Z columns
	protected List<U> CIndices = new ArrayList<U>();
	
	public HomologyBasisTracker(IntAbstractField field, Comparator<U> basisComparator, AbstractFilteredStream<U> stream) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.integerChainModule = new IntAlgebraicFreeModule<Integer>(this.field);
		this.basisComparator = basisComparator;
		this.filteredComparator = new FilteredComparator<U>(stream, this.basisComparator);
		this.stream = stream;
	}

	private static class IndexLinearizer {
		private final int J;

		IndexLinearizer(int I, int J) {
			this.J = J;
		}

		int getLinearIndex(int i, int j) {
			return i * J + j;
		}

		int getFirstIndex(int linearIndex) {
			return linearIndex / J;
		}

		int getSecondIndex(int linearIndex) {
			return linearIndex % J;
		}
	}

	public Collection<IntSparseFormalSum<Integer>> getB() {
		return B;
	}

	public Collection<IntSparseFormalSum<Integer>> getH() {
		Set<Integer> pivots = new HashSet<Integer>();

		for (IntSparseFormalSum<Integer> chain: B) {
			Integer low = low(chain, integerComparator);
			pivots.remove(low);
		}

		List<IntSparseFormalSum<Integer>> collection = new ArrayList<IntSparseFormalSum<Integer>>();

		for (Integer pivot: pivots) {
			collection.add(integerChainModule.createNewSum(pivot));
		}

		return collection;
	}

	public Collection<IntSparseFormalSum<Integer>> getV() {
		Collection<IntSparseFormalSum<Integer>> V = this.getB();
		V.addAll(this.getH());
		return V;
	}
	
	public void add(U sigma) {
		IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(sigma), stream.getBoundary(sigma));
		
		// compute representation of boundary of sigma in terms of cycles Z_i
		IntSparseFormalSum<Integer> v = new IntSparseFormalSum<Integer>();

		// compute v such that d sigma = Z v
		while (true) {
			U lowElement = HomologyBasisTracker.low(boundary, this.filteredComparator);
			if (lowElement == null) {
				break;
			}

			Integer columnWithLow = findColumnWithGivenLow(Z, lowElement, this.filteredComparator);
			if (columnWithLow == null) {
				break;
			}

			int a = Z.get(columnWithLow).getCoefficient(lowElement);
			int b = boundary.getCoefficient(lowElement);
			int multiplier = this.field.negate(this.field.divide(b, a));
			chainModule.accumulate(boundary, Z.get(columnWithLow), multiplier);

			v.put(field.negate(multiplier), columnWithLow);
		}

		IntSparseFormalSum<Integer> u = new IntSparseFormalSum<Integer>();

		// compute u, v' such that d sigma = Z g = Z (B u + v')
		while (true) {
			Integer lowElement = low(v, this.integerComparator);
			if (lowElement == null) {
				break;
			}

			int columnWithLow = findColumnWithGivenLow(B, lowElement, this.integerComparator);
			if (columnWithLow < 0) {
				break;
			}

			int a = B.get(columnWithLow).getCoefficient(lowElement);
			int b = v.getCoefficient(lowElement);
			int multiplier = this.field.negate(this.field.divide(b, a));
			integerChainModule.accumulate(v, B.get(columnWithLow), multiplier);

			u.put(field.negate(multiplier), columnWithLow);
		}

		IntSparseFormalSum<Integer> v_prime = v;

		/*
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
		 */
		
		i++;
	}

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

	protected static <X> X low(IntSparseFormalSum<X> chain, Comparator<X> comparator) {

		X maxObject = null;

		for (TObjectIntIterator<X> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || comparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}

		return maxObject;
	}
	
	protected static <Y> int findColumnWithGivenLow(List<IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

		for (int i = 0; i < m.size(); i++) {
			IntSparseFormalSum<Y> column = m.get(i);
			Y columnLow = low(column, comparator);
			if (columnLow != null && low.equals(columnLow)) {
				return i;
			}
		}

		return -1;
	}
	
	protected <X, Y> X findColumnWithGivenLow(Map<X, IntSparseFormalSum<Y>> m, Y low, Comparator<Y> comparator) {

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
}
