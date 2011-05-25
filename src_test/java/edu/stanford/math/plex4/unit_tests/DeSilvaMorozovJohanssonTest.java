package edu.stanford.math.plex4.unit_tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectRelativeHomology;
import edu.stanford.math.plex4.examples.DeSilvaMorozovJohanssonExample;
import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntegerField;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public class DeSilvaMorozovJohanssonTest {
	private final ObjectAbstractField<Integer> field = ModularIntegerField.getInstance(13);
	private final DeSilvaMorozovJohanssonExample<Integer> example = new DeSilvaMorozovJohanssonExample<Integer>(field);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAbsoluteHomology() {
		AbstractPersistenceBasisAlgorithm<Cell, ObjectSparseFormalSum<Integer, Cell>> persistenceAlgorithm = new ObjectAbsoluteHomology<Integer, Cell>(field, CellComparator.getInstance(), 0, 3);
		AnnotatedBarcodeCollection<Integer, ObjectSparseFormalSum<Integer, Cell>> collection = persistenceAlgorithm.computeAnnotatedIntervals(example.getCellComplex());
		assertTrue(collection.equals(example.getAbsoluteHomologyBarcodes()));
	}
	
	@Test
	public void testRelativeHomology() {
		AbstractPersistenceBasisAlgorithm<Cell, ObjectSparseFormalSum<Integer, Cell>> persistenceAlgorithm = new ObjectRelativeHomology<Integer, Cell>(field, CellComparator.getInstance(), 0, 3);
		AnnotatedBarcodeCollection<Integer, ObjectSparseFormalSum<Integer, Cell>> collection = persistenceAlgorithm.computeAnnotatedIntervals(example.getCellComplex());
		assertTrue(collection.equals(example.getRelativeHomologyBarcodes()));
	}
}
