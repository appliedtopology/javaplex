package edu.stanford.math.plex4.visualization;

public class PointCloudScaling {

	/**
	 * 
	 * Returns a scaled version of the point cloud. Scales the coordinates by
	 * computing overall minimum and maximum and scaling uniformly in all
	 * directions such that all coordinates fit in a box of dimensions: [-scale,
	 * scale] x ... x [-scale, scale]
	 * 
	 * @param points
	 * @param scale
	 * @return
	 */
	public static float[][] scaleCoordinates(final double[][] points, final float scale) {
		int dim = points[0].length;
		final float[][] coordinates = new float[points.length][dim];
		double min = Double.MAX_VALUE;
		double max = 0;

		for (final double[] point : points)
			for (int i = 0; i < dim; i++) {
				min = Math.min(min, point[i]);
				max = Math.max(max, point[i]);
			}

		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[0].length; j++)
				coordinates[i][j] = translateAndScale(points[i][j], min, max, scale);
		}
		return (coordinates);
	}

	public static float translateAndScale(double x, double a, double b, float scale) {
		double xTranslated = scale * (2 * x - a - b) / (b - a);
		return (float) xTranslated;
	}

}
