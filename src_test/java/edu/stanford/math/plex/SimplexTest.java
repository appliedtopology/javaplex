// 
//  SimplexTest.java
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

public class SimplexTest {

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
        
  @Test
    public void testSomeBehavior() {
    int[] vertices = new int[] {1, 3, 4, 2};
    int[] v6 = new int[] {1, 3, 4, 6, 2, 8};
    Simplex test = Simplex.getSimplex(vertices);
    Simplex t6 = Simplex.getSimplex(v6);
    assertTrue("test.subset(t6)", test.subset(t6));
    assertTrue("test.subset(t6)", test.subset(t6, new int[10], new int[8]));
    int[] tverts = test.vertices();
    int[] t6verts = t6.vertices();
    assertTrue("s.vertices equals original vertices", 
               Plex.equalPtArrays(Simplex.getSimplex(vertices).vertices(),
                                  Simplex.vertex_sort(vertices)));
    assertTrue("s.vertices equals original vertices", 
               Plex.equalPtArrays(Simplex.getSimplex(v6).vertices(),
                                  Simplex.vertex_sort(v6)));
    Simplex[] test_bdy = test.boundaryArray();
    Simplex[] t6_bdy = t6.boundaryArray();
    assertTrue("second bdy elt is correct",
               test_bdy[1].equals(Simplex.getSimplex(new int[] {1, 3, 4})));
    assertTrue("second bdy elt is correct",
               t6_bdy[1].equals(Simplex.getSimplex(new int[] {1, 3, 4, 6, 8})));
    Simplex e2 = Simplex.makeEdge(1, Integer.MAX_VALUE, -1);
    assertTrue("e2 is okay", e2.dimension() == 1);
    assertTrue("e2 is okay", e2 instanceof Packed2Simplex);
  }

  @Test(expected=IndexOutOfBoundsException.class)
    public void testForException() {
    Object o = emptyList.get(0);
  }
}
          
