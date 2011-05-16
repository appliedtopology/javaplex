package edu.stanford.math.plex;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class SimplexTest {

	private java.util.List<Object> emptyList;

	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		emptyList = new java.util.ArrayList<Object>();
	}

	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		emptyList = null;
	}

	@Test
	public void testSomeBehavior() {
		int[] vertices = new int[] {1, 3, 4, 2};
		int[] v6 = new int[] {1, 3, 4, 6, 2, 8};
		Simplex test = Simplex.getSimplex(vertices);
		Simplex t6 = Simplex.getSimplex(v6);
		assertTrue("test.subset(t6)", test.subset(t6));
		assertTrue("test.subset(t6)", test.subset(t6, new int[10], new int[8]));
		int[] tverts = test.vertices();
		int[] t6verts = t6.vertices();
		assertTrue("s.vertices equals original vertices", 
				Plex.equalPtArrays(Simplex.getSimplex(vertices).vertices(),
						Simplex.vertex_sort(vertices)));
		assertTrue("s.vertices equals original vertices", 
				Plex.equalPtArrays(Simplex.getSimplex(v6).vertices(),
						Simplex.vertex_sort(v6)));
		Simplex[] test_bdy = test.boundaryArray();
		Simplex[] t6_bdy = t6.boundaryArray();
		assertTrue("second bdy elt is correct",
				test_bdy[1].equals(Simplex.getSimplex(new int[] {1, 3, 4})));
		assertTrue("second bdy elt is correct",
				t6_bdy[1].equals(Simplex.getSimplex(new int[] {1, 3, 4, 6, 8})));
		Simplex e2 = Simplex.makeEdge(1, Integer.MAX_VALUE, -1);
		assertTrue("e2 is okay", e2.dimension() == 1);
		assertTrue("e2 is okay", e2 instanceof Packed2Simplex);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testForException() {
		Object o = emptyList.get(0);
	}
}

