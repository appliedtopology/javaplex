package edu.stanford.math.plex4.homology;

import org.apache.commons.lang.math.Fraction;

import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.RipsStream;
import edu.stanford.math.plex4.homology.PersistenceCalculationData.PersistenceAlgorithmType;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.FiltrationUtility;
import edu.stanford.math.plex4.homology.filtration.IdentityConverter;
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.storage_structures.HashedStorageStructure;
import edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.test_utility.Timing;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;

public class PersistenceAlgorithmInterface {
	
	public static PersistenceAlgorithmResult testVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3Vietoris(points, maxDimension, maxFiltrationValue, numDivisions);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points);
			StreamStorageStructure<Simplex> structure = new HashedStorageStructure<Simplex>(SimplexComparator.getInstance());
			VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension + 1, numDivisions, structure);
			stream.finalizeStream();
			DoubleBarcodeCollection barcodeCollection = computePlex4Barcodes(stream, maxDimension, stream.getConverter(), type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(maxFiltrationValue);
			result.setNumPoints(points.length);
			result.setNumSimplices(stream.getSize());
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}
	
	public static PersistenceAlgorithmResult testLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3LazyWitness(selector, maxDimension, maxFiltrationValue, numDivisions);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(selector.getUnderlyingMetricSpace(), selector, maxDimension + 1, maxFiltrationValue, numDivisions);
			stream.finalizeStream();
			DoubleBarcodeCollection barcodeCollection = computePlex4Barcodes(stream, maxDimension, stream.getConverter(), type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(maxFiltrationValue);
			result.setNumSimplices(stream.getSize());
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}

	public static PersistenceAlgorithmResult testExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream, int maxDimension, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3ExplicitStream(plex4Stream);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			DoubleBarcodeCollection barcodeCollection = computePlex4Barcodes(plex4Stream, maxDimension, IdentityConverter.getInstance(), type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(0);
			result.setNumSimplices(plex4Stream.getSize());
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}

	private static PersistenceAlgorithmResult testPlex3ExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		edu.stanford.math.plex.ExplicitStream plex3Stream = convertToPlex3ExplicitStream(plex4Stream, IdentityConverter.getInstance());
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(plex3Stream);
		DoubleBarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(plex3Stream.maxDimension());
		result.setMaxFiltrationValue(0);
		result.setNumSimplices(plex3Stream.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}
	
	private static DoubleBarcodeCollection testPlex3ExplicitStream(edu.stanford.math.plex.ExplicitStream plex3Stream) {
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(plex3Stream);
		return convertFromPlex3PersistenceIntervals(intervals);
	}
	
	private static PersistenceAlgorithmResult testPlex3Vietoris(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);		
		RipsStream stream = Plex.RipsStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, pData);
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(stream);
		DoubleBarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(maxDimension);
		result.setMaxFiltrationValue(maxFiltrationValue);
		result.setNumPoints(pData.count());
		result.setNumSimplices(stream.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}

	private static PersistenceAlgorithmResult testPlex3LazyWitness(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(selector.getUnderlyingMetricSpace().getPoints());
		edu.stanford.math.plex.LazyWitnessStream wit = Plex.LazyWitnessStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, 2, convertTo1Based(selector.getLandmarkPoints()), pData);
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(wit);
		DoubleBarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(maxDimension);
		result.setMaxFiltrationValue(maxFiltrationValue);
		result.setNumPoints(selector.size());
		result.setNumSimplices(wit.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}

	private static DoubleBarcodeCollection computePlex4Barcodes(AbstractFilteredStream<Simplex> stream, int d, FiltrationConverter converter, PersistenceAlgorithmType type) {
		switch (type) {
		case IntClassicalHomology:
			return computeIntClassicalHomology(stream, d, converter);
		case GenericAbsoluteHomology:
			return computeGenericAbsoluteHomology(stream, d, converter);
		case GenericAbsoluteCohomology:
			return computeGenericAbsoluteCohomology(stream, d, converter);
		case IntAbsoluteHomology:
			return computeIntAbsoluteHomology(stream, d, converter);
		default:
			return null;
		}
	}

	private static DoubleBarcodeCollection computeIntClassicalHomology(AbstractFilteredStream<Simplex> stream, int d, FiltrationConverter converter) {
		IntClassicalPersistentHomology<Simplex> classicalHomology = new IntClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		IntBarcodeCollection barcodes = classicalHomology.computeIntervals(stream, d + 1);
		return FiltrationUtility.transformBarcodeCollection(barcodes, converter);
	}
	
	private static DoubleBarcodeCollection computeIntAbsoluteHomology(AbstractFilteredStream<Simplex> stream, int d, FiltrationConverter converter) {
		IntPersistenceAlgorithm<Simplex> homology = new IntAbsoluteHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance(), d);
		IntBarcodeCollection barcodes = homology.computeIntervals(stream);
		return FiltrationUtility.transformBarcodeCollection(barcodes, converter);
	}
	
	private static DoubleBarcodeCollection computeGenericAbsoluteHomology(AbstractFilteredStream<Simplex> stream, int d, FiltrationConverter converter) {
		GenericPersistenceAlgorithm<Fraction, Simplex> homology = new GenericAbsoluteHomology<Fraction, Simplex>(RationalField.getInstance(), SimplexComparator.getInstance(), d);
		IntBarcodeCollection barcodes = homology.computeIntervals(stream);
		return FiltrationUtility.transformBarcodeCollection(barcodes, converter);
	}
	
	private static DoubleBarcodeCollection computeGenericAbsoluteCohomology(AbstractFilteredStream<Simplex> stream, int d, FiltrationConverter converter) {
		GenericPersistenceAlgorithm<Fraction, Simplex> homology = new GenericAbsoluteCohomology<Fraction, Simplex>(RationalField.getInstance(), SimplexComparator.getInstance(), d);
		IntBarcodeCollection barcodes = homology.computeIntervals(stream);
		return FiltrationUtility.transformBarcodeCollection(barcodes, converter);
	}

	private static DoubleBarcodeCollection convertFromPlex3PersistenceIntervals(PersistenceInterval[] intervals) {
		DoubleBarcodeCollection barcodes = new DoubleBarcodeCollection();

		for (PersistenceInterval interval: intervals) {
			if (interval.infiniteExtent()) {
				barcodes.addRightInfiniteInterval(interval.dimension, interval.toDouble()[0]);
			} else {
				barcodes.addInterval(interval.dimension, interval.toDouble()[0], interval.toDouble()[1]);
			}
		}

		return barcodes;
	}

	private static edu.stanford.math.plex.ExplicitStream convertToPlex3ExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream, FiltrationConverter converter) {
		edu.stanford.math.plex.ExplicitStream plex3Stream = new edu.stanford.math.plex.ExplicitStream();

		for (Simplex simplex: plex4Stream) {
			plex3Stream.add(IntArrayMath.scalarAdd(simplex.getVertices(), 1), converter.getFiltrationValue(plex4Stream.getFiltrationIndex(simplex)));
		}
		
		plex3Stream.close();

		return plex3Stream;
	}
	
	private static int[] convertTo1Based(int[] array) {
		int[] result = new int[array.length + 1];
		result[0] = 0;
		for (int i = 0; i < array.length; i++) {
			result[i + 1] = array[i] + 1;
		}
		return result;
	}
	
}
