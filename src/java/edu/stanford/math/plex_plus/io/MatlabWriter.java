package edu.stanford.math.plex_plus.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * This class encapsulates writing to Matlab files. It contains functions for
 * basic output (e.g. comments), as well as the output of numerous mathematical
 * objects.
 * 
 * Important Note: The user must call close in order to flush the output to the
 * file.
 * 
 * @author Andrew Tausz
 * 
 */
public class MatlabWriter {
	// BufferedWriter object does the actual writing
	private BufferedWriter out = null;
	// the boolean commentMode indicates whether comments are being written
	private boolean commentMode = false;

	// maps for plot styles, color and markers
	private String[] markerMap = new String[] { "+", "o", "*", ".", "x", "s", "d", "^", "v", ">", "<", "p", "h" };
	private String[] colorMap = new String[] { "r", "g", "b", "c", "m", "y", "k" };
	private String[] styleMap = new String[] { "-", "--", ":", "-." };

	private int markerIndex = 0;
	private int colorIndex = 0;
	private int styleIndex = 0;

	/**
	 * This constructor initializes the class to be ready to write to the file
	 * with the filename provided.
	 * 
	 * @param filename
	 *            the file to write to
	 * @throws IOException
	 */
	public MatlabWriter(String filename) throws IOException {
		this.out = new BufferedWriter(new FileWriter(filename));
	}

	/**
	 * This function flushes the output stream and closes it.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.out.close();
	}

	/**
	 * This function turns on the comment mode, meaning that any output will be
	 * written as a comment and not Matlab code.
	 */
	public void turnOnCommentMode() {
		this.commentMode = true;
	}

	/**
	 * This function turns off the comment mode. Thus any output will be written
	 * as actual Matlab code.
	 */
	public void turnOffCommentMode() {
		this.commentMode = false;
	}

	/**
	 * This function modifies the input string so that it will be properly
	 * printed as a comment in Matlab.
	 * 
	 * @param input
	 * @throws IOException
	 */
	protected String insertComments(String input) throws IOException {
		if (this.commentMode) {
			String result = input;
			result = "% " + result.replaceAll("\n", "\n% ");
			return result;
		} else {
			return input;
		}
	}

	/**
	 * This function writes a string to the file. If comment mode is turned on,
	 * then the string is written as a comment.
	 * 
	 * @param string
	 *            the string to output
	 * @throws IOException
	 */
	public void write(String string) throws IOException {
		this.out.write(insertComments(string));
	}

	/**
	 * This function outputs a new line to the file.
	 * 
	 * @throws IOException
	 */
	public void newLine() throws IOException {
		this.out.newLine();
	}

	/**
	 * This function begins a new cell in the Matlab file.
	 * 
	 * @param label
	 *            the label of the cell
	 * @throws IOException
	 */
	public void startNewCell(String label) throws IOException {
		this.out.write("%% " + label);
	}

	public void newPlotWindow() throws IOException {
		this.write("figure;");
		this.newLine();
	}

	public void holdOn() throws IOException {
		this.write("hold on;");
		this.newLine();
	}

	public void holdOff() throws IOException {
		this.write("hold off;");
		this.newLine();
	}

	public void gridOn() throws IOException {
		this.write("grid on;");
		this.newLine();
	}

	public void gridOff() throws IOException {
		this.write("grid off;");
		this.newLine();
	}

	public String getMarkerStyle(int index) {
		return markerMap[index % markerMap.length];
	}

	public String getColorCode(int index) {
		return colorMap[index % colorMap.length];
	}

	public String getLineStyle(int index) {
		return styleMap[index % styleMap.length];
	}

	public String getNextMarker() {
		return this.getMarkerStyle(this.markerIndex++);
	}

	public String getNextColor() {
		return this.getColorCode(this.colorIndex++);
	}

	public String getNextLineStyle() {
		return this.getLineStyle(this.styleIndex++);
	}

	/*
	 * Vector to Stream
	 */

	public void writeRowVector(DoubleMatrix1D array, String name) throws IOException {
		this.write(toRowVector(array, name));
	}

	public void writeColumnVector(DoubleMatrix1D array, String name) throws IOException {
		this.write(toColumnVector(array, name));
	}

	public void writeRowVector(DoubleMatrix1D array) throws IOException {
		this.write(toRowVector(array));
	}

	public void writeColumnVector(DoubleMatrix1D array) throws IOException {
		this.write(toColumnVector(array));
	}

	public void writeRowVector(double[] array, String name) throws IOException {
		this.write(toRowVector(array, name));
	}

	public void writeColumnVector(double[] array, String name) throws IOException {
		this.write(toColumnVector(array, name));
	}

	public void writeRowVector(double[] array) throws IOException {
		this.write(toRowVector(array));
	}

	public void writeColumnVector(double[] array) throws IOException {
		this.write(toColumnVector(array));
	}

	public void writeRowVector(int[] array, String name) throws IOException {
		this.write(toRowVector(array, name));
	}

	public void writeColumnVector(int[] array, String name) throws IOException {
		this.write(toColumnVector(array, name));
	}

