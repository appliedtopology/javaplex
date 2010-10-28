package edu.stanford.math.plex4.examples;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * This static class contains various functions which produce 
 * examples of point cloud data sets.
 * 
 * @author Andrew Tausz
 *
 */
public class PointCloudExamples {
	/**
	 * House example in the plex tutorial by Henry Adams.
	 */
	public static double[][] getHouseExample() {
		double[][] points = new double[][]{new double[]{0, 3}, 
				new double[]{1, 2},
				new double[]{1, 0},
				new double[]{-1, 0},
				new double[]{-1, 2}};
		
		return points;
	}
	
	public static double[][] getSquare() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};

		return points;
	}
	
	/**
	 * This function returns a dataset containing n equally spaced points on a
	 * circle.
	 * 
	 * @param n
	 * @return
	 */
	public static double[][] getEquispacedCirclePoints(int n) {
		double[][] points = new double[n][2];
		
		for (int i = 0; i < n; i++) {
			points[i][0] = Math.cos(2 * Math.PI * ((double) i / (double) n));
			points[i][1] = Math.sin(2 * Math.PI * ((double) i / (double) n));
		}

		return points;
	}
	
	public static double[][] getoctahedronVertices() {
		double[][] points = new double[6][3];
		
		points[0] = new double[]{1, 0, 0};
		points[1] = new double[]{0, -1, 0};
		points[2] = new double[]{-1, 0, 0};
		points[3] = new double[]{0, 1, 0};
		points[4] = new double[]{0, 0, 1};
		points[5] = new double[]{0, 0, -1};
		
		return points;
	}
	
	public static double[][] getRandomSpherePoints(int n, int d) {
		double[][] points = new double[n][d + 1];
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d + 1);
			points[i] = DoubleArrayMath.scalarMultiply(points[i], 1.0 / DoubleArrayMath.norm(points[i], 2));
		}

		return points;
	}
	
	public static double[][] getGaussianPoints(int n, int d) {
		double[][] points = new double[n][d];
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d);
		}

		return points;
	}
	
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
