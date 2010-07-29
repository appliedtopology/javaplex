package edu.stanford.math.plex4.optimization;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.utility.RandomUtility;

public class GradientDescentOptimizer extends FirstOrderOptimizer {

	public GradientDescentOptimizer(RealConvergenceChecker convergenceChecker) {
		super(convergenceChecker);
	}
	
	public GradientDescentOptimizer(RealConvergenceChecker convergenceChecker, int maxIterations, int maxEvaluations) {
		super(convergenceChecker, maxIterations, maxEvaluations);
	}

	@Override
	protected GenericPair<RealPointValuePair, RealPointValuePair> updateState(MultivariateRealFunction function, GenericPair<RealPointValuePair, RealPointValuePair> state, GoalType goalType, int k) throws FunctionEvaluationException, IllegalArgumentException {
		double[] iterate = state.getFirst().getPoint();
		double[] gradient = this.computeGradient(function, iterate);
		
		int r = RandomUtility.nextUniformInt(0, gradient.length - 1);
		gradient[r] += 0.1 * RandomUtility.nextNormal();
		
		double[] searchDirection = DoubleArrayMath.scalarMultiply(gradient, (goalType == GoalType.MAXIMIZE ? 1 : -1));		
		
		RealPointValuePair pair = this.performBacktracking(function, iterate, gradient, searchDirection, goalType);
		
		return new GenericPair<RealPointValuePair, RealPointValuePair>(pair, null);
	}

}
