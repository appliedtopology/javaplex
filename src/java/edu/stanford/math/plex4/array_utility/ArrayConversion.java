package edu.stanford.math.plex4.array_utility;

import java.util.Collection;

import cern.colt.matrix.DoubleMatrix1D;

public class ArrayConversion {
	public static double[] toDoubleArray(int[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i];
		}
		return result;
	}
	
	public static int[] toIntArray(double[] vector) {
		int n = vector.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = (int) vector[i];
		}
		return result;
	}
	
	public static double[] toDoubleArray(DoubleMatrix1D vector) {
		int n = vector.size();
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector.getQuick(i);
		}
		return result;
	}
	
	public static double[] toDoubleArray(Collection<Double> collection) {
		int n = collection.size();
		double[] result = new double[n];
		int index = 0;
		for (Double element : collection) {
			result[index] = element;
			index++;
		}
		return result;
	}
	
	public static int[] toIntArray(Collection<Integer> collection) {
		int n = collection.size();
		int[] result = new int[n];
		int index = 0;
		for (Integer element : collection) {
			result[index] = element;
			index++;
		}
		return result;
	}
	
	public static double[][] toMatrix(Collection<double[]> collection) {
		double[][] result = new double[collection.size()][];
		int index = 0;
		
		for (double[] array: collection) {
			result[index++] = array;
		}
		
		return result;
	}
}
