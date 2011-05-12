package edu.stanford.math.plex4.io;

import java.io.IOException;

import cern.colt.matrix.DoubleMatrix2D;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;

/**
 * This class has static methods for creating Matlab files to visualize data.
 * 
 * @author Andrew Tausz
 */

public class MatlabInterface{

	protected static String getDefaultExtension() {
		return "m";
	}

	/**
	 * This function creates and returns a new MatlabWriter.
	 * 
	 * @param label
	 *            a keyword for the filename
	 * @return a new MatlabWriter ready to use
	 * @throws IOException
	 */
	public static MatlabWriter makeNewMatlabWriter(String label) throws IOException {
		String filename = FileManager.getUniqueFilePath(label, getDefaultExtension());
		MatlabWriter writer = new MatlabWriter(filename);
		return writer;
	}

	/**
	 * This function cleans up a MatlabWriter when it is no longer in use.
	 * 
	 * @param writer
	 *            the MatlabWriter to clean up
	 * @throws IOException
	 */
	public static void finalizeMatlabWriter(MatlabWriter writer) throws IOException {
		writer.close();
	}

	/**
	 * This function writes the commands to create a scatter plot of the data in
	 * the supplied matrix. The matrix is assumed to have two columns, so that
	 * each row represents an (x, y) pair.
	 * 
	 * @param writer
	 *            the MatlabWriter to write to
	 * @param values
	 *            an n x 2 matrix containing the data to plot
	 * @param name
	 *            the variable name to write to
	 * @throws IOException
	 */
	public static void exportScatterPlot2D(MatlabWriter writer, DoubleMatrix2D values, String name) throws IOException {
		ExceptionUtility.verifyGreaterThanOrEqual(values.columns(), 2);
		writer.writeMatrix(values, name);
		writer.newLine();
		writer.write("plot(" + name + "(:, 1), " + name + "(:, 2), '" + writer.getNextColor() + writer.getNextMarker() + "');");
		writer.newLine();
	}

	/**
	 * This function writes the commands to create a 3-D scatter plot of the
	 * data in the supplied matrix. The data is assumed to have three columns,
	 * so that each row represents an (x, y, z) triple.
	 * 
	 * @param writer
	 *            the MatlabWriter to write to
	 * @param values
	 *            an n x 3 matrix containing the data to plot
	 * @param name
	 *            the variable name to write to
	 * @throws IOException
	 */
	public static void exportScatterPlot3D(MatlabWriter writer, DoubleMatrix2D values, String name) throws IOException {
		ExceptionUtility.verifyGreaterThanOrEqual(values.columns(), 3);
		writer.writeMatrix(values, name);
		writer.newLine();
		writer.write("plot3(" + name + "(:, 1), " + name + "(:, 2), " + name + "(:, 3), '" + writer.getNextColor() + writer.getNextMarker() + "');");
		writer.newLine();
	}

	/**
	 * This function writes the commands to create a scatter plot of the data in
	 * the supplied matrix. The matrix is assumed to have two columns, so that
	 * each row represents an (x, y) pair. Additionally the points are plotted
	 * with different markers and colors for each class.
	 * 
	 * @param writer
	 *            the MatlabWriter to write to
	 * @param values
	 *            an n x 3 matrix containing the data to plot
	 * @param name
	 *            the variable name to write to
	 * @param labels
	 *            the classes for each data point
	 * @throws IOException
	 */
	public static void exportScatterPlot2D(MatlabWriter writer, DoubleMatrix2D values, String name, int[] labels) throws IOException {
		ExceptionUtility.verifyGreaterThanOrEqual(values.columns(), 2);
		int maxLabel = IntArrayMath.max(labels);
		int minLabel = IntArrayMath.min(labels);
		writer.writeMatrix(values, name);
		writer.newLine();
		writer.writeRowVector(labels, "labels");
		writer.newLine();
		writer.newPlotWindow();
		writer.holdOn();
		for (int label = minLabel; label <= maxLabel; label++) {
			writer.write("[r, c, v] = find(labels == " + label + ");");
			writer.newLine();
			writer.write("plot(" + name + "(c, 1), " + name + "(c, 2), '" + writer.getColorCode(label) + writer.getMarkerStyle(label) + "');");
			writer.newLine();
		}
		writer.holdOff();
	}

	/**
	 * This function writes the commands to create a 3-D scatter plot of the
	 * data in the supplied matrix. The data is assumed to have three columns,
	 * so that each row represents an (x, y, z) triple. Additionally the points
	 * are plotted with different markers and colors for each class.
	 * 
	 * @param writer
	 *            the MatlabWriter to write to
	 * @param values
	 *            an n x 3 matrix containing the data to plot
	 * @param name
	 *            the variable name to write to
	 * @param labels
	 *            the classes for each data point
	 * @throws IOException
	 */
	public static void exportScatterPlot3D(MatlabWriter writer, DoubleMatrix2D values, String name, int[] labels) throws IOException {
		ExceptionUtility.verifyGreaterThanOrEqual(values.columns(), 3);
		int maxLabel = IntArrayMath.max(labels);
		int minLabel = IntArrayMath.min(labels);
		writer.writeMatrix(values, name);
		writer.newLine();
		writer.writeRowVector(labels, "labels");
		writer.newLine();
		writer.newPlotWindow();
		writer.holdOn();
		for (int label = minLabel; label <= maxLabel; label++) {
			writer.write("[r, c, v] = find(labels == " + label + ");");
			writer.newLine();
			writer.write("plot3(" + name + "(c, 1), " + name + "(c, 2), " + name + "(c, 3), '" + writer.getColorCode(label) + writer.getMarkerStyle(label) + "');");
			writer.newLine();
		}
		writer.holdOff();
	}
}
