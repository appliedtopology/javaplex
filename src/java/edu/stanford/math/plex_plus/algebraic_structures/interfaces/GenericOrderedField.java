package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

import java.util.Comparator;

public abstract class GenericOrderedField<T> extends GenericField<T> implements Comparator<T> {
	public abstract T abs(T a);
}
