package edu.stanford.math.plex_plus.datastructures;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class is a data structure for holding a formal sum.
 * Such an element can be thought of as being in the form
 * r_1 m_1 + ... + r_k m_k. An example of this would be chains
 * in homology theory where the objects {m_i} are simplices
 * 
 * The arithmetic operations of the free R-module whose elements
 * are formal sums is implemented in GenericFreeModule<R, M>.
 * 
 * @author Andrew Tausz
 *
 * @param <R> the coefficient type
 * @param <M> the object type 
 */
public class GenericFormalSum<R, M extends Comparable<M>> {
	private SortedMap<M, R> map = new TreeMap<M, R>();
	
	public GenericFormalSum() {}
	
	public GenericFormalSum(R coefficient, M object) {
		this.put(coefficient, object);
	}
	
	private GenericFormalSum(Map<M, R> map) {
		ExceptionUtility.verifyNonNull(map);
		this.map.putAll(map);
	}
	
	public GenericFormalSum(GenericFormalSum<R, M> formalSum) {
		this(formalSum.map);
	}
	
	public void put(R coefficient, M object) {
		ExceptionUtility.verifyNonNull(coefficient);
		ExceptionUtility.verifyNonNull(object);
		this.map.put(object, coefficient);
	}
	
	public boolean containsObject(M object) {
		ExceptionUtility.verifyNonNull(object);
		return this.map.containsKey(object);
	}
	
	public R getCoefficient(M object) {
		ExceptionUtility.verifyNonNull(object);
		return this.map.get(object);
	}
	
	public void remove(M object) {
		ExceptionUtility.verifyNonNull(object);
		this.map.remove(object);
	}
	
	public int size() {
		return this.map.size();
	}
	
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
}
