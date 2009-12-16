
package edu.stanford.math.plex;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>Packed6SimplexTest</code> class contains tests.
 *
 * @version $Id$
 */
@SuppressWarnings("unused")
public class Packed6SimplexTest {

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
		Simplex s6 = Simplex.getSimplex(new int[] {1, 2, 3, 4, Integer.MAX_VALUE});
		assertTrue("s6 is okay", s6.dimension() == 4);
		assertTrue("s6 is okay", s6 instanceof Packed6Simplex);
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
	public void testForException() {
		Simplex sloser0 = Simplex.getSimplex(new int[] {0, 2, 1, 4, Integer.MAX_VALUE});
		Simplex sloser1 = Simplex.getSimplex(new int[] {1, 2, 1, 4, Integer.MAX_VALUE});
	}
}

