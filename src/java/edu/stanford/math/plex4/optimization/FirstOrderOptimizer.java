package edu.stanford.math.plex4.optimization;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.utility.ExceptionUtility;

public abstract class FirstOrderOptimizer implements MultivariateRealOptimizer {
	private int maxEvaluations = 0;
	private int maxIterations = 0;
	private int evaluations = 0;
	private int iterations = 0;
	private RealConvergenceChecker convergenceChecker = null;

	private double gradientDelta = 1e-5;

	protected boolean descentSucceeded = true;
	protected double minStepSize = 1e-8;
	
	
	/**
	 * This is the step-size multiplier. We must have that beta < 1.
	 * A small value of beta will be easy to satisfy, but the function
	 * may not decrease that much in one step.
	 */
	private double beta = 0.8;
	
	/**
	 * This is the slope multiplier to ensure sufficient decrease. A value
	 * of alpha closer to 1 will make the function decrease more, but the step sizes
	 * will be smaller in general. A value of alpha that is too close to zero will
	 * be easy to satisfy, but the function may not decrease that much.
	 */
	private double alpha = 0.7;
	
	public FirstOrderOptimizer(RealConvergenceChecker convergenceChecker, int maxIterations, int maxEvaluations) {
		this.convergenceChecker = convergenceChecker;
		this.maxIterations = maxIterations;
		this.maxEvaluations = maxEvaluations;
	}
	
	public FirstOrderOptimizer(RealConvergenceChecker convergenceChecker) {
		this.convergenceChecker = convergenceChecker;
	}
	
	public RealConvergenceChecker getConvergenceChecker() {
		return this.convergenceChecker;
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
	
	public double getGradientDelta() {
		return this.gradientDelta;
	}
	
	public double getAlpha() {
		return this.alpha;
	}
	
	public double getBeta() {
		return this.beta;
	}

	public void setConvergenceChecker(RealConvergenceChecker arg0) {
		ExceptionUtility.verifyNonNull(arg0);
		this.convergenceChecker = arg0;
	}

	public void setMaxEvaluations(int arg0) {
		this.maxEvaluations = arg0;
	}

	public void setMaxIterations(int arg0) {
		this.maxIterations = arg0;
	}
	
	public void setGradientDelta(int arg0) {
		ExceptionUtility.verifyPositive(arg0);
		this.gradientDelta = arg0;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public void setBeta(double beta) {
		this.beta = beta;
	}

	public RealPointValuePair optimize(MultivariateRealFunction function, GoalType goalType, double[] initialPoint) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {	
		this.evaluations = 0;
		this.iterations = 0;
		double[] iterate = initialPoint.clone();
		boolean terminate = false;
		
		// initialize iterate
		double functionValue = this.evaluateFunction(function, iterate);
		
		// create current and previous
		GenericPair<RealPointValuePair, RealPointValuePair> previousState = new GenericPair<RealPointValuePair, RealPointValuePair>(new RealPointValuePair(iterate, functionValue), new RealPointValuePair(iterate, functionValue));
		GenericPair<RealPointValuePair, RealPointValuePair> currentState = new GenericPair<RealPointValuePair, RealPointValuePair>(new RealPointValuePair(iterate, functionValue), new RealPointValuePair(iterate, functionValue));
		
		// test convergence condition, just in case the starting point is already optimal
		//if (this.convergenceChecker.converged(0, previousState.getFirst(), currentState.getFirst())) {
		//	return currentState.getFirst();
		//}
		
		while (true) {
			// update number of iterations
			this.iterations++;
			this.descentSucceeded = true;
			
			// compute the new state
			currentState = this.updateState(function, currentState, goalType, this.iterations);
			
			// check termination condition and break out of loop if converged
			terminate = this.convergenceChecker.converged(this.iterations, previousState.getFirst(), currentState.getFirst());
			
			if (terminate && descentSucceeded) {
				break;
			}
			
			// copy current point value pair to previous
			previousState = currentState;
			
			if (this.maxEvaluations > 0 && this.evaluations >= this.maxEvaluations) {
				throw new OptimizationException("Optimization routine reached the maximum number of function evaluations before converging.");
			}
			
			if (this.maxIterations > 0 && this.iterations >= this.maxIterations) {
				throw new OptimizationException("Optimization routine reached the maximum number of iterations before converging.");
			} 
		}
		
		return currentState.getFirst();
	}
	
	protected abstract GenericPair<RealPointValuePair, RealPointValuePair> updateState(MultivariateRealFunction function, GenericPair<RealPointValuePair, RealPointValuePair> state, GoalType goalType, int k) throws FunctionEvaluationException, IllegalArgumentException;

	protected RealPointValuePair performBacktracking(MultivariateRealFunction function, double[] startingPoint, double[] gradient, double[] searchDirection, GoalType goalType) throws FunctionEvaluationException, IllegalArgumentException {
		double step_size = 1;
		
		/*
		 * Find a value of t such that
		 * f(x + t v) <= f(x) + alpha t <gradient f(x), v>
		 * 
		 * where v is the search direction
		 */
		
		double innerProduct = DoubleArrayMath.innerProduct(gradient, searchDirection);
		double functionValue = this.evaluateFunction(function, startingPoint);
		double newFunctionValue = functionValue;
		double[] iterate = null;
		boolean terminate = false;
		
		while (true) {
			
			// compute new iterate
			iterate = DoubleArrayMath.sum(startingPoint, DoubleArrayMath.scalarMultiply(searchDirection, step_size));
			newFunctionValue = this.evaluateFunction(function, iterate);
			
			// test for sufficient decrease/increase
			if (goalType == GoalType.MINIMIZE) {
				terminate = (newFunctionValue <= functionValue + this.alpha * step_size * innerProduct);
			} else {
				terminate = (newFunctionValue >= functionValue + this.alpha * step_size * innerProduct);
			}
			
			if (terminate) {
				break;
			}
			
			if (step_size < this.minStepSize) {
				this.descentSucceeded = false;
				break;
			}
			
			// update step size
			step_size *= this.beta;
		}
		
		return new RealPointValuePair(iterate, newFunctionValue);		
	}
	
	protected double[] computeGradient(MultivariateRealFunction function, double[] point) throws FunctionEvaluationException, IllegalArgumentException {
		int dimension = point.length;
		double[] gradient = new double[dimension];
		
		double functionValue = this.evaluateFunction(function, point);
		
		for (int i = 0; i < dimension; i++) {
			double componentValue = point[i];
			point[i] += this.gradientDelta;			
			
			double newFunctionValue = this.evaluateFunction(function, point);
			gradient[i] = (newFunctionValue - functionValue) / this.gradientDelta;
			
			point[i] = componentValue;
		}
		
		return gradient;
	}
	
	protected double evaluateFunction(MultivariateRealFunction function, double[] point) throws FunctionEvaluationException, IllegalArgumentException {
		this.evaluations++;
		return function.value(point);
	}
}
