/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.embedding.GraphEmbedding;
import edu.stanford.math.plex_plus.embedding.GraphMetricEmbedding;
import edu.stanford.math.plex_plus.embedding.MultidimensionalScaling;
import edu.stanford.math.plex_plus.graph_metric.ShortestPathMetric;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.Cell;
import edu.stanford.math.plex_plus.homology.chain_basis.CellComparator;
import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.mapping.GenericMappingComputation;
import edu.stanford.math.plex_plus.homology.streams.derived.DualStream;
import edu.stanford.math.plex_plus.homology.streams.derived.HomStream;
import edu.stanford.math.plex_plus.homology.streams.derived.TensorStream;
import edu.stanford.math.plex_plus.homology.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		simplicialTensorTest();
	}
	
	public static void CellularTest() {
		AbstractFilteredStream<Cell> stream = SimplexStreamExamples.getCellularSphere(1);
		testGenericDualityPersistentHomology(stream, CellComparator.getInstance(), RationalField.getInstance());
	}
	
	public static void SimplicalTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTorus();
		testGenericDualityPersistentHomology(stream, SimplexComparator.getInstance(), RationalField.getInstance());
	}
	
	public static <T extends PrimitiveBasisElement> void testClassicalPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntField field) {
		ClassicalPersistentHomology<T> homology = new ClassicalPersistentHomology<T>(field, comparator);
		BarcodeCollection barcodes = homology.computeIntervals(stream, 6);
		System.out.println(barcodes);
	}
	
	public static <T> void testIntDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, IntField field) {
		IntPersistentHomology<T> homology = new IntPersistentHomology<T>(field, comparator);
		AugmentedBarcodeCollection<IntFormalSum<T>> barcodes = homology.computeIntervals(stream, 20);
		System.out.println(barcodes);
	}
	
	public static <F, T> void testGenericDualityPersistentHomology(AbstractFilteredStream<T> stream, Comparator<T> comparator, GenericField<F> field) {
		GenericPersistentHomology<F, T> homology = new GenericPersistentHomology<F, T>(field, comparator);
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodes = homology.computeIntervals(stream, 20);
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
		AbstractFilteredStream<Cell> stream1 = SimplexStreamExamples.getCellularTorus();
		AbstractFilteredStream<Cell> stream2 = SimplexStreamExamples.getCellularSphere(2);
		TensorStream<Cell, Cell> tensorStream = new TensorStream<Cell, Cell>(stream1, stream2, CellComparator.getInstance(), CellComparator.getInstance());
		tensorStream.finalizeStream();
		testGenericDualityPersistentHomology(tensorStream, tensorStream.getDerivedComparator(), RationalField.getInstance());
	}
	
	public static void mixedTensorTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getTriangle();
		AbstractFilteredStream<Cell> stream2 = SimplexStreamExamples.getCellularSphere(1);
		TensorStream<Simplex, Cell> tensorStream = new TensorStream<Simplex, Cell>(stream1, stream2, SimplexComparator.getInstance(), CellComparator.getInstance());
		tensorStream.finalizeStream();
		testIntDualityPersistentHomology(tensorStream, tensorStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
	
	public static void simplicialDualTest() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getTorus();
		DualStream<Simplex> dualStream = new DualStream<Simplex>(stream, SimplexComparator.getInstance());
		dualStream.finalizeStream();
		testIntDualityPersistentHomology(dualStream, dualStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
	
	public static void simplicialHomTest() {
		AbstractFilteredStream<Simplex> stream1 = SimplexStreamExamples.getCircle(3);
		AbstractFilteredStream<Simplex> stream2 = SimplexStreamExamples.getCircle(3);
		GenericMappingComputation<Fraction, Simplex, Simplex> mappingComputation = new GenericMappingComputation<Fraction, Simplex, Simplex>(RationalField.getInstance());
		mappingComputation.computeMapping(stream1, stream2, SimplexComparator.getInstance(), SimplexComparator.getInstance());
		
	}
	
	public static void cellularHomTest() {
		AbstractFilteredStream<Cell> stream1 = SimplexStreamExamples.getCellularSphere(2);
		AbstractFilteredStream<Cell> stream2 = SimplexStreamExamples.getCellularTorus();
		HomStream<Cell, Cell> homStream = new HomStream<Cell, Cell>(stream1, stream2, CellComparator.getInstance(), CellComparator.getInstance());
		homStream.finalizeStream();
		testIntDualityPersistentHomology(homStream, homStream.getDerivedComparator(), ModularIntField.getInstance(2));
	}
}
