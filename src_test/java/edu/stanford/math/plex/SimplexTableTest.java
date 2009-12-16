// 
//  SimplexTableTest.java
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
 * The <code>SimplexTableTest</code> class.
 *
 * <p>Test code for the specialized Simplex interning class SimplexTable.
 *
 * @version $ID$
 */

public class SimplexTableTest {

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

  // Some not very precise timing information -- a very simple-minded
  // test suggested that Simplex creation was within a factor of 2 of a
  // handcrafted C code implementation. The numbers here are only
  // interesting as relative values, because they were collected without
  // trying for the best performance. I think that we can probably halve
  // these times when running in production mode.
  //
  // Construction only: 8.5M simplices Time: 3.6 secs
  // Construction and interning of 8.5M simplices -- Time: 6.8 secs
  // Construction, interning, bdy operation on 8.5M -- Time: 19.4 secs
  // Simplex construction -- Chain construction on 8.5M -- Time: 22.5 secs
  // Simplex construction -- add chains together on 8.5M -- Time: 25.8 secs

  /**
   * Tests creating a RipsStream and interning the elements in it, as
   * well as doing some Chain arithmetic.
   *
   */    
  @Test
    public void testSomeBehavior() {
    PointData rdata = (PointData) new EuclideanArrayData(40, 4); // EuclideanArrayData(120, 4);
    int rstr_size = 0; 
    int rtbl_size = 0;
    Simplex s, t;
    RipsStream rstr = new RipsStream(.05, 3, 2.0, rdata);
    int rstr_initial_size = rstr.size();
    SimplexTable rtbl = new SimplexTable(1000);
    int initial_limit = rtbl.limit();
    Iterator<Simplex> riter = rstr.iterator();
    while ((riter.hasNext() && ((s = riter.next()) != null)) && 
           (riter.hasNext() && ((t = riter.next()) != null))) {
      Simplex[] b = s.boundaryArray(); 
      Simplex[] c = t.boundaryArray(); 
      if (b != null) 
        for (int i = 0; i < b.length; i++) b[i] = rtbl.get(b[i]);
      rtbl.put(s);
      if (c != null) 
        for (int i = 0; i < c.length; i++) c[i] = rtbl.get(c[i]);
      rtbl.put(t);
      Chain d = Chain.fromBoundary(b, Persistence.baseModulus());
      Chain e = Chain.fromBoundary(c, Persistence.baseModulus());
      if(d != null) 
        assert(rtbl.get(d.maxS()) != null);
      if(e != null) {
        if (d != null)
          e = e.add(d, 1);
        assert((e == null) || (rtbl.get(e.maxS()) != null));
      }
    }
    assertTrue("table big enough", ((rtbl.size() >= (rstr_initial_size - 2)) && 
                                    (rtbl.size() > 0)));
    assertTrue("table grew", (rtbl.limit() > initial_limit));
    int found = 0;
    while (((s = rstr.next()) != null) && ((t = rstr.next()) != null)) {
      assertTrue("found s", rtbl.get(s) != null);
      assertTrue("found t", rtbl.get(t) != null);
      found += 2;
    }
    assertEquals("found all", rtbl.size(), found);
    assertEquals("stream empty", rstr.size(), 0);
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
          
