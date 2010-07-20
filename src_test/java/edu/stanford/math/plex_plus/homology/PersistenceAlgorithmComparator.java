package edu.stanford.math.plex_plus.homology;

import cern.colt.Arrays;
import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.RipsStream;
import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntegerField;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;

public class PersistenceAlgorithmComparator {
	public enum PersistenceAlgorithmType {
		GenericClassicalHomology,
		GenericDualityHomology,
		GenericDualityCohomology,
		Plex3Homology
	}
	
	//public static BarcodeCollection computeClassicalHomology(AbstractFilteredStream<Simplex> stream, int d) {
		
	//}
	
	
	
	public BarcodeCollection testPlex3Vietoris(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);
		
		RipsStream stream = Plex.RipsStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, pData);
		
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(stream);
		return this.convertFromPlex3PersistenceIntervals(intervals);
	}
	
	public static void testPlex3LazyWitness(double[][] points, LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);
		
		edu.stanford.math.plex.LazyWitnessStream wit = Plex.LazyWitnessStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, 2, selector.getLandmarkPoints(), pData);

		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(wit);
		System.out.println(Arrays.toString(intervals));
	}
	
	public BarcodeCollection computeClassicalHomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Classical");
		ClassicalPersistentHomology<Simplex> classicalHomology = new ClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		BarcodeCollection barcodes = classicalHomology.computeIntervals(stream, d + 1);
		return barcodes;
	}


	public BarcodeCollection computeDualityHomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Duality Homology");
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteHomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		return barcodes;
	}

	public BarcodeCollection computeDualityCohomology(AbstractFilteredStream<Simplex> stream, int d) {
		System.out.println("Duality Cohomology");
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteCohomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		return barcodes;
	}
	
	private BarcodeCollection convertFromPlex3PersistenceIntervals(PersistenceInterval[] intervals) {
		BarcodeCollection barcodes = new BarcodeCollection();
		
		for (PersistenceInterval interval: intervals) {
			if (interval.infiniteExtent()) {
				barcodes.addRightInfiniteInterval(interval.dimension, interval.toDouble()[0]);
			} else {
				barcodes.addInterval(interval.dimension, interval.toDouble()[0], interval.toDouble()[1]);
			}
		}
		
		return barcodes;
	}
	
	private edu.stanford.math.plex.ExplicitStream convertToPlex3ExplicitStream(edu.stanford.math.plex_plus.homology.streams.impl.ExplicitStream<Simplex> plex4Stream) {
		edu.stanford.math.plex.ExplicitStream plex3Stream = new edu.stanford.math.plex.ExplicitStream();
		
		for (Simplex simplex: plex4Stream) {
			plex3Stream.add(simplex.getVertices(), plex4Stream.getFiltrationValue(simplex));
		}
		
		return plex3Stream;
	}
}
