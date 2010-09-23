package edu.stanford.math.plex4.free_module;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectDoubleIterator;

/**
 * This class implements a data structure for formal sums 
 * with coefficients being doubles. Such an element can 
 * be thought of as being in the form r_1 m_1 + ... + r_k m_k, 
 * where the r_i are doubles, and the m_i are basis elements of
 * type M.
 * 
 * @author Andrew Tausz
 *
 * @param <M> the type of the underlying object
 */
public class DoubleFormalSum<M> {
	/**
	 * The coefficient-object pairs are held in a hash map, where the
	 * key is the object (e.g. a simplex), and the value is the coefficient.
	 * 
	 * Note that this data structure is not sorted, and does not rely on an
	 * ordering of the objects.
	 * 
	 */
	protected TObjectDoubleHashMap<M> map = new TObjectDoubleHashMap<M>();
	
	/**
	 * Default constructor which initializes the sum to be empty.
	 */
	public DoubleFormalSum() {}
	
	/**
	 * This constructor initializes the sum to contain one object.
	 * 
	 * @param coefficient the coefficient of the initializing object
	 * @param object the object to initialize to
	 */
	public DoubleFormalSum(double coefficient, M object) {
		this.put(coefficient, object);
	}
	
	/**
	 * This constructor initializes the sum to contain one object,
	 * with default coefficient 1.
	 * 
	 * @param object the object to initialize to
	 */
	public DoubleFormalSum(M object) {
		this.put(1, object);
	}
	
	/**
	 * This constructor constructs the sum from another hash map.
	 * 
	 * @param map the hash map to import from
	 */
	private DoubleFormalSum(TObjectDoubleHashMap<M> map) {
		ExceptionUtility.verifyNonNull(map);
		this.map.putAll(map);
	}
	
	/**
	 * This constructor initializes the sum from another DoubleFormalSum.
	 * 
	 * @param formalSum the DoubleFormalSum to import from
	 */
	public DoubleFormalSum(DoubleFormalSum<M> formalSum) {
		this(formalSum.map);
	}
	
	/**
	 * This function adds the supplied coefficient object pair
	 * to this, if the coefficient is non-zero. If the coefficient 
	 * is zero, it does not do anything. Note that if the object
	 * already exists in the sum, it simply replaces the old
	 * coefficient with the new one. A class which implements arithmetic
	 * of formal sums must be aware of this.
	 * 
	 * @param coefficient the coefficient of the object to add
	 * @param object the object to add
	 */
	public void put(double coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		if (coefficient != 0) {
			this.map.put(object, coefficient);
		}
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
	public double getCoefficient(M object) {
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
	 * This function returns an iterator for the sum of type
	 * TObjectDoubleIterator. Note that the class TObjectDoubleIterator
	 * does not implement the standard Iterator interface. For usage
	 * instructions, consult the Gnu Trove documentation.
	 * 
	 * @return an iterator of type TObjectDoubleIterator
	 */
	public TObjectDoubleIterator<M> iterator() {
		return this.map.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (TObjectDoubleIterator<M> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (index > 0) {
				builder.append(" + ");
			}
			if (iterator.value() == -1) {
				builder.append('-');
			} else if (iterator.value() != 1) {
				builder.append(iterator.value());
			}
			builder.append(iterator.key().toString());
			index++;
		}
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoubleFormalSum<?> other = (DoubleFormalSum<?>) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}
}
