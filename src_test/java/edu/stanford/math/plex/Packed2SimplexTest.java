package edu.stanford.math.plex;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>Packed2SimplexTest</code> class contains tests.
 *
 * @version $Id$
 */
public class Packed2SimplexTest {

	private java.util.List emptyList;

	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		emptyList = new java.util.ArrayList();
	}

	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		emptyList = null;
	}

	/**
	 * Tests some behavior.
	 *
	 * @exception  whatever
	 * @throws what is the difference between these 2
	 *
	 * @see        java.lang.System#getProperty(java.lang.String)
	 * @see        SecurityManager#checkPermission
	 */    
	@Test
	public void testSomeBehavior() {
		assertEquals("Empty list should have 0 elements", 0, emptyList.size());
	}

	/**
	 * Tests some exceptional behavior.
	 *
	 * @exception  IndexOutOfBoundsException
	 *
	 * @see        java.lang.System#getProperty(java.lang.String)
	 * @see        SecurityManager#checkPermission
	 */
	@Test(expected=IllegalArgumentException.class)
	@SuppressWarnings("unused")
	public void testForException() {
		Simplex sloser0 = Simplex.getSimplex(new int[] {-1, Integer.MAX_VALUE});
		Simplex sloser1 = Simplex.getSimplex(new int[] {1, 1});
	}
}

