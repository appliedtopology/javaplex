package edu.stanford.math.plex_plus.datastructures;

import edu.stanford.math.plex_plus.math.matrix.impl.sparse.IntSparseMatrix;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

public class IntLabeledGrid<T extends Comparable<T>> {
	protected final TObjectIntHashMap<GenericOrderedPair<T, T>> map = new TObjectIntHashMap<GenericOrderedPair<T, T>>();
	protected final TObjectIntHashMap<T> rowIndexmap = new TObjectIntHashMap<T>();
	protected final TObjectIntHashMap<T> colIndexmap = new TObjectIntHashMap<T>();
	protected int currentRowCount = 0;
	protected int currentColCount = 0;
	
	public int getEntry(T rowLabel, T columnLabel) {
		ExceptionUtility.verifyNonNull(rowLabel);
		ExceptionUtility.verifyNonNull(columnLabel);
		return this.map.get(new GenericOrderedPair<T, T>(rowLabel, columnLabel));
	}
	
	public void setEntry(T rowLabel, T columnLabel, int value) {
		ExceptionUtility.verifyNonNull(rowLabel);
		ExceptionUtility.verifyNonNull(columnLabel);
		
		// update actual matrix
		this.map.put(new GenericOrderedPair<T, T>(rowLabel, columnLabel), value);
		
		// update row count
		if (!this.rowIndexmap.containsKey(rowLabel)) {
			this.rowIndexmap.put(rowLabel, currentRowCount);
			this.currentRowCount++;
		}
		
		// update column count
		if (!this.colIndexmap.containsKey(columnLabel)) {
			this.colIndexmap.put(columnLabel, currentColCount);
			this.currentColCount++;
		}
	}
	
	public IntSparseMatrix getMatrix() {
		// initialize matrix
		IntSparseMatrix matrix = new IntSparseMatrix(this.currentRowCount, this.currentColCount);
		
		// Add contents
		for (TObjectIntIterator<GenericOrderedPair<T, T>> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			matrix.set(this.rowIndexmap.get(iterator.key().first), this.colIndexmap.get(iterator.key().second), iterator.value());
		}		
		
		return matrix;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (TObjectIntIterator<GenericOrderedPair<T, T>> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.key().toString());
			builder.append(": ");
			builder.append(iterator.value());
			builder.append("\n");
		}
		return builder.toString();
	}
}

/**
 * TODO: Reimplement - I don't like this
 * 
 * @author Andrew Tausz
 *
 * @param <T> row label class
 * @param <U> column label class
 * @param <V> entry class
 */
/*
public class LabeledGrid3<T extends Comparable<T>, U extends Comparable<U>, V> {
	protected SortedMap<GenericOrderedPair<T, U>, V> map = new TreeMap<GenericOrderedPair<T, U>, V>();
	
	public V getEntry(T rowLabel, U columnLabel) {
		ExceptionUtility.verifyNonNull(rowLabel);
		ExceptionUtility.verifyNonNull(columnLabel);
		return this.map.get(new GenericOrderedPair<T, U>(rowLabel, columnLabel));
	}
	
	public void setEntry(T rowLabel, U columnLabel, V value) {
		ExceptionUtility.verifyNonNull(rowLabel);
		ExceptionUtility.verifyNonNull(columnLabel);
		this.map.put(new GenericOrderedPair<T, U>(rowLabel, columnLabel), value);
	}
	
	public List<T> getRowLabelList() {
		List<T> list = new ArrayList<T>();
		Set<Map.Entry<GenericOrderedPair<T, U>, V> > entrySet = map.entrySet();
		for (Map.Entry<GenericOrderedPair<T, U>, V> entry : entrySet) {
			list.add(entry.getKey().getFirst());
		}
		
		return list;
	}
	
	public List<U> getColumnLabelSet() {
		List<U> list = new ArrayList<U>();
		Set<Map.Entry<GenericOrderedPair<T, U>, V> > entrySet = map.entrySet();
		for (Map.Entry<GenericOrderedPair<T, U>, V> entry : entrySet) {
			list.add(entry.getKey().getSecond());
		}
		
		return list;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Set<Map.Entry<GenericOrderedPair<T, U>, V> > entrySet = map.entrySet();
		for (Map.Entry<GenericOrderedPair<T, U>, V> entry : entrySet) {
			builder.append(entry.getKey().toString());
			builder.append(": ");
			builder.append(entry.getValue().toString());
			builder.append("\n");
		}
		return builder.toString();
	}
}
*/