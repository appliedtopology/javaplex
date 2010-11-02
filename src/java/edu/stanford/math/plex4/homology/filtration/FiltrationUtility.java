package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.DoubleAugmentedBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TIntObjectIterator;

public class FiltrationUtility {
	
	public static DoubleBarcode transformBarcode(IntBarcode intBarcode, FiltrationConverter converter) {
		DoubleBarcode barcode = new DoubleBarcode(intBarcode.getDimension());
		
		for (IntHalfOpenInterval interval: intBarcode.getIntervals()) {
			barcode.addInterval(converter.transformInterval(interval));
		}
		
		return barcode;
	}
	
	public static DoubleBarcodeCollection transformBarcodeCollection(IntBarcodeCollection intBarcodeCollection, FiltrationConverter converter) {
		DoubleBarcodeCollection barcodeCollection = new DoubleBarcodeCollection();
		
		for (TIntObjectIterator<IntBarcode> iterator = intBarcodeCollection.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			IntBarcode intBarcode = iterator.value();
			for (IntHalfOpenInterval interval: intBarcode.getIntervals()) {
				barcodeCollection.addInterval(iterator.key(), converter.transformInterval(interval));
			}
		}
		
		return barcodeCollection;
	}
	
	public static <T> DoubleAugmentedBarcode<T> transformBarcode(IntAugmentedBarcode<T> intBarcode, FiltrationConverter converter) {
		DoubleAugmentedBarcode<T> barcode = new DoubleAugmentedBarcode<T>(intBarcode.getDimension());
		
		for (ObjectObjectPair<IntHalfOpenInterval, T> pair: intBarcode.getIntervals()) {
			barcode.addInterval(converter.transformInterval(pair.getFirst()), pair.getSecond());
		}
		
		return barcode;
	}
	
	public static <T> DoubleAugmentedBarcodeCollection<T> transformBarcodeCollection(IntAugmentedBarcodeCollection<T> intBarcodeCollection, FiltrationConverter converter) {
		DoubleAugmentedBarcodeCollection<T> barcodeCollection = new DoubleAugmentedBarcodeCollection<T>();
		
		for (TIntObjectIterator<IntAugmentedBarcode<T>> iterator = intBarcodeCollection.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			IntAugmentedBarcode<T> intBarcode = iterator.value();
			for (ObjectObjectPair<IntHalfOpenInterval, T> pair: intBarcode.getIntervals()) {
				barcodeCollection.addInterval(iterator.key(), converter.transformInterval(pair.getFirst()), pair.getSecond());
			}
		}
		
		return barcodeCollection;
	}
}
