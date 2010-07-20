package edu.stanford.math.plex4.math.numerical_linear_algebra;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.Property;
import cern.jet.math.Functions;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MatrixUtility;

/**
 * This class implements the Lanczos algorithm for computing the eigenvalues 
 * and eigenvectors of a matrix. It is useful for sparse matrices and 
 * situations when only a few eigenvalues/vectors are needed.
 * 
 * References:
 * - http://www.cs.utk.edu/~dongarra/etemplates/node103.html
 * - "Matrix Computations" by Golub and Van Loan
 * - "Numerical Recipes in C" by Press, Teukolsky and Vetterling
 * 
 * @author Andrew Tausz
 *
 */
public class LanczosAlgorithm {
	private static final double tolerance = 1E-6;

	private DoubleMatrix1D eigenvalues;
	private DoubleMatrix2D eigenvectors;

	public DoubleMatrix1D getEigenvalues() {
		return eigenvalues;
	}

	public DoubleMatrix2D getEigenvectors() {
		return eigenvectors;
	}

	public void decompose(DoubleMatrix2D A, int k) {
		// make sure that matrix is square and symmetric
		Property.DEFAULT.checkSquare(A);
		Property.DEFAULT.isSymmetric(A);

		// declare variables
		int n = A.rows();
		int s = k;

		DoubleMatrix1D alphas = DoubleFactory1D.dense.make(s);
		DoubleMatrix1D betas = DoubleFactory1D.dense.make(s);
		DoubleMatrix2D V = DoubleFactory2D.dense.make(n, s);

		// make sure that k <= n
		ExceptionUtility.verifyLessThanOrEqual(k, n);

		// generate a new random vector
		DoubleMatrix1D r = MatrixUtility.randomNormalVector(n);

		// initialize arrays
		double[] d = new double[s + 1];
		double[] e = new double[s + 1];
		double[][] S = new double[s + 1][s + 1];

		// which ritzvalues are converged?
		boolean[] validEigenvalues = new boolean[s];

		// how many converged eigenvalues have been found?
		int numFound = 0;

		// algorithm iterations
		int j = 0;

		while (true) {
			// V_j = r / beta_{j - 1}
			if (j == 0) {
				V.viewColumn(j).assign(r).assign(Functions.div(Math.sqrt(Algebra.DEFAULT.norm2(r))));
			} else if (betas.getQuick(j - 1) != 0) {
				V.viewColumn(j).assign(r).assign(Functions.div(betas.getQuick(j - 1)));
			}

			// r = A * V_j
			r.assign(Algebra.DEFAULT.mult(A, V.viewColumn(j)));

			// r = r - V_{j - 1} * beta_{j - 1}
			if (j > 0) {
				DoubleMatrix1D t1 = V.viewColumn(j - 1).copy().assign(Functions.mult(betas.getQuick(j - 1)));
				r.assign(t1, Functions.minus);
			}

			// alpha_j = <V_j, r>
			alphas.setQuick(j, Algebra.DEFAULT.mult(V.viewColumn(j), r));

			// r = r - V_j * alpha_j
			DoubleMatrix1D t2 = V.viewColumn(j).copy().assign(Functions.mult(alphas.getQuick(j)));
			r.assign(t2, Functions.minus);

			// reorthogonalize to prevent accumulation of error
			V = LinearAlgebra.modifiedGramSchmidtUpdate(V, j);

			// beta_j = ||r||_2
			betas.setQuick(j, Math.sqrt(Algebra.DEFAULT.norm2(r)));

			// prepare to compute the eigenvalues and eigenvectors
			// of the tridiagonal matrix defined by a and b
			System.arraycopy(alphas.toArray(), 0, d, 1, j + 1);
			System.arraycopy(betas.toArray(), 0, e, 2, j);

			for (int p = 1; p <= j + 1; p++) {
				for (int q = 1; q <= j + 1; q++) {
					if (p == q) {
						S[p][q] = 1;
					} else {
						S[p][q] = 0;
					}
				}
			}

			// compute the eigenvalues and eigenvectors of the
			// tridiagonal matrix
			tridiagonalEigensolve(d, e, j + 1, S);

			// compute residuals and test accuracy
			// See:
			// http://www.cs.utk.edu/~dongarra/etemplates/node103.html#estimate_residual
			numFound = 0;
			for (int l = 0; l <= j; l++) {
				double residual = Math.abs(betas.getQuick(j) * S[j + 1][l + 1]);

				if (residual <= tolerance) {
					validEigenvalues[l] = true;
					numFound++;
				} else {
					validEigenvalues[l] = false;
				}
			}

			// break if enough eigenvalues have been found
			if (numFound >= k) {
				break;
			}

			j++;

			if (j >= n) {
				break;
			} else if (j >= s) {
				s = 2 * s;

				alphas = resize(alphas, s);
				betas = resize(betas, s);
				V = resize(V, s);
				d = resize(d, s + 1);
				e = resize(e, s + 1);
				S = resize(S, s + 1);
				validEigenvalues = resize(validEigenvalues, s + 1);
			}
		}

		this.eigenvalues = DoubleFactory1D.dense.make(numFound);
		this.eigenvectors = DoubleFactory2D.dense.make(n, numFound);
		DoubleMatrix2D ritzvectors = DoubleFactory2D.dense.make(S).viewPart(1, 1, s, s);

		int eigenIndex = 0;
		for (int col = 0; col < j; col++) {
			if (validEigenvalues[col]) {
				this.eigenvalues.setQuick(eigenIndex, d[col + 1]);
				this.eigenvectors.viewColumn(eigenIndex).assign(Algebra.DEFAULT.mult(V, ritzvectors.viewColumn(col)));
				eigenIndex++;
			}
		}
	}

