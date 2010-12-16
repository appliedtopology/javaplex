package edu.stanford.math.plex4.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DoubleArrayReaderWriter implements ObjectReader<double[][]>, ObjectWriter<double[][]> {
	private static final DoubleArrayReaderWriter instance = new DoubleArrayReaderWriter();

	private DoubleArrayReaderWriter() {}

	public static DoubleArrayReaderWriter getInstance() {
		return instance;
	}

	public double[][] importFromFile(String path) throws IOException {
		double[][] matrix = null;
		try {
			List<double[]> rows = FileIOUtility.readNumericCSVFile(path, ",");
			int n = rows.size();
			matrix = new double[n][];
			for (int i = 0; i < n; i++) {
				matrix[i] = rows.get(i);
			}

			return matrix;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getExtension() {
		return "csv";
	}

	public void writeToFile(double[][] object, String path) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path, false));
			
			for (double[] row: object) {
				for (int i = 0; i < row.length; i++) {
					if (i > 0) {
						writer.write(",");
					}
					writer.write(Double.toString(row[i]));
				}
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
