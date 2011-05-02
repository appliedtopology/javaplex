package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.nonautogen.WitnessBicomplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class WitnessBootstrapper<T> {
	protected final AbstractSearchableMetricSpace<T> metricSpace;
	protected final List<LandmarkSelector<T>> indexSelections;
	protected final IntAbstractField intField = ModularIntField.getInstance(2);

	protected final int maxDimension;
	protected final double maxDistance;

	public WitnessBootstrapper(AbstractSearchableMetricSpace<T> metricSpace, List<LandmarkSelector<T>> indexSelections, int maxDimension, double maxDistance) {
		this.metricSpace = metricSpace;
		this.indexSelections = indexSelections;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
	}

	public WitnessBootstrapper(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, int numSelections, int selectionSize) {
		this.metricSpace = metricSpace;
		this.indexSelections = new ArrayList<LandmarkSelector<T>>();
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;

		selectionSize = Math.min(metricSpace.size(), selectionSize);

		for (int selection = 0; selection < numSelections; selection++) {
			LandmarkSelector<T> selector = new MaxMinLandmarkSelector<T>(metricSpace, selectionSize);
			this.indexSelections.add(selector);
		}
	}

	public IntBarcodeCollection performBootstrap() {
		HomologyBasisTracker<SimplexPair> basisTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		/*
		 * 0    1        2    3        4
		 * X <- X x Y <- Z -> X x Y -> Y
		 * 0             1             2
		 */

		LazyWitnessStream<T> X_0 = new LazyWitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_0.finalizeStream();

		LazyWitnessStream<T> Y_0 = null;

		boolean x_added = false;

		List<SimplexPair> X = null;
		List<SimplexPair> Y = null;

		for (int j = 1; j < this.indexSelections.size(); j++) {
			Y_0 = new LazyWitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension, maxDistance, indexSelections.get(0).getLandmarkPoints());
			Y_0.finalizeStream();

			WitnessBicomplex<T> Z_0 = new WitnessBicomplex<T>(this.metricSpace, indexSelections.get(j-1), indexSelections.get(j), maxDimension, maxDistance, SimplexPairComparator.getInstance());
			Z_0.finalizeStream();

			Simplex x = getFirstVertex(X_0);
			Simplex y = getFirstVertex(Y_0);

			if (!x_added) {
				X = embedFirst(X_0, y);
				Collections.sort(X, SimplexPairComparator.getInstance());

				for (SimplexPair pair: X) {
					if (pair.getDimension() <= maxDimension + 1)
						basisTracker.add(pair, 4 * j - 4);
				}

				x_added = true;
			}

			Y = embedSecond(x, Y_0);

			List<SimplexPair> XY = createProductList(X_0, Y_0);
			List<SimplexPair> Z = getSortedList(Z_0, SimplexPairComparator.getInstance());

			List<SimplexPair> XY_diff_X = getDifference(XY, X);
			List<SimplexPair> XY_diff_Y = getDifference(XY, Y);
			List<SimplexPair> XY_diff_Z = getDifference(XY, Y);

			Collections.sort(XY_diff_X, SimplexPairComparator.getInstance());
			Collections.sort(XY_diff_Y, SimplexPairComparator.getInstance());
			Collections.reverse(XY_diff_Y);

			Collections.sort(XY_diff_Z, SimplexPairComparator.getInstance());
			Collections.reverse(XY_diff_Z);

			for (SimplexPair pair: XY_diff_X) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.add(pair, 4 * j - 3);
			}

			for (SimplexPair pair: XY_diff_Z) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.remove(pair, 4 * j - 2);
			}

			Collections.reverse(XY_diff_Z);

			for (SimplexPair pair: XY_diff_Z) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.add(pair, 4 * j - 1);
			}

			for (SimplexPair pair: XY_diff_Y) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.remove(pair, 4 * j);
			}

			X_0 = Y_0;
			X = Y;
		}

		return basisTracker.getBarcodes();
	}

	public static IntBarcodeCollection testSequence(AbstractFilteredStream<Simplex> X_0, AbstractFilteredStream<Simplex> Y_0, AbstractFilteredStream<SimplexPair> Z) {
		IntAbstractField intField = ModularIntField.getInstance(2);
		HomologyBasisTracker<SimplexPair> basisTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		/*
		 * 0    1        2    3        4
		 * X <- X x Y <- Z -> X x Y -> Y
		 * 0             1             2
		 */

		Simplex x = getFirstVertex(X_0);
		Simplex y = getFirstVertex(Y_0);

		List<SimplexPair> X = embedFirst(X_0, y);
		List<SimplexPair> Y = embedSecond(x, Y_0);
		List<SimplexPair> XY = createProductList(X_0, Y_0);

		Collections.sort(X, SimplexPairComparator.getInstance());

		for (SimplexPair pair: X) {
			basisTracker.add(pair, 0);
		}

		List<SimplexPair> XY_diff_X = getDifference(XY, X);
		List<SimplexPair> XY_diff_Y = getDifference(XY, Y);
		List<SimplexPair> XY_diff_Z = getDifference(XY, Y);

		Collections.sort(XY_diff_X, SimplexPairComparator.getInstance());
		Collections.sort(XY_diff_Y, SimplexPairComparator.getInstance());
		Collections.reverse(XY_diff_Y);

		Collections.sort(XY_diff_Z, SimplexPairComparator.getInstance());
		Collections.reverse(XY_diff_Z);

		for (SimplexPair pair: XY_diff_X) {
			basisTracker.add(pair, 1);
		}

		for (SimplexPair pair: XY_diff_Z) {
			basisTracker.remove(pair, 2);
		}

		Collections.reverse(XY_diff_Z);

		for (SimplexPair pair: XY_diff_Z) {
			basisTracker.add(pair, 3);
		}

		for (SimplexPair pair: XY_diff_Y) {
			basisTracker.remove(pair, 4);
		}

		return basisTracker.getBarcodes();
	}

	static <U> List<U> getDifference(Iterable<U> X, Iterable<U> A) {
		List<U> list = new ArrayList<U>();

		for (U x: X) {
			list.add(x);
		}

		for (U a: A) {
			list.remove(a);
		}

		return list;
	}

	static <U> List<U> getSortedList(Iterable<U> X, Comparator<U> comparator) {
		List<U> list = new ArrayList<U>();

		for (U x: X) {
			list.add(x);
		}

		Collections.sort(list, comparator);

		return list;
	}

	static Simplex getFirstVertex(Iterable<Simplex> X) {
		for (Simplex x: X) {
			if (x.getDimension() == 0) {
				return x;
			}
		}

		return null;
	}

	static List<SimplexPair> embedFirst(Iterable<Simplex> X, Simplex y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex x: X) {
			list.add(SimplexPair.createPair(x, y));
		}

		return list;
	}

	static List<SimplexPair> embedSecond(Simplex x, Iterable<Simplex> Y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex y: Y) {
			list.add(SimplexPair.createPair(x, y));
		}

		return list;
	}

	static List<SimplexPair> createProductList(Iterable<Simplex> X, Iterable<Simplex> Y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex x: X) {
			for (Simplex y: Y) {
				list.add(SimplexPair.createPair(x, y));
			}
		}

		Collections.sort(list, SimplexPairComparator.getInstance());

		return list;
	}

	static AbstractFilteredStream<SimplexPair> createProductStream(AbstractFilteredStream<Simplex> X, AbstractFilteredStream<Simplex> Y) {
		ExplicitStream<SimplexPair> product = new ExplicitStream<SimplexPair>(SimplexPairComparator.getInstance());

		for (Simplex x: X) {
			for (Simplex y: Y) {
				product.addElement(SimplexPair.createPair(x, y), Math.max(X.getFiltrationIndex(x), Y.getFiltrationIndex(y)));
			}
		}

		return product;
	}

	static AbstractFilteredStream<Simplex> projectFirst(AbstractFilteredStream<SimplexPair> Z) {
		ExplicitSimplexStream projection = new ExplicitSimplexStream();

		for (SimplexPair pair: Z) {
			projection.addElement(pair.getFirst(), Z.getFiltrationIndex(pair));
		}

		projection.ensureAllFaces();

		return projection;
	}

	static AbstractFilteredStream<Simplex> projectSecond(AbstractFilteredStream<SimplexPair> Z) {
		ExplicitSimplexStream projection = new ExplicitSimplexStream();

		for (SimplexPair pair: Z) {
			projection.addElement(pair.getSecond(), Z.getFiltrationIndex(pair));
		}

		projection.ensureAllFaces();

		return projection;
	}
}
