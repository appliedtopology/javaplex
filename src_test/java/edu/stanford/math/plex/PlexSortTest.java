
package edu.stanford.math.plex;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * The <code>PlexSortTest</code> class provides tests for PlexSort.java.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class PlexSortTest {

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

	public static int[] random_vec(int len, int max, Random rand) {
		int[] test = new int[len];

		for (int i = 0; i < len; i++) {
			int val = rand.nextInt() % max;
			test[i] = val;
		}

		return test;
	}

	public static class TestComp extends PlexSort.Comp {
		public final boolean increasing;
		public int fn(int i, int j) { 
			if (increasing) 
				return (i - j); 
			else  
				return (j - i);
		}

		public TestComp(boolean incr) {
			increasing = incr;
		}

		public boolean check_random_vec(int[] vec) {
			for (int i = 1; i < vec.length; i++) {
				if (increasing) {
					if (vec[i-1] > vec[i])
						return false;
				} else {
					if (vec[i-1] < vec[i])
						return false;
				}
			}
			return true;
		}

	}

	public static boolean check_vec(int[] vec) {
		for (int i = 1; i < vec.length; i++) {
			if (vec[i-1] > vec[i]) {
				System.out.printf("\n### ");
				for (int j = 0; j < vec.length; j++) {
					if (j == (i-1))
						System.out.printf(" >>> %d ", vec[j]);
					else if (j == i)
						System.out.printf("%d <<< ", vec[j]);
					else 
						System.out.printf("%d ", vec[j]);
				}
				System.out.printf("\n\n");
				return false;
			}
		}
		return true;
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
		int len_increasing = 100;
		TestComp tc_increasing = new TestComp(true);
		int[] tv_increasing = new int[len_increasing];
		for (int i = 0; i < tv_increasing.length; i++) {
			tv_increasing[i] =  tv_increasing.length - i;
		}
		//PlexSort.comp_sort(tv_increasing, 0, tv_increasing.length, tc_increasing);
		//assertTrue("Sorted correctly", tc_increasing.check_random_vec(tv_increasing));

		Random rand = new Random(1);
		int counter = 100;
		int max_len = 1000;
		int min_len = 10;
		while(counter-- > 0) {
			TestComp tc = new TestComp(rand.nextInt(2) == 1);
			int len = rand.nextInt(max_len - min_len) + min_len;
			int max = ((counter % 10) == 0)?2:(3+rand.nextInt(len-3));
			int[] vec = random_vec(len, max, rand);
			PlexSort.comp_sort(vec, 0, vec.length, tc);
			// PlexSort.xsort(vec, 0, vec.length); 
			assertTrue("Sorted correctly", tc.check_random_vec(vec));
		}

		// FIX THIS!
		if (false) {
			int pts[] = new int[] { 1, 6, 7, 8, 22, 30, 41, 42, 49 };
			double wts[] = new double[] { 1.0, 6.0, 7.0, 8.0, 22.0, 30.0, 41.0, 42.0, 49.0 };
			PlexSort.reverse_sort(pts, wts);
			for (int i = 0; i < pts.length-1; i++)
				assertTrue("sorting by decreasing weights", (pts[i] > pts[i+1]));
		}
		// FIX THIS!

	}

	/**
	 * tests some exceptional behavior.
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

