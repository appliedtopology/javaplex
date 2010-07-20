/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.dense;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrixIterator;

/**
 * @author Andris
 *
 */
public class DoubleDenseMatrixIterator implements DoubleAbstractMatrixIterator {
	private final DoubleDenseMatrix matrix;
	private final int numRows;
	private final int numColumns;
	
	private int row;
	private int column;
	
	/**
	 * 
	 */
	public DoubleDenseMatrixIterator(DoubleDenseMatrix matrix) {
		this.matrix = matrix;
		this.numRows = matrix.getNumRows();
		this.numColumns = matrix.getNumColumns();
		
		row = 0;
		column = -1;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#advance()
	 */
	public void advance() {
		if (column < numColumns - 1) {
			column++;
		} else {
			column = 0;
			row++;
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#column()
	 */
	public int column() {
		return this.column;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#hasNext()
	 */
	public boolean hasNext() {
		if (this.row < this.numRows) {
			return true;
		} else {
			return (this.column < this.numColumns - 1);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#row()
	 */
	public int row() {
		return this.row;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator#value()
	 */
	public double value() {
		return this.matrix.get(row, column);
	}

}
