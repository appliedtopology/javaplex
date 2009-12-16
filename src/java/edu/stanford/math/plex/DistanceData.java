// 
//  DistanceData.java
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
//  $Id$
// 

package edu.stanford.math.plex;

import java.util.*;

/**
 * The <code>DistanceData</code> class is the simplest implementation
 * of NSpace. Intended for smallish data sets.
 *
 * @version $Id$
 */

public final class DistanceData extends PointData {

  final double[][] d;
  final int numPoints;

  /**
   * The number of data points.
   *
   * <p>
   *
   * @return     the number of points
   */
  public final int count() {
    if (d != null)
      return (d.length);
    else
      return 0;
  }
   
  /**
   * Euclidean distance.
   *
   * <p>
   *
   * @param      p1   the first point
   * @param      p2   the second point
   * @return     the distance between p1 and p2.
   */
  public final double distance (int p1, int p2) {
    if (p1 == p2)
      return 0.0;
    else {
      return d[p1-1][p2-1];
    }

  }

  // Constructor for this class.
  public DistanceData(double[][] data) {
    super();
    d = data;
    numPoints = d.length;
  }


}
