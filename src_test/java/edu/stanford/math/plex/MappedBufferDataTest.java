package edu.stanford.math.plex;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>MappedBufferDataTest</code> class contains tests.
 *
 * @version $Id$
 */
@SuppressWarnings("unused")
public class MappedBufferDataTest {

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

	double val(int p, int d, int dim) {
		return (p + ((double)d)/((double) (1 + dim)));
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
		String filename = "tmp_mbd.bin";
		MappedBufferData.delete(filename);

		int len = 1000;
		int dim = 39;

		double[][] test_vals = new double[len+1][dim];

		for (int p = 1; p <= len; p++) {
			for (int d = 0; d < dim; d++) {
				test_vals[p][d] = val(p, d, dim);
			}
		}

		{
			MappedBufferData test = new MappedBufferData(filename, len, dim, 0.0, 1.0);
			test = null;
			System.gc();
			System.gc();
			System.gc();
		}

		boolean loser = false;

		{
			MappedBufferData test = new MappedBufferData(filename, true);
			double[][] check_vals = new double[len+1][dim];

			for (int p = 1; p <= len; p++) {
				test.set_pt(p, test_vals[p]);
			}

			for (int p = 1; p <= len; p++) {
				test.get_pt(p, check_vals[p]);
			}
			for (int p = 1; p <= len; p++) {
				for (int d = 0; d < dim; d++) {
					assertTrue("test/check the same", test_vals[p][d] == check_vals[p][d]);
				}
			}

			test.force();
			test = null;
			System.gc();
			System.gc();
			System.gc();
		}

		{
			MappedBufferData test = new MappedBufferData(filename, false);
			double[][] check_vals = new double[len+1][dim];

			for (int p = 1; p <= len; p++) {
				test.get_pt(p, check_vals[p]);
			}
			for (int p = 1; p <= len; p++) {
				for (int d = 0; d < dim; d++) {
					assertTrue("test/check the same", test_vals[p][d] == check_vals[p][d]);
				}
			}

			test = null;
			System.gc();
			System.gc();
			System.gc();
		}

		MappedBufferData.delete(filename);
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

