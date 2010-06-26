/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.embedding.GraphEmbedding;
import edu.stanford.math.plex_plus.embedding.GraphMetricEmbedding;
import edu.stanford.math.plex_plus.embedding.MultidimensionalScaling;
import edu.stanford.math.plex_plus.graph_metric.ShortestPathMetric;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.Cell;
import edu.stanford.math.plex_plus.homology.simplex.CellComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.SimplexComparator;
import edu.stanford.math.plex_plus.homology.simplex_streams.DualStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.GeometricSimplexStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.TensorStream;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		simplicialDualTest();
	}
	
	public static void CellularTest() {
		SimplexStream<Cell> stream = SimplexStreamExamples.getMorozovJohanssonExample();
		testDualityPersistentHomology(stream, CellComparator.getInstance(), ModularIntField.getInstance(7));
	}
	
	public static void SimplicalTest() {
		SimplexStream<Simplex> stream = SimplexStreamExamples.getCircle(4);
		testDualityPersistentHomology(stream, SimplexComparator.getInstance(), ModularIntField.getInstance(2));
	}
	
	public static <T extends ChainBasisElement> void testClassicalPersistentHomology(SimplexStream<T> stream, Comparator<T> comparator, IntField field) {
		ClassicalPersistentHomology<T> homology = new ClassicalPersistentHomology<T>(field, comparator);
		BarcodeCollection barcodes = homology.computeIntervals(stream, 6);
		System.out.println(barcodes);
	}
	
	public static <T extends ChainBasisElement> void testDualityPersistentHomology(SimplexStream<T> stream, Comparator<T> comparator, IntField field) {
		PersistentHomology<T> homology = new PersistentHomology<T>(field, comparator);
		AugmentedBarcodeCollection<T> barcodes = homology.computeIntervals(stream, 20);
		System.out.println(barcodes);
	}
	
	public static void testTriangle() {
		int dimension = 3;
		SimplexStream<Simplex> stream = SimplexStreamExamples.getTriangle();
		GraphEmbedding embedding = new GraphMetricEmbedding(ShortestPathMetric.getInstance(), MultidimensionalScaling.getInstance());
		GeometricSimplexStream geometricStream = new GeometricSimplexStream(stream, embedding, dimension);
	}
	
	public static void simplicialTensorTest() {
		SimplexStream<Simplex> stream1 = SimplexStreamExamples.getTorus();
		SimplexStream<Simplex> stream2 = SimplexStreamExamples.getTorus();
		TensorStream<Simplex> tensorStream = new TensorStream<Simplex>(stream1, stream2, SimplexComparator.getInstance());
		tensorStream.finalizeStream();
		testDualityPersistentHomology(tensorStream, tensorStream.getBasisComparator(), ModularIntField.getInstance(2));
	}
	
	public static void cellularTensorTest() {
		SimplexStream<Cell> stream1 = SimplexStreamExamples.getCellularSphere(2);
		SimplexStream<Cell> stream2 = SimplexStreamExamples.getCellularSphere(5);
		TensorStream<Cell> tensorStream = new TensorStream<Cell>(stream1, stream2, CellComparator.getInstance());
		tensorStream.finalizeStream();
		testDualityPersistentHomology(tensorStream, tensorStream.getBasisComparator(), ModularIntField.getInstance(2));
	}
	
	public static void simplicialDualTest() {
		SimplexStream<Simplex> stream = SimplexStreamExamples.getTorus();
		DualStream<Simplex> dualStream = new DualStream<Simplex>(stream, SimplexComparator.getInstance());
		dualStream.finalizeStream();
		testDualityPersistentHomology(dualStream, dualStream.getBasisComparator(), ModularIntField.getInstance(2));
	}
}
