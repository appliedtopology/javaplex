package edu.stanford.math.plex4.optimization;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;

public class NesterovGradientOptimizer extends FirstOrderOptimizer {

	public NesterovGradientOptimizer(RealConvergenceChecker convergenceChecker) {
		super(convergenceChecker);
	}
	
	public NesterovGradientOptimizer(RealConvergenceChecker convergenceChecker, int maxIterations, int maxEvaluations) {
		super(convergenceChecker, maxIterations, maxEvaluations);
	}

	@Override
	protected GenericPair<RealPointValuePair, RealPointValuePair> updateState(MultivariateRealFunction function, GenericPair<RealPointValuePair, RealPointValuePair> state, GoalType goalType, int k) throws FunctionEvaluationException, IllegalArgumentException {
		double[] x_k1 = state.getFirst().getPoint();
		double[] y_k1 = state.getSecond().getPoint();
		double f_xk1 = state.getFirst().getValue();
		double f_yk1 = state.getSecond().getValue();
		
		double[] gradient = this.computeGradient(function, y_k1);
		double[] searchDirection = DoubleArrayMath.scalarMultiply(gradient, (goalType == GoalType.MAXIMIZE ? 1 : -1));
		
		RealPointValuePair x_k_pair = this.performBacktracking(function, y_k1, gradient, searchDirection, goalType);
		
		double[] x_k = x_k_pair.getPoint();
		
		double theta_k = ((double) k - 1) / ((double) k + 2);
		double[] y_k = DoubleArrayMath.sum(x_k, DoubleArrayMath.scalarMultiply(DoubleArrayMath.difference(x_k, x_k1), theta_k));
		
		double f_xk = this.evaluateFunction(function, x_k);
		double f_yk = this.evaluateFunction(function, y_k);
		
		GenericPair<RealPointValuePair, RealPointValuePair> newState = new GenericPair<RealPointValuePair, RealPointValuePair>(new RealPointValuePair(x_k, f_xk), new RealPointValuePair(y_k, f_yk));
		
		return newState;
	}

}
