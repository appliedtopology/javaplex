package edu.stanford.math.plex4.free_module;

import java.util.Map;

public interface AbstractGenericFormalSum<R, M> extends Iterable<Map.Entry<M, R>>, Cloneable {	
	void put(R coefficient, M object);
	void remove(M object);
	boolean containsObject(M object);
	R getCoefficient(M object);
	
	int size();
	boolean isEmpty();
}
