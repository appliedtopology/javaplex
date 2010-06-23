package edu.stanford.math.plex_plus.utility;

import java.util.List;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;

public class MatrixUtility {
	protected static Normal normalGenerator = new Normal(0, 1,
			new cern.jet.random.engine.MersenneTwister(Uniform
					.staticNextIntFromTo(0, Integer.MAX_VALUE)));

	/*
	 * Compatibility validation
	 */
	public static void checkProductCompatibility(DoubleMatrix2D matrix,
			DoubleMatrix1D vector) {
		if (matrix.columns() != vector.size()) {
			throw new IllegalArgumentException(
					"Invalid matrix-vector product: matrix: [" + matrix.rows()
							+ " x " + matrix.columns() + "] vector: ["
							+ vector.size() + "]");
		}
	}

	public static void checkProductCompatibility(DoubleMatrix1D vector,
			DoubleMatrix2D matrix) {
		if (matrix.rows() != vector.size()) {
			throw new IllegalArgumentException(
					"Invalid vector-matrix product: vector: [" + vector.size()
							+ "] " + "matrix: [" + matrix.rows() + " x "
							+ matrix.columns() + "]");
		}
	}

	public static void checkProductCompatibility(DoubleMatrix2D matrix1,
			DoubleMatrix2D matrix2) {
		if (matrix1.columns() != matrix2.rows()) {
			throw new IllegalArgumentException(
					"Invalid matrix-vector product: matrix 1: ["
							+ matrix1.rows() + " x " + matrix1.columns()
							+ "] matrix 2: [" + matrix1.size() + " x "
							+ matrix1.columns() + "]");
		}
	}

	/*
	 * Type conversion
	 */
	
	public static DoubleMatrix1D toDoubleMatrix1D(double[] values) {
		int n = values.length;
		DoubleMatrix1D result = new DenseDoubleMatrix1D(n);
		for (int i = 0; i < n; i++) {
			result.setQuick(i, values[i]);
		}
		return result;
	}
	
	public static DoubleMatrix1D toDoubleMatrix1D(int[] values) {
		int n = values.length;
		DoubleMatrix1D result = new DenseDoubleMatrix1D(n);
		for (int i = 0; i < n; i++) {
			result.setQuick(i, values[i]);
		}
		return result;
	}
	
	public static DoubleMatrix2D toDiag(DoubleMatrix1D vector) {
		int n = vector.size();
		DoubleMatrix2D result = vector.like2D(n, n);
		for (int i = 0; i < n; i++) {
			result.setQuick(i, i, vector.getQuick(i));
		}
		return result;
	}
	
	/*
	 * Assignments, extraction, concatenation
	 */
	public static DoubleMatrix1D assign(DoubleMatrix1D vector, int[] values) {
		int n = vector.size();
		ExceptionUtility.verifyEqual(n, values.length);
		for (int i = 0; i < n; i++) {
			vector.setQuick(i, values[i]);
		}
		return vector;
	}
	
	public static DoubleMatrix2D copyColumn(DoubleMatrix2D source, DoubleMatrix2D dest, int sourceCol, int destCol) {
		ExceptionUtility.verifyEqual(source.rows(), dest.rows());
		ExceptionUtility.verifyLessThan(sourceCol, source.columns());
		ExceptionUtility.verifyLessThan(destCol, dest.columns());
		for (int i = 0; i < source.rows(); i++) {
			dest.setQuick(i, destCol, source.getQuick(i, sourceCol));
		}
		return dest;
	}
	
	/**
	 * 
	 * @param vector The vector to copy from
	 * @param start The index of the first element
	 * @param end One past the index of the last element
	 * @return A vector containing the elements {start, ... , end - 1}
	 */
	public static DoubleMatrix1D subvector(DoubleMatrix1D vector, int start, int end) {
		int length = end - start;
		DoubleMatrix1D result = vector.like(length);
		for (int i = 0; i < length; i++) {
			result.setQuick(i, vector.getQuick(i + start));
		}
		return result;
	}
	
