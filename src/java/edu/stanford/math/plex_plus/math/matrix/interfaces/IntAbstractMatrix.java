package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface IntAbstractMatrix {
	public int getNumRows();
	public int getNumColumns();
	
	public int get(int row, int column);
	public void set(int row, int column, int value);
	
	public IntAbstractMatrixIterator iterator();
	
	public IntAbstractVector multiply(IntAbstractVector vector);
	public IntAbstractMatrix transpose();
	
	public String toString();
}
