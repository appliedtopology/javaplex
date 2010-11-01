/**
 * 
 */
package edu.stanford.math.plex4.embedding;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.doublealgo.Sorting;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;

/**
 * @author Andris
 *
 */
public class MultidimensionalScaling implements MetricSpaceEmbedding {
	private static final MultidimensionalScaling instance = new MultidimensionalScaling();
	
	private MultidimensionalScaling(){}
	
	public static MultidimensionalScaling getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.embedding.MetricSpaceEmbedding#computedEmbedding(edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace, int)
	 */
	public double[][] computedEmbedding(AbstractIntMetricSpace metricSpace, int dimension) {
		return this.doScaling(metricSpace, dimension, TOLERANCE).toArray();
	}
	
	//protected DoubleMatrix2D distanceMatrix; // distance matrix
	//protected DoubleMatrix2D P; // rows contain Euclidean points
	//protected int p; // the dimension of each point
	//protected int n; // the number of points
	//protected double[] eigenvalues;
	public static double TOLERANCE = 0.00000001d;

	/**
	 * Implements the classical multidimensional scaling (MDS) scaling algorithm
	 * defined on pages 22-30 of the book: Cox, T. and Cox, M.,
	 * "Multidimensional Scaling", Chapman & Hall, 1994.
	 * 
	 * @param D distance matrix
	 * @throws MDSException
	 */
	/*
	public MDS(DoubleMatrix2D D) {
		this.distanceMatrix = D;
		ExceptionUtility.verifyEqual(this.distanceMatrix.rows(), this.distanceMatrix.columns());
		doScaling(D, 0, TOLERANCE);
	}

	public MDS(DoubleMatrix2D D, int dim) {
		this.distanceMatrix = D;
		ExceptionUtility.verifyEqual(this.distanceMatrix.rows(), this.distanceMatrix.columns());
		doScaling(D, dim, TOLERANCE);
	}
	 */
	
	protected DoubleMatrix2D doScaling(AbstractIntMetricSpace D, int dim, double tol) {
		// Build the intermediate matrix B
		DoubleMatrix2D B = buildB(D);
		DoubleMatrix2D P;
		int n = D.size();
		
		// Perform the eigenvalue decomposition
		EigenvalueDecomposition decomp;
		try {
			decomp = new EigenvalueDecomposition(B);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
					"Cannot compute eigenvalue decomposition.");
		}

		// Get the diagonal matrix of eigenvalues
		DoubleMatrix2D L = decomp.getD();

		int[] filter;
		if (dim == 0) { // Get the indices of the nonzero eigenvalues
			filter = findPositiveEntries(tol, L);
		} else {
			filter = findTopKPositiveEntries(dim, L);
		}

		// Fill up indices with values 0:n-1
		int[] indices = new int[n];
		for (int i = 0; i < n; i++) {
			indices[i] = i;
		}

		// Save the eigenvalues for later analysis
		double[] eigenvalues = decomp.getRealEigenvalues().toArray();

		// Reduce and multiply the matrices together
		DoubleMatrix2D L1 = L.viewSelection(filter, filter);
		DoubleMatrix2D V = decomp.getV();
		DoubleMatrix2D V1 = V.viewSelection(indices, filter);

		/*
		 * V1 is already normalized, but if it weren't then here is the code: //
		 * Normalize each column of the matrix V1 Algebra algebra = new
		 * Algebra(); DoubleMatrix1D col; double divisor; for (int i=0;
		 * i<V1.columns(); i++) { col = V1.viewColumn(i); divisor =
		 * algebra.norm2(col); col.assign(Functions.div(divisor)); }
		 */

		// Take the square root of each diagonal element in L1.
		double value;
		for (int i = 0; i < L1.columns(); i++) {
			value = L1.getQuick(i, i);
			L1.setQuick(i, i, Math.sqrt(Math.abs(value)));
		}

		// Make the matrix of Euclidean points.
		P = V1.zMult(L1, null);

		int p = P.rows();
		if (p == 0) {
			throw new IllegalArgumentException("The MDS algorithm failed to recover any points.");
		}
		
