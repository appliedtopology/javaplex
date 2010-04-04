package edu.stanford.math.plex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>SimplexFaceIteratorTest</code> class.
 *
 * <p>Among the facilities provided by the <code>SimplexFaceIteratorTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class SimplexFaceIteratorTest {

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
		assertTrue("vectors of the same length are equal", 
				Plex.equalPtArrays((new int[5]),(new int[5])));
		SimplexFaceIterator test = 
			new SimplexFaceIterator(new int[] {3, 17, 57, 81}, 1);
		assertTrue("explicit face iterator hasNext", test.hasNext());
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {3, 17}));
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {3, 57}));
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {3, 81}));
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {17, 57}));
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {17, 81}));
		assertTrue("we know next", Plex.equalPtArrays(test.next(), new int[] {57, 81}));
		assertFalse("explicit face iterator !hasNext", test.hasNext());
		int[] test_array = new int[] {1, 3, 17, 57, 81};
		assertTrue("array is length 5", (test_array.length == 5));
		SimplexFaceIterator test3 = new SimplexFaceIterator(test_array, 3);
		int counter = 0;
		while (test3.hasNext()) {
			int[] tmp = test3.next();
			counter++;
		}
		assertTrue("there are 5 cofaces", (counter == 5));
		SimplexFaceIterator test4 = new SimplexFaceIterator(test_array, 4);
		assertTrue("face iterator has one entry", test4.hasNext());
		test4.next();
		assertFalse("face iterator of length 1 !hasNext", test4.hasNext());
		SimplexFaceIterator test5 = new SimplexFaceIterator(test_array, 5);
		assertFalse("impossible face iterator !hasNext", test5.hasNext());
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

