package edu.stanford.math.plex4.homology.barcodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This class is designed to store persistence algorithm invariants for each dimension, with additional information
 * about the generators. It is primarily designed to be used in the case where the persistence invariant type (I) 
 * is an interval type, and the generator type (G) is the type of cycles.
 * 
 * @author Andrew Tausz
 *
 * @param <I> the persistence invariant type - ie. the interval type
 * @param <G> the generator type
 */
public class PersistenceInvariantDescriptor<I, G> implements Iterable<Entry<Integer, List<ObjectObjectPair<I, G>>>> {
	protected final Map<Integer, List<I>> intervals = new HashMap<Integer, List<I>>();
	protected final Map<Integer, List<G>> generators = new HashMap<Integer, List<G>>();
	protected final Map<Integer, List<ObjectObjectPair<I, G>>> intervalGeneratorPairs = new HashMap<Integer, List<ObjectObjectPair<I, G>>>();

	public PersistenceInvariantDescriptor() {}
	
	/**
	 * This function adds an interval and generator at the specified dimension to the
	 * collection.
	 * 
	 * @param dimension the dimension at which to add
	 * @param interval the interval
	 * @param generator the generator
	 */
	public void addInterval(int dimension, I interval, G generator) {
		if (!this.intervals.containsKey(dimension)) {
			this.intervals.put(dimension, new ArrayList<I>());
		}
		
		if (!this.generators.containsKey(dimension)) {
			this.generators.put(dimension, new ArrayList<G>());
		}
		
		if (!this.intervalGeneratorPairs.containsKey(dimension)) {
			this.intervalGeneratorPairs.put(dimension, new ArrayList<ObjectObjectPair<I, G>>());
		}

		this.intervals.get(dimension).add(interval);
		this.generators.get(dimension).add(generator);
		this.intervalGeneratorPairs.get(dimension).add(new ObjectObjectPair<I, G>(interval, generator));
	}
	
	/**
	 * This function returns an array of integers indicating the cardinality of the barcodes at each dimension.
	 * 
	 * @return an array indicating the cardinality of each barcode at each dimension
	 */
	public int[] getBettiSequence() {
		int[] result = null;

		int maxDimension = 0;

		for (Integer dimension: this.intervals.keySet()) {
			if (dimension > maxDimension) {
				maxDimension = dimension;
			}
		}

		result = new int[maxDimension + 1];

		for (Integer dimension: this.intervals.keySet()) {
			result[dimension] = this.intervals.get(dimension).size();
		}

		return result;
	}
	
	/**
	 * This function returns a string representation of the set of betti numbers.
	 * 
	 * @return a string showing a pretty version of the betti numbers
	 */
	public String getBettiNumbers() {
		StringBuilder builder = new StringBuilder();	
		
		builder.append("{");
		
		int i = 0;
		for (Integer dimension: this.intervals.keySet()) {
			int b = this.intervals.get(dimension).size();
			
			if (i > 0) {
				builder.append(", ");
			}
			
			builder.append(String.format("%d: %d", dimension, b));
			
			i++;
		}
		
		builder.append("}");
		
		
		return builder.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Integer dimension: this.intervals.keySet()) {
			List<I> intervalList = this.intervals.get(dimension);
			List<G> generatorList = this.generators.get(dimension);
			
			builder.append(String.format("Dimension: %d\n", dimension));
			
			assert (intervalList.size() == generatorList.size());
			
			for (int i = 0; i < intervalList.size(); i++) {
				I interval = intervalList.get(i);
				G generator = generatorList.get(i);
				
				if (generator != null) {
					builder.append(interval.toString());
					builder.append(": ");
					builder.append(generator.toString());
					builder.append("\n");
				} else {
					builder.append(interval.toString());
					builder.append("\n");
				}
			}
		}

