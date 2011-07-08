package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TObjectIntHashMap;

public class SimpleHomologyBasisTracker<U extends PrimitiveBasisElement> implements AbstractHomologyTracker<Integer, Integer, U, IntSparseFormalSum<U>> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final IntAbstractField field;

	/**
	 * This objects performs the chain computations.
	 */
	protected final IntAlgebraicFreeModule<U> chainModule;

	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<U> basisComparator;

	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
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

	THashMap<U, IntSparseFormalSum<U>> R = new THashMap<U, IntSparseFormalSum<U>>();
	THashMap<U, IntSparseFormalSum<U>> V = new THashMap<U, IntSparseFormalSum<U>>();

	/**
	 * This maps a simplex to the set of columns containing the key as its low value.
	 */
	THashMap<U, THashSet<U>> lowMap = new THashMap<U, THashSet<U>>();

	protected IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> cachedTracker = null;

	protected final TObjectIntHashMap<U> objectIndexMap = new TObjectIntHashMap<U>();

	protected final int minDimension, maxDimension;


	public SimpleHomologyBasisTracker(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}

	public void add(U sigma, Integer index) {
		U i = sigma;

		this.objectIndexMap.put(sigma, index);
		this.cachedTracker = null;

		// initialize V to be the identity matrix
		V.put(i, this.chainModule.createNewSum(this.field.valueOf(1), sigma));

		IntSparseFormalSum<U> boundary = BasisTrackingUtility.createNewSum(sigma.getBoundaryCoefficients(), sigma.getBoundaryArray());

		// form the column R[i] which equals the boundary of the current simplex.
		// store the column as a column in R
		R.put(sigma, boundary);

		// compute low_R(i)
		U low_R_i = this.low(R.get(i));

		// if the boundary of i is empty, then continue to next iteration since there
		// is nothing to process
		if (low_R_i == null) {
			return;
		}

		THashSet<U> matchingLowSimplices = lowMap.get(low_R_i);
		while (matchingLowSimplices != null && !matchingLowSimplices.isEmpty()) {
			Iterator<U> iterator = matchingLowSimplices.iterator();

			U j = iterator.next();

			int c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(low_R_i));
			int negative_c = field.negate(c);
			//R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
			//V.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, V.get(j))));
			this.chainModule.accumulate(R.get(i), R.get(j), negative_c);
			this.chainModule.accumulate(V.get(i), V.get(j), negative_c);

			// remove old low_R(i) entry
			//lowMap.get(low_R_i).remove(i);

			// recompute low_R(i)
			low_R_i = this.low(R.get(i));

			matchingLowSimplices = lowMap.get(low_R_i);
		}

		// store the low value in the map
		if (low_R_i != null) {
			if (!lowMap.containsKey(low_R_i)) {
				lowMap.put(low_R_i, new THashSet<U>());
			}
			lowMap.get(low_R_i).add(i);
		}
	}

	private U low(IntSparseFormalSum<U> intSparseFormalSum) {
		return BasisTrackingUtility.low(intSparseFormalSum, this.indexedComparator);
	}

	public IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> getState() {

		if (this.cachedTracker != null) {
			return this.cachedTracker;
		}

		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> tracker = new IntervalTracker<Integer, Integer, IntSparseFormalSum<U>>();

		tracker.setUseLeftClosedIntervals(true);
		tracker.setUseRightClosedIntervals(false);

		Set<U> births = new THashSet<U>();

		List<U> keySet = new ArrayList<U>(R.keySet());
		Collections.sort(keySet, this.indexedComparator);
		
		int key = 0;
		for (U i: keySet) {
			U low_R_i = this.low(R.get(i));
			int dimension = i.getDimension();
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
				int start = this.objectIndexMap.get(low_R_i);
				int end = this.objectIndexMap.get(i);
				if (end > start) {
					dimension = low_R_i.getDimension();
					if (dimension <= this.maxDimension && dimension >= this.minDimension)
					{
						//barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end, R.get(i));
						tracker.startInterval(key, start, dimension, R.get(i));
						tracker.endInterval(key, end);
					}
				}
			}
			key++;
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (U i: births) {
			int dimension = i.getDimension();
			if (dimension <= this.maxDimension && dimension >= this.minDimension) {
				//barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i), V.get(i));
				int start = this.objectIndexMap.get(i);
				tracker.startInterval(key, start, dimension, V.get(i));
			}
			key++;
		}

		this.cachedTracker = tracker;

		return tracker;
	}
	
	public IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> getStateWithoutFiniteBarcodes(int startOverride) {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> tracker = new IntervalTracker<Integer, Integer, IntSparseFormalSum<U>>();

		tracker.setUseLeftClosedIntervals(true);
		tracker.setUseRightClosedIntervals(false);

		Set<U> births = new THashSet<U>();

		List<U> keySet = new ArrayList<U>(R.keySet());
		Collections.sort(keySet, this.indexedComparator);
		
		int key = 0;
		for (U i: keySet) {
			U low_R_i = this.low(R.get(i));
			int dimension = i.getDimension();
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
			}
			key++;
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (U i: births) {
			int dimension = i.getDimension();
			if (dimension <= this.maxDimension && dimension >= this.minDimension) {
				//int start = this.objectIndexMap.get(i);
				tracker.startInterval(key, startOverride, dimension, V.get(i));
			}
			key++;
		}
		
		
		return tracker;
	}

	public boolean isBoundary(IntSparseFormalSum<U> generator) {
		ObjectObjectPair<IntSparseFormalSum<U>, IntSparseFormalSum<U>> reduction = BasisTrackingUtility.reduce(this.R, generator, this.indexedComparator, this.chainModule);

		IntSparseFormalSum<U> remainder = reduction.getSecond();

		return remainder.isEmpty();
	}

	public void remove(U sigma, Integer index) {
		throw new UnsupportedOperationException();
	}

	public IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> getIntervalTracker() {
		return this.getState();
	}

	public AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<U>> getFiniteAnnotatedBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getFiniteAnnotatedBarcodes();
	}

	public AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<U>> getInfiniteAnnotatedBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getInfiniteAnnotatedBarcodes();
	}

	public AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<U>> getAnnotatedBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getAnnotatedBarcodes();
	}

	public BarcodeCollection<Integer> getFiniteBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getFiniteBarcodes();
	}

	public BarcodeCollection<Integer> getInfiniteBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getInfiniteBarcodes();
	}

	public BarcodeCollection<Integer> getBarcodes() {
		IntervalTracker<Integer, Integer, IntSparseFormalSum<U>> intervalTracker = this.getState();
		return intervalTracker.getBarcodes();
	}
}


