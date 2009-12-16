// 
//  TmpStream.java
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
//  Temporary test file.
// 
//  $Id$
// 

package edu.stanford.math.plex;
import java.util.*;

/**
 * The <code>TmpStream</code> class. Test code. The <code>next()</code>
 * methods for instances of this class do <em>not</em> remove simplices
 * from the stream.
 *
 * @version $Id$
 */
public class TmpStream extends SimplexStream implements Iterable<Simplex>, Iterator<Simplex> {

  final Simplex[] simplices;
  private int current_index;
  private int end_index;
  private int max_dimension;

  // explicitly construct the example witness complex from the paper by
  // A. Zomorodian and G. Carlsson, "Computing persistent homology,"
  // <i>Discrete and Computational Geometry</i>, 33 (2), pp. 247-274.
  static Simplex[] PaperComplex() {
    Simplex[] return_value = new Simplex[11];
    int current = 0;
  
    return_value[current++] = Simplex.getSimplex(new int[] {1}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2}, 0);
      
    return_value[current++] = Simplex.getSimplex(new int[] {3}, 1);
    return_value[current++] = Simplex.getSimplex(new int[] {4}, 1);
    return_value[current++] = Simplex.getSimplex(new int[] {1, 2}, 1);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 3}, 1);

    return_value[current++] = Simplex.getSimplex(new int[] {3, 4}, 2);
    return_value[current++] = Simplex.getSimplex(new int[] {1, 4}, 2);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 3}, 3);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 2, 3}, 4);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 3, 4}, 5);
   
    return return_value;
  }

  // explicitly make a 2-d Torus.
  private static Simplex[] TorusComplex() {
    Simplex[] return_value = new Simplex[54];
    int current = 0;
   
    return_value[current++] = Simplex.getSimplex(new int[] {1}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {3}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {4}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {6}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {7}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {9}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {1, 3}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 3}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 4}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 5}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {3, 6}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 6}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 4}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {3, 5}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {4, 5}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {4, 6}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5, 6}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {4, 7}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {4, 9}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5, 7}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5, 8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {6, 8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {6, 9}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {7, 9}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8, 9}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {7, 1}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {7, 3}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {7, 8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8, 1}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8, 2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {9, 2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {9, 3}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {1, 2, 4}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 4, 5}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {2, 3, 5}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {3, 5, 6}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {1, 4, 6}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {1, 3, 6}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {4, 5, 7}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5, 7, 8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {5, 6, 8}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {6, 8, 9}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {4, 7, 9}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {4, 6, 9}, 0);

    return_value[current++] = Simplex.getSimplex(new int[] {7, 8, 1}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8, 1, 2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {8, 9, 2}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {9, 2, 3}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {7, 1, 3}, 0);
    return_value[current++] = Simplex.getSimplex(new int[] {7, 9, 3}, 0);

    assert current == return_value.length;
    return return_value;
  }


  // don't use
  private TmpStream() { simplices = null; }
   
  public TmpStream(Simplex[] entries) {
    simplices = entries;
    current_index = 0;
    {
      int max_d = 0;
      int index = 0;
      Simplex s;
      while((index < simplices.length) && ((s = simplices[index++]) != null)) {
        if (max_d < s.dimension())
          max_d = s.dimension();
        max_dimension = max_d + 1;
      }
      end_index = index;
    }
  }

  /**
   * Returns <tt>true</tt> if the stream has more simplices.
   *
   * @return <tt>true</tt> if the stream has more simplices, else
   * <tt>false</tt>.
   */
  public boolean hasNext() {
    return (current_index < end_index);
  }


  /**
   * Returns the next Simplex in the iteration of the Stack.
   * Used in concert with the {@link #hasNext()} method returns
   * return each Simplex in the Stack exactly once.
   * <p>
   * @return the next Simplex in the Stack.
   * @exception NoSuchElementException Stack has no more elements.
   */
  public Simplex next() {
    if (current_index < end_index) 
      return simplices[current_index++];
    else
      throw new NoSuchElementException();
  }

  /**
   * Unsupported remove() operation.
   *
   * @exception UnsupportedOperationException 
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }

  public int size() {
    return (end_index - current_index);
  }

  public int maxDimension() {
    return max_dimension;
  }

  public Iterator<Simplex> iterator() {
    return (Iterator<Simplex>) new TmpStream(simplices);
  }

  public static TmpStream PaperTestCase() {
    return new TmpStream(PaperComplex());
  }
  public static TmpStream Torus() {
    return new TmpStream(TorusComplex());
  }
}
