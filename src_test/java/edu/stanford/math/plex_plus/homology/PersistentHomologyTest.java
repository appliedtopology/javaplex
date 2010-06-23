/**
 * 
 */
package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.Cell;
import edu.stanford.math.plex_plus.homology.simplex.CellComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
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
		SimplexStream<Cell> stream = SimplexStreamExamples.getCellularSphere(2);
		testClassicalPersistentHomology(stream, CellComparator.getInstance());
	}
	
	public static <BasisElementType extends ChainBasisElement> void testClassicalPersistentHomology(SimplexStream<BasisElementType> stream, Comparator<BasisElementType> comparator) {
		ClassicalPersistentHomology<BasisElementType> homology = new ClassicalPersistentHomology<BasisElementType>(ModularIntField.getInstance(2), comparator);
		BarcodeCollection barcodes = homology.computeIntervals(stream, 6);
		System.out.println(barcodes);
	}
	
	
}
