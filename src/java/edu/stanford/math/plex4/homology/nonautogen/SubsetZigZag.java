package edu.stanford.math.plex4.homology.nonautogen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class SubsetZigZag {
	private static IntAbstractField intField = ModularIntField.getInstance(2);

	public static void test1(AbstractFilteredStream<Simplex> X, AbstractFilteredStream<Simplex> Y, AbstractFilteredStream<SimplexPair> Z) {
		HomologyBasisTracker<Simplex> xTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		HomologyBasisTracker<Simplex> yTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		
		AbstractFilteredStream<SimplexPair> XY = createProductStream(X, Y);
		
		HomologyBasisTracker<SimplexPair> xyTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());
		
		AbstractFilteredStream<Simplex> X_0 = projectFirst(Z);
		AbstractFilteredStream<Simplex> Y_0 = projectSecond(Z);
		
		/*
		 * 0    1        2    3        4
		 * X <- X x Y <- Z -> X x Y -> Y
		 * 0             1             2
		 */
		
		for (Simplex x: X) {
			//xTracker.add(x, 0);
		}
		
		for (Simplex y: Y) {
			//yTracker.add(y, 3);
		}
		
		
		List<SimplexPair> XYset = new ArrayList<SimplexPair>();
		for (SimplexPair pair: XY) {
			XYset.add(pair);
		}
		
		Collections.sort(XYset, SimplexPairComparator.getInstance());
		
		int i = 0; 
		for (SimplexPair pair: XYset) {
			xyTracker.add(pair, 0);
			i++;
		}
		
		//xyTracker.checkInvariant();
		
		List<SimplexPair> difference = getDifference(createProductStream(X, Y), Z, SimplexPairComparator.getInstance());
		
		for (SimplexPair pair: difference) {
			xyTracker.remove(pair, 2);
		}
		
		Collections.reverse(difference);
		
		for (SimplexPair pair: difference) {
			xyTracker.add(pair, 3);
		}

		IntBarcodeCollection bc = xyTracker.getBarcodes();
		
		bc.draw();
		
		System.out.println(bc);
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
	
	static void test2(AbstractFilteredStream<Simplex> X, AbstractFilteredStream<Simplex> Y, AbstractFilteredStream<SimplexPair> Z) {
		HomologyBasisTracker<Simplex> xTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		HomologyBasisTracker<Simplex> yTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		
		AbstractFilteredStream<Simplex> X_0 = projectFirst(Z);
		AbstractFilteredStream<Simplex> Y_0 = projectSecond(Z);
		
		/*
		 * 0    1      2            3    4            5      6
		 * X <- X_0 <- X_0 x Y_0 <- Z -> X_0 x Y_0 -> Y_0 -> Y
		 */
		
		for (Simplex x: X) {
			xTracker.add(x, 0);
		}
		
		List<Simplex> X_X_0_difference = getDifference(X, X_0, SimplexComparator.getInstance());
		
		for (Simplex x: X_X_0_difference) {
			//xTracker.
		}
		
		
		for (Simplex y: Y) {
			yTracker.add(y, 0);
		}
		
	}
	
	static <U> List<U> getDifference(AbstractFilteredStream<U> X, AbstractFilteredStream<U> A, Comparator<U> comparator) {
		List<U> list = new ArrayList<U>();
		
		for (U x: X) {
			list.add(x);
		}
		
		for (U a: A) {
			list.remove(a);
		}
		
		Collections.sort(list, comparator);
		Collections.reverse(list);
		
		return list;
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
