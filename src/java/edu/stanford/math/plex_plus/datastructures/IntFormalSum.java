package edu.stanford.math.plex_plus.datastructures;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * This class implements a data structure for formal sums 
 * with coefficients in the integers. Such an element can 
 * be thought of as being in the form r_1 m_1 + ... + r_k m_k. 
 * An example of this would be chains in homology theory where 
 * the objects {m_i} are simplices.
 * 
 * @author Andrew Tausz
 *
 * @param <M> the type of the underlying object
 */
public class IntFormalSum<M> {
	protected TObjectIntHashMap<M> map = new TObjectIntHashMap<M>();
	
	public IntFormalSum() {}
	
	public IntFormalSum(int coefficient, M object) {
		this.put(coefficient, object);
	}
	
	private IntFormalSum(TObjectIntHashMap<M> map) {
		ExceptionUtility.verifyNonNull(map);
		this.map.putAll(map);
	}
	
	public IntFormalSum(IntFormalSum<M> formalSum) {
		this(formalSum.map);
	}
	
	public void put(int coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		this.map.put(object, coefficient);
	}
	
	public boolean containsObject(M object) {
		ExceptionUtility.verifyNonNull(object);
		return this.map.containsKey(object);
	}
	
	public int getCoefficient(M object) {
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
	
	public TObjectIntIterator<M> iterator() {
		return this.map.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (TObjectIntIterator<M> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (index > 0) {
				builder.append(" + ");
			}
			builder.append(iterator.value());
			builder.append(" ");
			builder.append(iterator.key().toString());
			index++;
		}
		return builder.toString();
	}
}
