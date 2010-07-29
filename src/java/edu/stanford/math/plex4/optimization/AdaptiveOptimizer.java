package edu.stanford.math.plex4.optimization;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;

public class AdaptiveOptimizer {
	private int maxEvaluations = 0;
	private int maxIterations = 8;
	private int evaluations = 0;
	private int iterations = 0;
	private double parameterMultiplier = 0.2;
	private MultivariateRealOptimizer internalOptimizer = null;
	
	public AdaptiveOptimizer(MultivariateRealOptimizer internalOptimizer) {
		this.internalOptimizer = internalOptimizer;
	}
	
	public MultivariateRealOptimizer getInternalOptimizer() {
		return this.internalOptimizer;
	}

	public int getEvaluations() {
		return this.evaluations;
	}

	public int getIterations() {
		return this.iterations;
	}

	public int getMaxEvaluations() {
		return this.maxEvaluations;
	}

	public int getMaxIterations() {
		return this.maxIterations;
	}
	
	public double getParameterMultiplier() {
		return this.parameterMultiplier;
	}
	
	public void setMaxEvaluations(int arg0) {
		this.maxEvaluations = arg0;
	}

	public void setMaxIterations(int arg0) {
		this.maxIterations = arg0;
	}
	
	public void setParameterMultiplier(double value) {
		this.parameterMultiplier = value;
	}
	
	public RealPointValuePair optimize(ParameterizedMultivariateRealFunction function, GoalType goalType, double[] initialPoint, double initialParameterValue) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
		this.evaluations = 0;
		this.iterations = 0;
		
		RealPointValuePair current = new RealPointValuePair(initialPoint, 0);
		RealPointValuePair previous = new RealPointValuePair(initialPoint, 0);
		
		double parameterValue = initialParameterValue;
		
		while (true) {
			this.iterations++;
			
			current = this.internalOptimizer.optimize(this.curry(function, parameterValue), goalType, current.getPoint());
			
			this.evaluations += this.internalOptimizer.getEvaluations();
			
			boolean terminate = this.internalOptimizer.getConvergenceChecker().converged(this.iterations, previous, current);
			
			if (terminate) {
				//break;
			}
			
			if (this.maxEvaluations > 0 && this.evaluations >= this.maxEvaluations) {
				throw new OptimizationException("Optimization routine reached the maximum number of function evaluations before converging.");
			}
			
			if (this.iterations >= this.maxIterations) {
				break;
			}
			
			parameterValue *= this.parameterMultiplier;
			previous = current;
		}
		
		return current;
	}
	
	private MultivariateRealFunction curry(final ParameterizedMultivariateRealFunction function, final double parameterValue) {
		return new MultivariateRealFunction() {

			public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
				return function.value(arg0, parameterValue);
			}
			
		};
	}
}
