package edu.stanford.math.plex4.streams.filter;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import edu.stanford.math.plex4.metric.utility.DensityEstimation;

public class KernelDensityFilterFunction implements IntFilterFunction {
	private final double[] densities;
	private boolean maxMinComputed = false;
	private double maxValue = 0;
	private double minValue = 0;
	
	public KernelDensityFilterFunction(AbstractIntMetricSpace metricSpace, double sigma) {
		this.densities = DensityEstimation.performGaussianKernelDensityEstimation(metricSpace, sigma);
	}
	
	public double evaluate(int point) {
		return this.densities[point];
	}
	
	public double getMaxValue() {
		if (!this.maxMinComputed) {
			this.computeMaxMinValues();
		}
		return this.maxValue;
	}

	public double getMinValue() {
		if (!this.maxMinComputed) {
			this.computeMaxMinValues();
		}
		return this.minValue;
	}
	
	private void computeMaxMinValues() {
		for (int i = 0; i < this.densities.length; i++) {
			if (i == 0 || this.densities[i] > maxValue) {
				this.maxValue = this.densities[i];
			}
			
			if (i == 0 || this.densities[i] < minValue) {
				this.minValue = this.densities[i];
			}
		}
		
		this.maxMinComputed = true;
	}
	
	public double[] getValues() {
		return this.densities;
	}
}
