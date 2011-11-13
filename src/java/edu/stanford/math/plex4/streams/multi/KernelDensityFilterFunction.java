package edu.stanford.math.plex4.streams.multi;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.plex4.metric.utility.DensityEstimation;

public class KernelDensityFilterFunction implements IntFilterFunction {
	private final double[] densities;
	KernelDensityFilterFunction(AbstractIntMetricSpace metricSpace, double sigma) {
		this.densities = DensityEstimation.performGaussianKernelDensityEstimation(metricSpace, sigma);
	}
	
	public double evaluate(int point) {
		return this.densities[point];
	}
}
