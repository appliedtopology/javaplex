package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.homology.zigzag.ZigZagHomology;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class ZigZagTest {

	IntAbstractField intField = ModularIntField.getInstance(2);

	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	public List<AbstractPersistenceAlgorithm<Simplex>> getAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<Simplex>> algorithms = new ArrayList<AbstractPersistenceAlgorithm<Simplex>>();
		algorithms.add(PersistenceAlgorithmInterface.getPlex3SimplicialAbsoluteHomology(maxDimension));
		algorithms.add(new ZigZagHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension - 1));

		return algorithms;
	}

	/**
	 * This function adds simplices to a filtered triangle, and then removes them in reverse order.
	 */
	//@Test
	public void testAddAndRemove() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getZomorodianCarlssonExample();
		HomologyBasisTracker<Simplex> zz = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());

		List<Simplex> elements = new ArrayList<Simplex>();

		for (Simplex simplex: stream) {
			elements.add(simplex);
		}

		for (Simplex simplex: elements) {
			zz.add(simplex);
		}
		
		Collections.reverse(elements);

		for (Simplex simplex: elements) {
			zz.remove(simplex);
		}
		
		Collections.reverse(elements);

		for (Simplex simplex: elements) {
			zz.add(simplex);
		}
		
		Collections.reverse(elements);

		for (Simplex simplex: elements) {
			zz.remove(simplex);
		}

		IntBarcodeCollection collection = zz.getBarcodes();
		System.out.println(collection);
	}
	
	@Test
	public void testZomorodianCarlssonExample() {
		HomologyBasisTracker<Simplex> zz = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());

		zz.add(Simplex.makeSimplex(0), 0);
		zz.add(Simplex.makeSimplex(1), 0);
		zz.add(Simplex.makeSimplex(2), 1);
		zz.add(Simplex.makeSimplex(3), 1);
		zz.add(Simplex.makeSimplex(0, 1), 1);
		zz.add(Simplex.makeSimplex(1, 2), 1);
		zz.add(Simplex.makeSimplex(0, 3), 2);
		zz.add(Simplex.makeSimplex(2, 3), 2);
		zz.add(Simplex.makeSimplex(0, 2), 3);
		zz.add(Simplex.makeSimplex(0, 1, 2), 4);
		zz.add(Simplex.makeSimplex(0, 2, 3), 5);
		
		zz.remove(Simplex.makeSimplex(0, 2, 3), 6);
		zz.remove(Simplex.makeSimplex(0, 1, 2), 7);
		zz.remove(Simplex.makeSimplex(0, 2), 8);
		zz.remove(Simplex.makeSimplex(2, 3), 9);
		zz.remove(Simplex.makeSimplex(0, 3), 9);
		zz.remove(Simplex.makeSimplex(1, 2), 10);
		zz.remove(Simplex.makeSimplex(0, 1), 10);
		zz.remove(Simplex.makeSimplex(3), 10);
		zz.remove(Simplex.makeSimplex(2), 10);
		zz.remove(Simplex.makeSimplex(1), 11);
		zz.remove(Simplex.makeSimplex(0), 11);
		
		
		IntBarcodeCollection collection = zz.getBarcodes();
		System.out.println(collection);
	}

	//@Test
	public void testProduct() {
		ExplicitCellStream X = new ExplicitCellStream();

		int a = X.addNewVertex();
		int b = X.attachNewCellToPoint(1, a);
		int c = X.attachNewCellToPoint(1, a);
		int d = X.attachNewCell(2, new int[]{b});

		ExplicitCellStream Y = new ExplicitCellStream();

		int A = Y.addNewVertex();
		int B = Y.attachNewCellToPoint(1, A);
		int C = Y.attachNewCell(2, new int[]{B});
		
		HomologyBasisTracker<Cell> xTracker = new HomologyBasisTracker<Cell>(intField, CellComparator.getInstance());
		HomologyBasisTracker<Cell> yTracker = new HomologyBasisTracker<Cell>(intField, CellComparator.getInstance());
		
		for (Cell cell: X) {
			xTracker.add(cell);
		}
		
		for (Cell cell: Y) {
			yTracker.add(cell);
		}
		
		//HomologyBasisTracker<ObjectObjectPair<Cell, Cell>> xyTracker = HomologyBasisTracker.tensorProduct(xTracker, yTracker);
		//xyTracker.checkInvariant();
	}
}
