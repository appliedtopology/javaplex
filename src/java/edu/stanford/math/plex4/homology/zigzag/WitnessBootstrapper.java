package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.WitnessBicomplex;
import edu.stanford.math.plex4.streams.impl.WitnessStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;

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
		 * 0    1      2            3    4             5      6
		 * X <- X_0 <- X_0 x Y_0 <- Z -> X_0 x Y_0 -> Y_0 -> Y
		 * 0                        1                         2
		 */

		WitnessStream<T> X_stream = null;
		WitnessStream<T> Y_stream = null;
		List<SimplexPair> X = null;
		List<SimplexPair> Y = null;
		Simplex x = null;
		Simplex y = null;

		X_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_stream.finalizeStream();
		x = SimplexStreamUtility.getFirstVertex(X_stream);

		boolean x_added = false;

		for (int j = 1; j < this.indexSelections.size(); j++) {
			Y_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
			Y_stream.finalizeStream();
			y = SimplexStreamUtility.getFirstVertex(Y_stream);
			Y = SimplexStreamUtility.embedSecond(x, Y_stream);

			WitnessBicomplex<T> Z = new WitnessBicomplex<T>(X_stream, Y_stream, maxDimension + 1);
			Z.finalizeStream();

			if (!x_added) {
				X = SimplexStreamUtility.embedFirst(X_stream, y);
				SimplexStreamUtility.sortAscendingPairs(X);

				for (SimplexPair pair: X) {
					if (pair.getDimension() <= maxDimension + 1)
						basisTracker.add(pair, 2 * j - 2);
				}

				x_added = true;
			}

			AbstractFilteredStream<Simplex> X_0_stream = SimplexStreamUtility.projectFirst(Z);
			AbstractFilteredStream<Simplex> Y_0_stream = SimplexStreamUtility.projectSecond(Z);

			List<SimplexPair> X_0 = SimplexStreamUtility.embedFirst(X_0_stream, y);
			List<SimplexPair> Y_0 = SimplexStreamUtility.embedSecond(x, Y_0_stream);

			List<SimplexPair> XY_0 = SimplexStreamUtility.createProductList(X_0_stream, Y_0_stream);

			List<SimplexPair> X_diff_X_0 = SimplexStreamUtility.getDifference(X, X_0);
			List<SimplexPair> Y_diff_Y_0 = SimplexStreamUtility.getDifference(Y, Y_0);

			List<SimplexPair> XY_diff_X_0 = SimplexStreamUtility.getDifference(XY_0, X_0);
			List<SimplexPair> XY_diff_Z = SimplexStreamUtility.getDifference(XY_0, Z);
			List<SimplexPair> XY_diff_Y_0 = SimplexStreamUtility.getDifference(XY_0, Y_0);

			// X <- X_0
			SimplexStreamUtility.sortDescendingPairs(X_diff_X_0);

			for (SimplexPair pair: X_diff_X_0) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.remove(pair, 2 * j - 1);
			}

			// X_0 <- X_0 x Y_0
			SimplexStreamUtility.sortAscendingPairs(XY_diff_X_0);

			for (SimplexPair pair: XY_diff_X_0) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.add(pair, 2 * j - 1);
			}

			//  X_0 x Y_0 <- Z
			SimplexStreamUtility.sortDescendingPairs(XY_diff_Z);

			for (SimplexPair pair: XY_diff_Z) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.remove(pair, 2 * j - 1);
			}

			// Z -> X_0 x Y_0
			Collections.reverse(XY_diff_Z);

			for (SimplexPair pair: XY_diff_Z) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.add(pair, 2 * j);
			}

			// X_0 x Y_0  -> Y_0
			SimplexStreamUtility.sortDescendingPairs(XY_diff_Y_0);

			for (SimplexPair pair: XY_diff_Y_0) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.remove(pair, 2 * j);
			}

			// Y_0 -> Y
			SimplexStreamUtility.sortAscendingPairs(Y_diff_Y_0);

			for (SimplexPair pair: Y_diff_Y_0) {
				if (pair.getDimension() <= maxDimension + 1)
					basisTracker.add(pair, 2 * j);
			}

			X_stream = Y_stream;
			X = Y;
			x = y;
		}

		return basisTracker.getBarcodes().filterByMaxDimension(1);
	}

	public IntBarcodeCollection performBootstrapShort() {
		HomologyBasisTracker<SimplexPair> basisTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		/*
		 * 0    1        2    3        4
		 * X <- X x Y <- Z -> X x Y -> Y
		 * 0             1             2
		 */

		WitnessStream<T> X_0 = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_0.setPlex3Compatbility(false);
		X_0.finalizeStream();

		WitnessStream<T> Y_0 = null;

		boolean x_added = false;

		List<SimplexPair> X = null;
		List<SimplexPair> Y = null;

		for (int j = 1; j < this.indexSelections.size(); j++) {
			Y_0 = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
			Y_0.setPlex3Compatbility(false);
			Y_0.finalizeStream();

			WitnessBicomplex<T> Z_0 = new WitnessBicomplex<T>(X_0, Y_0, maxDimension);
			Z_0.finalizeStream();
			Z_0.ensureAllFaces();
			
			AbstractPersistenceBasisAlgorithm<SimplexPair, IntSparseFormalSum<SimplexPair>> algo = PersistenceAlgorithmInterface.getIntSimplexPairAbsoluteHomology(maxDimension + 1);
			IntAnnotatedBarcodeCollection<IntSparseFormalSum<SimplexPair>> bc = algo.computeAnnotatedIntervals(Z_0);
			System.out.println(bc);

			Simplex x = SimplexStreamUtility.getFirstVertex(X_0);
			Simplex y = SimplexStreamUtility.getFirstVertex(Y_0);

			if (!x_added) {
				X = SimplexStreamUtility.embedFirst(X_0, y);
				Collections.sort(X, SimplexPairComparator.getInstance());

				for (SimplexPair pair: X) {
					if (pair.getDimension() <= maxDimension + 1)
						basisTracker.add(pair, 4 * j - 4);
				}

				x_added = true;
			}

			Y = SimplexStreamUtility.embedSecond(x, Y_0);

			List<SimplexPair> XY = SimplexStreamUtility.createProductList(X_0, Y_0);

			List<SimplexPair> XY_diff_X = SimplexStreamUtility.getDifference(XY, X);
			List<SimplexPair> XY_diff_Y = SimplexStreamUtility.getDifference(XY, Y);
			List<SimplexPair> XY_diff_Z = SimplexStreamUtility.getDifference(XY, Y);

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

		return basisTracker.getBarcodes().filterByMaxDimension(maxDimension);
	}
}
