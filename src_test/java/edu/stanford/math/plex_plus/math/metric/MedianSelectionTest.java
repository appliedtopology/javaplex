package edu.stanford.math.plex_plus.math.metric;

import edu.stanford.math.plex_plus.utility.ArrayUtility;

public class MedianSelectionTest {
	public static void main(String[] args) {
		testMedian();
	}
	
	public static void testMedian() {
		int[] array = null;
		array = new int[]{6, 7, 1, 4, 9, 0, 1, 0, 0, 0, 1, 7, 234, -11, -2828};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
		
		array = new int[]{1};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
		
		array = new int[]{2, 1};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
		
		array = new int[]{1, 2, 3};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
		
		array = new int[]{1, 2, 3, 4};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
		
		array = new int[]{1, 2, 3, 4, 5};
		QuickSort.quickSort(array);
		System.out.println(ArrayUtility.toString(array));
		System.out.println(QuickSort.randomizedSelect(array, array.length / 2));
	}
}
