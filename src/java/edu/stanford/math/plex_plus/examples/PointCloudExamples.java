package edu.stanford.math.plex_plus.examples;

import edu.stanford.math.plex_plus.array_utility.DoubleArrayMath;
import edu.stanford.math.plex_plus.utility.RandomUtility;

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
	
	public static double[][] getEquispacedCirclePoints(int n) {
		double[][] points = new double[n][2];
		
		for (int i = 0; i < n; i++) {
			points[i][0] = Math.cos(2 * Math.PI * ((double) i / (double) n));
			points[i][1] = Math.sin(2 * Math.PI * ((double) i / (double) n));
		}

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
