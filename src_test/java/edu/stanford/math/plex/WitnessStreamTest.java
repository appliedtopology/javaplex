package edu.stanford.math.plex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The <code>WitnessStreamTest</code> class.
 *
 * <p>Among the facilities provided by the <code>WitnessStreamTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
@SuppressWarnings("unused")
public class WitnessStreamTest {

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

    /////
    ///// OLD NUMBERS -- not necessarily correct any longer
    /////
    /////for N=1000 L=40 
    ///// LZW stream has 19668 simplices, took 0.285000 seconds
    ///// Witness stream has 12916 simplices, took 10.476000 seconds
    /////for N=5000 L=40
    ///// LZW stream has 39137 simplices, took 0.161000 seconds
    ///// Witness stream has 28216 simplices, took 11.702000 seconds
    /////for N=10000 L=40
    ///// LZW stream has 53498 simplices, took 0.095000 seconds
    ///// Witness stream has 40272 simplices, took 3.201000 seconds
    /////for N=100,000 L=100
    ///// LZW stream has 119788 simplices, took 16.521000 seconds
    /////for N=100,000 L=200
    ///// LZW stream has 437243 simplices, took 48.663000 seconds
    /////for N=100,000 L=400
    ///// LZW stream has 1664175 simplices, took 191.450000 seconds
    /////for N=100,000 L=1000
    ///// LZW stream has 9878405 simplices, took 1201.353000 seconds
    /////
    ///// end of OLD NUMBERS 
    /////

    // For N=100,000 and L=100
    //With D: LZW stream has 119788 simplices, took 15.858000 seconds
    //No D, i_cache: LZW stream has 119788 simplices, took 29.480000 seconds
    //No D:  LZW stream has 119788 simplices, took 42.405000 seconds
    //Witness stream has 101736 simplices, took 385.740000 seconds

    // For N=100,000 and L=1000
    //With D: LZW stream has 9878405 simplices, took 1218.642000 seconds
    //No D: LZW stream has 9878405 simplices, took 4898.634000 seconds
    //No D, i_cache: LZW stream has 9878405 simplices, took 3323.336000 seconds
    // got bored waiting for the Witness results. I'll do it overnight.

    // For N=2,000,000 and L=100
    //LZW stream has 49840 simplices, took 416.387000 seconds

    // For N=2,000,000 and L=100, R_max/4
    //LZW stream has 26781 simplices, took 320.568000 seconds

    if (false) {
      // skip for now
      int N = 60000; // 00;
      int L = 100; // 0;
      PointData data = (PointData) new EuclideanArrayData(N, 4); 
      int[] landmarks = WitnessStream.makeRandomLandmarks(data, L);
      double R_max =  0.025631; // WitnessStream.estimateRmax(data, landmarks);
      long lazy_time = System.currentTimeMillis();
      LazyWitnessStream lstr = new LazyWitnessStream(.01, 3, R_max, 2, landmarks, data);
      lazy_time = System.currentTimeMillis() - lazy_time;
      System.out.printf(" For N=%d and L=%d\nLZW stream has %d simplices, took %f seconds\n",
                        N, L, lstr.size(), ((double)lazy_time)/1000);
      long w_time = System.currentTimeMillis();
      WitnessStream wstr = new WitnessStream(.01, 3, R_max, landmarks, data);
      w_time = System.currentTimeMillis() - w_time;
      System.out.printf("Witness stream has %d simplices, took %f seconds\n",
                        wstr.size(), ((double)w_time)/1000);
    }

    if (false) {
      // skip for now
      int N = 600; // 00;
      PointData data = (PointData) new EuclideanArrayData(N, 4); 
      long lazy_time = System.currentTimeMillis();
      RipsStream rstr = new RipsStream(.01, 2, 20.0, data);
      lazy_time = System.currentTimeMillis() - lazy_time;
      System.out.printf(" For N=%d Rips stream has %d simplices, took %f seconds\n",
                        N, rstr.size(), ((double)lazy_time)/1000);
    }
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
          
