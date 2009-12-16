// 
//  LazyWitnessStreamTest.java
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
 * Tests for <code>LazyWitnessStream</code>.
 *
 * @version $ID$
 */

public class LazyWitnessStreamTest {

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

    {
      Persistence p = Plex.Persistence();
      PointData data = (PointData) new EuclideanArrayData(5000, 3); 
      int[] landmarks = WitnessStream.makeRandomLandmarks(data, 20);
      double R_max =  WitnessStream.estimateRmax(data, landmarks);
      double granularity = .00001;
      assertTrue("R_max not too big", (R_max < .8));
      LazyWitnessStream lstr = 
        new LazyWitnessStream(granularity, 3, R_max, 2, landmarks, data);
      LazyWitnessStream zstr = 
        new LazyWitnessStream(0, 3, R_max, 2, landmarks, data);
      PersistenceInterval.Float[] lci = p.computeIntervals(lstr);
      PersistenceInterval.Float[] zci = p.computeIntervals(zstr);
      assertTrue("0-granularity test", Plex.equalPersistenceIntervals(lci, zci, granularity));
    }

    if (false) {
      PointData data = (PointData) new EuclideanArrayData(1000, 4); 
      int[] landmarks = WitnessStream.makeRandomLandmarks(data, 20);
      double R_max =  WitnessStream.estimateRmax(data, landmarks);
      assertTrue("R_max not too big", (R_max < .25));
      long lazy_time = System.currentTimeMillis();
      LazyWitnessStream lstr = new LazyWitnessStream(.01, 3, R_max, 2, landmarks, data);
      lazy_time = System.currentTimeMillis() - lazy_time;
      assertTrue("Didn't take too long", (lazy_time < 100));
      long w_time = System.currentTimeMillis();
      WitnessStream wstr = new WitnessStream(.01, 3, R_max, landmarks, data);
      w_time = System.currentTimeMillis() - w_time;
      assertTrue("Didn't take too long", (w_time < 2000));
      SimplexTable ltbl = new SimplexTable(lstr.size());
      Simplex s;
      while((s = lstr.next()) != null)
        ltbl.put(s);
      while((s = wstr.next()) != null) {
        assertTrue("found all witness simplices in lzw stream",
                   (ltbl.get(s) != null));
      }
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
    Object o = emptyList.get(0);
  }
}
          
