package edu.stanford.math.plex4.examples;

import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

/**
 * This static class contains various functions which produce 
 * examples of point cloud data sets.
 * 
 * @author Andrew Tausz
 *
 */
public class PointCloudExamples {
	/**
	 * This produces the house example in the tutorial by Henry Adams.
	 * 
	 * @return the requested point cloud
	 */
	public static double[][] getHouseExample() {
		double[][] points = new double[][]{new double[]{0, 3}, 
				new double[]{1, 2},
				new double[]{1, 0},
				new double[]{-1, 0},
				new double[]{-1, 2}};
		
		return points;
	}
	
	/**
	 * This function produces a square in the plane.
	 * 
	 * @return the requested point cloud
	 */
	public static double[][] getSquare() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};

		return points;
	}
	
	/**
	 * This function returns a point set containing n equally spaced points on a
	 * circle.
	 * 
	 * @param n
	 * @return the requested point cloud
	 */
	public static double[][] getEquispacedCirclePoints(int n) {
		double[][] points = new double[n][2];
		
		for (int i = 0; i < n; i++) {
			points[i][0] = Math.cos(2 * Math.PI * ((double) i / (double) n));
			points[i][1] = Math.sin(2 * Math.PI * ((double) i / (double) n));
		}

		return points;
	}
	
	/**
	 * This function returns the vertices of an octahedron.
	 * 
	 * @return the requested point cloud
	 */
	public static double[][] getOctahedronVertices() {
		double[][] points = new double[6][3];
		
		points[0] = new double[]{1, 0, 0};
		points[1] = new double[]{0, -1, 0};
		points[2] = new double[]{-1, 0, 0};
		points[3] = new double[]{0, 1, 0};
		points[4] = new double[]{0, 0, 1};
		points[5] = new double[]{0, 0, -1};
		
		return points;
	}
	
	/**
	 * This function returns the vertices of an tetrahedron.
	 * 
	 * @return the requested point cloud
	 */
	public static double[][] getTetrahedronVertices() {
		double[][] points = new double[4][3];
		
		points[0] = new double[]{0, 0, 0};
		points[1] = new double[]{1, 0, 0};
		points[2] = new double[]{0, 1, 0};
		points[3] = new double[]{0, 0, 1};
		
		return points;
	}
	
	/**
	 * This function returns n uniformly random point on the d-dimensional sphere as a subset
	 * of R^{d+1}.
	 * 
	 * @param n the number of points to generate
	 * @param d the dimension of the sphere to generate on
	 * @return n points in (d+1)-dimensional Euclidean space uniformly distributed on the d-sphere
	 */
	public static double[][] getRandomSpherePoints(int n, int d) {
		double[][] points = new double[n][];
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d + 1);
			points[i] = DoubleArrayMath.scalarMultiply(points[i], 1.0 / DoubleArrayMath.norm(points[i], 2));
		}

		return points;
	}
	
	/**
	 * This function returns n points distributed to a standard Gaussian distribution in d-dimensional
	 * Euclidean space.
	 * 
	 * @param n the number of points to generate
	 * @param d the dimension of the space to generate in
	 * @return n Gaussian points in R^d
	 */
	public static double[][] getGaussianPoints(int n, int d) {
		double[][] points = new double[n][];
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d);
		}

		return points;
	}
	
	/**
	 * This function returns non-uniformly generated points on the torus in R^3.
	 * 
	 * @param n the number of points to generate
	 * @param r the inner radius
	 * @param R the outer radius
	 * @return randomly generated points on the torus in R^3
	 */
	public static double[][] getRandomTorusPoints(int n, double r, double R) {
		double[][] points = new double[n][3];
		
		for (int i = 0; i < n; i++) {
			double u = RandomUtility.nextUniform() * 2 * Math.PI;
			double v = RandomUtility.nextUniform() * 2 * Math.PI;
			points[i][0] = (R + r * Math.cos(v)) * Math.cos(u);
			points[i][1] = (R + r * Math.cos(v)) * Math.sin(u);
			points[i][2] = r * Math.sin(v);
		}
		
		return points;
	}

	/**
	 * This function returns uniformly distributed points on the space S^k x ... x S^k.
	 * Special cases of this include the 2-torus S^1 x S^1 in R^4, as well as the sphere S^k.
	 * 
	 * @param numPoints the number of points to generate
	 * @param sphereDimension the sphere dimension (ie. the k in S^k x ... x S^k)
	 * @param numFactors the number of factors in the product S^k x ... x S^k
	 * @return uniformly generated points on the set S^k x ... x S^k
	 */
	public static double[][] getRandomSphereProductPoints(int numPoints, int sphereDimension, int numFactors) {
		int dimension = (sphereDimension + 1) * numFactors;
		double[][] points = new double[numPoints][];
		
		for (int i = 0; i < numPoints; i++) {
			points[i] = RandomUtility.normalArray(dimension);
			for (int N = 0; N < numFactors; N++) {
				double sum = 0;
				for (int j = 0; j < sphereDimension + 1; j++) {
					sum += points[i][N * (sphereDimension + 1) + j] * points[i][N * (sphereDimension + 1) + j];
				}
				sum = Math.sqrt(sum);
				for (int j = 0; j < sphereDimension + 1; j++) {
					points[i][N * (sphereDimension + 1) + j] /= sum;
				}
			}
		}

		return points;
	}
	
	/**
	 * This function returns randomly generated points on a figure-8 in the plane. This consists
	 * of two circles - one translated up by 1 unit, and the other translated down by 1 unit.
	 * 
	 * @param n the number of points to generate
	 * @return randomly generated points on a figure-8.
	 */
	public static double[][] getRandomFigure8Points(int n) {
		int m = n / 2;
		double[][] points = new double[n][2];
		
		for (int i = 0; i < m; i++) {
			points[i] = RandomUtility.normalArray(2);
			points[i] = DoubleArrayMath.scalarMultiply(points[i], 1.0 / DoubleArrayMath.norm(points[i], 2));
			
			// translate by +1 vertically
			points[i][1] += 1;
		}
		
		for (int i = m; i < n; i++) {
			points[i] = RandomUtility.normalArray(2);
			points[i] = DoubleArrayMath.scalarMultiply(points[i], 1.0 / DoubleArrayMath.norm(points[i], 2));
			
			// translate by -1 vertically
			points[i][1] -= 1;
		}		

		return points;
	}
}
