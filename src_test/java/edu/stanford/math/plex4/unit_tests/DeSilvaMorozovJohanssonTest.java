package edu.stanford.math.plex4.unit_tests;


import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.algebraic_structures.impl.ModularIntegerField;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.homology.GenericAbsoluteCohomology;
import edu.stanford.math.plex4.homology.GenericAbsoluteHomology;
import edu.stanford.math.plex4.homology.GenericPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.GenericRelativeHomology;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;

public class DeSilvaMorozovJohanssonTest {
	private final GenericField<Integer> field = ModularIntegerField.getInstance(13);
	private final DeSilvaMorozovJohanssonExample<Integer> example = new DeSilvaMorozovJohanssonExample<Integer>(field);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testAbsoluteHomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericAbsoluteHomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		AugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getAbsoluteHomologyBarcodes());
		
		assertTrue(collection.equals(example.getAbsoluteHomologyBarcodes()));
	}
	
	@Test
	public void testAbsoluteCohomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericAbsoluteCohomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		AugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getAbsoluteCohomologyBarcodes());
		
		assertTrue(collection.equals(example.getAbsoluteCohomologyBarcodes()));
	}
	
	//@Test
	public void testRelativeHomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericRelativeHomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		AugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getRelativeHomologyBarcodes());
		
		assertTrue(collection.equals(example.getRelativeHomologyBarcodes()));
	}
}
