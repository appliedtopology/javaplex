package edu.stanford.math.plex4.homology.zigzag;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;


public interface AbstractBasisTracker<U> {
	public void add(U sigma, int index);
	public void remove(U sigma, int index);
	
	public IntBarcodeCollection getBarcodes();
	
	//public <V> AbstractBasisTracker<ObjectObjectPair<U, V>> tensorProduct(AbstractBasisTracker<V> tracker);
}
