package edu.stanford.math.plex4.example_tests;

import java.util.Iterator;

import org.junit.Test;

import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;

public class FiltrationTimeTest {
	@Test
	public void testExample() {
		
		ExplicitSimplexStream stream = Plex4.createExplicitSimplexStream(100);
		stream.addVertex(1, 17.23);
		stream.finalizeStream();
		Iterator<Simplex> iterator = stream.iterator();

		double filtrationValue = 0;

		while (iterator.hasNext()) {
			Simplex simplex = iterator.next();
			filtrationValue = stream.getFiltrationValue(simplex);
		}

		AbstractPersistenceAlgorithm<Simplex> persistence = Plex4.getModularSimplicialAlgorithm(3, 2);
		BarcodeCollection<Double> intervals = persistence.computeIntervals(stream);

		System.out.println("FiltrationValue = " + filtrationValue);
		System.out.println("intervals = " + intervals);
	}
}
