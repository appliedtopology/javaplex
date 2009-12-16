// 
//  PlexSort.java
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
//  Generic sorting routines.
// 
//  $Id$
// 

package edu.stanford.math.plex;

/**
 * The <code>PlexSort</code> class provides a sorting routine that I'd prefer
 * was part of the standard libraries.
 *
 * @version $Id$
 */
public class PlexSort {

  /**
   * Instances of the <code>Comp</code> inner class are used as comparison
   * closures for the sorting routine <code>comp_sort</code>. In addition
   * to the obvious uses, such as sorting in decreasing order, using
   * closures allows us to sort arrays of points by increasing or
   * decreasing density (or any other real-valued function on the points).
   * There are also some slightly non-obvious applications, which we
   * probably won't use here, such a computing the inverse of a permutation
   * stored in an array p[]. If p is [1,2,0] -- that is, the permutation
   * mapping 0->1, 1->2, 2->0, then let x = [0,1,2], and sort the entries
   * of x[i] so that x[i] is less than x[j] iff p[x[i]] < p[x[j]]. Then the
   * sorted contents of x are [2,0,1]. This is the inverse of p[].
   *
   */
  public static abstract class Comp {
    /**
     * Compare two int arguments, and return an integer less than, equal
     * to, or greater than 0, depending on whether i is respectively less,
     * equivalent, or greater than, j.
     *
     * @param      i The first argument.
     * @param      j The second argument.
     * @return     an int that is -/0/+ -- intuitively, return (i-j) for 
     *             an increasing sort.
     */
    public abstract int fn(int i, int j);
  }

  public static abstract class CompL {
    /**
     * Compare two long arguments, and return an integer less than, equal
     * to, or greater than 0, depending on whether i is respectively less,
     * equivalent, or greater than, j.
     *
     * @param      i The first argument.
     * @param      j The second argument.
     * @return     an int that is -/0/+ -- intuitively, return (i-j) for 
     *             an increasing sort.
     */
    public abstract int fn(long i, long j);
  }

  public static abstract class CompObj {
    /**
     * Compare two object arguments, and return an integer less than, equal
     * to, or greater than 0, depending on whether i is respectively less,
     * equivalent, or greater than, j.
     *
     * @param      o1 The first argument.
     * @param      o2 The second argument.
     * @return     an int that is -/0/+ -- intuitively, return (i-j) for 
     *             an increasing sort.
     */
    public abstract int fn(Object o1 , Object o2);
  }

  // Exchange contents of x[i] and x[j].
  private static final void swap(int[] x, int i, int j) {
    int tmp = x[i];
    x[i] = x[j];
    x[j] = tmp;
  }

  // Returns the index of the median of the three argument entries.
  private static final int med3(int[] x, int a, int b, int c, Comp comp) {
    int xa = x[a];
    int xb = x[b];
    int xc = x[c];
    return ((comp.fn(xa, xb) < 0)?
            ((comp.fn(xb, xc) < 0)?b:((comp.fn(xa, xc)<0)?c:a)):
            ((comp.fn(xb, xc) > 0)?b:((comp.fn(xa, xc)>0)?c:a)));
  }

  // Exchange subintervals [i, i+(len-1)] and [j, j+(len-1)] in x[]; 
  private static final void swapfunc(int[] x, int i, int j, int len) {
    int stop = j + len;
    while (j < stop) {
      int tmp = x[i];
      x[i] = x[j];
      x[j] = tmp;
      i++;
      j++;
    }
  }

  // Exchange contents of x[i] and x[j].
  private static final void swap(long[] x, int i, int j) {
    long tmp = x[i];
    x[i] = x[j];
    x[j] = tmp;
  }

  // Returns the index of the median of the three argument entries.
  private static final int med3(long[] x, int a, int b, int c, CompL comp) {
    long xa = x[a];
    long xb = x[b];
    long xc = x[c];
    return ((comp.fn(xa, xb) < 0)?
            ((comp.fn(xb, xc) < 0)?b:((comp.fn(xa, xc)<0)?c:a)):
            ((comp.fn(xb, xc) > 0)?b:((comp.fn(xa, xc)>0)?c:a)));
  }

