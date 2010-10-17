package edu.stanford.math.plex4.free_module;

import java.util.Comparator;

import edu.stanford.math.plex4.array_utility.ArrayCreation;

/**
 * This class implements a formal sum of objects stored as a sorted 
 * array. 
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class IntArraySum<T> {
	private final int[] coefficients;
	private final T[] objects;

	//private static <T> IntArraySum<T> zero = new IntArraySum<T>();


	public IntArraySum() {
		this.coefficients = new int[0];
		this.objects = null;
	}

	public IntArraySum(int coefficient, T object) {
		this.coefficients = new int[]{coefficient};
		this.objects = ArrayCreation.newGenericArray(1, object);
		this.objects[0] = object;
	}

	public IntArraySum(T object) {
		this(1, object);
	}

	public IntArraySum(int[] coefficients, T[] objects) {
		this.coefficients = coefficients;
		this.objects = objects;
	}

	/*
	public IntArraySum(int[] coefficients, Collection<T> objects) {
		this.coefficients = coefficients;
		this.objects = ArrayCreation.newGenericArray(objects.size(), objects.iterator().next());
	}
	*/

	public int[] getCoefficients() {
		return this.coefficients;
	}

	public T[] getObjects() {
		return this.objects;
	}

	public int getLength() {
		return this.coefficients.length;
	}

	public int getMinCoefficient() {
		return this.coefficients[0];
	}

	public T getMinObject() {
		return this.objects[0];
	}

	public int getMaxCoefficient() {
		return this.coefficients[this.coefficients.length - 1];
	}

	public T getMaxObject() {
		return this.objects[this.objects.length - 1];
	}

	public boolean isEmpty() {
		return this.coefficients.length == 0;
	}

	public String toString() {
		if (this.coefficients.length == 0) {
			return "0";
		}
		int length = this.coefficients.length;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			assert (this.coefficients[i] != 0);
			
			if (i > 0) {
				builder.append(" + ");
			}
			
			if (this.coefficients[i] != 1) {
				builder.append(this.coefficients[i]);
			}
			builder.append(this.objects[i].toString());
		}
		
		return builder.toString();
	}
	
	/**
	 * Finds the index of the specified object. If the object is not found,
	 * returns -1;
	 * @param key
	 * @param comparator
	 * @return
	 */
	public int getIndex(T key, Comparator<? super T> comparator) {
		if (this.coefficients.length == 0) {
			return -1;
		}

		int low = 0;
		int high = this.objects.length - 1;

		while (low <= high) {
			int mid = (low + high) >> 1;
			T midVal = this.objects[mid];
			int cmp = comparator.compare(midVal, key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid;
		}
		return -1;
	}
	
	public boolean contains(T key, Comparator<? super T> comparator) {
		int index = this.getIndex(key, comparator);
		if (index < 0) {
			return false;
		}
		assert comparator.compare(this.objects[index], key) == 0;
		return true;
	}
	
	public int getCoefficient(T key, Comparator<? super T> comparator) {
		int index = this.getIndex(key, comparator);
		if (index < 0) {
			return 0;
		}
		assert comparator.compare(this.objects[index], key) == 0;
		return this.coefficients[index];
	}

	/**
	 * This function verifies that the objects are in ascending order.
	 * 
	 * TODO: move out of here
	 * 
	 * @param comparator
	 * @return
	 */
	public static <T> boolean validateSorted(T[] array, Comparator<T> comparator) {
		for (int i = 1; i < array.length; i++) {
			if (comparator.compare(array[i - 1], array[i]) > 0) {
				return false;
			}
		}
		return true;
	}
}
