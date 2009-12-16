// 
//  Packed6Simplex.java
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
//  Simplex implementation for H4 calculations.
// 
//  $Id$
// 

package edu.stanford.math.plex;

/**
 * <p>The <code>Packed6Simplex</code> class implements the abstract class
 * <code>Simplex</code> for Simplices of dimension 4-5 by storing the
 * vertices as 6 packed positive integer values in three long
 * integers. This limits us to H4 calculations with this representation,
 * but it is unlikely that we can go to higher dimensions without an
 * alteration of the basic algorithm.
 *
 * @version $Id$
 */
public final class Packed6Simplex extends Simplex {

  public final long bits_lo;
  public final long bits_mid;
  public final long bits_hi;

  // This is the largest vertex index that we can use for this kind of
  // Simplex.
  private static final int VERTEX_BIT_SZ = 32;
  private static final int MAX_6_INDEX = Integer.MAX_VALUE;
  private static final long DIM_4_MASK = ~((long) MAX_6_INDEX);

  static boolean checkConstants() {
    assert(MAX_6_INDEX == 0x7fffffff);
    assert(DIM_4_MASK == 0xffffffff80000000L);
    return true;
  }

  private static final boolean check_6_vertices(int[] v) {
    if (v.length <= 4)
      return false;
    else if ((v.length == 5) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]) &&
             (v[3] > v[2]) && (v[4] > v[3]))
      return true;
    else if ((v.length == 6) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]) &&
             (v[3] > v[2]) && (v[4] > v[3]) && (v[5] > v[4]))
      return true;
    else
      return false;
  }

  private static final void assert_6_vertices(int[] v) {
    if (!check_6_vertices(v))
      throw new 
        IllegalArgumentException
        ("Packed6Simplex instances must have either 5 or 6 " + 
         "distinct positive integer vertices.");
  }

  // Convert an array of vertices into the "bits_lo" of a
  // Packed6Simplex. Vertices MUST BE SORTED.
  private static final long p6_v_to_l_lo(int[] vertices) {
    return (((long)vertices[1]) << VERTEX_BIT_SZ) | ((long)vertices[0]);
  }

  // Convert an array of vertices into the "bits_mid" of a
  // Packed6Simplex. Vertices MUST BE SORTED.
  private static final long p6_v_to_l_mid(int[] vertices) {
    return (((long)vertices[3]) << VERTEX_BIT_SZ) | ((long)vertices[2]);
  }

  // Convert an array of vertices into the "bits_hi" of a
  // Packed6Simplex. Vertices MUST BE SORTED.
  private static final long p6_v_to_l_hi(int[] vertices) {
    if (vertices.length == 5)
      return (long) vertices[4];
    else 
      return (((long)vertices[5]) << VERTEX_BIT_SZ) | ((long)vertices[4]);
  }

  // Extract largest vertex.
  private final int v6() {
    return (int) ((bits_hi >>> VERTEX_BIT_SZ) & MAX_6_INDEX);
  }

  // Extract 5th vertex (second hi bits vertex).
  private final int v5() {
    return (int) (bits_hi & MAX_6_INDEX);
  }

  // Extract 4th vertex.
  private final int v4() {
    return (int) ((bits_mid >>> VERTEX_BIT_SZ) & MAX_6_INDEX);
  }

  // Extract 3rd vertex.
  private final int v3() {
    return (int) (bits_mid & MAX_6_INDEX);
  }

  // Extract 2nd vertex.
  private final int v2() {
    return (int) ((bits_lo >>> VERTEX_BIT_SZ) & MAX_6_INDEX);
  }

  // Extract smallest vertex.
  private final int v1() {
    return (int) (bits_lo & MAX_6_INDEX);
  }

  // We only use the constructors for Packed6Simplex internally. This one
  // is never used externally because none of bits_lo, bits_mid, or bits_hi
  // is ever 0.
  private Packed6Simplex() {
    bits_lo = 0;
    bits_mid = 0;
    bits_hi = 0;
  }

  // We only use the constructors for Packed6Simplex internally. 
  private Packed6Simplex(long bits_lo_val, long bits_mid_val, long bits_hi_val) {
    bits_lo = bits_lo_val;
    bits_mid = bits_mid_val;
    bits_hi = bits_hi_val;
  }

  // In the explicit constructors, the indices must all be non-zero and
  // be in increasing order.
  Packed6Simplex(int v1, int v2, int v3, int v4, int v5) {
    assert((v5 > v4) && (v4 > v3) && (v3 > v2) && (v2 > v1) && (v1 > 0));
    bits_hi = (long) v5;
    bits_mid = (((long) v4) << VERTEX_BIT_SZ) | ((long) v3);
    bits_lo = (((long) v2) << VERTEX_BIT_SZ) | ((long) v1);
  }
  Packed6Simplex(int v1, int v2, int v3, int v4, int v5, int v6) {
    assert((v6 > v5) && (v5 > v4) && (v4 > v3) && 
           (v3 > v2) && (v2 > v1) && (v1 > 0));
    bits_hi = (((long) v6) << VERTEX_BIT_SZ) | ((long) v5);
    bits_mid = (((long) v4) << VERTEX_BIT_SZ) | ((long) v3);
    bits_lo = (((long) v2) << VERTEX_BIT_SZ) | ((long) v1);
  }


  /**
   * Overrides Object hashcode.
   *
   * <p>
   * @return     CRC hash of the vertex set.
   *
   */
  public final int hashCode() {
    return CRC.hash32(bits_hi, CRC.hash32(bits_mid, CRC.hash32(bits_lo, 0)));
  }

  /**
   * Overrides Object equals.
   *
   * <p>
   * @param      obj   object to compare.
   * @return true or false, depending on whether or not the Simplex is =
   *                        to obj.
   *
   */
  public final boolean equals(Object obj) {
    if (!(obj instanceof Packed6Simplex))
      return false;
    Packed6Simplex s = (Packed6Simplex) obj;
    return ((bits_lo == s.bits_lo) && (bits_mid == s.bits_mid) && (bits_hi == s.bits_hi));
  }

  // Internal comparison routine.
  private final int compareTo(Packed6Simplex s) {
    if (bits_hi > s.bits_hi)
      return 1;
    else if (bits_hi < s.bits_hi)
      return -1;
    else if (bits_mid > s.bits_mid)
      return 1;
    else if (bits_mid < s.bits_mid)
      return -1;
    else if (bits_lo > s.bits_lo)
      return 1;
    else if (bits_lo < s.bits_lo)
      return -1;
    else
      return 0;
  }

  /**
   * Implements Comparable interface.
   *
   * <p>
   * @param      s   Simplex to compare.
   * @return     negative, 0, or positive, if this <, =, resp. > than s.
   *
   */
  public final int compareTo(Simplex s) {
    if (!(s instanceof Packed6Simplex))
      return (this.dimension() - s.dimension());
    else 
      return this.compareTo((Packed6Simplex)s);
  }

  /**
   * Simplex Less Than. Used internally in Persistence and Chain
   * code. Not the same as compareTo.
   *
   * <p>
   * @param      s   Simplex to compare.
   * @return     true if this < s, else false.
   *
   */
  final boolean slt(Simplex s) {
    if (this.findex() != s.findex())
      return (this.findex() < s.findex());
    else if (s instanceof Packed6Simplex) {
      Packed6Simplex ps = (Packed6Simplex) s;
      if (bits_hi < ps.bits_hi)
        return true;
      else if (bits_hi == ps.bits_hi) {
        if (bits_mid < ps.bits_mid)
          return true;
        else if (bits_mid == ps.bits_mid)
          return (bits_lo < ps.bits_lo);
      }
      return false;
    } else 
      return (this.dimension() < s.dimension());
  }

  /**
   * Simplex EQuals. Used internally in Persistence and Chain code.  
   *
   * <p>
   * @param      s   Simplex to compare.
   * @return     true is equal, else false.
   *
   */
  final boolean seq(Simplex s) {
    if (!(s instanceof Packed6Simplex))
      return false;
    Packed6Simplex ps = (Packed6Simplex) s;
    return ((bits_lo == ps.bits_lo) && (bits_mid == ps.bits_mid) && (bits_hi == ps.bits_hi));
  }

  /**
   * Make a "blank" copy of the Simplex -- equivalent to getSimplex(this.vertices()).
   * <p>
   *
   *
   * @return     the copied instance.
   *
   */    
  public final Simplex copy() {
    return new Packed6Simplex(bits_lo, bits_mid, bits_hi);
  }

  /**
   * Returns the dimension of self.
   * <p>
   *
   *
   * @return     int, the dimension of the simplex.
   *
   * @see        edu.stanford.math.plex.Simplex#dimension
   */    
  public final int dimension() { 
    if ((bits_hi & DIM_4_MASK) == 0) 
      return 4;
    else
      return 5;
  }

  /**
   * Returns the indices of self as an array.
   * <p>
   *
   *
   * @return     an int[] of vertices, or null if no vertices.
   *
   * @see        edu.stanford.math.plex.Simplex#vertices
   */    
  public final int[] vertices() { 
    final int dimension = dimension();
    int verts[] = new int[dimension+1];

    verts[0] = this.v1();
    verts[1] = this.v2();
    verts[2] = this.v3();
    verts[3] = this.v4();

    if (dimension == 4) {
      verts[4] = this.v5();
      return verts;
    }

    verts[4] = this.v5();
    verts[5] = this.v6();
    return verts;
  }


  /**
   * Returns the indices of self in the given array argument.
   * <p>
   *
   *
   * @param      verts   the int[] into which the vertices, if any, are written.
   * @return     the given array argument, or null if no vertices.
   *
   * @see        edu.stanford.math.plex.Simplex#vertices
   */
  public final int[] vertices(int[] verts) { 
    final int dimension = dimension();

    verts[0] = this.v1();
    verts[1] = this.v2();
    verts[2] = this.v3();
    verts[3] = this.v4();

    if (dimension == 4) {
      verts[4] = this.v5();
      return verts;
    }

    verts[4] = this.v5();
    verts[5] = this.v6();
    return verts;
  }

  // The obvious thing.
  public static final Simplex makeSimplex(int[] vertices) {
    Simplex.vertex_sort(vertices);
    assert_6_vertices(vertices);
    return new Packed6Simplex(p6_v_to_l_lo(vertices), 
                              p6_v_to_l_mid(vertices), 
                              p6_v_to_l_hi(vertices));
  }

  // The other obvious thing.
  public static final Simplex makeSimplexPresorted(int[] vertices) {
    assert_6_vertices(vertices);
    return new Packed6Simplex(p6_v_to_l_lo(vertices), 
                              p6_v_to_l_mid(vertices), 
                              p6_v_to_l_hi(vertices));
  }

  /**
   * Returns the boundary of self.  
   *
   * <p> It is simple enough to do explicitly, and it needs to be fast.
   *
   *
   * @return     [face0, face1, ...]
   *
   * @see        edu.stanford.math.plex.Simplex#vertices
   */
  public final Simplex[] boundaryArray() {
    final int dimension = dimension();
    Simplex[] return_value = new Simplex[dimension+1];

    if (dimension == 4) {
      return_value[0] = new Packed4Simplex(this.v2(), this.v3(), 
                                           this.v4(), this.v5());
      return_value[1] = new Packed4Simplex(this.v1(), this.v3(), 
                                           this.v4(), this.v5());
      return_value[2] = new Packed4Simplex(this.v1(), this.v2(), 
                                           this.v4(), this.v5());
      return_value[3] = new Packed4Simplex(this.v1(), this.v2(), 
                                           this.v3(), this.v5());
      return_value[4] = new Packed4Simplex(this.v1(), this.v2(), 
                                           this.v3(), this.v4());
    } else {
      return_value[0] = new Packed6Simplex(this.v2(), this.v3(), this.v4(), 
                                           this.v5(), this.v6());
      return_value[1] = new Packed6Simplex(this.v1(), this.v3(), this.v4(), 
                                           this.v5(), this.v6());
      return_value[2] = new Packed6Simplex(this.v1(), this.v2(), this.v4(), 
                                           this.v5(), this.v6());
      return_value[3] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
                                           this.v5(), this.v6());
      return_value[4] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
                                           this.v4(), this.v6());
      return_value[5] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
                                           this.v4(), this.v5());
    } 
    return return_value;
  }
}
