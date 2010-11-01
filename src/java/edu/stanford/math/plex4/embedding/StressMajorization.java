package edu.stanford.math.plex4.embedding;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import cern.jet.math.Functions;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;

public class StressMajorization implements MetricSpaceEmbedding {
	private static final StressMajorization instance = new StressMajorization();
	
	private StressMajorization(){}
	
	public static StressMajorization getInstance() {
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.stanford.math.plex_plus.embedding.MetricSpaceEmbedding#computedEmbedding(edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace, int)
	 */
	public double[][] computedEmbedding(AbstractIntMetricSpace metricSpace, int dimension) {
		DoubleMatrix2D P = doStressMajorization(metricSpace, dimension);
		return P.toArray();
	}

	/*
	 * The stress Majorization algorithm is below. It is described in "Graph
	 * Drawing by Stress Majorization" by Emden R. Gansner, Yehuda Koren and
	 * Stephen North. This is algorithm is an improved version of the Kamanda-
	 * Kawai algorithm used by neato in GraphViz.
	 * 
	 * All equations numbers in the comments below refer to equations that
	 * appear in the above paper.
	 */
	protected static double ALPHA = -2d;
	protected static double EPSILON = 1e-4d;
	protected static double TOL = 1e-8d;

	/**
	 * @param D  a matrix of distances between the N nodes of the graph
	 * @param dim the required dimension of the embedded points
	 * @return an N by 2 matrix containing the N layout points in R^dim
	 */
	public static DoubleMatrix2D doStressMajorization(AbstractIntMetricSpace D, int dim) {
		int n = D.size();

		// Housekeeping
		DoubleMatrix2D L, rL, B, LX, rB, rX, X;
		double s0, s1;

		// Compute L and a view of L with the first row and column removed
		L = computeL(D);
		rL = L.viewPart(1, 1, n - 1, n - 1);

		// Initialize X with n points
		X = new DenseDoubleMatrix2D(n, dim);
		X.assign(Functions.random());
		X.setQuick(0, 0, 0); // Set the first point to 0
		X.setQuick(0, 1, 0); // Set the first point to 0

		// Compute the initial stress
		s0 = stress(X, D);

		// Compute B and a view of B with the first row removed
		B = new DenseDoubleMatrix2D(n, dim);
		rB = B.viewPart(1, 0, n - 1, dim);

		CholeskyDecomposition chol = new CholeskyDecomposition(rL);

		boolean converged = false;

		int loopLimit = 1000;
		for (int i = 0; i < loopLimit; i++) {

			LX = computeLZ(D, X);
			LX.zMult(X, B); // compute B = LX*X

			rX = chol.solve(rB); // solve rL*rX = rB

			// Update the last n-1 points stored in X (recall that the first
			// point is zero)
			X.viewPart(1, 0, n - 1, dim).assign(rX);

			// Compute the new stress and check convergence
			s1 = stress(X, D);

			if (Math.abs(s0 - s1) < EPSILON * s0) {
				converged = true;
				// System.out.println("Converged!");
				break;
			}
			s0 = s1;
		}

		if (!converged) {
			System.err.println("Warning: stress majorization did not converge.");
		}

		return X;

	}

	/**
	 * @param D a matrix of distances between the N nodes of the graph
	 * @return the matrix L defined right below equation (4)
	 */
	protected static DoubleMatrix2D computeL(AbstractIntMetricSpace D) {
		DoubleMatrix2D L = new DenseDoubleMatrix2D(D.size(), D.size());

		int n = D.size();
		double d_ij, w_ij, w_ii;

		for (int i = 0; i < n; i++) { // loop over the rows
			w_ii = 0;
			inner: for (int j = 0; j < n; j++) { // loop over the columns
				if (i == j)
					continue inner;
				d_ij = D.distance(i, j);
				w_ij = Math.pow(d_ij, ALPHA);
				w_ii += w_ij;
				L.setQuick(i, j, -w_ij);
			}
			L.setQuick(i, i, w_ii);

			// Sanity check
			// double s;
			// if ((s = Math.abs(L.viewRow(i).zSum()))>TOL) {
			// throw new IllegalStateException("Row sum should equal 0. " +
			// "Current value is "+s);
			// }
		}

		return L;
	}

	/**
	 * @param D the matrix of distances between the N nodes of the graph
	 * @param Z the N by 2 matrix containing the N layout points in R^2
	 * @return the matrix L^Z defined above equation (6)
	 */
	protected static DoubleMatrix2D computeLZ(AbstractIntMetricSpace D, DoubleMatrix2D Z) {
		int n = D.size();

		DoubleMatrix2D LZ = new DenseDoubleMatrix2D(D.size(), D.size());

		double d_ij, w_ij, lz_ii, delta_ij, norm, inv, lz_ij;
		DoubleMatrix1D Z_i, Z_j, Y;

		for (int i = 0; i < n; i++) { // loop over the rows
			lz_ii = 0;
			inner: for (int j = 0; j < n; j++) { // loop over the columns
				if (i == j)
					continue inner;

				d_ij = D.distance(i, j);
				w_ij = Math.pow(d_ij, ALPHA);
				delta_ij = w_ij * d_ij;

				Z_i = Z.viewRow(i);
				Z_j = Z.viewRow(j).copy();
				Y = Z_j.assign(Z_i, Functions.minus);
				norm = Algebra.DEFAULT.norm1(Y);
				inv = (Math.abs(norm) < TOL) ? 0 : 1d / norm;
				lz_ij = -delta_ij * inv;
				lz_ii += lz_ij;
				LZ.setQuick(i, j, lz_ij);
			}
			LZ.setQuick(i, i, -lz_ii);

			// Sanity check
			// double s;
			// if ((s = Math.abs(LZ.viewRow(i).zSum()))>TOL) {
			// throw new IllegalStateException("Row sum should equal 0. " +
			// "Current value is "+s);
			// }

		}
		return LZ;
	}

	/**
	 * @param X a N by 2 matrix containing N layout points in R^2
	 * @param D a matrix of distances between the N nodes of the graph
	 * @return the result of applying equation (2) to X
	 */
	protected static double stress(DoubleMatrix2D X, AbstractIntMetricSpace D) {
		int n = D.size();
		DoubleMatrix1D X_i, X_j, Y;
		double stress = 0d, norm, w_ij, z0, z1, d_ij, z2;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				d_ij = D.distance(i, j);
				w_ij = Math.pow(d_ij, ALPHA);
				X_i = X.viewRow(i);
				X_j = X.viewRow(j).copy();
				Y = X_j.assign(X_i, Functions.minus);
				norm = Algebra.DEFAULT.normInfinity(Y);
				z0 = norm - d_ij;
				z1 = Math.pow(z0, 2);
				z2 = w_ij * z1;
				stress += z2;
			}
		}
		return stress;
	}
}
