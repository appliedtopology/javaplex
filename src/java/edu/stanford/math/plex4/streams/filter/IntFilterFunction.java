package edu.stanford.math.plex4.streams.filter;

public interface IntFilterFunction {
	double evaluate(int point);
	
	double getMaxValue();
	double getMinValue();
	
	double[] getValues();
}
