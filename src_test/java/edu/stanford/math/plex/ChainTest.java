// 
//  ChainTest.java
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
 * The <code>ChainTest</code> class.
 *
 * <p>Among the facilities provided by the <code>ChainTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */

public class ChainTest {

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
    int p = Persistence.baseModulus();
    int[] vertices = new int[] {1, 3, 4, 2};
    int[] v6 = new int[] {1, 3, 4, 2, 33, 11};
    Simplex test = Simplex.getSimplex(vertices);
    Simplex t6 = Simplex.getSimplex(v6);
    Chain tchain = Chain.fromBoundary(test.boundaryArray(), p);
    Chain tc6 = Chain.fromBoundary(t6.boundaryArray(), p);
    assertTrue("BDY(BDY(x)) is zero", (tchain.boundary(p).equals(Chain.zero(p))));
    assertTrue("BDY(BDY(x)) is zero", (tc6.boundary(p).equals(Chain.zero(p))));
    assertTrue("BDY(BDY(x)) is zero", (((tchain.add(tc6, 4)).boundary(p)).equals(Chain.zero(p))));

    for (int i = 0; i < 1000; i++) {
      Chain foo = Chain.random(p);
      Chain bar = Chain.random(p);
      Chain bfoo = foo.boundary(p);
      Chain bbar = bar.boundary(p);
      assertTrue("x+0 is x", foo.add(Chain.zero(p),1).equals(foo));
      assertTrue("0+x is x", Chain.zero(p).add(foo,1).equals(foo));
      assertTrue("BDY(BDY(x)) is zero", ((bfoo.equals(Chain.zero(p))) || ((bfoo.boundary(p)).equals(Chain.zero(p)))));
      if ((!bfoo.equals(Chain.zero(p))) && (!bbar.equals(Chain.zero(p))) && (!((foo.add(bar, 1))).equals(Chain.zero(p)))) {
        assertTrue("BDY(x + y) equals BDY(x) + BDY(y)",    
                   ((foo.add(bar, 1)).boundary(p)).ceq(bfoo.add(bbar, 1)));
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
          
