package edu.stanford.math.plex4.streams.filter;


public class ExplicitIntFilterFunction implements IntFilterFunction {
	private final double[] values;
	private double maxValue;
	private double minValue;
	
	public ExplicitIntFilterFunction(double[] values) {
		this.values = values;
		
		for (int i = 0; i < this.values.length; i++) {
			if (i == 0 || this.values[i] > this.maxValue) {
				this.maxValue = this.values[i];
			}
			
			if (i == 0 || this.values[i] < this.minValue) {
				this.minValue = this.values[i];
			}
		}
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
	
	public double[] getValues() {
		return this.values;
	}
}