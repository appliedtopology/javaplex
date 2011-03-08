package edu.stanford.math.plex4.unit_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.streams.derived.TensorStream;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class WitnessTest {
	IntAbstractField intField = ModularIntField.getInstance(11);
	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	@Test
	public void test() {
		ExplicitSimplexStream stream = new ExplicitSimplexStream();
		stream.addElement(new int[]{0});
		stream.addElement(new int[]{1});
		stream.addElement(new int[]{2});
		stream.addElement(new int[]{0, 1});
		stream.addElement(new int[]{0, 2});
		stream.addElement(new int[]{1, 2});
		
		TensorStream<Simplex, Simplex> tensorStream = new TensorStream<Simplex, Simplex>(stream, stream);
		
		IntAbsoluteHomology<Simplex> algorithm = new IntAbsoluteHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, 3);
		System.out.println(algorithm.computeAnnotatedIntervals(stream));
		
		IntAbsoluteHomology<ObjectObjectPair<Simplex, Simplex>> tensorAlgorithm = new IntAbsoluteHomology<ObjectObjectPair<Simplex, Simplex>>(intField, tensorStream.getBasisComparator(), 0, 3);
		System.out.println(tensorAlgorithm.computeAnnotatedIntervals(tensorStream));
		
		ExplicitStream<SimplexPair> subsetStream = new ExplicitStream<SimplexPair>(SimplexPairComparator.getInstance());
		
		subsetStream.addElement(new SimplexPair(new int[]{0}, new int[]{0}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{0}, new int[]{1}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{0}, new int[]{2}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{1}, new int[]{0}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{1}, new int[]{1}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{1}, new int[]{2}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{2}, new int[]{0}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{2}, new int[]{1}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{2}, new int[]{2}), 0);
		
		subsetStream.addElement(new SimplexPair(new int[]{0}, new int[]{1, 2}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{0, 2}, new int[]{1}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{2}, new int[]{0, 1}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{1, 2}, new int[]{0}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{1}, new int[]{0, 2}), 0);
		subsetStream.addElement(new SimplexPair(new int[]{0, 1}, new int[]{2}), 0);

		
		IntAbsoluteHomology<SimplexPair> algorithm2 = new IntAbsoluteHomology<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, 3);
		
		System.out.println(algorithm2.computeAnnotatedIntervals(subsetStream));
	}
}
