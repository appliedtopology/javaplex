package edu.stanford.math.plex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>Packed4SimplexTest</code> class.
 *
 * <p>Among the facilities provided by the <code>Packed4SimplexTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class Packed4SimplexTest {

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
		assertTrue("Check constants", Packed4Simplex.checkConstants());
		assertTrue("Packed4Simplex instances should get used", 
				(Simplex.getSimplex(new int[] {1, 2, 5})) instanceof Packed4Simplex);
		assertEquals("Packed4Simplex instances should be equal", 
				Simplex.getSimplex(new int[] {1, 2, 5}),
				Simplex.getSimplex(new int[] {5, 2, 1}));
		Simplex s4 = Simplex.getSimplex(new int[] {1, 2, 3, Integer.MAX_VALUE});
		assertTrue("s4 is okay", s4.dimension() == 3);
		assertTrue("s4 is okay", s4 instanceof Packed4Simplex);
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
		Simplex sloser0 = Simplex.getSimplex(new int[] {0, 1, 4, Integer.MAX_VALUE});
		Simplex sloser1 = Simplex.getSimplex(new int[] {1, 1, 4, Integer.MAX_VALUE});
	}
}