  // Exchange subintervals [i, i+(len-1)] and [j, j+(len-1)] in x[]; 
  private static final void swapfunc(long[] x, int i, int j, int len) {
    int stop = j + len;
    while (j < stop) {
      long tmp = x[i];
      x[i] = x[j];
      x[j] = tmp;
      i++;
      j++;
    }
  }

  // Exchange contents of x[i] and x[j].
  private static final void swap(Object[] x, int i, int j) {
    Object tmp = x[i];
    x[i] = x[j];
    x[j] = tmp;
  }

  // Returns the index of the median of the three argument entries.
  private static final int med3(Object[] x, int a, int b, int c, CompObj comp) {
    Object xa = x[a];
    Object xb = x[b];
    Object xc = x[c];
    return ((comp.fn(xa, xb) < 0)?
            ((comp.fn(xb, xc) < 0)?b:((comp.fn(xa, xc)<0)?c:a)):
            ((comp.fn(xb, xc) > 0)?b:((comp.fn(xa, xc)>0)?c:a)));
  }

  // Exchange subintervals [i, i+(len-1)] and [j, j+(len-1)] in x[]; 
  private static final void swapfunc(Object[] x, int i, int j, int len) {
    int stop = j + len;
    while (j < stop) {
      Object tmp = x[i];
      x[i] = x[j];
      x[j] = tmp;
      i++;
      j++;
    }
  }

  /**
   * Sort a subarray of an int[] using a comparison function. The sort is
   * not stable, but it uses no extra storage, and seems to be faster in
   * practice then even insertion sort, although it doesn't have that
   * algorithm's guarantee of optimal worst-case behavior. <p>
   *
   * This code is copied with trivial alterations from "Engineering a Sort
   * Function", by Bentley and McIlroy. I have no idea why there isn't
   * something like this already available in the class java.util.Arrays --
   * apparently the sort routines in that class are based on the same
   * paper, but support for a comparison function has been removed.
   *
   * @param      x int[] to sort.
   * @param      start Beginning of subarray to sort.
   * @param      len Length of the subarray of x to sort.
   * @param      comp PlexSort.Comp instance to use in sorting.
   *
   */
  public static final void comp_sort(int[] x, int start, int len, Comp comp) {

    if (len < 7) {
      // Use insertion sort on the smallest arrays.
	    for (int i = start; i< len + start; i++)
        for (int j = i; (j > start) && (comp.fn(x[j-1], x[j]) > 0); j--) {
          swap(x, j, j-1);
        }
	    return;
    }

    // Partition on the middle entry for small arrays.
    int pm = start + len/2;       
    if (len > 7) {
	    int p_bot = start;
	    int p_top = start + len - 1;
	    if (len > 40) {     
        // For big arrays, use the pseudomedian of 9 entries.
        int psm_incr = len/8;
        p_bot = med3(x, p_bot, p_bot+psm_incr, p_bot+(2*psm_incr), comp);
        pm = med3(x, pm-psm_incr, pm, pm+psm_incr, comp);
        p_top = med3(x, p_top-(2*psm_incr), p_top-psm_incr, p_top, comp);
	    }
      // For mid-size arrays, use the pseudomedian of 3 entries.
	    pm = med3(x, p_bot, pm, p_top, comp); 
    }
    int partition_value = x[pm];

    int pa = start, pb = pa;
    int pc = start + len - 1, pd = pc;
    while(true) {
      int comp_val;
	    while ((pb <= pc) && 
             ((comp_val = comp.fn(x[pb], partition_value)) <= 0)) {
        if (comp_val == 0) {
          swap(x, pa, pb);
          pa++;
        }
        pb++;
	    }
	    while ((pc >= pb) && 
             ((comp_val = comp.fn(x[pc], partition_value)) >= 0)) {
        if (comp_val == 0) {
          swap(x, pc, pd);
          pd--;
        }
        pc--;
	    }
      if (pb > pc)
        break;
	    swap(x, pb, pc);
      pb++;
      pc--;
    }

    int pn = start + len;
    int s;

    s = Math.min(pa-start, pb-pa);  
    swapfunc(x, start, pb-s, s);

    s = Math.min(pd-pc, pn-pd-1);  
    swapfunc(x, pb, pn-s, s);

    if ((s = pb-pa) > 1)
	    comp_sort(x, start, s, comp);
    if ((s = pd-pc) > 1)
	    comp_sort(x, pn-s, s, comp);
  }

