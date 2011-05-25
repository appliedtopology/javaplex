package edu.stanford.math.plex4.homology.zigzag;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;


public interface AbstractBasisTracker<U> {
	public void add(U sigma, int index);
	public void remove(U sigma, int index);
	
	public BarcodeCollection<Integer> getBarcodes();
}
