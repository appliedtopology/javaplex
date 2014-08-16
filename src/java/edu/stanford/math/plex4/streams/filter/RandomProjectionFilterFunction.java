package edu.stanford.math.plex4.streams.filter;

import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.utility.MathUtility;
import edu.stanford.math.plex4.utility.RandomUtility;

public class RandomProjectionFilterFunction implements IntFilterFunction {
	private final double[] direction;
	private final double[] values;

	private final double maxValue;
	private final double minValue;

	public RandomProjectionFilterFunction(EuclideanMetricSpace metricSpace) {
		this(metricSpace.getPoints());
	}

	public RandomProjectionFilterFunction(double[][] points) {
		this.direction = RandomUtility.normalArray(points[0].length, true);
		this.values = new double[points.length];

		double maxValue = Double.NEGATIVE_INFINITY;
		double minValue = Double.POSITIVE_INFINITY;

		double value;

		for (int i = 0; i < this.values.length; i++) {
			value = this.values[i] = MathUtility.dotProduct(this.direction, points[i]);
			maxValue = (value > maxValue ? value : maxValue);
			minValue = (value < minValue ? value : minValue);
		}

		this.maxValue = maxValue;
		this.minValue = minValue;
	}

	public double evaluate(int point) {
		return this.values[point];
	}

	public double getMaxValue() {
		return this.maxValue;
	}

	public double getMinValue() {
		return this.minValue;
	}

	public int getRangeMax() {
		return this.values.length;
	}

	public double[] getValues() {
		return this.values;
	}
}
