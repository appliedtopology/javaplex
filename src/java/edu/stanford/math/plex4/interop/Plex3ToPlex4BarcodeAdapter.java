package edu.stanford.math.plex4.interop;

import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;

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
