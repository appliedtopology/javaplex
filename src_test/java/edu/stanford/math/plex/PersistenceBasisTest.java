// 
//  PersistenceTest.java
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


/**
 * The <code>PersistenceTest</code> class.
 *
 * <p>Among the facilities provided by the <code>PersistenceTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */

public class PersistenceBasisTest {        
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
    PersistenceBasis P = new PersistenceBasis();
    PersistenceInterval[] expected = new PersistenceInterval[5];

    expected[0] = new PersistenceInterval.Int(0, 0, 1);
    expected[1] = new PersistenceInterval.Int(0, 0);
    //expected[2] = new PersistenceInterval.Int(0, 1, 1);
    expected[2] = new PersistenceInterval.Int(0, 1, 2);
    expected[3] = new PersistenceInterval.Int(1, 2, 5);
    expected[4] = new PersistenceInterval.Int(1, 3, 4);

    PersistenceBasisInterval[] intervals = P.computeRawIntervals(stream, 7);
    int counter = 0;
    for (PersistenceBasisInterval i : intervals) {
			System.out.println(i);
      assertEquals("Got expected results", i.toPersistenceInterval(),(expected[counter++]));
    }			

    // make sure that the homology of the d-sphere is what we expect
    for (int d = 1; d <= 5; d++) {
      SimplexStream ds = PointData.Discrete.DSphere(d);
      PersistenceBasis p = new PersistenceBasis();
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
      PersistenceBasis p;
      Plex.BettiNumbers gen_dk;

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(3,1);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 6}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(3,2);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 4}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(4,1);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 10}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(4,2);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 10}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(4,3);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 5}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(5,1);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 15}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(5,2);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 20}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(5,3);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 15}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(5,4);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 6}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(6,1);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 21}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(6,2);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 35}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(6,3);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 35}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(6,4);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 21}));

      p = new PersistenceBasis();
      ds = PointData.Discrete.DSphereKskeleton(6,5);
      gen_dk = Plex.FilterInfinite(p.computeIntervals(ds));
      assertEquals("S(d,k) homology", gen_dk, 
                   new Plex.BettiNumbers(new int[] {1, 0, 0, 0, 0, 7}));
    }
  }
}
          
