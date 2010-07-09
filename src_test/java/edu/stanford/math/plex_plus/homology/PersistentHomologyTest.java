/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.embedding.GraphEmbedding;
import edu.stanford.math.plex_plus.embedding.GraphMetricEmbedding;
import edu.stanford.math.plex_plus.embedding.MultidimensionalScaling;
import edu.stanford.math.plex_plus.examples.CellComplexOperations;
import edu.stanford.math.plex_plus.examples.CellStreamExamples;
import edu.stanford.math.plex_plus.examples.EuclideanMetricSpaceExamples;
import edu.stanford.math.plex_plus.examples.SimplexStreamExamples;
import edu.stanford.math.plex_plus.graph_metric.ShortestPathMetric;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.Cell;
import edu.stanford.math.plex_plus.homology.chain_basis.CellComparator;
import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.derived.DualStream;
import edu.stanford.math.plex_plus.homology.streams.derived.HomStream;
import edu.stanford.math.plex_plus.homology.streams.derived.TensorStream;
import edu.stanford.math.plex_plus.homology.streams.impl.ExplicitCellStream;
import edu.stanford.math.plex_plus.homology.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex_plus.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex_plus.homology.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex_plus.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_plus.math.metric.landmark.MaxMinLandmarkSelector;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//simplicialHomTest(RationalField.getInstance());
		CellularTest();
	}
	
	public static void CellularTest() {
		//AbstractFilteredStream<Cell> stream = CellStreamExamples.getMorozovJohanssonExample();
		ExplicitCellStream stream1 = CellStreamExamples.getCellularSphere(2);
		ExplicitCellStream stream2 = CellStreamExamples.getCellularSphere(4);
		ExplicitCellStream stream = CellComplexOperations.disjointUnion(stream1, stream2);
		testGenericDualityPersistentCohomology(stream, CellComparator.getInstance(), RationalField.getInstance());
	}
	
	public static void SimplicalTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTorus();
		testGenericDualityPersistentCohomology(stream, SimplexComparator.getInstance(), RationalField.getInstance());
	}
	
	public static <T extends PrimitiveBasisElement> void testClassicalPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntField field, int dimension) {
		ClassicalPersistentHomology<T> homology = new ClassicalPersistentHomology<T>(field, comparator);
		BarcodeCollection barcodes = homology.computeIntervals(stream, dimension);
		System.out.println(barcodes);
	}
	
	public static <T> void testIntDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntField field) {
		IntPersistentHomology<T> homology = new IntPersistentHomology<T>(field, comparator);
		AugmentedBarcodeCollection<IntFormalSum<T>> barcodes = homology.computeIntervals(stream, 2);
		System.out.println(barcodes);
	}
	
	public static <F, T> void testGenericDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, GenericField<F> field) {
		GenericPersistentHomology<F, T> homology = new GenericPersistentHomology<F, T>(field, comparator, 20);
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes);
	}
	
	public static <F, T> void testGenericDualityPersistentCohomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, GenericField<F> field) {
		GenericPersistentHomology<F, T> homology = new GenericPersistentHomology<F, T>(field, comparator, 20);
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodes = homology.pCoh(stream, 20);
		System.out.println(barcodes);
	}
	
	public static void testTriangle() {
		int dimension = 3;
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTriangle();
		GraphEmbedding embedding = new GraphMetricEmbedding(ShortestPathMetric.getInstance(), MultidimensionalScaling.getInstance());
		GeometricSimplexStream geometricStream = new GeometricSimplexStream(stream, embedding, dimension);
	}
	
	public static void simplicialTensorTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getTorus();
		AbstractFilteredStream<Simplex> stream2 = SimplexStreamExamples.getTorus();
		TensorStream<Simplex, Simplex> tensorStream = new TensorStream<Simplex, Simplex>(stream1, stream2, SimplexComparator.getInstance(), SimplexComparator.getInstance());
		tensorStream.finalizeStream();
		testIntDualityPersistentHomology(tensorStream, tensorStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
	
	public static void cellularTensorTest() {
		AbstractFilteredStream<Cell> stream1 = CellStreamExamples.getCellularTorus();
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularSphere(2);
		TensorStream<Cell, Cell> tensorStream = new TensorStream<Cell, Cell>(stream1, stream2, CellComparator.getInstance(), CellComparator.getInstance());
		tensorStream.finalizeStream();
		testGenericDualityPersistentHomology(tensorStream, tensorStream.getDerivedComparator(), RationalField.getInstance());
	}
	
	public static void mixedTensorTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getTriangle();
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularSphere(1);
		TensorStream<Simplex, Cell> tensorStream = new TensorStream<Simplex, Cell>(stream1, stream2, SimplexComparator.getInstance(), CellComparator.getInstance());
		tensorStream.finalizeStream();
		testIntDualityPersistentHomology(tensorStream, tensorStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
	
	public static void simplicialDualTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTorus();
		DualStream<Simplex> dualStream = new DualStream<Simplex>(stream, SimplexComparator.getInstance());
		dualStream.finalizeStream();
		testGenericDualityPersistentCohomology(dualStream, dualStream.getDerivedComparator(), RationalField.getInstance());
	}
	
	public static void cellularHomTest() {
		AbstractFilteredStream<Cell> stream1 = CellStreamExamples.getCellularSphere(2);
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularTorus();
		HomStream<Cell, Cell> homStream = new HomStream<Cell, Cell>(stream1, stream2, CellComparator.getInstance(), CellComparator.getInstance());
		homStream.finalizeStream();
		testIntDualityPersistentHomology(homStream, homStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
	
	public static void lazyWitnessTest() {
		int n = 100;
		int d = 3;
		SearchableFiniteMetricSpace<double[]> metricSpace = EuclideanMetricSpaceExamples.getRandomSpherePoints(n, d);
		
		LandmarkSelector<double[]> selector = new MaxMinLandmarkSelector<double[]>(metricSpace, 50);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, 3, 0.3);
		stream.finalizeStream();
		
		testClassicalPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2), 2);
	}
	
	public static void vietorisRipsTest() {
		int n = 20;
		SearchableFiniteMetricSpace<double[]> metricSpace = EuclideanMetricSpaceExamples.getEquispacedCirclePoints(n);
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 0.5, 2);
		stream.finalizeStream();
		
		testClassicalPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2), 2);
	}
}
