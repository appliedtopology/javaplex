package edu.stanford.math.plex_plus.free_module;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class OrderedGenericFormalSum<R, M> implements AbstractGenericFormalSum<R, M> {
	private final Comparator<M> comparator;
	
	/**
	 * The coefficient-object pairs are held in a hash map, where the
	 * key is the object (e.g. a simplex), and the value is the coefficient.
	 * 
	 */
	private final SortedMap<M, R> map;

	/**
	 * Default constructor which initializes the sum to be empty.
	 */
	public OrderedGenericFormalSum(Comparator<M> comparator) {
		this.comparator = comparator;
		this.map = new TreeMap<M, R>(this.comparator);
	}
	
	/**
	 * This constructor initializes the sum to contain one object.
	 * 
	 * @param coefficient the coefficient of the initializing object
	 * @param object the object to initialize to
	 */
	OrderedGenericFormalSum(R coefficient, M object, Comparator<M> comparator) {
		this(comparator);
		this.put(coefficient, object);
	}
	
	/**
	 * This constructor initializes the sum from another IntFormalSum.
	 * 
	 * @param formalSum the IntFormalSum to import from
	 */
	OrderedGenericFormalSum(AbstractGenericFormalSum<R, M> formalSum, Comparator<M> comparator) {
		this(comparator);
		for (Map.Entry<M, R> entry: formalSum) {
			this.map.put(entry.getKey(), entry.getValue());
		}
	}
	
	public M minimumObject() {
		return this.map.firstKey();
	}
	
	public M maximumObject() {
		return this.map.lastKey();
	}
	
	/**
	 * This function adds the supplied coefficient object pair
	 * to this. Note that if the object already exists in the sum, 
	 * it simply replaces the old coefficient with the new one. 
	 * A class which implements arithmetic of formal sums must be 
	 * aware of this.
	 * 
	 * @param coefficient the coefficient of the object to add
	 * @param object the object to add
	 */
	public void put(R coefficient, M object) {
		ExceptionUtility.verifyNonNull(coefficient);
		ExceptionUtility.verifyNonNull(object);
		this.map.put(object, coefficient);
	}
	
	/**
	 * This function returns true if the supplied object is a
	 * member of this, and false otherwise.
	 * 
	 * @param object the object to query
	 * @return true if this contains the supplied object, and false otherwise
	 */
	public boolean containsObject(M object) {
		ExceptionUtility.verifyNonNull(object);
		return this.map.containsKey(object);
	}
	
	/**
	 * This function returns the coefficient of the supplied object.
	 * If the object does not exist in the current sum, it returns 0.
	 * 
	 * @param object the object to query
	 * @return the coefficient of the supplied object
	 */
	public R getCoefficient(M object) {
		ExceptionUtility.verifyNonNull(object);
		return this.map.get(object);
	}
	
	/**
	 * This function removes the specified object.
	 * 
	 * @param object the object to remove
	 */
	public void remove(M object) {
		ExceptionUtility.verifyNonNull(object);
		this.map.remove(object);
	}
	
	/**
	 * This function returns the number of objects in the sum.
	 * 
	 * @return the number of terms in the sum
	 */
	public int size() {
		return this.map.size();
	}
	
	/**
	 * This function returns true if there are no terms in the sum,
	 * and false otherwise.
	 * 
	 * @return true if there are no terms in the sum
	 */
	public boolean isEmpty() {
		return this.map.isEmpty();
	}
	
	/**
	 * This function returns an iterator of type
	 * Iterator<Map.Entry<M, R>>.
	 * 
	 * @return an iterator for iterating through the sum
	 */
	public Iterator<Map.Entry<M, R>> iterator() {
		return this.map.entrySet().iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		R coefficient = null;
		for (Map.Entry<M, R> entry : this.map.entrySet()) {
			if (index > 0) {
				builder.append(" + ");
			}
			coefficient = entry.getValue();
			builder.append(coefficient);
			builder.append(" ");
			builder.append(entry.getKey());
			index++;
		}
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedGenericFormalSum<?, ?> other = (OrderedGenericFormalSum<?, ?>) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}
}
