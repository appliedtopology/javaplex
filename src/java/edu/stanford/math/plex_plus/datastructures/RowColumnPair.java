package edu.stanford.math.plex_plus.datastructures;

/**
 * This class is a (row, column) tuple which is 
 * designed to be used as an index in a sparse matrix
 * data structure.
 * 
 * @author Andrew Tausz
 *
 */
public class RowColumnPair implements Comparable<RowColumnPair> {
	private final int row;
	private final int column;
	
	public RowColumnPair(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int compareTo(RowColumnPair o) {
		if (row > o.row) {
			return 1;
		} else if (row < o.row) {
			return -1;
		} else {
			if (column > o.column) {
				return 1;
			} else if (column < o.column) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
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
		if (!(obj instanceof RowColumnPair))
			return false;
		RowColumnPair other = (RowColumnPair) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