	/**
	 * This function computes the eigenvalues and eigenvectors of a symmetric
	 * tridiagonal matrix. It is an adaptation of the procedure tqli from
	 * "Numerical Recipes in C", section 11.3. 
	 * 
	 * 
	 * @param diagonal the diagonal of the input matrix 
	 * @param superDiagonal the super-diagonal of the input matrix
	 * @param eigenvectors on input it contains the identity matrix, on output the eigenvectors
	 * 
	 */
	private void tridiagonalEigensolve(double[] diagonal, double[] superDiagonal, int n, double[][] eigenvectors) {
		int i;
		// Renumber elements of superDiagonal.
		for (i = 2; i <= n; i++) {
			superDiagonal[i - 1] = superDiagonal[i];
		}
		superDiagonal[n] = 0.0;
		for (int l = 1; l <= n; l++) {
			int iterations = 0;
			int m;
			do {
				for (m = l; m <= n - 1; m++) {
					double dd = Math.abs(diagonal[m]) + Math.abs(diagonal[m + 1]);
					if (Math.abs(superDiagonal[m]) + dd == dd) {
						break;
					}
				}
				if (m != l) {
					iterations = iterations + 1;
					if (iterations >= 30) {
						// TODO: handle error
					}
					// Form shift.
					double g = (diagonal[l + 1] - diagonal[l]) / (2.0 * superDiagonal[l]);
					double r = euclideanMagnitude(g, 1.0);
					// compute dm / ks.
					g = diagonal[m] - diagonal[l] + superDiagonal[l] / (g + copySign(r, g));
					double s, c;
					s = c = 1.0;
					double p = 0.0;
					// A plane rotation as in the original QL, followed by Givens rotations to restore tridiagonal form.
					for (i = m - 1; i >= l; i--) {
						double f = s * superDiagonal[i];
						double b = c * superDiagonal[i];
						superDiagonal[i + 1] = (r = euclideanMagnitude(f, g));
						// Recover from underflow.
						if (r == 0.0) {
							diagonal[i + 1] -= p;
							superDiagonal[m] = 0.0;
							break;
						}
						s = f / r;
						c = g / r;
						g = diagonal[i + 1] - p;
						r = (diagonal[i] - g) * s + 2.0 * c * b;
						diagonal[i + 1] = g + (p = s * r);
						g = c * r - b;
						// Form eigenvectors (optional).
						for (int k = 1; k <= n; k++) {
							f = eigenvectors[k][i + 1];
							eigenvectors[k][i + 1] = s * eigenvectors[k][i] + c * f;
							eigenvectors[k][i] = c * eigenvectors[k][i] - s * f;
						}
					}
					if (r == 0.0 && i >= l) {
						continue;
					}
					diagonal[l] -= p;
					superDiagonal[l] = g;
					superDiagonal[m] = 0.0;
				}
			} while (m != l);
		}
	}
	
	/**
	 * This function returns sign(b) * abs(a);
	 * @param a
	 * @param b
	 * @return
	 */
    private static double copySign(double a, double b) {
    	return Math.signum(b) * Math.abs(a);
    }
 
    /**
     * This function computes the value of sqrt(a^2 + b^2)
     * preventing overflow.
     * 
     * @param a
     * @param b
     * @return sqrt(a^2 + b^2)
     */
    private static double euclideanMagnitude(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
        	r = 0.0;
        }
        return r;
    }
    
    private DoubleMatrix1D resize(DoubleMatrix1D matrix, int length) { 
        DoubleMatrix1D result = DoubleFactory1D.dense.make(length); 
        for (int index = 0; index < matrix.size(); index++) { 
            result.setQuick(index, matrix.getQuick(index)); 
        } 
        return result; 
    } 
 
    private DoubleMatrix2D resize(DoubleMatrix2D matrix, int columns) { 
        DoubleMatrix2D result = DoubleFactory2D.dense.make(matrix.rows(), columns); 
        for (int col = 0; col < matrix.columns(); col++) { 
            result.viewColumn(col).assign(matrix.viewColumn(col)); 
        } 
        return result; 
    }
    
    private DoubleMatrix2D resize(DoubleMatrix2D matrix, int rows, int columns) { 
        DoubleMatrix2D result = DoubleFactory2D.dense.make(rows, columns); 
        for (int col = 0; col < matrix.columns(); col++) {
        	for (int row = 0; row < matrix.rows(); row++) {
        		result.setQuick(row, col, matrix.getQuick(row, col));
        	}
        } 
        return result; 
    } 
 
    private double[] resize(double[] array, int length) { 
        double[] result = new double[length]; 
        System.arraycopy(array, 0, result, 0, array.length); 
        return result; 
    } 
 
    private double[][] resize(double[][] array, int length) { 
        double[][] result = new double[length][length]; 
        for (int row = 0; row < array.length; row++) { 
            System.arraycopy(array[row], 0, result[row], 0, array.length); 
        } 
        return result; 
    } 
 
    private boolean[] resize(boolean[] array, int length) { 
        boolean[] result = new boolean[length]; 
        System.arraycopy(array, 0, result, 0, array.length); 
        return result; 
    } 
}