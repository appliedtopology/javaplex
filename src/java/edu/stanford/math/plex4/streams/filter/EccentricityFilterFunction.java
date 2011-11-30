package edu.stanford.math.plex4.streams.filter;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

public class EccentricityFilterFunction implements IntFilterFunction {
	private final double[] eccentricities;
	private double maxValue;
	private double minValue;
	
	public EccentricityFilterFunction(AbstractIntMetricSpace metricSpace) {
		int n = metricSpace.size();
		this.eccentricities = new double[n];
		double maximumDistance = 0;
		double distance = 0;
		
		this.maxValue = 0;
		this.minValue = 0;
		
		for (int i = 0; i < n; i++) {
			maximumDistance = metricSpace.distance(i, 0);
			for (int j = 1; j < n; j++) {
				distance = metricSpace.distance(i, j);
				if (distance > maximumDistance) {
					maximumDistance = distance;
				}
			}
			
			this.eccentricities[i] = maximumDistance;
			
			if (i == 0 || this.eccentricities[i] > this.maxValue) {
				this.maxValue = this.eccentricities[i];
			}
			
			if (i == 0 || this.eccentricities[i] < this.minValue) {
				this.minValue = this.eccentricities[i];
			}
		}
	}
	
	public double evaluate(int point) {
		return this.eccentricities[point];
	}

	public double getMaxValue() {
		return this.maxValue;
	}

	public double getMinValue() {
		return this.minValue;
	}
	
	public double[] getValues() {
		return this.eccentricities;
	}
}