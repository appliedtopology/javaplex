package edu.stanford.math.plex4.homology.mapping;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;

import edu.stanford.math.plex4.utility.RandomUtility;

public class ConvexUtility {
	public static boolean randomizedConvexityTest(MultivariateRealFunction function, int dimensionality, int maxRepititions) throws FunctionEvaluationException, IllegalArgumentException {
		
		for (int count = 0; count < maxRepititions; count++) {
			double[] a = RandomUtility.normalArray(dimensionality);
			double[] b = RandomUtility.normalArray(dimensionality);
			double theta = RandomUtility.nextUniform();
			double[] m = new double[dimensionality];
			for (int j = 0; j < dimensionality; j++) {
				m[j] = theta * a[j] + (1 - theta) * b[j];
			}
			
			double f_a = function.value(a);
			double f_b = function.value(b);
			double f_m = function.value(m);
			
			double interpolatedValue = theta * f_a + (1 - theta) * f_b;
			
			if (interpolatedValue < f_m) {
				return false;
			}
		}
		
		return true;
	}
}
