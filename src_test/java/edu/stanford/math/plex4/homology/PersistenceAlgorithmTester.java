package edu.stanford.math.plex4.homology;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.test_utility.Timing;

public class PersistenceAlgorithmTester<T> {
	private final List<AbstractPersistenceAlgorithm<T>> algorithms = new ArrayList<AbstractPersistenceAlgorithm<T>>();
	
	public void addAlgorithm(AbstractPersistenceAlgorithm<T> algorithm) {
		this.algorithms.add(algorithm);
	}
	
	public void verifyEquality(AbstractFilteredStream<T> stream) {
		System.out.println("Stream size: " + stream.getSize());
		List<IntBarcodeCollection> barcodes = new ArrayList<IntBarcodeCollection>();
		for (AbstractPersistenceAlgorithm<T> algorithm: algorithms) {
			Timing.restart();
			barcodes.add(algorithm.computeIntervals(stream));
			Timing.stopAndDisplay(algorithm.toString());
		}
		
		int n = barcodes.size();
		for (int i = 0; i < n; i++) {
			
			for (int j = i + 1; j < n; j++) {
				if (! barcodes.get(i).equals(barcodes.get(j))) {
					System.out.println(algorithms.get(i) + " != " + algorithms.get(j));
					System.out.println(algorithms.get(i));
					System.out.println(barcodes.get(i));
					System.out.println(algorithms.get(j));
					System.out.println(barcodes.get(j));
				}
				assertTrue("Computed barcodes are not equal for algorithms " + algorithms.get(i) + " and " + algorithms.get(j), barcodes.get(i).equals(barcodes.get(j)));
			}
		}	
	}
	
	public void verifyEqualityAll(Iterable<AbstractFilteredStream<T>> streams) {
		for (AbstractFilteredStream<T> stream: streams) {
			
			this.verifyEquality(stream);
		}
	}
}
