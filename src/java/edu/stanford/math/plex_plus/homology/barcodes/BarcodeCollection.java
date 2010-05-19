package edu.stanford.math.plex_plus.homology.barcodes;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

public class BarcodeCollection {
	private final TIntObjectHashMap<Barcode> barcodeMap = new TIntObjectHashMap<Barcode>();
	
	public void addInterval(int dimension, PersistenceInterval interval) {
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new Barcode(dimension));
		}
		this.barcodeMap.get(dimension).addInterval(interval);
	}
	
	public void addInterval(int dimension, double start, double end) {
		this.addInterval(dimension, new PersistenceInterval(start, end));
	}
	
	public void addInterval(int dimension, double start) {
		this.addInterval(dimension, new PersistenceInterval(start));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<Barcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.value().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}
