package edu.stanford.math.plex_plus.math.metric;

import java.util.Random;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class QuickSort {
	private static Random random = new Random();
	
	private QuickSort() {}
	
	public static void quickSort(int[] array) {
		quickSort(array, 0, array.length - 1);
	}
	
	public static void quickSort(int[] array, int startIndex, int endIndex) {
		if (startIndex < endIndex) {
			int partitionBoundaryIndex = partition(array, startIndex, endIndex);
			quickSort(array, startIndex, partitionBoundaryIndex);
			quickSort(array, partitionBoundaryIndex + 1, endIndex);
		}
	}
	
	public static void randomizedQuickSort(int[] array) {
		quickSort(array, 0, array.length - 1);
	}
	
	public static void randomizedQuickSort(int[] array, int startIndex, int endIndex) {
		if (startIndex < endIndex) {
			int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex);
			randomizedQuickSort(array, startIndex, partitionBoundaryIndex);
			randomizedQuickSort(array, partitionBoundaryIndex + 1, endIndex);
		}
	}
	
	/**
	 * 
	 * @param array
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static int partition(int[] array, int startIndex, int endIndex) {
		ExceptionUtility.verifyNonNegative(startIndex);
		ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
		ExceptionUtility.verifyLessThan(endIndex, array.length);
		
		int pivotValue = array[startIndex];
		int i = startIndex;
		int j = endIndex;
		
		while (true) {
			while (array[j] > pivotValue) {
				j--;
			}
			while (array[i] < pivotValue) {
				i++;
			}
			if (i < j) {
				swap(array, i, j);
				i++;
				j--;
			} else {
				return j;
			}
		}
	}
	
	public static int randomizedPartition(int[] array, int startIndex, int endIndex) {
		int i = startIndex + random.nextInt(endIndex - startIndex);
		swap(array, i, startIndex);
		return partition(array, startIndex, endIndex);
	}
	
	public static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static int randomizedSelect(int[] array, int rank) {
		return randomizedSelect(array, 0, array.length - 1, rank);
	}
	
	public static int randomizedSelect(int[] array, int startIndex, int endIndex, int rank) {
		if (startIndex == endIndex) {
			return array[startIndex];
		}
		
		int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex);
		int k = partitionBoundaryIndex - startIndex + 1;
		if (rank < k) {
			return randomizedSelect(array, startIndex, partitionBoundaryIndex, rank);
		} else {
			return randomizedSelect(array, partitionBoundaryIndex + 1, endIndex, rank - k);
		}
	}
}
