package edu.stanford.math.plex4.homology.zigzag;

import java.util.Collections;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;

public class ProductSubsetZigzag {
	public static IntBarcodeCollection testShortSequence(AbstractFilteredStream<Simplex> X, AbstractFilteredStream<Simplex> Y, AbstractFilteredStream<SimplexPair> Z) {
		IntAbstractField intField = ModularIntField.getInstance(2);
		HomologyBasisTracker<SimplexPair> basisTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		/*
		 * 0    1        2    3        4
		 * X <- X x Y <- Z -> X x Y -> Y
		 */

		Simplex x = SimplexStreamUtility.getFirstVertex(X);
		Simplex y = SimplexStreamUtility.getFirstVertex(Y);

		List<SimplexPair> Xlist = SimplexStreamUtility.embedFirst(X, y);
		List<SimplexPair> Ylist = SimplexStreamUtility.embedSecond(x, Y);
		List<SimplexPair> XY = SimplexStreamUtility.createProductList(X, Y);

		Collections.sort(Xlist, SimplexPairComparator.getInstance());

		for (SimplexPair pair: Xlist) {
			basisTracker.add(pair, 0);
		}

		List<SimplexPair> XY_diff_X = SimplexStreamUtility.getDifference(XY, Xlist);
		List<SimplexPair> XY_diff_Y = SimplexStreamUtility.getDifference(XY, Ylist);
		List<SimplexPair> XY_diff_Z = SimplexStreamUtility.getDifference(XY, Ylist);

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

	public static IntBarcodeCollection testLongSequence(AbstractFilteredStream<Simplex> X_stream, AbstractFilteredStream<Simplex> Y_stream, AbstractFilteredStream<SimplexPair> Z) {
	
		/*
		 * 0    1      2            3    4             5      6
		 * X <- X_0 <- X_0 x Y_0 <- Z -> X_0 x Y_0  -> Y_0 -> Y
		 */
		
		IntAbstractField intField = ModularIntField.getInstance(2);
		HomologyBasisTracker<SimplexPair> basisTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		Simplex x = SimplexStreamUtility.getFirstVertex(X_stream);
		Simplex y = SimplexStreamUtility.getFirstVertex(Y_stream);

		List<SimplexPair> X = SimplexStreamUtility.embedFirst(X_stream, y);
		List<SimplexPair> Y = SimplexStreamUtility.embedSecond(x, Y_stream);
		
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
		
		SimplexStreamUtility.sortAscendingPairs(X);
		
		for (SimplexPair pair: X) {
			basisTracker.add(pair, 0);
		}
		
		SimplexStreamUtility.sortDescendingPairs(X_diff_X_0);
		
		for (SimplexPair pair: X_diff_X_0) {
			basisTracker.remove(pair, 1);
		}
		
		SimplexStreamUtility.sortAscendingPairs(XY_diff_X_0);
		
		for (SimplexPair pair: XY_diff_X_0) {
			basisTracker.add(pair, 2);
		}
		
		SimplexStreamUtility.sortDescendingPairs(XY_diff_Z);
		
		for (SimplexPair pair: XY_diff_Z) {
			basisTracker.remove(pair, 3);
		}
		
		Collections.reverse(XY_diff_Z);
		
		for (SimplexPair pair: XY_diff_Z) {
			basisTracker.add(pair, 4);
		}
		
		SimplexStreamUtility.sortDescendingPairs(XY_diff_Y_0);
		
		for (SimplexPair pair: XY_diff_Y_0) {
			basisTracker.remove(pair, 5);
		}
		
		SimplexStreamUtility.sortAscendingPairs(Y_diff_Y_0);
		
		for (SimplexPair pair: Y_diff_Y_0) {
			basisTracker.add(pair, 6);
		}
		
		return basisTracker.getBarcodes();
	}
	
	public static IntBarcodeCollection testProjection(AbstractFilteredStream<Simplex> X_stream, AbstractFilteredStream<Simplex> Y_stream, AbstractFilteredStream<SimplexPair> Z_stream) {
		IntAbstractField intField = ModularIntField.getInstance(2);
		HomologyBasisTracker<SimplexPair> ZTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());
		HomologyBasisTracker<Simplex> XTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		HomologyBasisTracker<Simplex> YTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		
		for (Simplex x: X_stream) {
			XTracker.add(x, 0);
		}
		
		for (SimplexPair z: Z_stream) {
			ZTracker.add(z, 1);
		}
		
		for (Simplex y: Y_stream) {
			YTracker.add(y, 2);
		}
		
		AnnotatedIntervalTracker<Integer, IntSparseFormalSum<SimplexPair>> intermediate = HomologyBasisTracker.doSomething(XTracker.intervalTracker, ZTracker, 1, XTracker);
		AnnotatedIntervalTracker<Integer, IntSparseFormalSum<Simplex>> result = HomologyBasisTracker.doSomethingSecond(intermediate, YTracker, 2);
		
		return result.getBarcodes();
	}
}
