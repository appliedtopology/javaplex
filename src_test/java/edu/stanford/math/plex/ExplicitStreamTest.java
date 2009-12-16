package edu.stanford.math.plex;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * The <code>ExplicitStreamTest</code> class.
 *
 * <p>Among the facilities provided by the <code>ExplicitStreamTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class ExplicitStreamTest {

	private static boolean member(int x, int[][] set) {
		for (int i = 0; i < set.length; i++) {
			for (int j = 0; j < set[i].length; j++) {
				if (x == set[i][j])
					return true;
			}
		}
		return false;
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

		// make sure that next() and size() agree
		{
			ExplicitStream ex1 = new ExplicitStream();
			ex1.add(new int[] {1}, 0.0);
			ex1.add(new int[] {2}, 0.0);
			ex1.add(new int[] {3}, 0.0);
			ex1.add(new int[] {1,2}, 1.0);
			ex1.add(new int[] {2,3}, 1.0);
			ex1.add(new int[] {1,3}, 1.0);
			ex1.add(new int[] {1,2,3}, 2.0);
			ex1.close();
			assertTrue("ex1 has 7 elements", ex1.size() == 7);
			int ex1_counter = 0;
			while (ex1.hasNext()) {
				ex1.next();
				ex1_counter++;
			}
			assertTrue("ex1 really has 7 elements", ex1_counter == 7);
		}

		// make sure that next() and dump() agree
		{
			PointData rdata = (PointData) new EuclideanArrayData(20, 4);
			RipsStream zstr = new RipsStream(0.0, 2, 2.0, rdata);
			SimplexStream zstream = (SimplexStream) zstr;
			ExplicitStream estream = Plex.makeExplicit(zstr);
			assertEquals("converted right number of simplices", zstr.size(), estream.size());
			ExplicitStream.DComplex points = estream.dump(0);
			int counter = (points.C()).length;
			while (points.hasNext()) {
				points = points.next();
				counter += (points.C()).length;
			}
			assertEquals("counted right number of simplices", zstr.size(), counter);

			ExplicitStream.DComplex triangles = estream.dump(2);
			int[][] tvec = triangles.C();
			int[] s2 = tvec[tvec.length/2];
			int[] l1 = new int[] {s2[0], s2[1]};
			estream.remove(l1);
			boolean caught_the_ise = false;
			assertFalse("verification fails", (estream.verify(false)));
			try{ estream.close(); } 
			catch(IllegalStateException ise) { 
				caught_the_ise = true;
				estream.ensure_all_faces();        
			}
			finally{}
			assertTrue("catch occurred", caught_the_ise);
			assertTrue("verification succeeds after catch", (estream.verify(false)));
			estream.close();

			Iterator<Simplex> iterator = estream.iterator();
			int last_fi = -1;
			int last_dimension = -1;
			Simplex iterator_current = null;
			Simplex stream_current = null;

			assertTrue("iterator start check", iterator.hasNext());
			assertTrue("iterator start check", estream.hasNext());
			while(iterator.hasNext()) {
				iterator_current = iterator.next();
				assertTrue("iterator check", (estream.hasNext() &&
						((stream_current = estream.next()) != null) &&
						(iterator_current.equals(stream_current))));
				if (iterator_current.dimension() > last_dimension) {
					last_dimension = iterator_current.dimension();
					last_fi = -1;
				}
				assertTrue("nondecreasing fi's", 
						(last_fi <= iterator_current.findex()));
				last_fi = iterator_current.findex();
			}
			assertTrue("estream iterator end check", !estream.hasNext());
		}

		// check prune() operations
		{
			PointData rdata = (PointData) new EuclideanArrayData(20, 4);
			ExplicitStream ex2 = Plex.makeExplicit(new RipsStream(0.0, 3, 2.0, rdata));
			int before_size = ex2.size();
			ex2.prune(new int[] {1});
			ex2.close();
			int after_size = ex2.size();
			assertTrue("ex2 got smaller", (before_size > after_size));
			ExplicitStream.DComplex pruned = ex2.dump(0);
			assertTrue("1 has been removed", !member(1, pruned.C()));
			while (pruned.hasNext()) {
				pruned = pruned.next();
				assertTrue("1 has been removed", !member(1, pruned.C()));
			}
		}


		// check iterator and stream next equivalence


		ExplicitStream estream2 = Plex.ExplicitStream();
		estream2.add(new double[][] { {2.1, 3.1}, {1.4, 2.3}, {2.9, .99} }, 
				new double[] {0.0, 0.0, 1.0});
		estream2.add(new int[][] { {1}, {3}, {2} }, 
				new double[] {0.0, 0.0, 0.0});
		ExplicitStream.DComplex force_close = estream2.dump(1);
	}

	/**
	 * Tests trying to close inconsistent stream.
	 */
	@Test(expected=IllegalStateException.class)
	public void testForException1() {
		ExplicitStream estream = Plex.ExplicitStream();
		estream.add(new int[][] { {2, 3}, {1, 2}, {3, 1} }, 
				new double[] {0.0, 0.0, 1.0});
		estream.add(new int[][] { {1}, {3}, {2} }, 
				new double[] {0.0, 0.0, 0.0});
		estream.remove(new int[] {1});
		ExplicitStream.DComplex loser = estream.dump(1);
	}

	/**
	 * Tests mismatch of arguments to add().
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testForException2() {
		ExplicitStream estream = Plex.ExplicitStream();
		estream.add(new int[][] { {2, 3}, {1, 2}, {3, 1} }, 
				new double[] {0.0, 0.0, 1.0, 0.0});
	}
}

