package edu.stanford.math.plex_plus.math.numerical_linear_algebra;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;

public class LinearAlgebra {
	public static DoubleMatrix2D modifiedGramSchmidt(DoubleMatrix2D matrix) {
		DoubleMatrix2D result = matrix.copy();
		int n = result.columns();
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < j; i++) {
				double u_v = Algebra.DEFAULT.mult(result.viewColumn(i), result.viewColumn(j));
				double u_u = Algebra.DEFAULT.norm2(result.viewColumn(i));
				result.viewColumn(j).assign(result.viewColumn(i), Functions.minusMult(u_v / u_u));
			}
			result.viewColumn(j).assign(Functions.div(Math.sqrt(Algebra.DEFAULT.norm2(result.viewColumn(j)))));
		}		
		return result;
	}
	
	public static DoubleMatrix2D modifiedGramSchmidtUpdate(DoubleMatrix2D matrix, int column) {
		int j = column;
		for (int i = 0; i < j; i++) {
			double u_v = Algebra.DEFAULT.mult(matrix.viewColumn(i), matrix.viewColumn(j));
			double u_u = Algebra.DEFAULT.norm2(matrix.viewColumn(i));
			matrix.viewColumn(j).assign(matrix.viewColumn(i), Functions.minusMult(u_v / u_u));
		}
		matrix.viewColumn(j).assign(Functions.div(Math.sqrt(Algebra.DEFAULT.norm2(matrix.viewColumn(j)))));
		return matrix;
	}
	
	public static DoubleMatrix1D projection(DoubleMatrix1D u, DoubleMatrix1D v) {
		DoubleMatrix1D result = u.copy();
		double u_v = Algebra.DEFAULT.mult(u, v);
		double u_u = Algebra.DEFAULT.norm2(u);
		result.assign(Functions.mult(u_v / u_u));
		return result;
	}
}
