package edu.stanford.math.plex4.homology.zigzag.deprecated;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class ProductSubsetZigzag {
	
	public static BarcodeCollection<Integer> testProjection(AbstractFilteredStream<Simplex> X_stream, AbstractFilteredStream<Simplex> Y_stream, AbstractFilteredStream<SimplexPair> Z_stream) {
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
		
		//WitnessBootstrap
		
		//AnnotatedIntervalTracker<Integer, IntSparseFormalSum<SimplexPair>> intermediate = HomologyBasisTrackerOld.projectFirst(XTracker.intervalTracker, ZTracker, 1, XTracker);
		//AnnotatedIntervalTracker<Integer, IntSparseFormalSum<Simplex>> result = HomologyBasisTrackerOld.projectSecond(intermediate, YTracker, 2);
		//result.endAllIntervals(2);
		//return result.getBarcodes();
		return null;
	}
}
