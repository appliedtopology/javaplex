// 
//  MappedBufferDataTest.java
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
//  Test file for class MappedBufferDataTest.
// 
//  $Id$
// 

package edu.stanford.math.plex;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;


/**
 * The <code>MappedBufferDataTest</code> class contains tests.
 *
 * @version $Id$
 */

public class MappedBufferDataTest {

  private static Random rand = new Random();

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

