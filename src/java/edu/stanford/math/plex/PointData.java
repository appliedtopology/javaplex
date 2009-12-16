// 
//  PointData.java
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
//  Point information.
// 
//  $Id$
// 

package edu.stanford.math.plex;
import java.util.*;

/**
 * A <code>PointData</code> instance encodes distance information for a
 * data set.
 *
 * @version $Id$
 */
public abstract class PointData {


  private static Random rand = null;

  /**
   * Distance between 2 points.
   * <p>
   *
   * @param      p1   index of the first point
   * @param      p2   index of the second point
   * @return     distance between the two points.
   */    
  abstract public double distance(int p1, int p2);

  /**
   * The number of points in this data set. 
   * 
   * <p> Indexing begins from 1, not 0, which means that the points go
   * from 1 to count(), inclusive. It is acceptable for an implementation
   * to allow points to be added.
   *
   * <p>
   *
   * @return     The number of points in the data set.
   */    
  abstract public int count();


  /**
   * Return true if the new point is good enough to be added to the current
   * set of points. Here, "good enough" means distinct from the other
   * points, but this can be overridden by subclasses.
   *
   * @param      pt Point under consideration.
   * @param      other_pts Points to compare with new points.
   * @param      other_pts_count How many other points.
   * @return     true if new pt is good enough, else false.
   */    
  boolean good_enough_point (int pt, int[] other_pts, int other_pts_count) {
    for (int i = 0; i < other_pts_count; i++) {
      if (other_pts[i] == pt)
        return false;
    }
    return true;
  }

  /**
   * Return a sorted array of distinct indices for "random points" in the
   * metric space.  <p>
   *
   * @param      number_of_points How many points we want.
   * @return     An array of indices for the random points.
   */    
  public int[] random_points(int number_of_points) {
    if (rand == null)
      rand = new Random();
    int max_pt = count();
    int[] return_value = new int[number_of_points];
    int i = 0;
    boolean all_good = true;
    while (true) {
      while (i < number_of_points) {
        int pt = 1 + rand.nextInt(max_pt);
        if (good_enough_point(pt, return_value, i))
          return_value[i++] = pt;
      }
      Arrays.sort(return_value);
      all_good = true;
      for (i = 1; i < number_of_points; i++) {
        if (return_value[i-1] == return_value[i]) {
          return_value[i] = 1 + max_pt + i;
          all_good = false;
        }
      }
      if (all_good) 
        return return_value;
      else
        Arrays.sort(return_value);
      i = number_of_points;
      while (return_value[i-1] > max_pt) {
        i--;
        assert (i > 0);
      }
    }
  }

  /**
   * <code>NSpace</code> instances also provide dimension information.
   */
  public abstract static class NSpace extends PointData {
    /**
     * Dimension of the space.
     * <p>
     *
     * @return     dimension of the space.
     */    
    abstract public int dimension();
    abstract public double coordinate(int p, int i);
    public double xi(int p, int i) {
      return coordinate(p, i-1);
    }
  }

  public static final class Discrete extends PointData {
    private final int N;

    private static class DSS extends SimplexStream {
      private SimplexStream ss;
      private int max_dimension;
      private int num_pts;

      public boolean hasNext() {
        return ss.hasNext();
      }

      public Simplex next() {
        return ss.next();
      }

      public int size() {
        return ss.size();
      }

      public int maxDimension() {
        return max_dimension;
      }

      /**
       * Make an iterator for the stream.
       * <p>
       *
       * @return  Iterator<Simplex> instance for the stream.
       *
       * @see        java.util.Iterator
       */
      public Iterator<Simplex> iterator() {
        return ss.iterator();
      }

      private DSS() {
        ss = null;
        max_dimension = 0;
      }

      public DSS(SimplexStream s, int n, int max_d) {
        ss = s;
        num_pts = n;
        max_dimension = max_d;
      }

      /**
       * Take the product of 2 DSS streams and return a third.
       * <p>
       *
       * @return     product stream.
       */    
      public DSS times(DSS s) {
        int new_num = this.num_pts * s.num_pts;
        int new_max_dim = this.max_dimension * s.max_dimension;
        SimplexStream.Stack stack = new SimplexStream.Stack(1, new_max_dim);
        for(Simplex first : this.ss) {
          for(Simplex second : s.ss) {
          }
        }
        return null;
      }
    }

    /**
     * Distance between 2 points.
     * <p>
     *
     * @param      p1   index of the first point
     * @param      p2   index of the second point
     * @return     distance between the two points.
     */    
    public double distance(int p1, int p2) {
      if (p1 == p2)
        return 0.0;
      else 
        return 1.0;
    }

    /**
     * The number of points in this data set. 
     * 
     * <p> Indexing begins from 1, not 0, which means that the points go
     * from 1 to count(), inclusive. It is acceptable for an implementation
     * to allow points to be added.
     *
     * <p>
     *
     * @return     The number of points in the data set.
     */    
    public int count() {
      return N;
    }

    private Discrete() {
      N = 0;
    }

    /**
     * Make a PointData.Discrete with N points.
     * <p>
     *
     * @param    n The number of points in the discrete set.
     */
    public Discrete(int n) {
      N = n;
    }



    /**
     * Make an instance of the boundary of the canonical (D+1)-Simplex.
     * <p>
     *
     * @param    d The dimension of the "sphere-like" simplicial complex.
     */
    public static SimplexStream DSphere(int d) {
      PointData p = new Discrete(d+2);
      RipsStream r = new RipsStream(2.0, d, 2.0, p);
      assert r.verify();
      return (SimplexStream) new DSS(r, d+2, d+1);
    }


    public static SimplexStream DSphereKskeleton(int d, int k) {
      PointData p = new Discrete(d+2);
      RipsStream r = new RipsStream(2.0, k, 2.0, p);
      assert r.verify(); 
      return (SimplexStream) new DSS(r, d+2, k+1);
    }
  }

  /**
   * <code>Predicate</code> instances are used weed out points that are
   * uninteresting in a given context (e.g., density too low or high).
   */
  public static abstract class Predicate {
 
    /**
     * Underlying PointData for the predicate.
     * <p>
     *
     * @return A PointData instance.
     *
     */
    abstract public PointData data();

    /**
     * Is the point i interesting to this predicate?
     *
     * <p>
     *
     * @param      i  The index of the point being considered.
     * @return True if we care about the point here, else false.
     *
     */
    abstract public boolean use(int i);
  }
}

