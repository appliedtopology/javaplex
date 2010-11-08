package edu.stanford.math.plex4.api;

import java.io.IOException;
import java.util.Comparator;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.autogen.homology.BooleanClassicalHomology;
import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.io.BarcodeWriter;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex4.metric.utility.MetricUtility;
import edu.stanford.math.plex4.streams.derived.HomStream;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleMatrixConverter;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPairComparator;
import edu.stanford.math.primitivelib.collections.utility.ReversedComparator;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;
import gnu.trove.TIntObjectIterator;

/**
 * This class contains static functions that are designed to be callable
 * from Matlab.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex4 {
	private static int DEFAULT_NUM_DIVISIONS = 20;
	
	public static void main(String[] args) {}
	
	public static EuclideanMetricSpace createEuclideanMetricSpace(double[][] points) {
		return new EuclideanMetricSpace(points);
	}
	
	public static double estimateDiameter(AbstractIntMetricSpace metricSpace) {
		return MetricUtility.estimateDiameter(metricSpace);
	}
	
	public static ExplicitSimplexStream createExplicitSimplexStream() {
		return new ExplicitSimplexStream();
	}
	
	public static ExplicitCellStream createExplicitCellStream() {
		return new ExplicitCellStream();
	}
	
	public static VietorisRipsStream<double[]> createVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredComplexInterface.createPlex4VietorisRipsStream(points, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	public static VietorisRipsStream<double[]> createVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue) {
		return FilteredComplexInterface.createPlex4VietorisRipsStream(points, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	public static LazyWitnessStream<double[]> createLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredComplexInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	public static LazyWitnessStream<double[]> createLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue) {
		return FilteredComplexInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	public static LandmarkSelector<double[]> createMaxMinSelector(double[][] points, int numLandmarkPoints) {
		return new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(points), numLandmarkPoints);
	}
	
	public static LandmarkSelector<double[]> createRandomSelector(double[][] points, int numLandmarkPoints) {
		return new RandomLandmarkSelector<double[]>(new EuclideanMetricSpace(points), numLandmarkPoints);
	}
	
	public static <T> LandmarkSelector<T> createMaxMinSelector(AbstractSearchableMetricSpace<T> metricSpace, int numLandmarkPoints) {
		return new MaxMinLandmarkSelector<T>(metricSpace, numLandmarkPoints);
	}
	
	public static <T> LandmarkSelector<T> createRandomSelector(AbstractSearchableMetricSpace<T> metricSpace, int numLandmarkPoints) {
		return new RandomLandmarkSelector<T>(metricSpace, numLandmarkPoints);
	}
	
	public static AbstractPersistenceAlgorithm<Simplex> getDefaultSimplicialAlgorithm(int maxDimension) {
		return new BooleanClassicalHomology<Simplex>(SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Cell> getModularCellularAlgorithm(int maxDimension, int prime) {
		return new IntAbsoluteHomology<Cell>(ModularIntField.getInstance(prime), CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Cell> getRationalCellularAlgorithm(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, Cell>(RationalField.getInstance(), CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static void createBarcodePlot(DoubleBarcodeCollection collection, String caption, double endPoint) throws IOException {
		for (TIntObjectIterator<DoubleBarcode> iterator = collection.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			int dimension = iterator.key();
			DoubleBarcode barcode = iterator.value();
			String imageFilename = caption + "_" + dimension;
			BarcodeWriter writer = BarcodeWriter.getInstance();
			writer.writeToFile(barcode, imageFilename + "." + writer.getExtension(), endPoint);
		}
	}
	
	public static <T, U> HomStream<T, U> createHomStream(AbstractFilteredStream<T> domainStream, AbstractFilteredStream<U> codomainStream) {
		return new HomStream<T, U>(domainStream, codomainStream);
	}
	
	public static AbstractPersistenceAlgorithm<ObjectObjectPair<Simplex, Simplex>> getRationalHomAlgorithm() {
		Comparator<ObjectObjectPair<Simplex, Simplex>> comparator = new ObjectObjectPairComparator<Simplex, Simplex>(new ReversedComparator<Simplex>(SimplexComparator.getInstance()), SimplexComparator.getInstance());
		AbstractPersistenceAlgorithm<ObjectObjectPair<Simplex, Simplex>> algorithm = new ObjectAbsoluteHomology<Fraction, ObjectObjectPair<Simplex, Simplex>>(RationalField.getInstance(), comparator, 0, 1);
		return algorithm;
	}
	
	public static DoubleMatrixConverter<Simplex, Simplex> createHomMatrixConverter(AbstractFilteredStream<Simplex> domainStream, AbstractFilteredStream<Simplex> codomainStream) {
		return new DoubleMatrixConverter<Simplex, Simplex>(domainStream, codomainStream);
	}
}
