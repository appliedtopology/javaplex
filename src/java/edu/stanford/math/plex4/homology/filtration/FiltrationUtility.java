package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.DoubleAnnotatedBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TIntObjectIterator;

/**
 * This class contains various static functions that aid in the transformation between objects
 * defined in terms of filtration indices to objects defined in terms of filtration values.
 * 
 * @author Andrew Tausz
 *
 */
public class FiltrationUtility {
	
	/**
	 * This function converts a filtration index barcode to a filtration value barcode.
	 * 
	 * @param intBarcode the integer barcode to convert
	 * @param converter the FiltrationConverter object that computes the index to value mapping
	 * @return the filtration value function applied to the barcode
	 */
	public static DoubleBarcode transformBarcode(IntBarcode intBarcode, FiltrationConverter converter) {
		DoubleBarcode barcode = new DoubleBarcode(intBarcode.getDimension());
		
		for (IntHalfOpenInterval interval: intBarcode.getIntervals()) {
			barcode.addInterval(converter.transform(interval));
		}
		
		return barcode;
	}
	
	/**
	 * This function converts a filtration index barcode collection to a filtration 
	 * value barcode collection.
	 * 
	 * @param intBarcodeCollection the integer barcode collection to convert
	 * @param converter the FiltrationConverter object that computes the index to value mapping
	 * @return the filtration value function applied to the barcode collection
	 */
	public static DoubleBarcodeCollection transformBarcodeCollection(IntBarcodeCollection intBarcodeCollection, FiltrationConverter converter) {
		DoubleBarcodeCollection barcodeCollection = new DoubleBarcodeCollection();
		
		for (TIntObjectIterator<IntBarcode> iterator = intBarcodeCollection.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			IntBarcode intBarcode = iterator.value();
			for (IntHalfOpenInterval interval: intBarcode.getIntervals()) {
				barcodeCollection.addInterval(iterator.key(), converter.transform(interval));
			}
		}
		
		return barcodeCollection;
	}
	
	/**
	 * This function converts a filtration index barcode to a filtration value barcode.
	 * 
	 * @param intBarcode the integer barcode to convert
	 * @param converter the FiltrationConverter object that computes the index to value mapping
	 * @return the filtration value function applied to the barcode
	 */
	public static <T> DoubleAnnotatedBarcode<T> transformBarcode(IntAnnotatedBarcode<T> intBarcode, FiltrationConverter converter) {
		DoubleAnnotatedBarcode<T> barcode = new DoubleAnnotatedBarcode<T>(intBarcode.getDimension());
		
		for (ObjectObjectPair<IntHalfOpenInterval, T> pair: intBarcode.getIntervals()) {
			barcode.addInterval(converter.transform(pair.getFirst()), pair.getSecond());
		}
		
		return barcode;
	}
	
	/**
	 * This function converts a filtration index barcode collection to a filtration value barcode collection.
	 * 
	 * @param intBarcodeCollection the integer barcode collection to convert
	 * @param converter the FiltrationConverter object that computes the index to value mapping
	 * @return the filtration value function applied to the barcode collection
	 */
	public static <T> DoubleAnnotatedBarcodeCollection<T> transformBarcodeCollection(IntAnnotatedBarcodeCollection<T> intBarcodeCollection, FiltrationConverter converter) {
		DoubleAnnotatedBarcodeCollection<T> barcodeCollection = new DoubleAnnotatedBarcodeCollection<T>();
		
		for (TIntObjectIterator<IntAnnotatedBarcode<T>> iterator = intBarcodeCollection.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			IntAnnotatedBarcode<T> intBarcode = iterator.value();
			for (ObjectObjectPair<IntHalfOpenInterval, T> pair: intBarcode.getIntervals()) {
				barcodeCollection.addInterval(iterator.key(), converter.transform(pair.getFirst()), pair.getSecond());
			}
		}
		
		return barcodeCollection;
	}
}
