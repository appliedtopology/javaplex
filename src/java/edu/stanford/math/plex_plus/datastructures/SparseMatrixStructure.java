/**
 * 
 */
package edu.stanford.math.plex_plus.datastructures;

import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements a sparse matrix data structure.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the entry type
 */
public class SparseMatrixStructure<T> {	
	protected SortedMap<RowColumnPair, T> map = new TreeMap<RowColumnPair, T>();
	protected final int rows;
	protected final int columns;
	
	public SparseMatrixStructure(int rows, int columns) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		this.rows = rows;
		this.columns = columns;
	}
	
	public int rows() {
		return this.rows;
	}
	
	public int columns() {
		return this.columns;
	}
	
	public void set(int row, int column, T value) {
		ExceptionUtility.verifyIndex(this.rows, row);
		ExceptionUtility.verifyIndex(this.columns, column);
		this.map.put(new RowColumnPair(row, column), value);
	}
	
	public void get(int row, int column) {
		ExceptionUtility.verifyIndex(this.rows, row);
		ExceptionUtility.verifyIndex(this.columns, column);
		this.map.get(new RowColumnPair(row, column));
	}
	
	public int getNumNonzeroElements() {
		return this.map.size();
	}
	
	public double getDensity() {
		return ((double) this.getNumNonzeroElements()) / ((double) (rows * columns));
	}
}
