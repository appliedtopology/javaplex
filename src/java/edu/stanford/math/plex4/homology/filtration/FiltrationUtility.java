package edu.stanford.math.plex4.homology.filtration;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This class contains various static functions that aid in the transformation between objects
 * defined in terms of filtration indices to objects defined in terms of filtration values.
 * 
 * @author Andrew Tausz
 *
 */
public class FiltrationUtility {
	
	@SuppressWarnings("unchecked")
	public static <G, X, Y> PersistenceInvariantDescriptor<Y, G> transform(PersistenceInvariantDescriptor<X, G> invariantDescriptor, ObjectObjectFunction<X, Y> converter) {
		PersistenceInvariantDescriptor<Y, G> result = null;
		
		try {
			result = invariantDescriptor.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		for (Iterator<Entry<Integer, List<ObjectObjectPair<X, G>>>> iterator = invariantDescriptor.getIntervalGeneratorPairIterator(); iterator.hasNext(); ) {
			Entry<Integer, List<ObjectObjectPair<X, G>>> entry = iterator.next();
			Integer dimension = entry.getKey();
			for (ObjectObjectPair<X, G> pair: entry.getValue()) {
				Y transformedObject = converter.evaluate(pair.getFirst());
				G generator = pair.getSecond();
				result.addInterval(dimension, transformedObject, generator);
			}
		}
		
		return result;
	}
	
	public static <G> PersistenceInvariantDescriptor<Interval<Double>, G> transformByIdentity(PersistenceInvariantDescriptor<Interval<Integer>, G> invariantDescriptor) {
		IdentityConverter converter = IdentityConverter.getInstance();
		return transform(invariantDescriptor, converter);
	}
}
