package edu.stanford.math.plex4.interop;

import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;

/**
 * This class implements a function object which converts plex 3 barcodes to plex 4 barcodes.
 * It is designed to convert the result of the computeRawIntervals function in plex 3 to the type IntBarcodeCollection
 * so that it can be compared with the results of the plex 4 homology algorithms. Note that even though 
 * this function object accepts arguments of type PersistenceInterval[], it is required that the elements in the
 * array are of type PersistenceInterval.Int. The reason for this is that the internal plex 3 homology algorithm
 * computes the integral filtration intervals, but then converts the results to the PersistenceInterval class. Thus,
 * this requirement is mainly for legacy reasons.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex3ToPlex4BarcodeAdapter implements ObjectObjectFunction<PersistenceInterval[], IntBarcodeCollection> {
	private static Plex3ToPlex4BarcodeAdapter instance = new Plex3ToPlex4BarcodeAdapter();

	public static Plex3ToPlex4BarcodeAdapter getInstance() {
		return instance;
	}

	private Plex3ToPlex4BarcodeAdapter(){}

	public IntBarcodeCollection evaluate(PersistenceInterval[] argument) {
		IntBarcodeCollection barcodeCollection = new IntBarcodeCollection();

		for (PersistenceInterval interval: argument) {
			assert (interval instanceof PersistenceInterval.Int);
			PersistenceInterval.Int intInterval = (PersistenceInterval.Int) interval;
			if (interval.infiniteExtent()) {
				barcodeCollection.addRightInfiniteInterval(interval.dimension, intInterval.start);
			} else {
				barcodeCollection.addInterval(interval.dimension, intInterval.start, intInterval.end);
			}
		}

		return barcodeCollection;
	}

}
