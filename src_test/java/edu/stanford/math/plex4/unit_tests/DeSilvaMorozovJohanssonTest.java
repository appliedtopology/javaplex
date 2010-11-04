package edu.stanford.math.plex4.unit_tests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.primitivelib.algebraic.impl.ModularIntegerField;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;

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
		//List<AbstractPersistenceAlgorithm<Simplex>> algorithms = PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(maxDimension - 1);
		
	}
	
	/*
	@Test
	public void testAbsoluteHomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericAbsoluteHomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		IntAugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getAbsoluteHomologyBarcodes());
		
		assertTrue(collection.equals(example.getAbsoluteHomologyBarcodes()));
	}
	
	//@Test
	public void testAbsoluteCohomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericAbsoluteCohomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		IntAugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getAbsoluteCohomologyBarcodes());
		
		assertTrue(collection.equals(example.getAbsoluteCohomologyBarcodes()));
	}
	
	@Test
	public void testRelativeHomology() {
		GenericPersistenceAlgorithm<Integer, Cell> homology = new GenericRelativeHomology<Integer, Cell>(field, CellComparator.getInstance(), 8);
		IntAugmentedBarcodeCollection<?> collection = homology.computeAugmentedIntervals(example.getCellComplex());
		System.out.println("Computed barcodes");
		System.out.println(collection);
		System.out.println("Actual barcodes");
		System.out.println(example.getRelativeHomologyBarcodes());
		
		assertTrue(collection.equals(example.getRelativeHomologyBarcodes()));
	}
	*/
}