		return builder.toString();
	}
	
	/**
	 * This function returns an iterator for traversing the intervals. Each element returned
	 * by the iterator is a pair consisting of the dimension, and the list of intervals at
	 * that dimension.
	 * 
	 * @return an iterator for the intervals
	 */
	public Iterator<Entry<Integer, List<I>>> getIntervalIterator() {
		return this.intervals.entrySet().iterator();
	}
	
	/**
	 * This function returns an iterator for traversing the generators. Each element returned
	 * by the iterator is a pair consisting of the dimension, and the list of generators at
	 * that dimension.
	 * 
	 * @return an iterator for the generators
	 */
	public Iterator<Entry<Integer, List<G>>> getGeneratorIterator() {
		return this.generators.entrySet().iterator();
	}
	
	/**
	 * This function returns an iterator for traversing the interval-generator pairs. Each 
	 * element returned by the iterator is a pair consisting of the dimension, and the list of 
	 * interval-generator pairs at that dimension.
	 * 
	 * @return an iterator for the generators
	 */
	public Iterator<Entry<Integer, List<ObjectObjectPair<I, G>>>> getIntervalGeneratorPairIterator() {
		return this.intervalGeneratorPairs.entrySet().iterator();
	}
	
	/**
	 * This function returns the set of dimensions at which there are intervals.
	 * 
	 * @return the set of dimensions.
	 */
	public Set<Integer> getDimensions() {
		return this.intervals.keySet();
	}
	
	/**
	 * This function returns the set of intervals at a specified dimension.
	 * 
	 * @param dimension the dimension to get the intervals at
	 * @return the intervals at the given dimension
	 */
	public List<I> getIntervalsAtDimension(int dimension) {
		if (this.intervals.containsKey(dimension)) {
			return this.intervals.get(dimension);
		} else {
			return new ArrayList<I>();
		}
	}
	
	/**
	 * This function returns the set of generators at a specified dimension.
	 * 
	 * @param dimension the dimension to get the generators at
	 * @return the generators at the given dimension
	 */
	public List<G> getGeneratorsAtDimension(int dimension) {
		if (this.generators.containsKey(dimension)) {
			return this.generators.get(dimension);
		} else {
			return new ArrayList<G>();
		}
	}
	
	/**
	 * This function returns the set of interval-generator pairs at a specified dimension.
	 * 
	 * @param dimension the dimension to get the interval-generator pairs at
	 * @return the interval-generator pairs at the given dimension
	 */
	public List<ObjectObjectPair<I, G>> getIntervalGeneratorPairsAtDimension(int dimension) {
		if (this.intervalGeneratorPairs.containsKey(dimension)) {
			return this.intervalGeneratorPairs.get(dimension);
		} else {
			return new ArrayList<ObjectObjectPair<I, G>>();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((generators == null) ? 0 : generators.hashCode());
		result = prime * result + ((intervalGeneratorPairs == null) ? 0 : intervalGeneratorPairs.hashCode());
		result = prime * result + ((intervals == null) ? 0 : intervals.hashCode());
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
		PersistenceInvariantDescriptor<?, ?> other = (PersistenceInvariantDescriptor<?, ?>) obj;
		if (generators == null) {
			if (other.generators != null)
				return false;
		} else if (!mapSetEquals(this.generators, other.generators))
			return false;
		if (intervalGeneratorPairs == null) {
			if (other.intervalGeneratorPairs != null)
				return false;
		} else if (!mapSetEquals(this.intervalGeneratorPairs, other.intervalGeneratorPairs))
			return false;
		if (intervals == null) {
			if (other.intervals != null)
				return false;
		} else if (!mapSetEquals(this.intervals, other.intervals))
			return false;
		return true;
	}

	public Iterator<Entry<Integer, List<ObjectObjectPair<I, G>>>> iterator() {
		return this.getIntervalGeneratorPairIterator();
	}

	/**
	 * This function computes the union of two collections of persistence invariants.
	 * 
	 * @param <I> the persistence invariant type - ie. the interval type
	 * @param <G> the generator type
	 * @param a the first collection
	 * @param b the second collection
	 * @return the union of the two collections
	 */
	@SuppressWarnings("unchecked")
	public static <I, G> PersistenceInvariantDescriptor<I, G> union(final PersistenceInvariantDescriptor<I, G> a, final PersistenceInvariantDescriptor<I, G> b) {
		
		// attempt to instantiate as low as possible
		PersistenceInvariantDescriptor<I, G> c = null;
		try {
			c = a.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		for (Iterator<Entry<Integer, List<ObjectObjectPair<I, G>>>> iterator = a.getIntervalGeneratorPairIterator(); iterator.hasNext(); ) {
			Entry<Integer, List<ObjectObjectPair<I, G>>> entry = iterator.next();
			Integer dimension = entry.getKey();
			List<ObjectObjectPair<I, G>> values = entry.getValue();
			for (ObjectObjectPair<I, G> pair: values) {
				c.addInterval(dimension, pair.getFirst(), pair.getSecond());
			}
		}
		
		for (Iterator<Entry<Integer, List<ObjectObjectPair<I, G>>>> iterator = b.getIntervalGeneratorPairIterator(); iterator.hasNext(); ) {
			Entry<Integer, List<ObjectObjectPair<I, G>>> entry = iterator.next();
			Integer dimension = entry.getKey();
			List<ObjectObjectPair<I, G>> values = entry.getValue();
			for (ObjectObjectPair<I, G> pair: values) {
				c.addInterval(dimension, pair.getFirst(), pair.getSecond());
			}
		}
		
		return c;
	}

	/**
	 * This function tests for equality of two maps where the value types are lists. It
	 * tests whether the values are equal as sets. In other words, it ignores the order
	 * of the elements.
	 * 
	 * @param <X>
	 * @param a the first map
	 * @param b the second map
	 * @return true if the maps have equal keys and values (up to re-ordering)
	 */
	private static <X> boolean mapSetEquals(Map<Integer, List<X>> a, Map<?, ?> b) {
		Set<?> a_keys = a.keySet();
		Set<?> b_keys = b.keySet();
		
		if (!ComparisonUtility.setEquals(a_keys, b_keys)) {
			return false;
		}
		
		for (Object key: a_keys) {
			Collection<?> a_collection = a.get(key);
			
			Object b_object = b.get(key);
			
			if (!(b_object instanceof List<?>)) {
				return false;
			}
			
			List<?> b_collection = (List<?>) b_object;

			if (!ComparisonUtility.setEquals(a_collection, b_collection)) {
				return false;
			}
		}
		
		
		return true;
	}
}
