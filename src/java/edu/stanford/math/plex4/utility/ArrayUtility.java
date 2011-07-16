package edu.stanford.math.plex4.utility;

import java.util.Arrays;

public class ArrayUtility {
	public static double[][] getSubset(double[][] points, int[] indices) {
		double[][] result = new double[indices.length][];
		
		for (int i = 0; i < indices.length; i++) {
			result[i] = points[indices[i]];
		}
		
		return result;
	}
	
	public static boolean isMonotoneIncreasing(int[] a) {
		for (int i = 1; i < a.length; i++) {
			if (a[i] <= a[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	public static int[] makeMonotone(int[] a) {
		int[] temp = ArrayUtility.copyOf(a, a.length);
		Arrays.sort(temp);
		int k = 0, i = 0;
		int[] result = new int[temp.length];
		while (i < temp.length && k < result.length) {
			if (k > 0 && result[k - 1] == temp[i]) {
				i++;
				continue;
			}
			result[k] = temp[i];
			i++;
			k++;
		}
		return ArrayUtility.copyOf(result, k);
	}
	
	public static int[] union(int[] a, int[] b) {
		int i = 0, j = 0, k = 0;

		int[] temp = new int[a.length + b.length];
		
		while (i < a.length && j < b.length) {
			if (a[i] < b[j]) {
				temp[k] = a[i];
				k++;
				i++;
			} else if (a[i] > b[j]) {
				temp[k] = b[j];
				k++;
				j++;
			} else {
				temp[k] = a[i];
				i++;
				j++;
				k++;
			}
		}
		
		while (i < a.length) {
			temp[k] = a[i];
			i++;
			k++;
		}
		
		while (j < b.length) {
			temp[k] = b[j];
			j++;
			k++;
		}
		
		return ArrayUtility.copyOf(temp, k);
	}
	
	public static double[] copyOf(double[] array, int length) {
		double[] result = new double[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static int[] copyOf(int[] array, int length) {
		int[] result = new int[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static float[] copyOf(float[] array, int length) {
		float[] result = new float[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static short[] copyOf(short[] array, int length) {
		short[] result = new short[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static long[] copyOf(long[] array, int length) {
		long[] result = new long[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static boolean[] copyOf(boolean[] array, int length) {
		boolean[] result = new boolean[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static byte[] copyOf(byte[] array, int length) {
		byte[] result = new byte[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static char[] copyOf(char[] array, int length) {
		char[] result = new char[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
	
	public static Object[] copyOf(Object[] array, int length) {
		Object[] result = new Object[length];
		
		int n = Math.min(length, array.length);
		
		for (int i = 0; i < n; i++) {
			result[i] = array[i];
		}
		
		return result;
	}
}
