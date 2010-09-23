package edu.stanford.math.plex4.deprecated_tests;

import edu.stanford.math.plex4.array_utility.ArrayPrinting;
import edu.stanford.math.plex4.utility.Quicksort;

public class MedianSelectionTest {
	public static void main(String[] args) {
		testMedian();
	}
	
	public static void testMedian() {
		int[] array = null;
		array = new int[]{6, 7, 1, 4, 9, 0, 1, 0, 0, 0, 1, 7, 234, -11, -2828};
		Quicksort.Int.randomizedQuicksort(array);
		System.out.println(ArrayPrinting.toString(array));
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		
		array = new int[]{1};
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		System.out.println(ArrayPrinting.toString(array));
		
		array = new int[]{2, 1};
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		System.out.println(ArrayPrinting.toString(array));
		
		array = new int[]{3, 1, 2};
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		System.out.println(ArrayPrinting.toString(array));
		
		array = new int[]{1, 3, 4, 2};
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		System.out.println(ArrayPrinting.toString(array));
		
		array = new int[]{5, 4, 3, 2, 1};
		System.out.println(Quicksort.Int.randomizedSelect(array, array.length / 2));
		System.out.println(ArrayPrinting.toString(array));
	}
}