	public void writeRowVector(int[] array) throws IOException {
		this.write(toRowVector(array));
	}

	public void writeColumnVector(int[] array) throws IOException {
		this.write(toColumnVector(array));
	}

	/*
	 * Matrix to Stream
	 */

	public void writeMatrix(DoubleMatrix2D matrix, String name) throws IOException {
		this.out.write(name + " = ");
		this.out.write("[");
		for (int i = 0; i < matrix.rows(); i++) {
			if (i > 0) {
				this.out.write(";\n");
			}
			for (int j = 0; j < matrix.columns(); j++) {
				if (j > 0) {
					this.out.write(", ");
				}
				this.out.write(Double.toString(matrix.getQuick(i, j)));
			}
		}
		this.out.write("];");
	}

	public void writeMatrix(DoubleMatrix2D matrix) throws IOException {
		this.out.write("[");
		for (int i = 0; i < matrix.rows(); i++) {
			if (i > 0) {
				this.out.write(";\n");
			}
			for (int j = 0; j < matrix.columns(); j++) {
				if (j > 0) {
					this.out.write(", ");
				}
				this.out.write(Double.toString(matrix.getQuick(i, j)));
			}
		}
		this.out.write("];");
	}

	public void writeMatrix(double[][] matrix, String name) throws IOException {
		this.write(toMatrix(matrix, name));
	}

	public void writeMatrix(double[][] matrix) throws IOException {
		this.write(toMatrix(matrix));
	}

	public void writeMatrix(int[][] matrix, String name) throws IOException {
		this.write(toMatrix(matrix, name));
	}

	public void writeMatrix(int[][] matrix) throws IOException {
		this.write(toMatrix(matrix));
	}

	/*
	 * Vector to String
	 */

	public static String toRowVector(DoubleMatrix1D array, String name) {
		return (name + " = " + toVector(array, ", ") + ";");
	}

	public static String toColumnVector(DoubleMatrix1D array, String name) {
		return (name + " = " + toVector(array, "; ") + ";");
	}

	public static String toRowVector(DoubleMatrix1D array) {
		return toVector(array, ", ");
	}

	public static String toColumnVector(DoubleMatrix1D array) {
		return toVector(array, "; ");
	}

	public static String toRowVector(int[] array, String name) {
		return (name + " = " + toVector(array, ", ") + ";");
	}

	public static String toColumnVector(int[] array, String name) {
		return (name + " = " + toVector(array, "; ") + ";");
	}

	public static String toRowVector(int[] array) {
		return toVector(array, ", ");
	}

	public static String toColumnVector(int[] array) {
		return toVector(array, "; ");
	}

	public static String toRowVector(double[] array, String name) {
		return (name + " = " + toVector(array, ", ") + ";");
	}

	public static String toColumnVector(double[] array, String name) {
		return (name + " = " + toVector(array, "; ") + ";");
	}

	public static String toRowVector(double[] array) {
		return toVector(array, ", ");
	}

	public static String toColumnVector(double[] array) {
		return toVector(array, "; ");
	}

	/*
	 * Matrix to String
	 */

	public static String toMatrix(DoubleMatrix2D matrix, String name) {
		return (name + " = " + toMatrix(matrix) + ";");
	}

	public static String toMatrix(double[][] matrix, String name) {
		return (name + " = " + toMatrix(matrix) + ";");
	}

	public static String toMatrix(int[][] matrix, String name) {
		return (name + " = " + toMatrix(matrix) + ";");
	}

	/*
	 * Helper functions
	 */

	private static String toVector(int[] array, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				stringBuilder.append(separator + array[i]);
			} else {
				stringBuilder.append(array[i]);
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	private static String toVector(double[] array, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				stringBuilder.append(separator + array[i]);
			} else {
				stringBuilder.append(array[i]);
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	private static String toVector(DoubleMatrix1D array, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < array.size(); i++) {
			if (i > 0) {
				stringBuilder.append(separator + array.getQuick(i));
			} else {
				stringBuilder.append(array.getQuick(i));
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	public static String toMatrix(DoubleMatrix2D matrix) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < matrix.rows(); i++) {
			if (i > 0) {
				stringBuilder.append(";\n");
			}
			for (int j = 0; j < matrix.columns(); j++) {
				if (j > 0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(matrix.getQuick(i, j));
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	public static String toMatrix(double[][] matrix) {
		int m = matrix.length;
		if (m == 0) {
			return "[]";
		}
		int n = matrix[0].length;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < m; i++) {
			if (i > 0) {
				stringBuilder.append(";\n");
			}
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(matrix[i][j]);
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	public static String toMatrix(int[][] matrix) {
		int m = matrix.length;
		if (m == 0) {
			return "[]";
		}
		int n = matrix[0].length;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i = 0; i < m; i++) {
			if (i > 0) {
				stringBuilder.append(";\n");
			}
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(matrix[i][j]);
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/*
	 * String output
	 */

	public static String toStringList(List<String> array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < array.size(); i++) {
			if (i > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append("\'");
			stringBuilder.append(array.get(i));
			stringBuilder.append("\'");
		}
		return stringBuilder.toString();
	}
}