	public static DoubleMatrix2D submatrix(DoubleMatrix2D matrix, int startRow, int endRow, int startColumn, int endColumn) {
		int rows = endRow - startRow;
		int columns = endColumn - startColumn;
		DoubleMatrix2D result = matrix.like(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result.setQuick(i, j, matrix.getQuick(startRow + i, startColumn + j));
			}
		}
		return result;
	}
	
	public static DoubleMatrix1D extractColumnCopy(DoubleMatrix2D matrix, int column) {
		int m = matrix.rows();
		DoubleMatrix1D vector = matrix.like1D(m);
		for (int i = 0; i < m; i++) {
			vector.setQuick(i, matrix.getQuick(i, column));
		}
		return vector;
	}

	public static DoubleMatrix1D extractRowCopy(DoubleMatrix2D matrix, int row) {
		int n = matrix.columns();
		DoubleMatrix1D vector = matrix.like1D(n);
		for (int j = 0; j < n; j++) {
			vector.setQuick(j, matrix.getQuick(row, j));
		}
		return vector;
	}

	public static DoubleMatrix2D writeColumn(DoubleMatrix2D matrix,
			DoubleMatrix1D vector, int column) {
		int m = matrix.rows();
		for (int i = 0; i < m; i++) {
			matrix.setQuick(i, column, vector.getQuick(i));
		}
		return matrix;
	}

	public static DoubleMatrix2D writeRow(DoubleMatrix2D matrix,
			DoubleMatrix1D vector, int row) {
		int n = matrix.columns();
		for (int j = 0; j < n; j++) {
			matrix.setQuick(row, j, vector.getQuick(j));
		}
		return matrix;
	}
	
	public static DoubleMatrix2D stackRows(List<DoubleMatrix1D> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int n = vectors.get(0).size();
		int m = vectors.size();
		DoubleMatrix2D result = vectors.get(0).like2D(m, n);
		for (int i = 0; i < m; i++) {
			ExceptionUtility.verifyEqual(n, vectors.get(i).size());
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, vectors.get(i).getQuick(j));
			}
		}
		return result;
	}
	
	public static DoubleMatrix2D stackColumns(List<DoubleMatrix1D> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int m = vectors.get(0).size();
		int n = vectors.size();
		DoubleMatrix2D result = vectors.get(0).like2D(m, n);
		for (int j = 0; j < n; j++) {
			ExceptionUtility.verifyEqual(m, vectors.get(j).size());
			for (int i = 0; i < m; i++) {
				result.setQuick(i, j, vectors.get(j).getQuick(i));
			}
		}
		return result;
	}
	
	/*
	 * Vector operations
	 */
	public static DoubleMatrix1D sum(DoubleMatrix1D vector1,
			DoubleMatrix1D vector2) {
		vector1.checkSize(vector2);
		DoubleMatrix1D result = vector1.like();
		int n = vector1.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, vector1.getQuick(i) + vector2.getQuick(i));
		}
		return result;
	}

	public static DoubleMatrix1D difference(DoubleMatrix1D vector1,
			DoubleMatrix1D vector2) {
		vector1.checkSize(vector2);
		DoubleMatrix1D result = vector1.like();
		int n = vector1.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, vector1.getQuick(i) - vector2.getQuick(i));
		}
		return result;
	}

	public static DoubleMatrix1D scalarMultiply(DoubleMatrix1D vector,
			double scalar) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, scalar * vector.getQuick(i));
		}
		return result;
	}

	public static DoubleMatrix1D scalarAdd(DoubleMatrix1D vector, double scalar) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, scalar + vector.getQuick(i));
		}
		return result;
	}

	public static DoubleMatrix1D negative(DoubleMatrix1D vector) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, -vector.getQuick(i));
		}
		return result;
	}

	/*
	 * Matrix operations
	 */
	public static DoubleMatrix2D sum(DoubleMatrix2D matrix1,
			DoubleMatrix2D matrix2) {
		matrix1.checkShape(matrix2);
		int m = matrix1.rows();
		int n = matrix1.columns();
		DoubleMatrix2D result = matrix1.like();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, matrix1.getQuick(i, j)
						+ matrix2.getQuick(i, j));
			}
		}
		return result;
	}

	public static DoubleMatrix2D difference(DoubleMatrix2D matrix1,
			DoubleMatrix2D matrix2) {
		matrix1.checkShape(matrix2);
		int m = matrix1.rows();
		int n = matrix1.columns();
		DoubleMatrix2D result = matrix1.like();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, matrix1.getQuick(i, j)
						- matrix2.getQuick(i, j));
			}
		}
		return result;
	}

	public static DoubleMatrix2D scalarMultiply(DoubleMatrix2D matrix,
			double scalar) {
		int m = matrix.rows();
		int n = matrix.columns();
		DoubleMatrix2D result = matrix.like();
		if (scalar == 0) {
			return result;
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, matrix.getQuick(i, j) * scalar);
			}
		}
		return result;
	}

	public static DoubleMatrix2D scalarAdd(DoubleMatrix2D matrix, double scalar) {
		int m = matrix.rows();
		int n = matrix.columns();
		DoubleMatrix2D result = matrix.like();
		if (scalar == 0) {
			return result;
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, matrix.getQuick(i, j) + scalar);
			}
		}
		return result;
	}

	public static DoubleMatrix2D negative(DoubleMatrix2D matrix, double scalar) {
		int m = matrix.rows();
		int n = matrix.columns();
		DoubleMatrix2D result = matrix.like();
		if (scalar == 0) {
			return result;
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, -matrix.getQuick(i, j));
			}
		}
		return result;
	}

	public static DoubleMatrix1D product(DoubleMatrix2D matrix,
			DoubleMatrix1D vector) {
		checkProductCompatibility(matrix, vector);
		return Algebra.DEFAULT.mult(matrix, vector);
	}

	public static DoubleMatrix1D product(DoubleMatrix1D vector,
			DoubleMatrix2D matrix) {
		checkProductCompatibility(vector, matrix);
		return Algebra.DEFAULT.mult(Algebra.DEFAULT.transpose(matrix), vector);
	}

	public static DoubleMatrix2D product(DoubleMatrix2D matrix1,
			DoubleMatrix2D matrix2) {
		checkProductCompatibility(matrix1, matrix2);
		return Algebra.DEFAULT.mult(matrix1, matrix2);
	}

	public static DoubleMatrix2D scaleRows(DoubleMatrix2D matrix, DoubleMatrix1D vector) {
		checkProductCompatibility(vector, matrix); 
		int m = matrix.rows();
		int n = matrix.columns();
		double multiplier = 0;
		for (int i = 0; i < m; i++) {
			multiplier = vector.getQuick(i);
			for (int j = 0; j < n; j++) {
				matrix.setQuick(i, j, multiplier * matrix.getQuick(i, j));
			}
		}
		return matrix;
	}
	
	public static DoubleMatrix2D scaleColumns(DoubleMatrix2D matrix, DoubleMatrix1D vector) {
		checkProductCompatibility(matrix, vector); 
		int m = matrix.rows();
		int n = matrix.columns();
		double multiplier = 0;
		for (int j = 0; j < n; j++) {
			multiplier = vector.getQuick(j);
			for (int i = 0; i < m; i++) {
				matrix.setQuick(i, j, multiplier * matrix.getQuick(i, j));
			}
		}
		return matrix;
	}

	/*
	 * Componentwise functions
	 */
	
	public static DoubleMatrix1D abs(DoubleMatrix1D vector) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, Math.abs(vector.getQuick(i)));
		}
		return result;
	}
	
	public static DoubleMatrix1D sqrt(DoubleMatrix1D vector) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, Math.sqrt(vector.getQuick(i)));
		}
		return result;
	}
	
	public static DoubleMatrix1D reciprocal(DoubleMatrix1D vector) {
		DoubleMatrix1D result = vector.like();
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			result.setQuick(i, 1 / vector.getQuick(i));
		}
		return result;
	}
	
	public static double max(DoubleMatrix1D vector) {
		double max = Infinity.Double.getNegativeInfinity();
		int n = vector.size();
		double entry;
		for (int i = 0; i < n; i++) {
			entry = vector.getQuick(i);
			if (entry > max) {
				max = entry;
			}
		}
		return max;
	}
	
	public static double min(DoubleMatrix1D vector) {
		double min = Infinity.Double.getPositiveInfinity();
		int n = vector.size();
		double entry;
		for (int i = 0; i < n; i++) {
			entry = vector.getQuick(i);
			if (entry < min) {
				min = entry;
			}
		}
		return min;
	}

	/*
	 * Norms, inner products and related functions
	 */
	
	public static double distance(DoubleMatrix1D vector1,
			DoubleMatrix1D vector2, double p) {
		vector1.checkSize(vector2);
		double sum = 0;
		int n = vector1.size();
		for (int i = 0; i < n; i++) {
			sum += Math.pow(
					Math.abs(vector1.getQuick(i) - vector2.getQuick(i)), p);
		}
		return Math.pow(sum, 1 / p);
	}
	
	public static double huberPenaltyFunction(DoubleMatrix1D vector1,
			DoubleMatrix1D vector2, double M) {
		vector1.checkSize(vector2);
		double sum = 0;
		int n = vector1.size();
		for (int i = 0; i < n; i++) {
			sum += MathUtility.huberPenaltyFunction(vector1.getQuick(i) - vector2.getQuick(i), M);
		}
		return sum;
	}

	public static double norm(DoubleMatrix1D vector, double p) {
		double sum = 0;
		int n = vector.size();
		for (int i = 0; i < n; i++) {
			sum += Math.pow(Math.abs(vector.getQuick(i)), p);
		}
		return Math.pow(sum, 1 / p);
	}

	public static double infinityNorm(DoubleMatrix1D vector) {
		return Algebra.DEFAULT.normInfinity(vector);
	}
	
	public static double innerProduct(DoubleMatrix1D vector1, DoubleMatrix1D vector2) {
		return Algebra.DEFAULT.mult(vector1, vector2);
	}

	public static double meanSquareDistance(DoubleMatrix1D vector1, DoubleMatrix1D vector2) {
		vector1.checkSize(vector2);
		double sum = 0;
		double difference = 0;
		int n = vector1.size();
		for (int i = 0; i < n; i++) {
			difference = vector1.getQuick(i) - vector2.getQuick(i);
			sum += difference * difference;
		}
		return sum / n;
	}
	
	public static double infinityNorm(DoubleMatrix2D matrix) {
		return Algebra.DEFAULT.normInfinity(matrix);
	}
	
	public static double oneNorm(DoubleMatrix2D matrix) {
		return Algebra.DEFAULT.norm1(matrix);
	}

	public static double frobeniusNorm(DoubleMatrix2D matrix) {
		return Algebra.DEFAULT.normF(matrix);
	}
	
	public static DoubleMatrix2D transpose(DoubleMatrix2D matrix) {
		int m = matrix.rows();
		int n = matrix.columns();
		DoubleMatrix2D result = matrix.like(n, m);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result.setQuick(j, i, matrix.getQuick(i, j));
			}
		}
		return result;
	}
	
	public static double innerProduct(DoubleMatrix2D matrix1, DoubleMatrix2D matrix2) {
		matrix1.checkShape(matrix2);
		double sum = 0;
		int m = matrix1.rows();
		int n = matrix1.columns();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += matrix1.getQuick(i, j) * matrix2.getQuick(i, j);
			}
		}
		return sum;
	}
	
	public static double trace(DoubleMatrix2D matrix) {
		double sum = 0;
		int m = Math.min(matrix.rows(), matrix.columns());
		for (int i = 0; i < m; i++) {
			sum += matrix.getQuick(i, i);
		}
		return sum;
	}

	public static DoubleMatrix1D normalize(DoubleMatrix1D vector) {
		double norm = norm(vector, 2);
		if (norm != 0) {
			return scalarMultiply(vector, 1 / norm);
		} else {
			return vector.like();
		}
	}
	
	public static double mean(DoubleMatrix1D array) {
		return array.zSum() / array.size();
	}
	
	public static double standardDeviation(DoubleMatrix1D array) {
		if (array.size() <= 1) {
			return 0;
		}
		int n = array.size();
		double mean = mean(array);
		double sd = 0;
		for (int i = 0; i < n; i++) {
			sd += (array.getQuick(i) - mean) * (array.getQuick(i) - mean);
		}
		sd = Math.sqrt(sd / ((double) (n - 1)));
		return sd;
	}
	
	public static double meanSquareDistance(DoubleMatrix2D matrix1, DoubleMatrix2D matrix2) {
		matrix1.checkShape(matrix2);
		double sum = 0;
		double difference = 0;
		int m = matrix1.rows();
		int n = matrix1.columns();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				difference = matrix1.getQuick(i, j) - matrix2.getQuick(i, j);
				sum += difference * difference;
			}
		}
		return sum / (m * n);
	}
	
	/*
	 * Random generation
	 */

	/**
	 * Generates a matrix with m rows and n columns with entries that are iid
	 * normally distributed.
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static DoubleMatrix2D randomNormalMatrix(int m, int n) {
		DoubleMatrix2D matrix = new DenseDoubleMatrix2D(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix.setQuick(i, j, normalGenerator.nextDouble());
			}
		}
		return matrix;
	}

	/**
	 * This function generates a vector with normally distributed entries.
	 * 
	 * @return
	 */
	public static DoubleMatrix1D randomNormalVector(int n) {
		DoubleMatrix1D vector = new DenseDoubleMatrix1D(n);
		for (int i = 0; i < n; i++) {
			vector.setQuick(i, normalGenerator.nextDouble());
		}
		return vector;
	}
	
	public static DoubleMatrix2D zScoreRows(DoubleMatrix2D matrix) {
		DoubleMatrix2D result = matrix.like();
		int m = matrix.rows();
		int n = matrix.columns();
		double standardDeviation = 0;
		double mean = 0;
		for (int i = 0; i < m; i++) {
			mean = mean(matrix.viewRow(i));
			standardDeviation = standardDeviation(extractRowCopy(matrix, i));
			for (int j = 0; j < n; j++) {
				result.setQuick(i, j, (matrix.getQuick(i, j) - mean) / standardDeviation);
			}
		}
		return result;
	}
}
