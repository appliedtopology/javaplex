package edu.stanford.math.plex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>PersistenceTest</code> class.
 *
 * <p>Among the facilities provided by the <code>PersistenceTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
public class PersistenceTest {

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
		SimplexStream stream = TmpStream.PaperTestCase();
		Persistence P = new Persistence();
		PersistenceInterval[] expected = new PersistenceInterval[5];

		expected[0] = new PersistenceInterval.Int(0, 0, 1);
		expected[1] = new PersistenceInterval.Int(0, 0);
		//expected[2] = new PersistenceInterval.Int(0, 1, 1);
		expected[2] = new PersistenceInterval.Int(0, 1, 2);
		expected[3] = new PersistenceInterval.Int(1, 2, 5);
		expected[4] = new PersistenceInterval.Int(1, 3, 4);

		PersistenceInterval[] intervals = P.computeRawIntervals(stream, false, 7);
		int counter = 0;
		for (PersistenceInterval i : intervals) {
			assertTrue("Got expected results", i.equals(expected[counter++]));
		}

		// make sure that the homology of the d-sphere is what we expect
		for (int d = 1; d <= 5; d++) {
			SimplexStream ds = PointData.Discrete.DSphere(d);
			Persistence p = Plex.Persistence();
			Plex.BettiNumbers gen = 
				Plex.FilterInfinite(p.computeIntervals(ds));
			int[] exp_init = new int[d+1];
			exp_init[0] = 1;
			exp_init[d] = 1;
			Plex.BettiNumbers exp_bn = new Plex.BettiNumbers(exp_init);
			assertEquals("sphere H* generators", exp_bn, gen);
		}

		// Make sure that the homology of the S(d,k) (the k-skeleton of the
		// d-sphere) is what we expect.
		{
			SimplexStream ds;
			Persistence p;
			Plex.BettiNumbers gen_dk;

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(3,1);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 6}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(3,2);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 4}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(4,1);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 10}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(4,2);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 10}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(4,3);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 5}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(5,1);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 15}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(5,2);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 20}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(5,3);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 15}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(5,4);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 6}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(6,1);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 21}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(6,2);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 35}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(6,3);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 35}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(6,4);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 21}));

			p = Plex.Persistence();
			ds = PointData.Discrete.DSphereKskeleton(6,5);
			gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
			assertEquals("S(d,k) homology", gen_dk, 
					new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 0, 7}));
		}
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

