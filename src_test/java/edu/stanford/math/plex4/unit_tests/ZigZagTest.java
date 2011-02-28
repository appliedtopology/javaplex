package edu.stanford.math.plex4.unit_tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.api.PersistenceAlgorithmInterface;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.nonautogen.ZigZagHomology;
import edu.stanford.math.plex4.homology.nonautogen.ZigZagPrototype;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;

public class ZigZagTest {

	IntAbstractField intField = ModularIntField.getInstance(11);

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
	@Test
	public void testTriangle() {
		AbstractFilteredStream<Simplex> stream = SimplexStreamExamples.getFilteredTriangle();
		ZigZagPrototype<Simplex> zz = new ZigZagPrototype<Simplex>(intField, SimplexComparator.getInstance(), stream);

		List<Simplex> elements = new ArrayList<Simplex>();

		for (Simplex simplex: stream) {
			elements.add(simplex);

			zz.add(simplex);
		}

		Collections.reverse(elements);

		for (Simplex simplex: elements) {
			zz.remove(simplex);
		}

		List<ObjectObjectPair<Integer, Simplex>> pairs = zz.getPairs();
		THashMap<Integer, Simplex> birth = zz.getBirth();

		for (ObjectObjectPair<Integer, Simplex> pair: pairs) {
			System.out.print(pair);
			birth.remove(pair.getFirst());
		}
	}
}
