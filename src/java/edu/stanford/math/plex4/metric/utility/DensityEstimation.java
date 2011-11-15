package edu.stanford.math.plex4.metric.utility;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

public class DensityEstimation {
	public static double[] performGaussianKernelDensityEstimation(AbstractIntMetricSpace metricSpace, double sigma) {
		int n = metricSpace.size();
		double[] densities = new double[n];
		double distance = 0;
		double sigmaSquared = sigma * sigma;
		double factor = 1.0d / (Math.sqrt(2 * Math.PI) * sigma * n);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				distance = metricSpace.distance(i, j);
				densities[i] += Math.exp(- (distance * distance / sigmaSquared));
			}
			densities[i] *= factor;
		}
		
		return densities;
	}
}
