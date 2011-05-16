
package edu.stanford.math.plex;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class PlexTest {

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

	private static boolean is_present(int x, int[] y) {
		for (int i = 0; i < y.length; i++) {
			if (x == y[i])
				return true;
		}
		return false;
	}

	@Test
	public void testSomeBehavior() {
		int[] set = new int[] {2, 4, 6, 8, 10, 15, 23, 45, 99, 1000};
		int[] subset = new int[3];
		int[] subset2 = new int[5];
		int[] subset3 = new int[50];
		int subset2_len = subset2.length - 1;
		int subset3_len = subset3.length/2;
		Plex.random_subset(set, set.length, subset, subset.length);
		Plex.random_subset(set, set.length, subset2, subset2_len);
		Plex.random_subset(100, subset3, subset3_len);
		for(int i = 0; i < subset.length-1; i++) {
			assertTrue("increasing subset", (subset[i] < subset[i+1]));
			assertTrue("is present", is_present(subset[i], set));
			assertTrue("is present", is_present(subset[i+1], set));
		}
		for(int i = 0; i < subset2_len-1; i++) {
			assertTrue("increasing subset", (subset2[i] < subset2[i+1]));
			assertTrue("is present", is_present(subset2[i], set));
			assertTrue("is present", is_present(subset2[i+1], set));
		}
		for(int i = 0; i < subset3_len-1; i++) 
			assertTrue("increasing subset", (subset3[i] < subset3[i+1]));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testForException() {
		Object o = emptyList.get(0);
	}
}

