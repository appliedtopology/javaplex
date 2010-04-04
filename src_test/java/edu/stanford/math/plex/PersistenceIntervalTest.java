package edu.stanford.math.plex;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>PersistenceIntervalTest</code> class.
 *
 * <p>Among the facilities provided by the <code>PersistenceIntervalTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */

public class PersistenceIntervalTest {

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
	@Test(expected=IndexOutOfBoundsException.class)
	public void testForException() {
		@SuppressWarnings("unused")
		Object o = emptyList.get(0);
	}
}