  /**
   * Same as comp_sort(int[], int, int, Comp), but for arrays of long.
   */
  public static final void comp_sort(long[] x, int start, int len, CompL comp) {

    if (len < 7) {
      // Use insertion sort on the smallest arrays.
	    for (int i = start; i< len + start; i++)
        for (int j = i; (j > start) && (comp.fn(x[j-1], x[j]) > 0); j--) {
          swap(x, j, j-1);
        }
	    return;
    }

    // Partition on the middle entry for small arrays.
    int pm = start + len/2;       
    if (len > 7) {
	    int p_bot = start;
	    int p_top = start + len - 1;
	    if (len > 40) {     
        // For big arrays, use the pseudomedian of 9 entries.
        int psm_incr = len/8;
        p_bot = med3(x, p_bot, p_bot+psm_incr, p_bot+(2*psm_incr), comp);
        pm = med3(x, pm-psm_incr, pm, pm+psm_incr, comp);
        p_top = med3(x, p_top-(2*psm_incr), p_top-psm_incr, p_top, comp);
	    }
      // For mid-size arrays, use the pseudomedian of 3 entries.
	    pm = med3(x, p_bot, pm, p_top, comp); 
    }
    long partition_value = x[pm];

    int pa = start, pb = pa;
    int pc = start + len - 1, pd = pc;
    while(true) {
      int comp_val;
	    while ((pb <= pc) && 
             ((comp_val = comp.fn(x[pb], partition_value)) <= 0)) {
        if (comp_val == 0) {
          swap(x, pa, pb);
          pa++;
        }
        pb++;
	    }
	    while ((pc >= pb) && 
             ((comp_val = comp.fn(x[pc], partition_value)) >= 0)) {
        if (comp_val == 0) {
          swap(x, pc, pd);
          pd--;
        }
        pc--;
	    }
      if (pb > pc)
        break;
	    swap(x, pb, pc);
      pb++;
      pc--;
    }

    int pn = start + len;
    int s;

    s = Math.min(pa-start, pb-pa);  
    swapfunc(x, start, pb-s, s);

    s = Math.min(pd-pc, pn-pd-1);  
    swapfunc(x, pb, pn-s, s);

    if ((s = pb-pa) > 1)
	    comp_sort(x, start, s, comp);
    if ((s = pd-pc) > 1)
	    comp_sort(x, pn-s, s, comp);
  }

