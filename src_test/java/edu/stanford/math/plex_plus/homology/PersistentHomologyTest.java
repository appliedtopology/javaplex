/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.Cell;
import edu.stanford.math.plex_plus.homology.simplex.CellComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.SimplexComparator;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;

/**
 * @author atausz
 *
 */
public class PersistentHomologyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CellularTest();
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
		AugmentedBarcodeCollection<T> barcodes = homology.computeIntervals(stream, 6);
		System.out.println(barcodes);
	}
}
