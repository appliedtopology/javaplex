package edu.stanford.math.plex;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;


/**
 * The <code>RDirectBufferData</code> is a mechanism for transforming an R
 * distance matrix into a PointData.
 *
 * @version $Id$
 */
public class RDirectBufferData extends PointData {
  
  /**
   * Max number of points.
   */
  protected final int p_max;

  protected final DoubleBuffer distanceBuffer;
		  
  /**
   * Use a pre-allocated buffer of doubles as the distance matrix.
   * The ByteBuffer passed to the constructor will be viewed as a
   * DoubleBuffer, which will let us use the capacity function to get the
   * max entry. 
   *
   * <p>
   *
   * @param count The upper bound on i. i is 1-indexed
   * @param buf ByteBuffer instance to be used.
   *
   */
  public RDirectBufferData(int count, ByteBuffer buf) {
    buf = buf.order(ByteOrder.nativeOrder());
    distanceBuffer = buf.asDoubleBuffer();
    p_max = count;
    if(count > Math.sqrt(distanceBuffer.capacity()))
      throw new IllegalArgumentException
        ("Specified count, " + count + ", exceeds capacity of data, " +
         (int)Math.sqrt(distanceBuffer.capacity()) + ".");
  }

  /**
   * Return the distance between the two points using the external
   * data, which is stored as 0-based columns.
   *
   * <p>
   *
   * @param     p1   the first point
   * @param     p2   the second point
   * @return    the distance between p1 and p2
   */
  public double distance(int p1, int p2) {
    if(p1 == p2)
      return 0.0;
    return distanceBuffer.get(p_max*(p2-1) + (p1-1));
  }

  public int count() {
    return p_max;
  }
}
