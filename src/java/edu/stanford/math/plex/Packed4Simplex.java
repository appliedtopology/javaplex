// 
//  Packed4Simplex.java
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
//  Larger form of Simplex. Can be used for H2 calculations.
// 
//  $Id$
// 

package edu.stanford.math.plex;

/**
 * <p>The <code>Packed4Simplex</code> class implements the abstract class
 * <code>Simplex</code> for Simplices of dimension at most 3 by storing the
 * vertices as 4 packed positive integer values in a pair of long
 * ints. This limits us to H2 calculations with this representation, but
 * this is the next most important case, and having a more compact
 * representation for this case makes it worthwhile.
 *
 * @version $Id$
 */
public final class Packed4Simplex extends Simplex {

  public final long bits_lo;
  public final long bits_hi;

  // This is the largest vertex index that we can use for this kind of
  // Simplex.
  private static final int VERTEX_BIT_SZ = 32;
  private static final int MAX_4_INDEX = Integer.MAX_VALUE;
  private static final long DIM_2_MASK = ~((long) MAX_4_INDEX);

  static boolean checkConstants() {
    assert(MAX_4_INDEX == 0x7fffffff);
    assert(DIM_2_MASK == 0xffffffff80000000L);
    return true;
  }

  // Extract smallest vertex.
  private final int v1() {
    return (int) (bits_lo & MAX_4_INDEX);
  }

  // Extract second vertex.
  private final int v2() {
    return (int) ((bits_lo >>> VERTEX_BIT_SZ) & MAX_4_INDEX);
  }

  // Extract third vertex.
  private final int v3() {
    return (int) (bits_hi & MAX_4_INDEX);
  }

  // Extract largest vertex.
  private final int v4() {
    return (int) ((bits_hi >>> VERTEX_BIT_SZ) & MAX_4_INDEX);
  }

  // We only use the constructors for Packed4Simplex internally. This one
  // is never used.
  private Packed4Simplex() {
    bits_lo = 0;
    bits_hi = 0;
  }

  // We only use the constructors for Packed4Simplex internally. 
  private Packed4Simplex(long bits_lo, long bits_hi) {
    this.bits_lo = bits_lo;
    this.bits_hi = bits_hi;
  }

  private static final boolean check_4_vertices(int[] v) {
    if (v.length <= 2)
      return false;
    else if ((v.length == 3) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]))
      return true;
    else if ((v.length == 4) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]) && 
             (v[3] > v[2]))
      return true;
    else
      return false;
  }

  private static final void assert_4_vertices(int[] v) {
    if (!check_4_vertices(v)) {
      throw new 
        IllegalArgumentException
        ("Packed4Simplex instances must have either 3 or 4 " + 
         "distinct positive integer vertices.");
    }
  }

  // In the explicit constructors, the indices must all be non-zero and
  // be in increasing order.
  private Packed4Simplex(int v1, int v2, int v3) {
    assert((v3 > v2) && (v2 > v1) && (v1 > 0));
    bits_lo = ((((long)v2) << VERTEX_BIT_SZ) | ((long)v1));
    bits_hi = (long)v3;
  }
  // Boundary method in Packed8Simplex needs this constructor.
  Packed4Simplex(int v1, int v2, int v3, int v4) {
    assert((v4 > v3) && (v3 > v2) && (v2 > v1) && (v1 > 0));
    bits_lo = ((((long)v2) << VERTEX_BIT_SZ) | ((long)v1));
    bits_hi = ((((long)v4) << VERTEX_BIT_SZ) | ((long)v3));
  }


  /**
   * Overrides Object hashcode.
   *
   * <p>
   * @return     CRC hash of the vertex set.
   *
   */
  public final int hashCode() {
    return CRC.hash32(bits_hi, CRC.hash32(bits_lo, 0));
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
    if (!(obj instanceof Packed4Simplex))
      return false;
    Packed4Simplex s = (Packed4Simplex) obj;
    return ((bits_lo == s.bits_lo) && (bits_hi == s.bits_hi));
  }

  // Internal comparison routine.
  private final int compareTo(Packed4Simplex s) {
    if (bits_hi > s.bits_hi)
      return 1;
    else if (bits_hi < s.bits_hi)
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
    if (!(s instanceof Packed4Simplex))
      return (this.dimension() - s.dimension());
    else 
      return this.compareTo((Packed4Simplex)s);
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
    else if (s instanceof Packed4Simplex) {
      Packed4Simplex ps = (Packed4Simplex) s;
      if (bits_hi < ps.bits_hi)
        return true;
      else if (bits_hi == ps.bits_hi)
        return (bits_lo < ps.bits_lo);
      else
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
    if (!(s instanceof Packed4Simplex))
      return false;
    Packed4Simplex ps = (Packed4Simplex) s;
    return ((bits_lo == ps.bits_lo) && (bits_hi == ps.bits_hi));
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
    return new Packed4Simplex(bits_lo, bits_hi);
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
    if ((bits_hi & DIM_2_MASK) == 0) 
      return 2;
    else
      return 3;
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
    int dim = dimension();
    int verts[] = new int[dimension()+1];
    if (dim == 2) {
      verts[0] = this.v1();
      verts[1] = this.v2();
      verts[2] = this.v3();
      return verts;
    }
    verts[0] = this.v1();
    verts[1] = this.v2();
    verts[2] = this.v3();
    verts[3] = this.v4();
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
    int dim = dimension();
    if (dim == 2) {
      verts[0] = this.v1();
      verts[1] = this.v2();
      verts[2] = this.v3();
      return verts;
    }
    verts[0] = this.v1();
    verts[1] = this.v2();
    verts[2] = this.v3();
    verts[3] = this.v4();
    return verts;
  }

  // The obvious thing.
  public static final Simplex makeSimplex(int[] vertices) {
    Simplex.vertex_sort(vertices);
    assert_4_vertices(vertices);
    if (vertices.length == 3) 
      return new Packed4Simplex(vertices[0], vertices[1], vertices[2]);
    else
      return new Packed4Simplex(vertices[0], vertices[1], 
                                vertices[2], vertices[3]);
  }

  // Another obvious thing.
  public static final Simplex makeSimplexPresorted(int[] vertices) {
    assert(check_4_vertices(vertices));
    if (vertices.length == 3) 
      return new Packed4Simplex(vertices[0], vertices[1], vertices[2]);
    else
      return new Packed4Simplex(vertices[0], vertices[1], 
                                vertices[2], vertices[3]);
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
    Simplex[] return_value;
    if (dimension == 2) {
      return_value = new Simplex[3];
      return_value[0] = new Packed2Simplex(this.v2(), this.v3());
      return_value[1] = new Packed2Simplex(this.v1(), this.v3());
      return_value[2] = new Packed2Simplex(this.v1(), this.v2());
    } else {
      return_value = new Simplex[4];
      return_value[0] = 
        new Packed4Simplex(this.v2(), this.v3(), this.v4());
      return_value[1] = 
        new Packed4Simplex(this.v1(), this.v3(), this.v4());
      return_value[2] = 
        new Packed4Simplex(this.v1(), this.v2(), this.v4());
      return_value[3] = 
        new Packed4Simplex(this.v1(), this.v2(), this.v3());
    }
    return return_value;
  }
}
