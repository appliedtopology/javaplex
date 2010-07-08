/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.GenericAbstractMatrixIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;

/**
 * @author atausz
 *
 */
public class GenericSparseMatrixIterator<T> implements GenericAbstractMatrixIterator<T> {
	private final TIntObjectIterator<GenericSparseVector<T>> rowIterator;
	private TIntObjectIterator<T> columnIterator = null;
	
	public GenericSparseMatrixIterator(GenericSparseMatrix<T> matrix) {
		ExceptionUtility.verifyNonNull(matrix);
		this.rowIterator = matrix.map.iterator();
		this.rowIterator.advance();
		this.columnIterator = this.rowIterator.value().map.iterator();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#advance()
	 */
	public void advance() {
		if (this.columnIterator.hasNext()) {
			this.columnIterator.advance();
		} else {
			this.rowIterator.advance();
			this.columnIterator = this.rowIterator.value().map.iterator();
			this.columnIterator.advance();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#column()
	 */
	public int column() {
		return this.columnIterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#hasNext()
	 */
	public boolean hasNext() {
		if (this.rowIterator.hasNext()) {
			return true;
		} else {
			return this.columnIterator.hasNext();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#remove()
	 */
	public void remove() {
		this.columnIterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#row()
	 */
	public int row() {
		return this.rowIterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrixIterator#value()
	 */
	public T value() {
		return this.columnIterator.value();
	}

}
