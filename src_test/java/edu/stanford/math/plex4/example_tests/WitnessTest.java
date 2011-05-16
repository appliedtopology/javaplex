package edu.stanford.math.plex4.example_tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.homology.zigzag.ProductSubsetZigzag;
import edu.stanford.math.plex4.streams.derived.TensorStream;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class WitnessTest {
	IntAbstractField intField = ModularIntField.getInstance(2);
	@Before
	public void setUp() {
		RandomUtility.initializeWithSeed(0);
	}

	@After
	public void tearDown() {}

	
	public ExplicitSimplexStream getX() {
		ExplicitSimplexStream stream = new ExplicitSimplexStream();
		stream.addElement(new int[]{0});
		stream.addElement(new int[]{1});
		stream.addElement(new int[]{2});
		stream.addElement(new int[]{0, 1});
		stream.addElement(new int[]{0, 2});
		stream.addElement(new int[]{1, 2});
		return stream;
	}
	
	public ExplicitStream<SimplexPair> getZ() {
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
		return subsetStream;
	}
	
	public ExplicitStream<SimplexPair> getXY() {
		ExplicitStream<SimplexPair> stream = new ExplicitStream<SimplexPair>(SimplexPairComparator.getInstance());
		ExplicitSimplexStream X = this.getX();
		ExplicitSimplexStream Y = this.getX();
		
		for (Simplex x: X) {
			for (Simplex y: Y) {
				stream.addElement(new SimplexPair(x, y), 0);
			}
		}

		return stream;
	}
	
	@Test
	public void testSubsetZigZag() throws IOException {
		ExplicitSimplexStream X = this.getX();
		ExplicitSimplexStream Y = this.getX();
		ExplicitStream<SimplexPair> Z = this.getZ();
		
		IntBarcodeCollection bc = ProductSubsetZigzag.testLongSequence(X, Y, Z);

		bc.draw();
		
		System.out.println(bc);
	}
	
	//@Test
	public void testZZ() {
		
		ExplicitStream<SimplexPair> Z = this.getZ();
		ExplicitStream<SimplexPair> XY = this.getXY();
		
		HomologyBasisTracker<SimplexPair> zz = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());

		List<SimplexPair> Zset = new ArrayList<SimplexPair>();
		for (SimplexPair pair: Z) {
			Zset.add(pair);
		}
		
		List<SimplexPair> XYset = new ArrayList<SimplexPair>();
		for (SimplexPair pair: XY) {
			XYset.add(pair);
		}
		
		Collections.sort(Zset, SimplexPairComparator.getInstance());
		Collections.sort(XYset, SimplexPairComparator.getInstance());
		
		System.out.println("Adding pairs in X x Y");
		for (SimplexPair pair: XYset) {
			zz.add(pair);
		}
		
		Collections.reverse(XYset);
		
		System.out.println("Removing pairs in X x Y \\ Z");
		for (SimplexPair pair: XYset) {
			if (!Zset.contains(pair)) {
				zz.remove(pair);
			}
		}
	}
	
	//@Test
	public void test() {
		ExplicitSimplexStream stream = this.getX();
		
		TensorStream<Simplex, Simplex> tensorStream = new TensorStream<Simplex, Simplex>(stream, stream);
		
		IntAbsoluteHomology<Simplex> algorithm = new IntAbsoluteHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, 3);
		System.out.println(algorithm.computeAnnotatedIntervals(stream));
		
		IntAbsoluteHomology<ObjectObjectPair<Simplex, Simplex>> tensorAlgorithm = new IntAbsoluteHomology<ObjectObjectPair<Simplex, Simplex>>(intField, tensorStream.getBasisComparator(), 0, 3);
		System.out.println(tensorAlgorithm.computeAnnotatedIntervals(tensorStream));
		
		ExplicitStream<SimplexPair> subsetStream = this.getZ();
		
		IntAbsoluteHomology<SimplexPair> algorithm2 = new IntAbsoluteHomology<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, 3);
		
		System.out.println(algorithm2.computeAnnotatedIntervals(subsetStream));
	}
}
