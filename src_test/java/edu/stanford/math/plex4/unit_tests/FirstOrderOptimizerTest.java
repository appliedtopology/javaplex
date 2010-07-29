package edu.stanford.math.plex4.unit_tests;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.SimpleScalarValueChecker;
import org.junit.Test;

import cern.colt.Arrays;
import edu.stanford.math.plex4.array_utility.ArrayGeneration;
import edu.stanford.math.plex4.optimization.AdaptiveOptimizer;
import edu.stanford.math.plex4.optimization.GradientDescentOptimizer;
import edu.stanford.math.plex4.optimization.NesterovGradientOptimizer;
import edu.stanford.math.plex4.optimization.ParameterizedMultivariateRealFunction;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MathUtility;
import edu.stanford.math.plex4.utility.RandomUtility;

public class FirstOrderOptimizerTest {
	private MultivariateRealFunction getQuadratic(final double[] coefficients, final double[] vertex) {
		ExceptionUtility.verifyEqual(coefficients.length, vertex.length);
		return new MultivariateRealFunction() {
			public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
				double result = 0;
				
				if (arg0.length != coefficients.length) {
					throw new FunctionEvaluationException(arg0);
				}
				
				for (int i = 0; i < arg0.length; i++) {
					result += coefficients[i] * (arg0[i] - vertex[i]) * (arg0[i] - vertex[i]);
				}
				
				return result;
			}
		};
	}
	
	private ParameterizedMultivariateRealFunction getL1Approximation() {
		return new ParameterizedMultivariateRealFunction() {
			public double value(double[] arg0, double parameter) throws FunctionEvaluationException, IllegalArgumentException {
				double result = 0;

				for (int i = 0; i < arg0.length; i++) {
					result += MathUtility.absApproximation(arg0[i], parameter);
				}
				
				return result;
			}
		};
	}
	
	private MultivariateRealFunction getMaxFunction() {
		return new MultivariateRealFunction() {
			public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
				double result = 0;
				
				for (int i = 0; i < arg0.length; i++) {
					result = MathUtility.maxApproximation(result, Math.abs(arg0[i]), 0.001);
				}

				return result;
			}
		};
	}
	
	private MultivariateRealFunction getIllConditionedQuadratic(int dimension, double L) {
		double[] coefficients = ArrayGeneration.replicate((double) 1, dimension);
		coefficients[0] = L;
		double[] vertex = new double[dimension];
		return this.getQuadratic(coefficients, vertex);
	}
	
	void testOptimizer(MultivariateRealOptimizer optimizer, MultivariateRealFunction function, GoalType goalType, double[] initialPoint) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		RealPointValuePair pair = optimizer.optimize(function, goalType, initialPoint);
		
		System.out.println("Optimizer: " + Arrays.toString(pair.getPoint()));
		System.out.println("Optimum: " + pair.getValue());
		System.out.println("Number of iterations: " + optimizer.getIterations());
		System.out.println("Number of evaluations: " + optimizer.getEvaluations());
	}
	
	void testOptimizer(AdaptiveOptimizer optimizer, ParameterizedMultivariateRealFunction function, GoalType goalType, double[] initialPoint, double initialParameterValue) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		RealPointValuePair pair = optimizer.optimize(function, goalType, initialPoint, initialParameterValue);
		
		System.out.println("Optimizer: " + Arrays.toString(pair.getPoint()));
		System.out.println("Optimum: " + pair.getValue());
		System.out.println("Number of iterations: " + optimizer.getIterations());
		System.out.println("Number of evaluations: " + optimizer.getEvaluations());
	}

	@Test
	public void testGradientDescent() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int dimension = 10;
		double L = 100;
		double tolerance = 1e-4;
		MultivariateRealFunction function = this.getMaxFunction();
		RealConvergenceChecker checker = new SimpleScalarValueChecker(tolerance, tolerance);
		
		MultivariateRealOptimizer optimizer = new GradientDescentOptimizer(checker, 0, 0);
		
		System.out.println("Gradient Descent");
		
		this.testOptimizer(optimizer, function, GoalType.MINIMIZE, RandomUtility.normalArray(dimension));
	}
	
	@Test
	public void testNesterov() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int dimension = 10;
		double L = 100;
		double tolerance = 1e-4;
		MultivariateRealFunction function = this.getMaxFunction();
		RealConvergenceChecker checker = new SimpleScalarValueChecker(tolerance, tolerance);
		
		MultivariateRealOptimizer optimizer = new NesterovGradientOptimizer(checker, 0, 0);
		
		System.out.println("Nesterov Gradient");
		
		this.testOptimizer(optimizer, function, GoalType.MINIMIZE, RandomUtility.normalArray(dimension));
	}
	
	//@Test
	public void testGradientDescentAdaptive() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
		int dimension = 1000;

		double tolerance = 1e-4;
		ParameterizedMultivariateRealFunction function = this.getL1Approximation();
		RealConvergenceChecker checker = new SimpleScalarValueChecker(tolerance, tolerance);
		
		MultivariateRealOptimizer internalOptimizer = new GradientDescentOptimizer(checker, 0, 0);
		
		AdaptiveOptimizer optimizer = new AdaptiveOptimizer(internalOptimizer);
		
		System.out.println("Gradient Descent");
		
		this.testOptimizer(optimizer, function, GoalType.MINIMIZE, RandomUtility.normalArray(dimension), 1);
	}
	
}
