package edu.stanford.math.plex4.optimization;

import org.apache.commons.math.FunctionEvaluationException;

public interface ParameterizedMultivariateRealFunction {
	double value(double[] point, double parameter) throws FunctionEvaluationException, IllegalArgumentException;
}