  /**
   * Same as comp_sort(int[], int, int, Comp), but for arrays of Object.
   */
  public static final void comp_sort(Object[] x, int start, int len, CompObj comp) {

    if (len < 7) {
      // Use insertion sort on the smallest arrays.
	    for (int i = start; i< len + start; i++)
        for (int j = i; (j > start) && (comp.fn(x[j-1], x[j]) > 0); j--) {
          swap(x, j, j-1);
        }
	    return;
    }

    // Partition on the middle entry for small arrays.
    int pm = start + len/2;       
    if (len > 7) {
	    int p_bot = start;
	    int p_top = start + len - 1;
	    if (len > 40) {     
        // For big arrays, use the pseudomedian of 9 entries.
        int psm_incr = len/8;
        p_bot = med3(x, p_bot, p_bot+psm_incr, p_bot+(2*psm_incr), comp);
        pm = med3(x, pm-psm_incr, pm, pm+psm_incr, comp);
        p_top = med3(x, p_top-(2*psm_incr), p_top-psm_incr, p_top, comp);
	    }
      // For mid-size arrays, use the pseudomedian of 3 entries.
	    pm = med3(x, p_bot, pm, p_top, comp); 
    }
    Object partition_value = x[pm];

    int pa = start, pb = pa;
    int pc = start + len - 1, pd = pc;
    while(true) {
      int comp_val;
	    while ((pb <= pc) && 
             ((comp_val = comp.fn(x[pb], partition_value)) <= 0)) {
        if (comp_val == 0) {
          swap(x, pa, pb);
          pa++;
        }
        pb++;
	    }
	    while ((pc >= pb) && 
             ((comp_val = comp.fn(x[pc], partition_value)) >= 0)) {
        if (comp_val == 0) {
          swap(x, pc, pd);
          pd--;
        }
        pc--;
	    }
      if (pb > pc)
        break;
	    swap(x, pb, pc);
      pb++;
      pc--;
    }

    int pn = start + len;
    int s;

    s = Math.min(pa-start, pb-pa);  
    swapfunc(x, start, pb-s, s);

    s = Math.min(pd-pc, pn-pd-1);  
    swapfunc(x, pb, pn-s, s);

    if ((s = pb-pa) > 1)
	    comp_sort(x, start, s, comp);
    if ((s = pd-pc) > 1)
	    comp_sort(x, pn-s, s, comp);
  }

  /**
   * Take an array of indices and sort them by the values in an array.
   * <p>
   * @param      indices An int[] of indices to be sorted.
   * @param      start The first entry in indices to be sort.
   * @param      end  The first entry in indices to not be sorted.
   * @param      vals An array of integer values against which to sort indices.
   * @param reverse A boolean which, if true, causes the indices to be
   * sorted into decreasing order.
   */
  public static void sort(int[] indices, int start, int end, 
                          final int[] vals, boolean reverse) {
    if (reverse) {
      comp_sort(indices, start, end-start, 
                new Comp() {
                  public int fn(int i, int j) { 
                    return (vals[j]-vals[i]);
                  }
                } );
    } else {
      comp_sort(indices, start, end-start, 
                new Comp() {
                  public int fn(int i, int j) { 
                    return (vals[i]-vals[j]);
                  }
                } );
    }
  }

  public static void sort(int[] indices, int start, int end, 
                          final double[] vals, boolean reverse) {
    if (reverse) {
      comp_sort(indices, start, end-start, 
                new Comp() {
                  public int fn(int i, int j) { 
                    if (vals[i] > vals[j])
                      return -1;
                    else if (vals[i] < vals[j])
                      return 1;
                    else
                      return 0; 
                  } 
                } );
    } else {
      comp_sort(indices, start, end-start, 
                new Comp() {
                  public int fn(int i, int j) { 
                    if (vals[i] < vals[j])
                      return -1;
                    else if (vals[i] > vals[j])
                      return 1;
                    else
                      return 0;
                  }
                } );
    }
  }

  public static void sort(int[] indices, final int[] vals, boolean reverse) {
    sort(indices, 0, indices.length, vals, reverse);
  }

  public static void sort(int[] indices, final int[] vals) {
    sort(indices, 0, indices.length, vals, false);
  }

  public static void reverse_sort(int[] indices, final int[] vals) {
    sort(indices, 0, indices.length, vals, true);
  }

  public static void sort(int[] indices, final double[] vals, boolean reverse) {
    sort(indices, 0, indices.length, vals, reverse);
  }

  public static void sort(int[] indices, final double[] vals) {
    sort(indices, 0, indices.length, vals, false);
  }

  public static void reverse_sort(int[] indices, final double[] vals) {
    sort(indices, 0, indices.length, vals, true);
  }


}
