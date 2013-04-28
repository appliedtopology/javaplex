package edu.stanford.math.plex4.api;

import java.io.IOException;
import java.util.Comparator;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.autogen.homology.BooleanClassicalHomology;
import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.io.BarcodeWriter;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.ExplicitLandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex4.metric.utility.MetricUtility;
import edu.stanford.math.plex4.streams.derived.HomStream;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.impl.WitnessStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleVectorConverter;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPairComparator;
import edu.stanford.math.primitivelib.collections.utility.ReversedComparator;

/**
 * This class contains static functions that are designed to be callable
 * from Matlab.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex4 {
	/**
	 * The default number of divisions for the filtered complexes.
	 */
	private static int DEFAULT_NUM_DIVISIONS = 20;
	
	/**
	 * Empty main function for convenience only.
	 * @param args
	 */
	public static void main(String[] args) {}
	
	/**
	 * This function creates a new EuclideanMetricSpace object.
	 * 
	 * @param points the array of points to include in the metric space
	 * @return a new EuclideanMetricSpace object containing the supplied points
	 */
	public static EuclideanMetricSpace createEuclideanMetricSpace(double[][] points) {
		return new EuclideanMetricSpace(points);
	}
	
	/**
	 * This function estimates the diameter of a metric space. The diameter is defined
	 * as the maximum pairwise distance within the space.
	 * 
	 * @param metricSpace the metric space
	 * @return the estimated diameter
	 */
	public static double estimateDiameter(AbstractIntMetricSpace metricSpace) {
		return MetricUtility.estimateDiameter(metricSpace);
	}
	
	/**
	 * This function creates a new ExplicitSimplexStream object.
	 * 
	 * @return a new ExplicitSimplexStream
	 */
	public static ExplicitSimplexStream createExplicitSimplexStream() {
		return new ExplicitSimplexStream();
	}
	
	public static ExplicitSimplexStream createExplicitSimplexStream(double maxFiltrationValue) {
		return new ExplicitSimplexStream(maxFiltrationValue);
	}
	
	/**
	 * This function creates a new ExplicitCellStream object.
	 * 
	 * @return a new ExplicitCellStream
	 */
	public static ExplicitCellStream createExplicitCellStream() {
		return new ExplicitCellStream();
	}
	
	public static ExplicitCellStream createExplicitCellStream(double maxFiltrationValue) {
		return new ExplicitCellStream(maxFiltrationValue);
	}
	
	/**
	 * This function creates a Vietoris-Rips complex given a point cloud.
	 * 
	 * @param points the points in the data set 
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param maxFiltrationValue the maximum filtration value
	 * @param numDivisions the number of divisions to use in the filtration
	 * @return a new VietorisRipsStream object
	 */
	public static VietorisRipsStream<double[]> createVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(points, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	/**
	 * This function creates a Vietoris-Rips complex given a point cloud. It uses 20
	 * as the default number of divisions.
	 * 
	 * @param points the points in the data set 
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param maxFiltrationValue the maximum filtration value
	 * @return a new VietorisRipsStream object
	 */
	public static VietorisRipsStream<double[]> createVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(points, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	/**
	 * This function creates a Vietoris-Rips complex from a metric space.
	 * 
	 * @param <T>
	 * @param metricSpace the metric space
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param maxFiltrationValue the maximum filtration value
	 * @param numDivisions the number of divisions to use in the filtration
	 * @return a new VietorisRipsStream object
	 */
	public static <T> VietorisRipsStream<T> createVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(metricSpace, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	/**
	 * This function creates a Vietoris-Rips complex given a metric space. It uses 20
	 * as the default number of divisions.
	 * 
	 * @param metricSpace the metric space
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param maxFiltrationValue the maximum filtration value
	 * @return a new VietorisRipsStream object
	 */
	public static <T> VietorisRipsStream<T> createVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, int maxDimension, double maxFiltrationValue) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(metricSpace, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	/**
	 * This function creates a Vietoris-Rips complex given a point cloud.
	 * 
	 * @param points the points in the data set 
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param filtrationValues the set of filtration values to use
	 * @return a new VietorisRipsStream object
	 */
	public static VietorisRipsStream<double[]> createVietorisRipsStream(double[][] points, int maxDimension, double[] filtrationValues) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(points, maxDimension, filtrationValues);
	}

	/**
	 * This function creates a Vietoris-Rips complex from a metric space.
	 * 
	 * @param <T>
	 * @param metricSpace the metric space
	 * @param maxDimension the maximum simplicial dimension in the complex
	 * @param filtrationValues the set of filtration values to use
	 * @return a new VietorisRipsStream object
	 */
	public static <T> VietorisRipsStream<T> createVietorisRipsStream(AbstractSearchableMetricSpace<T> metricSpace, int maxDimension, double[] filtrationValues) {
		return FilteredStreamInterface.createPlex4VietorisRipsStream(metricSpace, maxDimension, filtrationValues);
	}
	
	/**
	 * This function create a new lazy-witness stream given a point cloud.
	 * 
	 * @param selector the landmark selection
	 * @param maxDimension the maximum simplicial dimension in the complex 
	 * @param maxFiltrationValue the maximum filtration value
	 * @param numDivisions the number of divisions to use in the filtration
	 * @return a new LazyWitnessStream object
	 */
	public static LazyWitnessStream<double[]> createLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredStreamInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	/**
	 * This function create a new witness stream given a point cloud. It uses 20
	 * as the default number of divisions.
	 * 
	 * @param selector the landmark selection
	 * @param maxDimension the maximum simplicial dimension in the complex 
	 * @param maxFiltrationValue the maximum filtration value
	 * @return a new LazyWitnessStream object
	 */
	public static WitnessStream<double[]> createWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue) {
		return FilteredStreamInterface.createPlex4WitnessStream(selector, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	/**
	 * This function create a new witness stream given a point cloud.
	 * 
	 * @param selector the landmark selection
	 * @param maxDimension the maximum simplicial dimension in the complex 
	 * @param maxFiltrationValue the maximum filtration value
	 * @param numDivisions the number of divisions to use in the filtration
	 * @return a new LazyWitnessStream object
	 */
	public static WitnessStream<double[]> createWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		return FilteredStreamInterface.createPlex4WitnessStream(selector, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	/**
	 * This function create a new lazy-witness stream given a point cloud. It uses 20
	 * as the default number of divisions.
	 * 
	 * @param selector the landmark selection
	 * @param maxDimension the maximum simplicial dimension in the complex 
	 * @param maxFiltrationValue the maximum filtration value
	 * @return a new LazyWitnessStream object
	 */
	public static LazyWitnessStream<double[]> createLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue) {
		return FilteredStreamInterface.createPlex4LazyWitnessStream(selector, maxDimension, maxFiltrationValue, DEFAULT_NUM_DIVISIONS);
	}
	
	/**
	 * This function creates a new max-min landmark selector on a point cloud.
	 * 
	 * @param points the data set
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @return a new MaxMinLandmarkSelector
	 */
	public static MaxMinLandmarkSelector<double[]> createMaxMinSelector(double[][] points, int numLandmarkPoints) {
		return new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(points), numLandmarkPoints);
	}
	
	/**
	 * This function creates a new max-min landmark selector on a point cloud.
	 * 
	 * @param points the data set
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @param firstPoint the initial point in the landmark set
	 * @return a new MaxMinLandmarkSelector
	 */
	public static MaxMinLandmarkSelector<double[]> createMaxMinSelector(double[][] points, int numLandmarkPoints, int firstPoint) {
		return new MaxMinLandmarkSelector<double[]>(new EuclideanMetricSpace(points), numLandmarkPoints, firstPoint);
	}
	
	/**
	 * This function creates a new random landmark point selector on a point cloud.
	 * 
	 * @param points the data set
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @return a new RandomLandmarkSelector
	 */
	public static RandomLandmarkSelector<double[]> createRandomSelector(double[][] points, int numLandmarkPoints) {
		return new RandomLandmarkSelector<double[]>(new EuclideanMetricSpace(points), numLandmarkPoints);
	}
	
	/**
	 * This function creates a new max-min landmark selector on an abstract metric space.
	 * 
	 * @param <T> the underlying type of the metric space
	 * @param metricSpace the metric space to select from
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @return a new MaxMinLandmarkSelector
	 */
	public static <T> MaxMinLandmarkSelector<T> createMaxMinSelector(AbstractSearchableMetricSpace<T> metricSpace, int numLandmarkPoints) {
		return new MaxMinLandmarkSelector<T>(metricSpace, numLandmarkPoints);
	}
	
	/**
	 * This function creates a new max-min landmark selector on an abstract metric space.
	 * 
	 * @param <T> the underlying type of the metric space
	 * @param metricSpace the metric space to select from
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @param firstPoint the initial point in the landmark set
	 * @return a new MaxMinLandmarkSelector
	 */
	public static <T> MaxMinLandmarkSelector<T> createMaxMinSelector(AbstractSearchableMetricSpace<T> metricSpace, int numLandmarkPoints, int firstPoint) {
		return new MaxMinLandmarkSelector<T>(metricSpace, numLandmarkPoints, firstPoint);
	}
	
	/**
	 * This function creates a new randomized landmark selector on an abstract metric space.
	 * 
	 * @param <T> the underlying type of the metric space
	 * @param metricSpace the metric space to select from
	 * @param numLandmarkPoints the number of points to include in the selection
	 * @return a new RandomLandmarkSelector
	 */
	public static <T> RandomLandmarkSelector<T> createRandomSelector(AbstractSearchableMetricSpace<T> metricSpace, int numLandmarkPoints) {
		return new RandomLandmarkSelector<T>(metricSpace, numLandmarkPoints);
	}
	
	/**
	 * This function creates a new explicit landmark selector on a point cloud.
	 * 
	 * @param points the data set
	 * @param landmarkPoints the set of landmark points
	 * @return a new ExplicitLandmarkSelector
	 */
	public static ExplicitLandmarkSelector<double[]> createExplicitSelector(double[][] points, int[] landmarkPoints) {
		return new ExplicitLandmarkSelector<double[]>(new EuclideanMetricSpace(points), landmarkPoints);
	}
	
	/**
	 * This function creates a new explicit landmark selector on an abstract metric space.
	 * 
	 * @param metricSpace the metric space to select from
	 * @param landmarkPoints the set of landmark points
	 * @return a new ExplicitLandmarkSelector
	 */
	public static <T> ExplicitLandmarkSelector<T> createExplicitSelector(AbstractSearchableMetricSpace<T> metricSpace, int[] landmarkPoints) {
		return new ExplicitLandmarkSelector<T>(metricSpace, landmarkPoints);
	}
	
	/**
	 * This function returns the default (absolute) simplicial persistent homology algorithm.
	 *  
	 * @param maxDimension the maximum dimension the algorithm should compute homology for
	 * @return the default simplicial homology algorithm
	 */
	public static AbstractPersistenceAlgorithm<Simplex> getDefaultSimplicialAlgorithm(int maxDimension) {
		return new BooleanClassicalHomology<Simplex>(SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<SimplexPair> getDefaultSimplicialPairAlgorithm(int maxDimension) {
		//return new IntAbsoluteHomology<SimplexPair>(ModularIntField.getInstance(2), SimplexPairComparator.getInstance(), 0, maxDimension);
		return new BooleanClassicalHomology<SimplexPair>(SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function returns the default (absolute) cellular persistent homology algorithm.
	 *  
	 * @param maxDimension the maximum dimension the algorithm should compute homology for
	 * @return the default cellular homology algorithm
	 */
	public static AbstractPersistenceAlgorithm<Cell> getDefaultCellularAlgorithm(int maxDimension) {
		return new BooleanClassicalHomology<Cell>(CellComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function returns a cellular persistence algorithm over the finite field Z/pZ.
	 * 
	 * @param maxDimension the maximum dimension the algorithm should compute homology for 
	 * @param prime the order of the underlying finite field
	 * @return a cellular homology algorithm over Z/pZ
	 */
	public static AbstractPersistenceAlgorithm<Cell> getModularCellularAlgorithm(int maxDimension, int prime) {
		return new IntAbsoluteHomology<Cell>(ModularIntField.getInstance(prime), CellComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function returns a simplicial persistence algorithm over the finite field Z/pZ.
	 * 
	 * @param maxDimension the maximum dimension the algorithm should compute homology for 
	 * @param prime the order of the underlying finite field
	 * @return a simpliciald homology algorithm over Z/pZ
	 */
	public static AbstractPersistenceAlgorithm<Simplex> getModularSimplicialAlgorithm(int maxDimension, int prime) {
		return new IntAbsoluteHomology<Simplex>(ModularIntField.getInstance(prime), SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function returns a simplicial persistence algorithm over the the rational numbers.
	 * 
	 * @param maxDimension the maximum dimension the algorithm should compute homology for 
	 * @return a simplicial homology algorithm over the rational numbers
	 */
	public static ObjectAbsoluteHomology<Fraction, Simplex> getRationalSimplicialAlgorithm(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, Simplex>(RationalField.getInstance(), SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function returns a cellular persistence algorithm over the the rational numbers.
	 * 
	 * @param maxDimension the maximum dimension the algorithm should compute homology for 
	 * @return a cellular homology algorithm over the rational numbers
	 */
	public static AbstractPersistenceAlgorithm<Cell> getRationalCellularAlgorithm(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, Cell>(RationalField.getInstance(), CellComparator.getInstance(), 0, maxDimension);
	}
	
	/**
	 * This function creates an images of a collection of barcodes and writes it to a file.
	 * 
	 * @param collection the collection of barcodes
	 * @param caption the caption for the image
	 * @param endPoint the maximum endpoint
	 * @throws IOException 
	 */
	public static <G> void createBarcodePlot(PersistenceInvariantDescriptor<Interval<Double>, G> collection, String caption, double endPoint) throws IOException {
		BarcodeWriter writer = BarcodeWriter.getInstance();
		
		for (int dimension: collection.getDimensions()) {
			String imageFilename = caption + "_" + dimension;
			String path = imageFilename + "." + writer.getExtension();
			writer.writeToFile(collection, dimension, endPoint, caption, path);
		}
	}
	
	/**
	 * This function creates a new hom stream from two given filtered streams.
	 * 
	 * @param <T> basis type for the first complex
	 * @param <U> the basis type for the second complex
	 * @param domainStream the domain complex
	 * @param codomainStream the codomain complex
	 * @return a new hom complex stream
	 */
	public static <T, U> HomStream<T, U> createHomStream(AbstractFilteredStream<T> domainStream, AbstractFilteredStream<U> codomainStream) {
		return new HomStream<T, U>(domainStream, codomainStream);
	}
	
	/**
	 * This function creates a persistent homology algorithm over the rationals for computing homology of simplicial hom complexes.
	 * @return a persistent homology algorithm
	 */
	public static ObjectPersistenceAlgorithm<Fraction, ObjectObjectPair<Simplex, Simplex>> getRationalHomAlgorithm() {
		Comparator<ObjectObjectPair<Simplex, Simplex>> comparator = new ObjectObjectPairComparator<Simplex, Simplex>(new ReversedComparator<Simplex>(SimplexComparator.getInstance()), SimplexComparator.getInstance());
		ObjectPersistenceAlgorithm<Fraction, ObjectObjectPair<Simplex, Simplex>> algorithm = new ObjectAbsoluteHomology<Fraction, ObjectObjectPair<Simplex, Simplex>>(RationalField.getInstance(), comparator, 0, 1);
		return algorithm;
	}
	
	/**
	 * This function creates a matrix converter object for converting between formal sums, sparse and dense matrices.
	 * 
	 * @param domainStream the stream which provides the basis elements for the domain
	 * @param codomainStream the stream which provides the basis elements for the codomain
	 * @return a DoubleMatrixConverter
	 */
	public static DoubleMatrixConverter<Simplex, Simplex> createHomMatrixConverter(AbstractFilteredStream<Simplex> domainStream, AbstractFilteredStream<Simplex> codomainStream) {
		return new DoubleMatrixConverter<Simplex, Simplex>(domainStream, codomainStream);
	}
	
	/**
	 * This function creates a vector converter for converting between formal sums, and sparse and dense arrays.
	 * @param stream the stream which provides the basis elements
	 * @return a DoubleVectorConverter
	 */
	public static DoubleVectorConverter<Simplex> createMatrixConverter(AbstractFilteredStream<Simplex> stream) {
		return new DoubleVectorConverter<Simplex>(stream);
	}
}
