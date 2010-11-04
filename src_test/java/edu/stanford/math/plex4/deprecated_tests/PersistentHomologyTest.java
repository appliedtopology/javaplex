/**
 * 
 */
package edu.stanford.math.plex4.deprecated_tests;

import java.util.Comparator;

import edu.stanford.math.plex4.examples.CellStreamExamples;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.GenericAbsoluteCohomology;
import edu.stanford.math.plex4.homology.GenericAbsoluteHomology;
import edu.stanford.math.plex4.homology.GenericPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.homology.IntClassicalAbsoluteHomology;
import edu.stanford.math.plex4.homology.IntPersistentHomology;
import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.derived.DualStream;
import edu.stanford.math.plex4.homology.streams.derived.HomStream;
import edu.stanford.math.plex4.homology.streams.derived.TensorStream;
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPairComparator;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;


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
		AbstractFilteredStream<Cell> stream = CellStreamExamples.getMorozovJohanssonExample();
		//ExplicitCellStream stream1 = CellStreamExamples.getCellularTorus();
		//ExplicitCellStream stream2 = CellStreamExamples.getCellularSphere(2);
		//ExplicitCellStream stream = CellComplexOperations.disjointUnion(stream1, stream2);
		//CellComplexOperations.identifyPoints(stream, new int[]{0, 2});
		testGenericDualityPersistentCohomology(stream, CellComparator.getInstance(), RationalField.getInstance());
	}
	
	public static void SimplicalTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTriangle();
		testGenericDualityPersistentCohomology(stream, SimplexComparator.getInstance(), RationalField.getInstance());
	}
	
	public static <T extends PrimitiveBasisElement> void testClassicalPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntAbstractField field, int dimension) {
		IntClassicalAbsoluteHomology<T> homology = new IntClassicalAbsoluteHomology<T>(field, comparator, dimension);
		IntBarcodeCollection barcodes = homology.computeIntervals(stream);
		System.out.println(barcodes);
	}
	
	public static <T> void testIntDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntAbstractField field) {
		IntPersistentHomology<T> homology = new IntAbsoluteHomology<T>(field, comparator, 8);
		IntAugmentedBarcodeCollection<IntSparseFormalSum<T>> barcodes = homology.computeAugmentedIntervals(stream);
		System.out.println(barcodes);
	}
	
	public static <F, T> void testGenericDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, ObjectAbstractField<F> field) {
		GenericPersistenceAlgorithm<F, T> homology = new GenericAbsoluteHomology<F, T>(field, comparator, 8);
		//BarcodeCollection barcodes = homology.computeIntervals(stream);
		IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> barcodes = homology.computeAugmentedIntervals(stream);
		System.out.println(barcodes);
	}
	
	public static <F, T> void testGenericDualityPersistentCohomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, ObjectAbstractField<F> field) {
		GenericPersistenceAlgorithm<F, T> homology = new GenericAbsoluteCohomology<F, T>(field, comparator, 7);
		//BarcodeCollection barcodes = homology.computeIntervals(stream);
		//System.out.println(homology.getBoundaryColumns(stream, 0));
		IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> barcodes = homology.computeAugmentedIntervals(stream);
		
		System.out.println(barcodes);
	}
	
	public static void simplicialTensorTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getTorus();
		AbstractFilteredStream<Simplex> stream2 = SimplexStreamExamples.getTorus();
		TensorStream<Simplex, Simplex> tensorStream = new TensorStream<Simplex, Simplex>(stream1, stream2);
		tensorStream.finalizeStream();
		testIntDualityPersistentHomology(tensorStream, new ObjectObjectPairComparator<Simplex, Simplex>(SimplexComparator.getInstance(), SimplexComparator.getInstance()), ModularIntField.getInstance(2));
	}
	
	public static void cellularTensorTest() {
		AbstractFilteredStream<Cell> stream1 = CellStreamExamples.getCellularTorus();
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularSphere(2);
		TensorStream<Cell, Cell> tensorStream = new TensorStream<Cell, Cell>(stream1, stream2);
		tensorStream.finalizeStream();
		testGenericDualityPersistentHomology(tensorStream, new ObjectObjectPairComparator<Cell, Cell>(CellComparator.getInstance(), CellComparator.getInstance()), RationalField.getInstance());
	}
	
	public static void mixedTensorTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getTriangle();
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularSphere(1);
		TensorStream<Simplex, Cell> tensorStream = new TensorStream<Simplex, Cell>(stream1, stream2);
		tensorStream.finalizeStream();
		testIntDualityPersistentHomology(tensorStream, new ObjectObjectPairComparator<Simplex, Cell>(SimplexComparator.getInstance(), CellComparator.getInstance()), ModularIntField.getInstance(2));
	}
	
	public static void simplicialDualTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTorus();
		DualStream<Simplex> dualStream = new DualStream<Simplex>(stream);
		dualStream.finalizeStream();
		//testGenericDualityPersistentCohomology(dualStream, new ReversedComparator<Simplex>(SimplexComparator.getInstance()), RationalField.getInstance());
	}
	
	public static void cellularHomTest() {
		AbstractFilteredStream<Cell> stream1 = CellStreamExamples.getCellularSphere(2);
		AbstractFilteredStream<Cell> stream2 = CellStreamExamples.getCellularTorus();
		HomStream<Cell, Cell> homStream = new HomStream<Cell, Cell>(stream1, stream2);
		homStream.finalizeStream();
		//testIntDualityPersistentHomology(homStream, new ObjectObjectPairComparator<Cell, Cell>(new ReversedComparator<Cell>(CellComparator.getInstance()), CellComparator.getInstance()), ModularIntField.getInstance(2));
	}
	
	public static void lazyWitnessTest() {
		int n = 100;
		int d = 3;
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, d));
		
		LandmarkSelector<double[]> selector = new MaxMinLandmarkSelector<double[]>(metricSpace, 50);
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, selector, 3, 0.3, 100);
		stream.finalizeStream();
		
		testClassicalPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2), 2);
	}
	
	public static void vietorisRipsTest() {
		int n = 20;
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(n));
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, 0.5, 2);
		stream.finalizeStream();
		
		testClassicalPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2), 2);
	}
}
