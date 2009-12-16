// 
//  Packed4SimplexTest.java
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
 * The <code>Packed4SimplexTest</code> class.
 *
 * <p>Among the facilities provided by the <code>Packed4SimplexTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */

public class Packed4SimplexTest {

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
    assertTrue("Check constants", Packed4Simplex.checkConstants());
    assertTrue("Packed4Simplex instances should get used", 
               (Simplex.getSimplex(new int[] {1, 2, 5})) instanceof Packed4Simplex);
    assertEquals("Packed4Simplex instances should be equal", 
                 Simplex.getSimplex(new int[] {1, 2, 5}),
                 Simplex.getSimplex(new int[] {5, 2, 1}));
    Simplex s4 = Simplex.getSimplex(new int[] {1, 2, 3, Integer.MAX_VALUE});
    assertTrue("s4 is okay", s4.dimension() == 3);
    assertTrue("s4 is okay", s4 instanceof Packed4Simplex);
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
    Simplex sloser0 = Simplex.getSimplex(new int[] {0, 1, 4, Integer.MAX_VALUE});
    Simplex sloser1 = Simplex.getSimplex(new int[] {1, 1, 4, Integer.MAX_VALUE});
  }
}
          
