// 
//  RipsStreamTest.java
// 
//  ***************************************************************************
// 
//  Copyright 2008, Stanford University
// 
//  Permission to use, copy, modify, and distribute this software and its
//  documentation for any purpose and without fee is hereby granted,
//  provided that the above copyright notice appear in all copies and that
//  both that copyright notice and this permission notice appear in
//  supporting documentation, and that the name of Stanford University not
//  be used in advertising or publicity pertaining to distribution of the
//  software without specific, written prior permission.  Stanford
//  University makes no representations about the suitability of this
//  software for any purpose.  It is provided "as is" without express or
//  implied warranty.
// 
//  ***************************************************************************
// 
//  Test file for class <short description of the file>
// 
//  $Id$
// 

package edu.stanford.math.plex;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import static java.lang.Math.*;

/**
 * The <code>RipsStreamTest</code> class.
 *
 * <p>Among the facilities provided by the <code>RipsStreamTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */

public class RipsStreamTest {

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

    // check some misc things about RipsStreams
    {
      assertEquals("edge indices are okay", true, RipsStream.check_edge_index(222));
      PointData data = (PointData) new Torus();
      PointData rdata = (PointData) new EuclideanArrayData(40, 4);
      RipsStream str = new RipsStream(.1, 3, 2.0, data);
      RipsStream rstr = new RipsStream(.05, 3, 2.0, rdata);
      RipsStream zstr = new RipsStream(0.0, 3, 2.0, rdata);
      SimplexStream stream = (SimplexStream) str;
      SimplexStream rstream = (SimplexStream) rstr;
      SimplexStream zstream = (SimplexStream) zstr;
      assertTrue("big enough stream", (rstr.size() > 100000));
      assertTrue("big enough stream", (zstr.size() > 0));
    }
      
    // check iterator and stream next for equivalence
    {
      Persistence p = Plex.Persistence();
      PointData data = (PointData) new EuclideanArrayData(100, 3); 
      double max_dist = 2.0;
      RipsStream str = new RipsStream(.1, 2, max_dist, data);
      RipsStream strcpy = new RipsStream(.1, 2, max_dist, data);
      Iterator<Simplex> iterator = strcpy.iterator();

      if (str.size() < 1000000) {
        while(iterator.hasNext()) {
          assertTrue("iterator check", (str.hasNext() &&
                                        (iterator.next()).equals(str.next())));
        }
        assertTrue("iterator check", !str.hasNext());
      }
    }

    // make sure that the fixed dimensional iterators work
    {
      PointData data = (PointData) new EuclideanArrayData(20, 3); 
      RipsStream rc = Plex.RipsStream(.1, 3, 20, data);
      RipsStream zc = Plex.RipsStream(0, 3, 20, data);
      int test_dimension = 1;
      Iterator<Simplex> it_dim = rc.iterator(test_dimension);
      Iterator<Simplex> it = rc.iterator();
      int counter = 0;
      while(it.hasNext()) {
        Simplex n = it.next();
        if (n.dimension() == test_dimension) {
          Simplex n_d = it_dim.next();  
          counter++;
          assertEquals("fixed d iterator", n, n_d);
        }
      }
      assertTrue("fixed d iterator test", (counter > 0));
      assertFalse("fixed d iterator done", it_dim.hasNext());
      double cutoff = .75;
      Simplex[] matches_le = rc.matchingSimplices(test_dimension, cutoff, 
                                                  SimplexStream.ComparisonType.LE);
      Simplex[] matches_gt = rc.matchingSimplices(test_dimension, cutoff, 
                                                  SimplexStream.ComparisonType.GT);
      assertTrue("matching simplex test", ((matches_gt.length > 0) &&
                                           (matches_le.length > 0)));
      assertTrue("matching simplex test", (counter == (matches_gt.length +
                                                       matches_le.length)));
      Simplex[] zmatches_le = zc.matchingSimplices(test_dimension, cutoff, 
                                                   SimplexStream.ComparisonType.LE);
      Simplex[] zmatches_gt = zc.matchingSimplices(test_dimension, cutoff, 
                                                   SimplexStream.ComparisonType.GT);
      assertTrue("matching simplex test", ((zmatches_gt.length > 0) &&
                                           (zmatches_le.length > 0)));
      assertTrue("matching simplex test", (counter == (zmatches_gt.length +
                                                       zmatches_le.length)));
    }

    // Check our calculation for the Homology of the 2-Torus for both a
    // RipsStream and an explicitly constructed one.
    {
      Persistence p = Plex.Persistence();
      int n = 20;
      int d = 2;
      double incr = (2.0 * PI)/n;
      Torus tor = Plex.Torus(n, d);
      double slop = 1.2 * sqrt(((double)d) * 
                               (((1.0 - cos(incr)) * (1.0 - cos(incr))) + 
                                (sin(incr) * sin(incr))));
      double granularity = slop/1000.0;
      RipsStream rc = Plex.RipsStream(granularity, d+1, slop, tor);
      RipsStream zc = Plex.RipsStream(0,           d+1, slop, tor);
      PersistenceInterval.Float[] rci = p.computeIntervals(rc);
      Plex.BettiNumbers gen_rc = Plex.FilterInfinite(rci);
      PersistenceInterval.Float[] zci = p.computeIntervals(zc);
      Plex.BettiNumbers expected_rc = 
        new Plex.BettiNumbers(new int[] {1, 2, 1});
      assertEquals("2-torus betti numbers", gen_rc, expected_rc);
      assertEquals("rci/zci lengths", rci.length, zci.length);

      if (false) {
        System.out.printf("\n\n rci/zci (granularity = %f): \n", granularity);
        for (int idx = 0; idx < rci.length; idx++) {
          System.out.printf("%s/%s\n", rci[idx].toString(), zci[idx].toString());
        }
        System.out.printf("\n\n");
      }
      assertTrue("0-granularity test", Plex.equalPersistenceIntervals(rci, zci, granularity));

      if (false) {
        // example for the doc -- make sure it works
        System.out.printf("\n\np = Plex.Persistence()\n");
        System.out.printf("tor = Plex.Torus(%d, %d)\n", n, d);
        System.out.printf("rc = Plex.RipsStream(%f, %d, %f, tor)\n",
                          slop/10, d+1, slop);
        System.out.printf("rc.size()\n");
        System.out.printf("res = Plex.FilterInfinite(p.computeIntervals(rc))\n");
        System.out.printf(" %s\n\n", gen_rc.toString());
      }
    }

    // test the homology calculation for a handmade torus
    {
      SimplexStream ttor = TmpStream.Torus();
      Persistence pt = new Persistence();
      Plex.BettiNumbers gen_tt = 
        Plex.FilterInfinite(pt.computeIntervals(ttor));
      Plex.BettiNumbers expected_tt = 
        new Plex.BettiNumbers(new int[] {1, 2, 1});
      assertEquals("2-torus betti numbers", gen_tt, expected_tt);
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
  @Test(expected=IllegalArgumentException.class)
    public void testForException() {
    EuclideanArrayData edata = new EuclideanArrayData(40, 4);
    double x = edata.coordinate(10, 4);
  }
}
          
