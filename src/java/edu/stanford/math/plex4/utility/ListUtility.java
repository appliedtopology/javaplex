package edu.stanford.math.plex4.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.math.plex4.array_utility.IntArrayManipulation;

public class ListUtility {
	
	public static String toString(Collection<int[]> list) {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		for (int[] element: list) {
			builder.append(ArrayUtility2.toString(element));
			builder.append("\n");
		}
		builder.append('}');
		return builder.toString();
	}
	
	public static List<Integer> toList(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static List<Double> toList(double[] array) {
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static <T> List<T> toList(T[] array) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	/**
	 * This function extracts the entries of the specified array
	 * which have index in the indices array.
	 * @param <T>
	 * @param array the array of values
	 * @param indices the indices to select
	 * @return a sub-array containing those values at the specified indices
	 */
	public static <T> List<T> getArraySubset(List<T> array, int[] indices) {
		List<T> subArray = new ArrayList<T>();
		for (int i = 0; i < indices.length; i++) {
			subArray.add(array.get(indices[i]));
		}
		return subArray;
	}
	
	/**
	 * This function extracts the entries of the array which have index
	 * not in the indices array. In other words it performs the complement
	 * of getArraySubset.
	 * @param <T>
	 * 
	 * @param array the array of values
	 * @param indices the indices to omit
	 * @return a sub-array containing the values excluding those at the specified indices
	 */
	public static <T> List<T> getArraySubsetComplement(List<T> array, int[] indices) {
		indices = IntArrayManipulation.sortAscending(indices);
		List<T> subArray = new ArrayList<T>();
		//int subArrayIndex = 0;
		int omissionIndex = 0;
		for (int i = 0; i < array.size(); i++) {
			if (omissionIndex < indices.length && i == indices[omissionIndex]) {
				omissionIndex++;
			} else {
				subArray.add(array.get(i));
			}
		}
		return subArray;
	}
}
