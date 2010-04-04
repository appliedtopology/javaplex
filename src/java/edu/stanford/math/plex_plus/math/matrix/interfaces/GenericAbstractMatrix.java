package edu.stanford.math.plex_plus.math.matrix.interfaces;


public interface GenericAbstractMatrix<T> {
	public int getNumRows();
	public int getNumColumns();
	
	public T get(int row, int column);
	public void set(int row, int column, T value);
	
	public GenericAbstractMatrixIterator<T> iterator();
	public String toString();
}
