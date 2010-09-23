package edu.stanford.math.plex4.utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * This class contains an implement of the quicksort and other related
 * algorithms, such as the selection of order statistics.
 * 
 * It is based on chapter 8 and 10 of "Introduction to 
 * Algorithms" by Cormen, Leiserson and Rivest. 
 * 
 * @author Andrew Tausz
 *
 */
public class Quicksort {
	private static Random random = new Random();

	private Quicksort() {}

	public static class Int {
		/**
		 * Performs the quicksort algorithm in place on the supplied array.
		 * 
		 * @param array the array to sort
		 */
		public static void quicksort(int[] array) {
			quicksort(array, 0, array.length - 1);
		}

		/**
		 * Performs the quicksort algorithm in place on a subarray of the 
		 * supplied array. 
		 * 
		 * @param array the array to sort
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 */
		public static void quicksort(int[] array, int startIndex, int endIndex) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.length);

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = partition(array, startIndex, endIndex);
				quicksort(array, startIndex, partitionBoundaryIndex);
				quicksort(array, partitionBoundaryIndex + 1, endIndex);
			}
		}

		/**
		 * This performs the randomized quicksort algorithm in place on the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array
		 */
		public static void randomizedQuicksort(int[] array) {
			quicksort(array, 0, array.length - 1);
		}

		/**
		 * This performs the randomized quicksort algorithm in place on a subarray of the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array the array to sort
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 */
		public static void randomizedQuicksort(int[] array, int startIndex, int endIndex) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.length);

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex);
				randomizedQuicksort(array, startIndex, partitionBoundaryIndex);
				randomizedQuicksort(array, partitionBoundaryIndex + 1, endIndex);
			}
		}

		/**
		 * The fundamental partition operation for quicksort. This function partitions
		 * the array into two parts determined by the pivot element array[startIndex].
		 * It arranges the array so that the first section contains elements less than
		 * or equal to each element of the second section. The last index of the first
		 * section is returned. 
		 * 
		 * @param array the array to arrange
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @return the last index of the first partition
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

		/**
		 * This function performs the same function as the partition function, except that
		 * it uses a randomly chosen pivot. 
		 * 
		 * @param array the array to arrange
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @return the last index of the first partition
		 */
		public static int randomizedPartition(int[] array, int startIndex, int endIndex) {
			int i = startIndex + random.nextInt(endIndex - startIndex);
			swap(array, i, startIndex);
			return partition(array, startIndex, endIndex);
		}

		/**
		 * This function returns the i-th order statistic of the data in the supplied array.
		 * It also modifies the array so that the i-th order statistic is in the i-th position
		 * of the array, for j < i array[j] <= array[i], and for j > i array[j] >= array[i].
		 * In other words it partially sorts the array so that it is partitioned into the subset
		 * of elements less or equal to the i-th order statistic, and greater than or equal it. 
		 * 
		 * If one wanted to find the median of a list of numbers, then one could call
		 * randomizedSelect(array, array.length / 2). 
		 * 
		 * @param array the array to search
		 * @param i the rank of the order statistic to find
		 * @return the i-th order statistic
		 */
		public static int randomizedSelect(int[] array, int i) {
			return randomizedSelect(array, 0, array.length - 1, i);
		}

		/**
		 * This function performs the randomizedSelect on a subarray of the supplied array. It
		 * operates on the subarray <code>array[startIndex, ..., endIndex]</code>. 
		 * 
		 * @param array the array to search
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @param i he rank of the order statistic to find
		 * @return the i-th order statistic
		 */
		public static int randomizedSelect(int[] array, int startIndex, int endIndex, int i) {
			if (startIndex == endIndex) {
				return array[startIndex];
			}

			int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex);
			int k = partitionBoundaryIndex - startIndex + 1;
			if (i < k) {
				return randomizedSelect(array, startIndex, partitionBoundaryIndex, i);
			} else {
				return randomizedSelect(array, partitionBoundaryIndex + 1, endIndex, i - k);
			}
		}
		
		private static void swap(int[] array, int i, int j) {
			int temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}

	public static class GenericArray<T> {
		/**
		 * Performs the quicksort algorithm in place on the supplied array.
		 * 
		 * @param array the array to sort
		 */
		public static <T> void quicksort(T[] array, Comparator<T> comparator) {
			quicksort(array, 0, array.length - 1, comparator);
		}

		/**
		 * Performs the quicksort algorithm in place on a subarray of the 
		 * supplied array. 
		 * 
		 * @param array the array to sort
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 */
		public static <T> void quicksort(T[] array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.length);

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = partition(array, startIndex, endIndex, comparator);
				quicksort(array, startIndex, partitionBoundaryIndex, comparator);
				quicksort(array, partitionBoundaryIndex + 1, endIndex, comparator);
			}
		}

		/**
		 * This performs the randomized quicksort algorithm in place on the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array
		 */
		public static <T> void randomizedQuicksort(T[] array, Comparator<T> comparator) {
			quicksort(array, 0, array.length - 1, comparator);
		}

		/**
		 * This performs the randomized quicksort algorithm in place on a subarray of the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array the array to sort
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 */
		public static <T> void randomizedQuicksort(T[] array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.length);

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex, comparator);
				randomizedQuicksort(array, startIndex, partitionBoundaryIndex, comparator);
				randomizedQuicksort(array, partitionBoundaryIndex + 1, endIndex, comparator);
			}
		}

		/**
		 * The fundamental partition operation for quicksort. This function partitions
		 * the array into two parts determined by the pivot element array[startIndex].
		 * It arranges the array so that the first section contains elements less than
		 * or equal to each element of the second section. The last index of the first
		 * section is returned. 
		 * 
		 * @param array the array to arrange
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @return the last index of the first partition
		 */
		public static <T> int partition(T[] array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.length);

			T pivotValue = array[startIndex];
			int i = startIndex;
			int j = endIndex;

			while (true) {
				while (comparator.compare(array[j], pivotValue) > 0) {
					j--;
				}
				while(comparator.compare(array[i], pivotValue) < 0) {
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

		/**
		 * This function performs the same function as the partition function, except that
		 * it uses a randomly chosen pivot. 
		 * 
		 * @param array the array to arrange
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @return the last index of the first partition
		 */
		public static <T> int randomizedPartition(T[] array, int startIndex, int endIndex, Comparator<T> comparator) {
			int i = startIndex + random.nextInt(endIndex - startIndex);
			swap(array, i, startIndex);
			return partition(array, startIndex, endIndex, comparator);
		}

		/**
		 * This function returns the i-th order statistic of the data in the supplied array.
		 * It also modifies the array so that the i-th order statistic is in the i-th position
		 * of the array, for j < i array[j] <= array[i], and for j > i array[j] >= array[i].
		 * In other words it partially sorts the array so that it is partitioned into the subset
		 * of elements less or equal to the i-th order statistic, and greater than or equal it. 
		 * 
		 * If one wanted to find the median of a list of numbers, then one could call
		 * randomizedSelect(array, array.length / 2). 
		 * 
		 * @param array the array to search
		 * @param i the rank of the order statistic to find
		 * @return the i-th order statistic
		 */
		public static <T> T randomizedSelect(T[] array, int i, Comparator<T> comparator) {
			return randomizedSelect(array, 0, array.length - 1, i, comparator);
		}

		/**
		 * This function performs the randomizedSelect on a subarray of the supplied array. It
		 * operates on the subarray <code>array[startIndex, ..., endIndex]</code>. 
		 * 
		 * @param array the array to search
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 * @param i he rank of the order statistic to find
		 * @return the i-th order statistic
		 */
		public static <T> T randomizedSelect(T[] array, int startIndex, int endIndex, int i, Comparator<T> comparator) {
			if (startIndex == endIndex) {
				return array[startIndex];
			}

			int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex, comparator);
			int k = partitionBoundaryIndex - startIndex + 1;
			if (i < k) {
				return randomizedSelect(array, startIndex, partitionBoundaryIndex, i, comparator);
			} else {
				return randomizedSelect(array, partitionBoundaryIndex + 1, endIndex, i - k, comparator);
			}
		}
		
		private static <T> void swap(T[] array, int i, int j) {
			T temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}

	public static class GenericList<T> {

		public static <T> void quicksort(List<T> array, Comparator<T> comparator) {
			quicksort(array, 0, array.size() - 1, comparator);
		}


		public static <T> void quicksort(List<T> array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.size());

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = partition(array, startIndex, endIndex, comparator);
				quicksort(array, startIndex, partitionBoundaryIndex, comparator);
				quicksort(array, partitionBoundaryIndex + 1, endIndex, comparator);
			}
		}

		/**
		 * This performs the randomized quicksort algorithm in place on the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array
		 */
		public static <T> void randomizedQuicksort(List<T> array, Comparator<T> comparator) {
			quicksort(array, 0, array.size() - 1, comparator);
		}

		/**
		 * This performs the randomized quicksort algorithm in place on a subarray of the
		 * supplied array. The randomized quicksort algorithm has the advantage that
		 * no particular input can force the algorithm to deliver its worst case
		 * performance.
		 * 
		 * @param array the array to sort
		 * @param startIndex the first index of the subarray
		 * @param endIndex the last index of the subarray
		 */
		public static <T> void randomizedQuicksort(List<T> array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.size());

			if (startIndex < endIndex) {
				int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex, comparator);
				randomizedQuicksort(array, startIndex, partitionBoundaryIndex, comparator);
				randomizedQuicksort(array, partitionBoundaryIndex + 1, endIndex, comparator);
			}
		}

		public static <T> int partition(List<T> array, int startIndex, int endIndex, Comparator<T> comparator) {
			ExceptionUtility.verifyNonNegative(startIndex);
			ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
			ExceptionUtility.verifyLessThan(endIndex, array.size());

			T pivotValue = array.get(startIndex);
			int i = startIndex;
			int j = endIndex;

			while (true) {
				while (comparator.compare(array.get(j), pivotValue) > 0) {
					j--;
				}
				while(comparator.compare(array.get(i), pivotValue) < 0) {
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

		public static <T> int randomizedPartition(List<T> array, int startIndex, int endIndex, Comparator<T> comparator) {
			int i = startIndex + random.nextInt(endIndex - startIndex);
			swap(array, i, startIndex);
			return partition(array, startIndex, endIndex, comparator);
		}

		public static <T> T randomizedSelect(List<T> array, int i, Comparator<T> comparator) {
			return randomizedSelect(array, 0, array.size() - 1, i, comparator);
		}

		public static <T> T randomizedSelect(List<T> array, int startIndex, int endIndex, int i, Comparator<T> comparator) {
			if (startIndex == endIndex) {
				return array.get(startIndex);
			}

			int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex, comparator);
			int k = partitionBoundaryIndex - startIndex + 1;
			if (i < k) {
				return randomizedSelect(array, startIndex, partitionBoundaryIndex, i, comparator);
			} else {
				return randomizedSelect(array, partitionBoundaryIndex + 1, endIndex, i - k, comparator);
			}
		}
		
		private static <T> List<T> swap(List<T> array, int i, int j) {
			Collections.swap(array, i, j);
			return array;
		}
	}
}
