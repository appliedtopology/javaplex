package edu.stanford.math.plex4.math.matrix.interfaces;


public interface GenericAbstractMatrix<T> {
	public int getNumRows();
	public int getNumColumns();
	
	public T get(int row, int column);
	public void set(int row, int column, T value);
	
	public GenericAbstractMatrixIterator<T> iterator();
	public String toString();
}
