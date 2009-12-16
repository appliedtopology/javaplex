// 
//  MappedBufferData.java
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
import java.nio.*;
import java.nio.channels.*;
import java.io.*;
import java.text.*;


/**
 * The <code>MappedBufferData</code> class is another very simple implementation
 * of NSpace. Intended for larger, but still mappable, data sets.
 *
 * @version $Id$
 */

public final class MappedBufferData extends PointData.NSpace {

  final MappedByteBuffer points_buf;
  final DoubleBuffer points;
  final int dimensions;
  final int start_dimension;
  final int stop_dimension;

  /**
   * The number of dimensions of the space.
   *
   * <p>
   *
   * @return     the dimension of the data.
   */
  public final int dimension() {
    return dimensions;
  }
   
  /**
   * The first data dimension of the space.
   *
   * <p>
   *
   * @return     the first data dimension of the space.
   */
  public final int start() {
    return start_dimension;
  }
   
  /**
   * The last data dimension of the space.
   *
   * <p>
   *
   * @return     the last data dimension of the space.
   */
  public final int stop() {
    return stop_dimension;
  }
   
  /**
   * The number of data points.
   *
   * <p>
   *
   * @return     the number of points
   */
  public final int count() {
    if (points != null)
      return ((points.capacity()/dimensions)-1);
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
   * @return     the Euclidean distance between p1 and p2.
   */
  public final double distance (int p1, int p2) {
    if (p1 == p2)
      return 0.0;
    else if ((p1 == 0) || (p2 == 0))
      return Double.MAX_VALUE;
    else {
      int p1_base = dimensions * p1;
      int p2_base = dimensions * p2;
      double accum = 0.0;
   
      for (int i = start_dimension; i <= stop_dimension; i++) {
        double diff = points.get(p1_base + i) - points.get(p2_base + i);
        accum += diff * diff;
      }

      return Math.sqrt(accum);
    }
  }

  /**
   * Get point. No checking.
   *
   * <p>
   *
   * @param      p   the point
   * @param      vec   place to store point
   * @return     Coordinates of p;
   */
  public final double[] get_pt (int p, double[] vec) {
    assert(dimensions == vec.length);
    for (int i = 0; i < dimensions; i++) {
      vec[i] = points.get((dimensions * p)+i);
      //assert(vec[i] == coordinate(p, i));
    }
    return vec;
  }

  /**
   * Set point. No checking. Just for testing.
   *
   * <p>
   *
   * @param      p   the point
   * @param      vec   the coordinate values
   * @return     The coordinates of p.
   */
  public final double[] set_pt (int p, double[] vec) {
    assert(dimensions == vec.length);
    for (int i = 0; i < dimensions; i++) {
      points.put((dimensions * p)+i, vec[i]);
      //assert(points.get((dimensions * p)+i) == vec[i]);
    }
    return vec;
  }

  /**
   * Get coordinates. Not much checking.
   *
   * <p>
   *
   * @param      p   the point
   * @param      i   the coordinate
   * @return     The i-th coordinate of p;
   */
  public final double coordinate (int p, int i) {
    if ((i >= dimensions) || (i < 0))
      throw new IllegalArgumentException("Coordinate index " + i + 
                                         "must be in range [0, " + 
                                         (dimensions-1) + "].");
    return points.get((dimensions * p) + i);
  }

  /**
   * Set coordinates. No checking.
   *
   * <p>
   *
   * @param      p   the point
   * @param      i   the coordinate
   * @param      v   the coordinate value
   * @return     The i-th coordinate of p;
   */
  public final double set_coordinate (int p, int i, double v) {
    // really shouldn't be here
    assert(false);
    if ((i >= dimensions) || (i < 0))
      throw new IllegalArgumentException("Coordinate index " + i + 
                                         "must be in range [0, " + 
                                         (dimensions-1) + "].");
    points.put((dimensions * p) + i, v);
    //assert(points.get((dimensions * p)+i) == v);
    return v;
  }

  /**
   * Force changes to data back to file. 
   *
   * <p>
   */
  public final void force () {
    points_buf.force();
  }



  // Don't call this one.
  private MappedBufferData() {
    super();
    points_buf = null;
    points = null;
    dimensions = 0;
    start_dimension = 0;
    stop_dimension = 0;
  }

  // Does a file exist?
  private static boolean file_exists(String filename, boolean andWriteable) {
    File file = new File(filename);
    if ((file != null) && file.exists() && file.canRead()) {
      if (andWriteable) {
        return file.canWrite();
      } else
        return true;
    } else {
      return false;
    }
  }

  // Does a file exist?
  public static void delete(String filename) {
    File file = new File(filename);
    if ((file != null) && file.exists()) {
      file.delete();
    }
  }

  // Make an empty file of a given size suitable for mapping.
  private static void make_empty_file(String filename, int bytes) {
    try {
      FileOutputStream stream = new FileOutputStream(filename);
      byte[] tmp = new byte[1024 * 256];
      while(bytes > 0) {
        int len = (bytes > tmp.length)?tmp.length:bytes;
        stream.write(tmp, 0, len);
        bytes -= len;
      }
      stream.flush();
      stream.close();
    }
    catch (java.io.FileNotFoundException fnfe) {
      throw new IllegalArgumentException("Cannot open or create file: "
                                         + filename);
    } catch (java.io.IOException ioe) {
      throw new IllegalArgumentException("IO exception writing empty file: " 
                                         + filename);
    }
  }

  // Open the empty buffer file.
  private static MappedByteBuffer empty_dbl_file(String filename, int len) {
    if (file_exists(filename, false))
      throw new IllegalArgumentException("File already exists: " + filename);

    make_empty_file(filename, 8 * len);
    return map_file(filename, true);
  }

  // Map a file;
  private static MappedByteBuffer map_file(String filename, boolean writable) {
    try {
      FileChannel.MapMode mode = 
        (writable)?FileChannel.MapMode.READ_WRITE:FileChannel.MapMode.READ_ONLY;
      String open_mode = (writable)?"rw":"r";
      FileChannel chnl = new RandomAccessFile(filename, open_mode).getChannel();
      MappedByteBuffer buf = chnl.map(mode, 0, (int) chnl.size());
      buf.load();
      return buf;
    } catch (java.io.FileNotFoundException fnfe) {
      throw new IllegalArgumentException("Cannot find data file: " + filename);
    } catch (java.io.IOException ioe) {
      throw new IllegalArgumentException("IO exception mapping data file: " 
                                         + filename);
    }
  }

  // Make sure that the buf and dimension arguments are compatible.
  private boolean buf_is_consistent(DoubleBuffer buf, int dimension, 
                                    boolean just_created) {
    if (dimension < 1)
      return false;
    if ((buf.capacity() % dimension) != 0)
      return false;
    if (buf.capacity() <= dimension)
      return false;
    if (!just_created && ((int)buf.get(0) != dimension))
      return false;
    for (int i = 1; i < dimension; i++) {
      if (buf.get(i) != 0.0)
        return false;
    }
    return true;
  }

  // create mapped data file 
  public MappedBufferData(String filename, int length, int dimension, 
                          int start_dimension, int stop_dimension) {
    if (file_exists(filename, false))
      throw new IllegalArgumentException("Data file already exists: " + filename);
    points_buf = empty_dbl_file(filename, (length+1)*dimension);
    points = points_buf.asDoubleBuffer();
    if (!buf_is_consistent(points, dimension, true))
      throw new IllegalArgumentException("Data file not properly sized" + 
                                         " or initialized: " + filename);
    if ((start_dimension < 0) || (start_dimension > stop_dimension) ||
        (stop_dimension >= dimension))
      throw new IllegalArgumentException("start/stop dimension arguments must be" +
                                         " in the range of [0,"
                                         + (dimension-1) + "].");
    points.put(0, (double)dimension);
    dimensions = dimension;
    this.start_dimension = start_dimension;
    this.stop_dimension = stop_dimension;
    points_buf.force();
  }

  // map data from file, possibly writably
  public MappedBufferData(String filename, int start_dimension, 
                          int stop_dimension, boolean writable) {
    if (!file_exists(filename, writable)) {
      if (writable)
        throw new IllegalArgumentException("Data file not found or not" +
                                           " readable and writable: " + 
                                           filename);
      else
        throw new IllegalArgumentException("Data file not found or not readable: " +
                                           filename);
    }
    points_buf = map_file(filename, writable);
    points = points_buf.asDoubleBuffer();
    if (!buf_is_consistent(points, (int)points.get(0), false))
      throw new IllegalArgumentException("Data file not properly sized" + 
                                         " or initialized: " + filename);
    dimensions = (int)points.get(0);
    if (!buf_is_consistent(points, (int)points.get(0), false))
      throw new IllegalArgumentException("Data file not properly sized" +
                                         " or initialized: " + filename);
    if (stop_dimension == 0)
      stop_dimension = dimensions-1;
    if ((start_dimension < 0) || (start_dimension > stop_dimension) ||
        (stop_dimension >= dimensions))
      throw new IllegalArgumentException("start/stop dimension arguments must" +
                                         " be in the range of [0,"
                                         + (dimensions-1) + "].");
    this.start_dimension = start_dimension;
    this.stop_dimension = stop_dimension;
  }

  // map data from file readonly
  public MappedBufferData(String filename) {
    this(filename, 0, 0, false);
  }

  // map data from file readonly
  public MappedBufferData(String filename, boolean writable) {
    this(filename, 0, 0, writable);
  }

  // map data from file readonly, ignoring some leading dimensions
  public MappedBufferData(String filename, int start_dimension) {
    this(filename, start_dimension, 0, false);
  }

  // Constructor for test use.
  public MappedBufferData(String filename, int num_pts, int dimension, 
                          double min, double max) {
    super();
    Random rand = new Random(0L);
    points_buf = empty_dbl_file(filename, (num_pts+1)*dimension);
    points = points_buf.asDoubleBuffer();
    assert(buf_is_consistent(points, dimension, true));
    double len = max - min;
    for (int i = 1; i <= num_pts; i++) {
      for (int j = 0; j < dimension; j++) {
        points.put((i * dimension) + j, (len * rand.nextDouble()) + min);
      }
    }
    points.put(0, (double)dimension);
    points_buf.force();
    dimensions = dimension;
    start_dimension = 0;
    stop_dimension = dimensions-1;
  }

  // Constructor for test use.
  public MappedBufferData(String filename, int num_pts, 
                          int dimension, int partition_count) {
    super();
    assert(partition_count > 1);
    Random rand = new Random(0L);
    points_buf = empty_dbl_file(filename, (num_pts+1)*dimension);
    points = points_buf.asDoubleBuffer();
    assert(buf_is_consistent(points, dimension, true));
    for (int i = 1; i <= num_pts; i++) {
      points.put((i * dimension), (double)(1+rand.nextInt(partition_count-1)));
      for (int j = 1; j < dimension; j++) {
        points.put((i * dimension) + j, rand.nextDouble());
      }
    }
    points.put(0, (double)dimension);
    points_buf.force();
    dimensions = dimension;
    start_dimension = 1;
    stop_dimension = dimensions-1;
  }
}
