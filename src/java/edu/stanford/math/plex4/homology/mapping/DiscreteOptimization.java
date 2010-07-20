package edu.stanford.math.plex4.homology.mapping;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.RealPointValuePair;

import edu.stanford.math.plex4.utility.RandomUtility;

public class DiscreteOptimization  {
	private int minValue = -1;
	private int maxValue = +1;
	
	public DiscreteOptimization() {}
	
	public RealPointValuePair multistartOptimize(MultivariateRealFunction function, int dimensionality, int starts) throws FunctionEvaluationException, IllegalArgumentException {
		RealPointValuePair minimizingPair = null;
		
		for (int start = 0; start < starts; start++) {
			double[] iterate = this.randomInitializer(dimensionality);
			RealPointValuePair candidatePair = this.greedyOptimization(function, iterate);
			if (minimizingPair == null || (candidatePair.getValue() < minimizingPair.getValue())) {
				minimizingPair = candidatePair;
			}
		}
		
		return minimizingPair;
	}
	
	public RealPointValuePair greedyOptimization(MultivariateRealFunction function, double[] initialPoint) throws FunctionEvaluationException, IllegalArgumentException {
		
		double minimumValue = function.value(initialPoint);
		double[] minimizer = initialPoint.clone();
		double[] iterate = initialPoint.clone();
		int dimension = initialPoint.length;
		
		boolean minimumFound = false;
		
		while (!minimumFound) {
			boolean functionDecreased = false;
			
			for (int i = 0; i < dimension; i++) {
				for (int coefficient = this.minValue; coefficient <= this.maxValue; coefficient++) {
					
					double oldValue = iterate[i];
					iterate[i] = coefficient;
					double functionValue = function.value(iterate);
					
					if (functionValue < minimumValue) {
						minimizer = iterate.clone();
						minimumValue = functionValue;
						functionDecreased = true;
					}
					
					iterate[i] = oldValue;
				}
			}
			
			if (!functionDecreased) {
				minimumFound = true;
			}
		}
		
		
		
		return new RealPointValuePair(minimizer, minimumValue);
	}
	
	private double[] randomInitializer(int dimension) {
		double[] result = new double[dimension];
		
		for (int i = 0; i < dimension; i++) {
			result[i] = RandomUtility.nextUniformInt(this.minValue, this.maxValue);
		}
		
		return result;
	}
}