		return P;
	}

	/**
	 * Returns the row indices of the largest k diagonal elements in D. Not my
	 * best work but it works.
	 * 
	 * @param k
	 * @param D a diagonal matrix
	 * @return
	 */
	// TODO: not sure if this works when some values are negative
	protected int[] findTopKPositiveEntries(int k, DoubleMatrix2D D) {
		int[] indices = new int[k];
		int n = D.rows();

		// Extract the diagonal elements
		DoubleMatrix1D diag = new DenseDoubleMatrix1D(n);
		for (int i = 0; i < n; i++) {
			diag.setQuick(i, D.getQuick(i, i));
		}

		// Sort the diagonal elements in ascending order
		DoubleMatrix1D S = Sorting.quickSort.sort(diag);

		// In matlab pseudocode, perform find(D>=a) for a=S(n-k+1:n)
		int cnt = 0;
		outer: for (int i = 0; i < n; i++) {
			double curval = D.getQuick(i, i);
			inner: for (int j = n - k; j < n; j++) {
				if (S.getQuick(j) == curval) {
					indices[cnt] = i;
					cnt++;
					if (cnt == k) {
						// this is necessary if there are duplicate
						// elements in the top k list
						break outer;
					}
					break inner;
				}
			}
		}
		return indices;
	}

	/**
	 * @param tol
	 * @param D a diagonal matrix
	 * @return
	 */
	protected int[] findPositiveEntries(double tol, DoubleMatrix2D D) {
		int n = D.size();
		// Get the indices of the positive entries of L
		int[] indices = new int[n];
		int cnt = 0;
		for (int i = 0; i < n; i++) {
			if (D.getQuick(i, i) > tol) {
				indices[cnt] = i;
				cnt += 1;
			}
		}
		// Remove empty items at the end (i.e. shrink the size of the list)
		int[] nonzero = new int[cnt];
		for (int i = 0; i < cnt; i++) {
			nonzero[i] = indices[i];
		}
		return nonzero;
	}

	protected DoubleMatrix2D buildB(AbstractIntMetricSpace D) {
		int n = D.size();
		double ndub = n;
		DoubleMatrix2D B = new DenseDoubleMatrix2D(n, n);

		// Store a vector of dot products of each row with itself
		double val0 = 0;
		DoubleMatrix2D rowdots = new DenseDoubleMatrix2D(n, 1);
		for (int i = 0; i < n; i++) { // rows
			for (int j = 0; j < n; j++) { // column
				val0 += Math.pow(D.distance(i, j), 2d);
			}
			rowdots.setQuick(i, 0, val0);
			val0 = 0;
		}

		// Store a vector of dot products of each column with itself
		double val1 = 0;
		DoubleMatrix2D coldots = new DenseDoubleMatrix2D(1, n);
		for (int j = 0; j < n; j++) { // rows
			for (int i = 0; i < n; i++) { // column
				val1 += Math.pow(D.distance(i, j), 2d);
			}
			rowdots.setQuick(0, j, val1);
			val1 = 0;
		}

		// Get the sum of the squares of all the elements of D
		double sumsquares = 0;
		for (int i = 0; i < n; i++) {
			sumsquares += rowdots.getQuick(i, 0);
		}

		// Construct the matrix B
		double val2 = 0;
		double oneOverN = 1d / ndub;
		double oneOverNSquared = (1d / Math.pow(ndub, 2d));
		for (int r = 0; r < n; r++) {
			for (int s = 0; s < n; s++) {
				val2 = Math.pow(D.distance(r, s), 2d);
				val2 -= oneOverN * rowdots.getQuick(r, 0);
				val2 -= oneOverN * coldots.getQuick(0, s);
				val2 += oneOverNSquared * sumsquares;
				val2 *= -0.5d;
				B.setQuick(r, s, val2);
			}
		}
		return B;
	}

	/**
	 * Returns a matrix of the distance differences between A and B. Matrix A is
	 * obtained by computing euclidean distance between MDS points. Matrix B is
	 * the original set of distances that MDS tried to preserve.
	 * 
	 * @return A matrix of differences between actual and MDS generated
	 *         distances.
	 */
	/*
	public DoubleMatrix2D getDistortionMatrix(DoubleMatrix2D D, DoubleMatrix2D P) {
		
		DoubleMatrix2D mdsDistances = new DenseDoubleMatrix2D(n, n);
		DoubleMatrix2D distortion = new DenseDoubleMatrix2D(n, n);
		double[] xi, xj;
		double value;
		for (int i = 0; i < n; i++) {
			xi = getPoint(i);
			for (int j = 0; j < n; j++) {
				xj = getPoint(j);
				value = ArrayUtility.distance(xi, xj, 2);
				mdsDistances.setQuick(i, j, value);
				distortion.setQuick(i, j, Math.abs(value - distanceMatrix.getQuick(i, j)));
			}
		}
		return distortion;
	}
*/
	/**
	 * @return a matrix obtained by computing euclidean distance between MDS points
	 */
	/*
	public DoubleMatrix2D getDistanceMatrix() {
		DoubleMatrix2D mdsDistances = new DenseDoubleMatrix2D(n, n);
		double[] xi, xj;
		double value;
		for (int i = 0; i < n; i++) {
			xi = getPoint(i);
			for (int j = 0; j < n; j++) {
				xj = getPoint(j);
				value = ArrayUtility.distance(xi, xj, 2);
				mdsDistances.setQuick(i, j, value);
			}
		}
		return mdsDistances;
	}
*/
	/**
	 * Returns the normalized distortion, which is the norm of the distortion
	 * matrix divided by the norm of the original distance matrix.
	 * 
	 * @return
	 */
	/*
	public double getDistortion() {
		DoubleMatrix2D distortion = getDistortionMatrix();
		Algebra algebra = new Algebra();
		double numerator = algebra.norm2(distortion);
		double denominator = algebra.norm2(this.distanceMatrix);
		return numerator / denominator;
	}
	*/

}
