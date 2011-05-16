
package edu.stanford.math.plex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>TorusTest</code> class.
 *
 * <p>Among the facilities provided by the <code>TorusTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class TorusTest {

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
		Torus tdata = new Torus();
		RipsStream rstr = new RipsStream(0.1, 3, 2.0, tdata); 
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
		Object o = emptyList.get(0);
	}
}

